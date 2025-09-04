package com.brazz.jurassicadventure.machines.incubator;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.ModItems;
import com.brazz.jurassicadventure.energy.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IncubatorBlockEntity extends BlockEntity implements MenuProvider {

    // --- LÓGICA E ESTADO DA MÁQUINA ---
    public enum Status { EMPTY, INCUBATING, READY_TO_HATCH }
    private Status status = Status.EMPTY;

    // --- INVENTÁRIO, ENERGIA E DADOS DA GUI (Antes em AllSettingsEntity) ---
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final CustomEnergyStorage energyStorage;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    
    protected final ContainerData data;
    private int progress = 0;
    private final int maxProgress = 600; // Total de ticks para incubar

    // Balanceamento
    private static final int ENERGY_CAPACITY = 40000; // Um buffer grande, digno de uma máquina final
    private static final int ENERGY_RECEIVE_RATE = 32;
    private static final int ENERGY_CONSUMPTION_PER_TICK = 16;

    public IncubatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.INCUBATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.energyStorage = new CustomEnergyStorage(ENERGY_CAPACITY, ENERGY_RECEIVE_RATE, ENERGY_RECEIVE_RATE);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> IncubatorBlockEntity.this.progress;
                    case 1 -> IncubatorBlockEntity.this.maxProgress;
                    case 2 -> IncubatorBlockEntity.this.energyStorage.getEnergyStored();
                    case 3 -> IncubatorBlockEntity.this.energyStorage.getMaxEnergyStored();
                    case 4 -> IncubatorBlockEntity.this.status.ordinal();
                    default -> 0;
                };
            }
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> IncubatorBlockEntity.this.progress = value;
                }
            }
            public int getCount() {
                return 5;
            }
        };
    }

    // --- LÓGICA DE FUNCIONAMENTO (TICK) ---
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        switch (status) {
            case EMPTY:
                if (hasValidEgg() && hasEnoughEnergy()) {
                    status = Status.INCUBATING;
                    setChanged();
                }
                break;
            case INCUBATING:
                if (hasValidEgg() && hasEnoughEnergy()) {
                    energyStorage.extractEnergy(ENERGY_CONSUMPTION_PER_TICK, false);
                    progress++;
                    if (progress >= maxProgress) {
                        status = Status.READY_TO_HATCH;
                        progress = maxProgress; 
                    }
                    setChanged();
                } else {
                    status = Status.EMPTY;
                    progress = 0;
                    setChanged();
                }
                break;
            case READY_TO_HATCH:
                // << CORREÇÃO DO BUG #2: VERIFICAÇÃO CONSTANTE >>
                // Se o ovo for removido, mesmo no estado "Pronto", a máquina deve resetar.
                if (!hasValidEgg()) {
                    status = Status.EMPTY;
                    progress = 0;
                    setChanged();
                }
                break;
        }
    }


    public void hatch() {
        if (status != Status.READY_TO_HATCH || level == null || level.isClientSide()) {
            return;
        }

        ItemStack egg = itemHandler.getStackInSlot(0);
        if (egg.isEmpty() || egg.getItem() != ModItems.DINOSAUR_EGG.get()) {
            status = Status.EMPTY;
            progress = 0;
            setChanged();
            return;
        }

        if (egg.hasTag() && egg.getTag().contains("dino_type")) {
            String dinoId = egg.getTag().getString("dino_type");
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(dinoId));
            
            if (entityType != null) {
                // --- A LÓGICA CORRETA DE SPAWN ---
                
                // 1. Cria um "pacote de dados" NBT para enviar com o spawn.
                CompoundTag spawnData = new CompoundTag();
                spawnData.putBoolean("IsIncubatorBaby", true); // Envia a "ordem": "Nasça como um bebê da incubadora"

                System.out.println("INCUBADORA: Enviando ordem 'IsIncubatorBaby: true' para o spawn.");

                // 2. Chama o método de spawn que aceita esses dados extras.
                entityType.spawn((ServerLevel) this.level, spawnData, null, this.worldPosition.above(), 
                                MobSpawnType.SPAWN_EGG, false, false);
                
                // ---------------------------------
                
                // Apenas reseta se o spawn foi bem-sucedido
                itemHandler.extractItem(0, 1, false);
                status = Status.EMPTY;
                progress = 0;
                setChanged();
            } else {
                System.out.println("ERRO: Dino com ID '" + dinoId + "' não encontrado!");
            }
        }
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    private boolean hasValidEgg() {
        return itemHandler.getStackInSlot(0).getItem() == ModItems.DINOSAUR_EGG.get();
    }

    private boolean hasEnoughEnergy() {
        return energyStorage.getEnergyStored() >= ENERGY_CONSUMPTION_PER_TICK;
    }

    // --- MÉTODOS DE BASE (Antes em AllSettingsEntity) ---
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
        if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }
    @Override
    public void onLoad() {
        super.onLoad();
        this.lazyItemHandler = LazyOptional.of(() -> this.itemHandler);
        this.lazyEnergyHandler = LazyOptional.of(() -> this.energyStorage);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.lazyItemHandler.invalidate();
        this.lazyEnergyHandler.invalidate();
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", this.itemHandler.serializeNBT());
        nbt.putInt("progress", progress);
        nbt.putInt("energy", energyStorage.getEnergyStored());
        nbt.putInt("status", this.status.ordinal());
        super.saveAdditional(nbt);
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.progress = nbt.getInt("progress");
        this.energyStorage.setEnergy(nbt.getInt("energy"));
        this.status = Status.values()[nbt.getInt("status")];
    }
    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Incubadora");
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new IncubatorMenu(pContainerId, pPlayerInventory, this, this.data);
    }
}