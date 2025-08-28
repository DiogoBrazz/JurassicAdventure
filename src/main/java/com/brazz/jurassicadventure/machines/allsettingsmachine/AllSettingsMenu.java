package com.brazz.jurassicadventure.machines.allsettingsmachine;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level; // << IMPORT CORRIGIDO
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class AllSettingsMenu extends AbstractContainerMenu {

    public final AllSettingsEntity blockEntity;
    protected final Level level;
    protected final ContainerData data;

    // Campos para guardar os valores customizáveis da GUI
    private final int progressArrowSize;
    private final int energyBarHeight;

    public AllSettingsMenu(MenuType<?> pMenuType, int pContainerId, Inventory inv, BlockEntity entity, ContainerData data, int itemHandlerSize, int progressArrowSize, int energyBarHeight) {
        super(pMenuType, pContainerId);
        checkContainerSize(inv, itemHandlerSize);
        this.blockEntity = (AllSettingsEntity) entity;
        this.level = inv.player.level();
        this.data = data;
        
        // Armazena os valores recebidos para uso nos métodos da GUI
        this.progressArrowSize = progressArrowSize;
        this.energyBarHeight = energyBarHeight;

        addDataSlots(data);
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    protected class OutputSlot extends SlotItemHandler {
        public OutputSlot(ItemStackHandler itemHandler, int index, int x, int y) {
            super(itemHandler, index, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }
    }

    protected abstract int getMachineInventorySlotCount();

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        final int HOTBAR_SLOT_COUNT = 9;
        final int PLAYER_INVENTORY_ROW_COUNT = 3;
        final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
        final int PLAYER_INVENTORY_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_ROW_COUNT * PLAYER_INVENTORY_COLUMN_COUNT;
        
        final int VANILLA_FIRST_SLOT_INDEX = 0;
        final int VANILLA_LAST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT - 1;
        
        final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_LAST_SLOT_INDEX + 1;
        final int TE_INVENTORY_SLOT_COUNT = this.getMachineInventorySlotCount();
        final int TE_INVENTORY_LAST_SLOT_INDEX = TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT - 1;

        // Item vindo da máquina, movendo para o jogador
        if (index >= TE_INVENTORY_FIRST_SLOT_INDEX && index <= TE_INVENTORY_LAST_SLOT_INDEX) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_LAST_SLOT_INDEX + 1, false)) {
                return ItemStack.EMPTY;
            }
        } 
        // Item vindo do jogador, movendo para a máquina
        else if (index >= VANILLA_FIRST_SLOT_INDEX && index <= VANILLA_LAST_SLOT_INDEX) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_LAST_SLOT_INDEX + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Índice de slot inválido no quickMoveStack: " + index);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        if (sourceStack.getCount() == copyOfSourceStack.getCount()) {
            return ItemStack.EMPTY;
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }
    
    // --- MÉTODOS DE LÓGICA DA GUI ---
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        // Usa a variável da classe, tornando-o customizável
        return maxProgress != 0 && progress != 0 ? progress * this.progressArrowSize / maxProgress : 0;
    }

    public int getEnergyStored() {
        return this.data.get(2);
    }

    public int getMaxEnergyStored() {
        return this.data.get(3);
    }

    public int getEnergyHeight() {
        int maxEnergy = this.getMaxEnergyStored();
        if (maxEnergy == 0) return 0;
        // Usa a variável da classe, tornando-o customizável
        return (int) (this.energyBarHeight * ((float) this.getEnergyStored() / maxEnergy));
    }
}