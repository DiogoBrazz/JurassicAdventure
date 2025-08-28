package com.brazz.jurassicadventure.machines.sequencer;

import com.brazz.jurassicadventure.ModBlocks;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.ModMenuTypes;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsMenu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SequencerMenu extends AllSettingsMenu {
    public final SequencerBlockEntity blockEntity;
    private final Level level;

    public SequencerMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(4)); //número de campos de dados que você quer expor: progress / maxProgress
    }

    @Override
    protected int getMachineInventorySlotCount() {
        return 4; //IMPORTANTE, COLOCAR O NUMERO DE SLOTS DA MÁQUINA
    }

    public SequencerMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.SEQUENCER_MENU.get(), pContainerId, inv, entity, data, 8, 21, 42);
        checkContainerSize(inv, this.getMachineInventorySlotCount());
        this.blockEntity = (SequencerBlockEntity) entity;
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 0, 58, 11) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ModItems.DNA_SYRINGE.get();
            }
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 1, 102, 11) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ModItems.DNA_SYRINGE.get();
            }
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 2, 16, 13) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ModItems.TUBE.get();
            }
        });
        this.addSlot(new OutputSlot(this.blockEntity.getItemHandler(), 3, 80, 57));

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer,
                ModBlocks.SEQUENCER.get());
    }

}