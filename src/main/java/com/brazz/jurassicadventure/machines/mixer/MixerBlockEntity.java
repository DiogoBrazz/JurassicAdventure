package com.brazz.jurassicadventure.machines.mixer;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.ModTags; // << IMPORT ADICIONADO
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items; // << IMPORT ADICIONADO
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MixerBlockEntity extends AllSettingsEntity {
    
    // Construtor: 4 slots, 200 ticks de processamento
    public MixerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MIXER_BLOCK_ENTITY.get(), pPos, pBlockState, 4, 200);
    }

    // Balanceamento específico do Mixer
    private static final int ENERGY_CONSUMPTION_PER_TICK = 8;

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new MixerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    private boolean hasEnoughEnergy() {
        return this.energyStorage.getEnergyStored() >= ENERGY_CONSUMPTION_PER_TICK;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        // A máquina só funciona se tiver uma receita válida, espaço na saída E energia.
        if (hasRecipe() && canInsertResult() && hasEnoughEnergy()) {
            // Consome energia e avança o progresso
            this.energyStorage.extractEnergy(ENERGY_CONSUMPTION_PER_TICK, false);
            progress++;
            setChanged(level, pos, state);

            if (progress >= maxProgress) {
                craftItem();
                progress = 0;
            }
        } else {
            // Se qualquer condição falhar, reseta o progresso
            progress = 0;
            setChanged(level, pos, state);
        }
    }

    private boolean hasRecipe() {
        ItemStack foodSlot = this.itemHandler.getStackInSlot(0);
        ItemStack syringeSlot = this.itemHandler.getStackInSlot(1);
        ItemStack milkSlot = this.itemHandler.getStackInSlot(2);

        // A receita é válida se:
        // 1. O primeiro slot tiver um item da nossa tag de comidas.
        // 2. O segundo slot tiver uma seringa.
        // 3. O terceiro slot tiver um balde de leite.
        boolean hasIngredients = foodSlot.is(ModTags.Items.NATURAL_FOODS) &&
                                 syringeSlot.getItem() == ModItems.SYRINGE.get() &&
                                 milkSlot.getItem() == Items.MILK_BUCKET;

        return hasIngredients;
    }

    private boolean canInsertResult() {
        ItemStack result = new ItemStack(ModItems.NUTRITIONAL_SYRINGE.get());
        ItemStack outputSlot = this.itemHandler.getStackInSlot(3);

        // Retorna true se o slot de saída estiver vazio,
        // OU se o item for o mesmo e houver espaço para mais um.
        return outputSlot.isEmpty() || 
               (ItemStack.isSameItem(outputSlot, result) && outputSlot.getCount() < outputSlot.getMaxStackSize());
    }

    private void craftItem() {
        ItemStack result = new ItemStack(ModItems.NUTRITIONAL_SYRINGE.get());

        // Consome os ingredientes
        this.itemHandler.extractItem(0, 1, false); // Comida
        this.itemHandler.extractItem(1, 1, false); // Seringa
        
        // --- LÓGICA ESPECIAL DO BALDE ---
        // Em vez de extrair, nós substituímos o balde de leite por um balde vazio.
        this.itemHandler.setStackInSlot(2, new ItemStack(Items.BUCKET));

        // Coloca o resultado na saída
        ItemStack outputSlot = this.itemHandler.getStackInSlot(3);
        if (outputSlot.isEmpty()) {
            this.itemHandler.setStackInSlot(3, result);
        } else {
            outputSlot.grow(1);
        }
    }
}