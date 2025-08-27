package com.brazz.jurassicadventure.machines.allsettingsmachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public abstract class AllSettingsEntity extends BlockEntity implements MenuProvider, AllSettingsBlock.ItemStackHandlerProvider{

    protected final ItemStackHandler itemHandler;
    protected final ContainerData data;
    protected int progress = 0;
    protected int maxProgress;

    public AllSettingsEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int inventorySize, int maxProgress) {
        super(pType, pPos, pBlockState);
        this.itemHandler = new ItemStackHandler(inventorySize);
        this.maxProgress = maxProgress;
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> AllSettingsEntity.this.progress;
                    case 1 -> AllSettingsEntity.this.maxProgress;
                    default -> 0;
                };
            }
            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> AllSettingsEntity.this.progress = pValue;
                    case 1 -> AllSettingsEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        // Implementação padrão: Pega no ID do bloco (ex: "block.jurassicadventure.analyzer")
        // e formata-o para "Analyzer". As classes filhas podem sobrescrever isto se quiserem.
        String blockId = this.getBlockState().getBlock().getDescriptionId();
        String formattedName = blockId.replace("block.jurassicadventure.", "").replace("_", " ");
        // Lógica simples para capitalizar a primeira letra
        return Component.literal(formattedName.substring(0, 1).toUpperCase() + formattedName.substring(1));
    }

    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer);

    @Override
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", this.itemHandler.serializeNBT());
        nbt.putInt("progress", progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("progress");
    }
}
