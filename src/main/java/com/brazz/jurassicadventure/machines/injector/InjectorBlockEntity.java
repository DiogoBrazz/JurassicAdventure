package com.brazz.jurassicadventure.machines.injector;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsEntity;
import com.brazz.jurassicadventure.util.DinoTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InjectorBlockEntity extends AllSettingsEntity{
    public InjectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.INJECTOR_BLOCK_ENTITY.get(), pPos, pBlockState, 3, 400);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new InjectorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        // 1. Verifica se a máquina pode processar os itens atuais.
        if (canProcess()) {
            // 2. Se sim, avança o progresso.
            progress++;
            setChanged(pLevel, pPos, pState);

            // 3. Se o progresso estiver completo, executa a receita.
            if (progress >= maxProgress) {
                processItem();
                progress = 0;
            }
        } else {
            // 4. Se não puder processar, reinicia o progresso.
            progress = 0;
            setChanged(pLevel, pPos, pState);
        }
    }

    
     // Verifica se os ingredientes e as condições para a receita são válidos. return true se a máquina puder trabalhar, false caso contrário.
    private boolean canProcess() {
        ItemStack genomeTube = this.itemHandler.getStackInSlot(0);
        ItemStack nutritionalSyringe = this.itemHandler.getStackInSlot(1);
        ItemStack outputSlot = this.itemHandler.getStackInSlot(2);

        // A receita só é válida se tivermos os itens corretos, o genoma tiver uma tag, e a saída estiver vazia.
        return genomeTube.getItem() == ModItems.COMPLETE_GENOME_TUBE.get() &&
               genomeTube.hasTag() &&
               nutritionalSyringe.getItem() == ModItems.NUTRITIONAL_SYRINGE.get() &&
               outputSlot.isEmpty();
    }

    
    // Executa a receita: consome os ingredientes e cria o resultado.
    @SuppressWarnings("removal")
    private void processItem() {
        ItemStack genomeTube = this.itemHandler.getStackInSlot(0);
        String dinoTypeId = genomeTube.getTag().getString("dino_type");
        
        // Se, por alguma razão, a tag estiver vazia, não fazemos nada para evitar erros.
        if(dinoTypeId.isEmpty()) return;

        ResourceLocation dinoType = new ResourceLocation(dinoTypeId);

        // --- LÓGICA DE DECISÃO (A SUA IDEIA) ---
        // Verifica se o dinossauro está na nossa lista de ovíparos.
        boolean isOviparous = DinoTypes.OVIPAROUS_DINOS.contains(dinoType);

        // Cria o item de resultado (a seringa de embrião genérica).
        ItemStack result = new ItemStack(ModItems.EMBRYO_SYRINGE.get());
        
        // Adiciona a tag do tipo de dino
        result.getOrCreateTag().putString("dino_type", dinoTypeId);
        // E adiciona a nossa nova tag que diz se ele põe ovos ou não!
        result.getOrCreateTag().putBoolean("is_oviparous", isOviparous);
        
        // --- AÇÕES FINAIS ---
        // Consome os ingredientes.
        this.itemHandler.extractItem(0, 1, false);
        this.itemHandler.extractItem(1, 1, false);
        // Coloca o resultado no slot de saída.
        this.itemHandler.setStackInSlot(2, result);
    }
}
