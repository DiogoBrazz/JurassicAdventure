package com.brazz.jurassicadventure.machines.generator;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.energy.CustomEnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    // --- MUDANÇAS PRINCIPAIS AQUI ---
    private final CustomEnergyStorage energyStorage;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 0; // O tempo de queima do combustível

    private static final int ENERGY_CAPACITY = 10000;       //Armazenamento
    private static final int ENERGY_TRANSFER_RATE = 32;     //Velocidade de tranferencia
    private static final int ENERGY_GENERATION_RATE = 4;   //Quantidade de energia gerada por tick
    private static final int FUEL_EFFICIENCY_DIVISOR = 4; //Divide o tempo de queima total do item por 4, diminui a geração padrão em 4x

    public GeneratorBlockEntity(BlockPos pPos, BlockState pState) {
        super(ModBlockEntities.GENERATOR_BLOCK_ENTITY.get(), pPos, pState);

        // << 2. USANDO CustomEnergyStorage (Capacidade, Max. Recebimento, Max. Extração)
        // Gerador não recebe energia (0), apenas extrai (256).
        this.energyStorage = new CustomEnergyStorage(ENERGY_CAPACITY, 0, ENERGY_TRANSFER_RATE);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> GeneratorBlockEntity.this.progress;
                    case 1 -> GeneratorBlockEntity.this.maxProgress;
                    // --- ADICIONADO ---
                    case 2 -> GeneratorBlockEntity.this.energyStorage.getEnergyStored();
                    case 3 -> GeneratorBlockEntity.this.energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }
            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> GeneratorBlockEntity.this.progress = pValue;
                    case 1 -> GeneratorBlockEntity.this.maxProgress = pValue;
                    // --- ADICIONADO (O cliente não deve setar a energia, mas é boa prática) ---
                    case 2 -> GeneratorBlockEntity.this.energyStorage.setEnergy(pValue);
                    case 3 -> { /* Max energy não é setável */ }
                }
            }

            @Override
            public int getCount() {
                // --- CORRIGIDO ---
                return 4; // Agora estamos sincronizando 4 valores
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Gerador de Energia");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GeneratorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
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
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("generator.progress", progress);
        pTag.putInt("generator.maxProgress", maxProgress);
        // << 3. SALVANDO ENERGIA CORRETAMENTE
        pTag.putInt("energy", energyStorage.getEnergyStored());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("generator.progress");
        maxProgress = pTag.getInt("generator.maxProgress");
        // << 4. CARREGANDO ENERGIA CORRETAMENTE
        energyStorage.setEnergy(pTag.getInt("energy"));
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, GeneratorBlockEntity pBlockEntity) {
        if (pLevel.isClientSide()) {
            return;
        }

        if (isBurning(pBlockEntity)) {
            pBlockEntity.progress--;
            generateEnergy(pBlockEntity, ENERGY_GENERATION_RATE);
            setChanged(pLevel, pPos, pState);
        } else {
            ItemStack fuel = pBlockEntity.itemHandler.getStackInSlot(0);
            int burnTime = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
            int effectiveBurnTime = burnTime / FUEL_EFFICIENCY_DIVISOR;
            if (effectiveBurnTime > 0) {
                pBlockEntity.itemHandler.extractItem(0, 1, false);
                pBlockEntity.maxProgress = effectiveBurnTime;
                pBlockEntity.progress = pBlockEntity.maxProgress;
                setChanged(pLevel, pPos, pState);
            }
        }

        // Lógica para transferir energia para blocos vizinhos
        transferEnergy(pBlockEntity);
    }

    private static void transferEnergy(GeneratorBlockEntity pBlockEntity) {
        if (pBlockEntity.energyStorage.getEnergyStored() <= 0) {
            return;
        }

        // Simplesmente tenta empurrar energia para os 6 vizinhos diretos
        for(Direction direction : Direction.values()) {
            BlockEntity neighbor = pBlockEntity.getLevel().getBlockEntity(pBlockEntity.getBlockPos().relative(direction));
            if(neighbor != null) {
                neighbor.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                    if (storage.canReceive()) {
                        // Simula quanto pode extrair (sem chamar o método problemático)
                        int energyToSend = pBlockEntity.energyStorage.extractEnergy(Integer.MAX_VALUE, true);

                        // Envia a energia
                        int received = storage.receiveEnergy(energyToSend, false);

                        // Extrai do gerador o que foi realmente aceito
                        pBlockEntity.energyStorage.extractEnergy(received, false);
                    }
                });
            }
        }
    }


    private static void generateEnergy(GeneratorBlockEntity pBlockEntity, int amount) {
        // O método "receiveEnergy" aqui significa que o BUFFER INTERNO está recebendo a energia gerada
        pBlockEntity.energyStorage.produceEnergy(amount); 
    }

    private static boolean isBurning(GeneratorBlockEntity pBlockEntity) {
        return pBlockEntity.progress > 0;
    }
    
}