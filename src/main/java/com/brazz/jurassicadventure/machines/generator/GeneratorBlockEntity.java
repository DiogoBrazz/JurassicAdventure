package com.brazz.jurassicadventure.machines.generator;

import com.brazz.jurassicadventure.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlockEntity extends BlockEntity implements MenuProvider {

    public final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
        
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ForgeHooks.getBurnTime(stack, null) > 0;
        }
    };

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(2000, 512, 0);
    
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);

    private int burnTime;
    private int burnTimeTotal;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> burnTime;
                case 1 -> burnTimeTotal;
                case 2 -> energyStorage.getEnergyStored();
                case 3 -> energyStorage.getMaxEnergyStored();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> burnTime = value;
                case 1 -> burnTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GENERATOR_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.jurassicadventure.generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new GeneratorMenu(containerId, inventory, this, this.data);
    }

    // Método tick com a assinatura correta para o BlockEntityTicker
    public static void tick(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity blockEntity) {
        if (level.isClientSide()) return;

        boolean changed = false;

        // Se está queimando, produz energia
        if (blockEntity.burnTime > 0) {
            blockEntity.burnTime--;
            blockEntity.energyStorage.addEnergy(1);
            changed = true;
        }

        // Se não está queimando mas tem combustível, começa a queimar
        if (blockEntity.burnTime <= 0 && blockEntity.energyStorage.getEnergyStored() < blockEntity.energyStorage.getMaxEnergyStored()) {
            ItemStack fuel = blockEntity.itemHandler.getStackInSlot(0);
            if (!fuel.isEmpty()) {
                int burnTimeForFuel = ForgeHooks.getBurnTime(fuel, null);
                if (burnTimeForFuel > 0) {
                    blockEntity.burnTime = burnTimeForFuel;
                    blockEntity.burnTimeTotal = burnTimeForFuel;
                    fuel.shrink(1);
                    changed = true;
                }
            }
        }

        if (changed) {
            blockEntity.setChanged();
        }
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.put("energy", energyStorage.serializeNBT());
        nbt.putInt("burnTime", burnTime);
        nbt.putInt("burnTimeTotal", burnTimeTotal);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        energyStorage.deserializeNBT(nbt.get("energy"));
        burnTime = nbt.getInt("burnTime");
        burnTimeTotal = nbt.getInt("burnTimeTotal");
    }

    // Custom Energy Storage para melhor controle
    public class CustomEnergyStorage extends EnergyStorage {
        public CustomEnergyStorage(int capacity, int maxTransfer, int maxExtract) {
            super(capacity, maxTransfer, maxExtract);
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }

        public void addEnergy(int energy) {
            this.energy = Math.min(this.energy + energy, capacity);
        }

        public void consumeEnergy(int energy) {
            this.energy = Math.max(this.energy - energy, 0);
        }
    }
}