package com.brazz.jurassicadventure.machines.allsettingsmachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.brazz.jurassicadventure.energy.CustomEnergyStorage;

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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class AllSettingsEntity extends BlockEntity implements MenuProvider, AllSettingsBlock.ItemStackHandlerProvider{

    protected final ItemStackHandler itemHandler;
    protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final CustomEnergyStorage energyStorage;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;
    protected int progress = 0;
    protected int maxProgress;

    // << Constantes de energia compartilhadas por todas as máquinas
    private static final int ENERGY_CAPACITY = 1000;
    private static final int ENERGY_RECEIVE_RATE = 32;

    public AllSettingsEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int itemHandlerSize, int maxProgress) {
        super(pType, pPos, pBlockState);
        this.itemHandler = new ItemStackHandler(itemHandlerSize);
        this.maxProgress = maxProgress;

        // << Inicializa o "tanque" de energia. Máquinas só recebem (extração = 0).
        this.energyStorage = new CustomEnergyStorage(ENERGY_CAPACITY, ENERGY_RECEIVE_RATE, ENERGY_RECEIVE_RATE);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AllSettingsEntity.this.progress;
                    case 1 -> AllSettingsEntity.this.maxProgress;
                    case 2 -> AllSettingsEntity.this.energyStorage.getEnergyStored();
                    case 3 -> AllSettingsEntity.this.energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AllSettingsEntity.this.progress = value;
                    case 1 -> AllSettingsEntity.this.maxProgress = value;
                    case 2 -> AllSettingsEntity.this.energyStorage.setEnergy(value);
                    case 3 -> { /* Max energy is not settable */ }
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
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
        super.saveAdditional(nbt);
        nbt.put("inventory", this.itemHandler.serializeNBT());
        nbt.putInt("progress", progress);
        nbt.putInt("energy", energyStorage.getEnergyStored());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("progress");
        energyStorage.setEnergy(nbt.getInt("energy"));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        // Quando o bloco é carregado, nós preenchemos as "caixas" com nossos objetos
        this.lazyItemHandler = LazyOptional.of(this::getItemHandler);
        this.lazyEnergyHandler = LazyOptional.of(() -> this.energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        // Quando o bloco é removido, nós esvaziamos as "caixas"
        this.lazyItemHandler.invalidate();
        this.lazyEnergyHandler.invalidate();
    }
}
