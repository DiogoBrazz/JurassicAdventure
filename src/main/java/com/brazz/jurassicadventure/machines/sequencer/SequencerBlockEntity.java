package com.brazz.jurassicadventure.machines.sequencer;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsEntity;
import com.brazz.jurassicadventure.util.DinoTypes;
import com.brazz.jurassicadventure.util.MobTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SequencerBlockEntity extends AllSettingsEntity {
    
    public SequencerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SEQUENCER_BLOCK_ENTITY.get(), pPos, pBlockState, 4, 200);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new SequencerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        Optional<ItemStack> recipeResult = getRecipeResult();

        ItemStack tube = itemHandler.getStackInSlot(2);
        boolean hasTube = tube.getItem() == ModItems.TUBE.get();

        if (recipeResult.isPresent() && hasTube && canInsertResult(recipeResult.get())) {
            progress++;
            setChanged(pLevel, pPos, pState);
            if (progress >= maxProgress) {
                processItem(recipeResult.get());
                progress = 0;
            }
        } else {
            progress = 0;
            setChanged(pLevel, pPos, pState);
        }
    }

    public record RecipeKey(String type1, String type2) {
        public RecipeKey {
            // Garante que a ordem seja sempre a mesma para comparação
            if (type1.compareTo(type2) > 0) {
                String temp = type1;
                type1 = type2;
                type2 = temp;
            }
        }
    }

    public class SequencerRecipes {
        public static final Map<RecipeKey, ItemStack> RECIPES = new HashMap<>();

        static {
            // Exemplo: River Frog + T-Rex → Genoma parcial de T-Rex
            ItemStack rex = new ItemStack(ModItems.PARTIAL_GENOME_TUBE.get());
            rex.getOrCreateTag().putString("dino_type", DinoTypes.REX.toString());
            RECIPES.put(new RecipeKey(MobTypes.RIVER_FROG.toString(), DinoTypes.REX.toString()), rex);

            ItemStack triceratops = new ItemStack(ModItems.PARTIAL_GENOME_TUBE.get());
            triceratops.getOrCreateTag().putString("dino_type", DinoTypes.TRICERATOPS.toString());
            RECIPES.put(new RecipeKey(MobTypes.COW.toString(), DinoTypes.TRICERATOPS.toString()), triceratops);

        }
    }

    private Optional<ItemStack> getRecipeResult() {
        ItemStack stackA = itemHandler.getStackInSlot(0);
        ItemStack stackB = itemHandler.getStackInSlot(1);

        // Apenas aceita seringas
        if (stackA.getItem() != ModItems.DNA_SYRINGE.get() ||
                stackB.getItem() != ModItems.DNA_SYRINGE.get()) {
            return Optional.empty();
        }

        // Lê tags se existirem
        String typeA_dino = stackA.hasTag() ? stackA.getTag().getString("dino_type") : "";
        String typeA_mob = stackA.hasTag() ? stackA.getTag().getString("mob_type") : "";
        String typeB_dino = stackB.hasTag() ? stackB.getTag().getString("dino_type") : "";
        String typeB_mob = stackB.hasTag() ? stackB.getTag().getString("mob_type") : "";

        // Pega o tipo de cada seringa (dino ou mob, prioridade pro dino se existir)
        String type1 = !typeA_dino.isEmpty() ? typeA_dino : typeA_mob;
        String type2 = !typeB_dino.isEmpty() ? typeB_dino : typeB_mob;

        if (type1.isEmpty() || type2.isEmpty()) {
            // Tem seringa mas sem dados → Falha
            return Optional.of(new ItemStack(ModItems.FAILED_TUBE.get()));
        }

        // Procura no mapa
        RecipeKey key = new RecipeKey(type1, type2);
        if (SequencerRecipes.RECIPES.containsKey(key)) {
            return Optional.of(SequencerRecipes.RECIPES.get(key).copy());
        }

        // Se não achou → falha
        return Optional.of(new ItemStack(ModItems.FAILED_TUBE.get()));
    }

    private boolean canInsertResult(ItemStack result) {
        ItemStack outputSlot = itemHandler.getStackInSlot(3);
        return outputSlot.isEmpty() || (ItemStack.isSameItemSameTags(outputSlot, result)
                && outputSlot.getCount() < outputSlot.getMaxStackSize());
    }

    private void processItem(ItemStack result) {
        // Sempre consome os dois ingredientes
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, 1, false);
        itemHandler.extractItem(2, 1, false);

        // Coloca o resultado no slot 3
        if (itemHandler.getStackInSlot(3).isEmpty()) {
            itemHandler.setStackInSlot(3, result);
        } else {
            itemHandler.getStackInSlot(3).grow(result.getCount());
        }
    }
}