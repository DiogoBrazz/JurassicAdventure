package com.brazz.jurassicadventure.machines.analyzer;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.ModTags;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsEntity;
import com.brazz.jurassicadventure.util.DinoTypes;
import com.brazz.jurassicadventure.util.MobTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class AnalyzerBlockEntity extends AllSettingsEntity {
    
    // O construtor está a chamar a classe mãe, o que está correto, mas precisa de ser ajustado
    public AnalyzerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ANALYZER_BLOCK_ENTITY.get(), pPos, pBlockState, 8, 100);
    }

    

    // --- MÉTODOS OBRIGATÓRIOS DO MenuProvider ---

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new AnalyzerMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    // Retorna true se todos os slots de saída (2 a 7) estiverem ocupados
    private boolean areOutputSlotsFull() {
        for (int i = 2; i <= 7; i++) {
            if (this.itemHandler.getStackInSlot(i).isEmpty()) {
                return false; // Encontrou um slot vazio
            }
        }
        return true; // Todos os slots ocupados
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        ItemStack recipeResult = getRecipeResult();
        // Se não houver resultado válido ou todos os slots de saída estiverem cheios, reinicia progresso e retorna
        if (recipeResult.isEmpty() || areOutputSlotsFull()) {
            if (progress != 0) {
                progress = 0;
                setChanged(pLevel, pPos, pState);
            }
            return; // Máquina para aqui
        }
        // Avança o progresso
        progress++;
        setChanged(pLevel, pPos, pState);
        // Quando o progresso atingir o máximo, processa o item e reseta o progresso
        if (progress >= maxProgress) {
            processItem(recipeResult);
            progress = 0;
        }
    }

    // Este método agora só decide o resultado.
    private ItemStack getRecipeResult() {
        ItemStack inputStack = this.itemHandler.getStackInSlot(0);
        ItemStack syringeStack = this.itemHandler.getStackInSlot(1);

        // Condição inicial: precisamos de um item para analisar e uma seringa.
        if (inputStack.isEmpty() || !inputStack.is(ModTags.Items.ANALYZABLE_ITEMS) || syringeStack.getItem() != ModItems.SYRINGE.get()) {
            return ItemStack.EMPTY; // Retorna um resultado vazio se não tivermos os ingredientes.
        }

        float chance = this.level.getRandom().nextFloat();
        
        // Lógica para Âmbar
        if (inputStack.getItem() == ModItems.RAW_AMBER.get()) {
            ItemStack result;
            if (chance < 0.08f) { 
                result = new ItemStack(ModItems.DNA_SYRINGE.get());
                //Sorteia um dino aleatório da lista
                ResourceLocation randomDino = DinoTypes.ALL_DINOS.get(this.level.getRandom().nextInt(DinoTypes.ALL_DINOS.size()));
                result.getOrCreateTag().putString("dino_type", randomDino.toString());
                return result;
            } else {
                int trash = this.level.getRandom().nextInt(5);
                result = switch (trash) {
                    case 0 -> new ItemStack(Items.DIRT);
                    case 1 -> new ItemStack(Items.COBBLESTONE);
                    case 2 -> new ItemStack(Items.BONE);
                    case 3 -> new ItemStack(Items.BONE_MEAL);
                    default -> {
                        yield new ItemStack(ModItems.BROKEN_SYRINGE.get());
                    }
                };
            }
            return result;
        } 
        // Lógica para Carnes e outros
        else {
            if (chance < 0.5f) {
                return new ItemStack(ModItems.FAILED_SYRINGE.get());
            } else {
                ItemStack result = new ItemStack(ModItems.DNA_SYRINGE.get());
                Item inputItem = inputStack.getItem();
                if (inputItem == Items.BEEF) { result.getOrCreateTag().putString("mob_type", MobTypes.COW.toString()); }
                else if (inputItem == Items.PORKCHOP) { result.getOrCreateTag().putString("mob_type", MobTypes.PIG.toString()); }
                else if (inputItem == Items.MUTTON) { result.getOrCreateTag().putString("mob_type", MobTypes.SHEEP.toString()); }
                else if (inputItem == Items.CHICKEN) { result.getOrCreateTag().putString("mob_type", MobTypes.CHICKEN.toString()); }
                else if (inputItem == Items.SALMON) { result.getOrCreateTag().putString("mob_type", MobTypes.SALMON.toString()); }
                else if (inputItem == ModItems.FROG_MEAT.get()) { result.getOrCreateTag().putString("mob_type", MobTypes.RIVER_FROG.toString()); }
                else {
                    // Se o item for da tag mas não tiver uma receita de DNA, a extração também falha.
                    return new ItemStack(ModItems.FAILED_SYRINGE.get());
                }
                return result;
            }
        }
    }

    // Este método apenas executa as ações de consumir e colocar o resultado.
    private void processItem(ItemStack result) {
        // A seringa só é consumida se o resultado for um DNA OU FALHA(e não lixo).
        if (result.getItem() == ModItems.DNA_SYRINGE.get() || result.getItem() == ModItems.FAILED_SYRINGE.get() || result.getItem() == ModItems.BROKEN_SYRINGE.get()) {
            this.itemHandler.extractItem(1, 1, false); // Consome a seringa
        }
        this.itemHandler.extractItem(0, 1, false); // Consome a entrada

        // Tenta acumular primeiro
        for (int i = 2; i <= 7; i++) {
            ItemStack outputSlot = this.itemHandler.getStackInSlot(i);
            if (ItemStack.isSameItemSameTags(outputSlot, result) && outputSlot.getCount() < outputSlot.getMaxStackSize()) {
                outputSlot.grow(1);
                return; // Para assim que conseguir acumular
            }
        }
        // Se não conseguiu acumular, coloca num slot vazio
        for (int i = 2; i <= 7; i++) {
            if (this.itemHandler.getStackInSlot(i).isEmpty()) {
                this.itemHandler.setStackInSlot(i, result.copy()); // Usa .copy() para segurança
                return; // Para assim que encontrar um slot vazio
            }
        }
    }
    

}