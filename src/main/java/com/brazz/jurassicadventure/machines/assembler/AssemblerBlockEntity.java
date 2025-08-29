package com.brazz.jurassicadventure.machines.assembler;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class AssemblerBlockEntity extends AllSettingsEntity {

    // O construtor está a chamar a classe mãe, mas precisa de ser ajustado
    public AssemblerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ASSEMBLER_BLOCK_ENTITY.get(), pPos, pBlockState, 6, 300);
    }

    private static final int ENERGY_CONSUMPTION_PER_TICK = 8;

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new AssemblerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    private boolean hasEnoughEnergy() {
        return this.energyStorage.getEnergyStored() >= ENERGY_CONSUMPTION_PER_TICK;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        // 1. Descobre qual seria o resultado da receita com base nos inputs atuais.
        Optional<ItemStack> recipeResult = getRecipeResult();

        // 2. Se a receita for válida E o resultado puder ser inserido na saída...
        if (recipeResult.isPresent() && canInsertResult(recipeResult.get()) && hasEnoughEnergy()) {
            // ...o progresso avança.
            this.energyStorage.extractEnergy(ENERGY_CONSUMPTION_PER_TICK, false);
            progress++;
            setChanged(level, pos, state);
            if (progress >= maxProgress) {
                processItem(recipeResult.get());
                progress = 0;
            }
        } else {
            // Se não, o progresso é reiniciado.
            progress = 0;
            setChanged(level, pos, state);
        }
    }

    private Optional<ItemStack> getRecipeResult() {
        // Verifica se todas as 5 entradas estão preenchidas com os tipos corretos de itens.
        for (int i = 0; i < 4; i++) {
            if (itemHandler.getStackInSlot(i).getItem() != ModItems.PARTIAL_GENOME_TUBE.get()) {
                return Optional.empty(); // Se algum slot de genoma não tiver o item certo, não há receita.
            }
        }
        if (itemHandler.getStackInSlot(4).getItem() != ModItems.TUBE.get()) {
            return Optional.empty(); // Se não houver um tubo vazio, não há receita.
        }

        // Se todas as entradas estiverem corretas, determina o resultado.
        String referenceDnaType = itemHandler.getStackInSlot(0).getTag().getString("dino_type");
        if (referenceDnaType.isEmpty()) {
            return Optional.of(new ItemStack(ModItems.FAILED_TUBE.get())); // Se o primeiro item não tiver NBT, falha.
        }

        boolean allMatch = true;
        for (int i = 1; i < 4; i++) {
            String currentDnaType = itemHandler.getStackInSlot(i).getTag().getString("dino_type");
            if (!Objects.equals(referenceDnaType, currentDnaType)) {
                allMatch = false;
                break;
            }
        }

        if (allMatch) {
            ItemStack result = new ItemStack(ModItems.COMPLETE_GENOME_TUBE.get());
            result.getOrCreateTag().putString("dino_type", referenceDnaType);
            return Optional.of(result);
        } else {
            return Optional.of(new ItemStack(ModItems.FAILED_TUBE.get()));
        }
    }

    private boolean canInsertResult(ItemStack result) {
        ItemStack outputSlot = this.itemHandler.getStackInSlot(5);
        // A saída pode aceitar o item se estiver vazia, OU se o item for o mesmo e houver espaço para acumular.
        return outputSlot.isEmpty() || (ItemStack.isSameItemSameTags(outputSlot, result) && outputSlot.getCount() < outputSlot.getMaxStackSize());
    }

    private void processItem(ItemStack result) {
        // Consome os 5 itens de entrada.
        for (int i = 0; i < 5; i++) {
            this.itemHandler.extractItem(i, 1, false);
        }

        // Coloca o resultado na saída, acumulando se possível.
        ItemStack outputSlot = this.itemHandler.getStackInSlot(5);
        if (outputSlot.isEmpty()) {
            this.itemHandler.setStackInSlot(5, result);
        } else {
            outputSlot.grow(1);
        }
    }
}
