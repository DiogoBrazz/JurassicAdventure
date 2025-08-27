package com.brazz.jurassicadventure.machines.generator;

import com.brazz.jurassicadventure.ModBlocks;
import com.brazz.jurassicadventure.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.SlotItemHandler;

public class GeneratorMenu extends AbstractContainerMenu {

    public final GeneratorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public GeneratorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public GeneratorMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.GENERATOR_MENU.get(), containerId);
        
        if (entity instanceof GeneratorBlockEntity generator) {
            this.blockEntity = generator;
            this.level = inv.player.level();
            this.data = data;
            
            checkContainerSize(inv, 1);

            // Slot de combustível
            this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 80, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return ForgeHooks.getBurnTime(stack, null) > 0;
                }
            });

            // Inventário do jogador
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 9; col++) {
                    this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
                }
            }

            // Hotbar
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
            }

            addDataSlots(data);
        } else {
            throw new IllegalStateException("Block entity is not a GeneratorBlockEntity");
        }
    }

    public int getBurnProgress() {
        int burnTime = data.get(0);
        int burnTimeTotal = data.get(1);
        if (burnTimeTotal == 0 || burnTime == 0) return 0;
        return burnTime * 24 / burnTimeTotal;
    }

    public int getEnergyStored() {
        return data.get(2);
    }

    public int getMaxEnergyStored() {
        return data.get(3);
    }

    public int getEnergyHeight() {
        int energy = getEnergyStored();
        int maxEnergy = getMaxEnergyStored();
        if (maxEnergy == 0) return 0;
        return (int) (56 * ((float) energy / maxEnergy));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), 
                         player, ModBlocks.GENERATOR.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Slot da máquina (0)
            if (index == 0) {
                // Mover para inventário do jogador
                if (!this.moveItemStackTo(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } 
            // Slots do jogador (1-36)
            else {
                // Verificar se é combustível
                if (ForgeHooks.getBurnTime(itemstack1, null) > 0) {
                    // Tentar mover para slot da máquina
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } 
                // Slots do inventário (1-27)
                else if (index >= 1 && index < 28) {
                    // Mover para hotbar
                    if (!this.moveItemStackTo(itemstack1, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } 
                // Slots da hotbar (28-36)
                else if (index >= 28 && index < 37) {
                    // Mover para inventário
                    if (!this.moveItemStackTo(itemstack1, 1, 28, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}