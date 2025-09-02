package com.brazz.jurassicadventure.machines.electricfence;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.energy.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectricFenceBlockEntity extends BlockEntity {
    private final CustomEnergyStorage energyStorage;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    // Balanceamento da Cerca
    private static final int CAPACITY = 128;      
    private static final int TRANSFER_RATE = 32; // Transfere energia mais lentamente que os cabos

    public ElectricFenceBlockEntity(BlockPos pPos, BlockState pState) {
        super(ModBlockEntities.ELECTRIC_FENCE_BE.get(), pPos, pState);
        this.energyStorage = new CustomEnergyStorage(CAPACITY, TRANSFER_RATE);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, ElectricFenceBlockEntity pBlockEntity) {
        if (pLevel.isClientSide()) return;
        if (pBlockEntity.energyStorage.getEnergyStored() > 0) {
            pBlockEntity.distributeEnergy();
        }
    }

    private void distributeEnergy() {
        int energyAvailableToSend = Math.min(this.energyStorage.getEnergyStored(), TRANSFER_RATE);
        if (energyAvailableToSend <= 0) return;

        for (Direction direction : Direction.values()) {
            BlockEntity be = level.getBlockEntity(worldPosition.relative(direction));
            if (be instanceof ElectricFenceBlockEntity neighborFence) {
                
                // << CORREÇÃO: Usando if (isPresent()) em vez de .ifPresent() >>
                LazyOptional<IEnergyStorage> lazyNeighbor = neighborFence.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());
                if (lazyNeighbor.isPresent()) {
                    IEnergyStorage neighborStorage = lazyNeighbor.resolve().get();

                    if (neighborStorage.canReceive() && this.energyStorage.getEnergyStored() > neighborStorage.getEnergyStored()) {
                        int diff = this.energyStorage.getEnergyStored() - neighborStorage.getEnergyStored();
                        int amountToSend = (diff + 1) / 2;
                        amountToSend = Math.min(amountToSend, energyAvailableToSend);
                        
                        if(amountToSend > 0) {
                            int accepted = neighborStorage.receiveEnergy(amountToSend, false);
                            this.energyStorage.extractEnergy(accepted, false);
                            // Agora esta linha funciona sem erros
                            energyAvailableToSend -= accepted;
                        }
                    }
                }
            }

            if (energyAvailableToSend <= 0) break;
        }
    }

    public CustomEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    // --- Métodos de Base (Capabilities, NBT, etc.) ---
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
        return super.getCapability(cap, side);
    }
    @Override
    public void onLoad() {
        super.onLoad();
        this.lazyEnergyHandler = LazyOptional.of(() -> this.energyStorage);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyEnergyHandler.invalidate();
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("energy", energyStorage.getEnergyStored());
        super.saveAdditional(nbt);
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        energyStorage.setEnergy(nbt.getInt("energy"));
    }
}