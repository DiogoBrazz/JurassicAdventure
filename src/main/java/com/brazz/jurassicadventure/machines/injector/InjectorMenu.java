package com.brazz.jurassicadventure.machines.injector;

import org.jetbrains.annotations.NotNull;

import com.brazz.jurassicadventure.ModBlocks;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.ModMenuTypes;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsMenu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class InjectorMenu extends AllSettingsMenu{
    
    public final InjectorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public InjectorMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    @Override
    protected int getMachineInventorySlotCount() {
        return 3; //IMPORTANTE, COLOCAR O NUMERO DE SLOTS DA MÁQUINA
    }

    public InjectorMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.INJECTOR_MENU.get(), pContainerId, inv, entity, data, 8, 28, 56);
        checkContainerSize(inv, this.getMachineInventorySlotCount());
        this.blockEntity = (InjectorBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // --- Configuração dos Slots ---
        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 0, 11, 30){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ModItems.COMPLETE_GENOME_TUBE.get();
            }
        });

        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 1, 51, 30){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ModItems.NUTRITIONAL_SYRINGE.get();
            }
        });

        this.addSlot(new OutputSlot(this.blockEntity.getItemHandler(), 2, 132, 43));

        addDataSlots(data);
    }

    // Métodos para a barra de progresso
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 53; // Mude este valor para a largura da sua seta de progresso em pixels
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.INJECTOR.get());
    }

}
