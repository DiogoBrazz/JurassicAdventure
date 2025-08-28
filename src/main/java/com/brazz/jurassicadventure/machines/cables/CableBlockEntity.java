// PACOTE: src/main/java/com/brazz/jurassicadventure/machines/cables/CableBlockEntity.java
package com.brazz.jurassicadventure.machines.cables;

import com.brazz.jurassicadventure.ModBlockEntities;
import com.brazz.jurassicadventure.energy.CustomEnergyStorage; // << IMPORT ATUALIZADO
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

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CableBlockEntity extends BlockEntity {
    // Usando sua nova classe customizada
    private final CustomEnergyStorage energyStorage;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private static final int CAPACITY = 256;
    private static final int TRANSFER_RATE = 256;

    public CableBlockEntity(BlockPos pPos, BlockState pState) {
        super(ModBlockEntities.CABLE_BLOCK_ENTITY.get(), pPos, pState);
        // Agora o construtor é mais simples
        this.energyStorage = new CustomEnergyStorage(CAPACITY, TRANSFER_RATE);
    }

    // ... (o método tick e distributeEnergy continuam exatamente iguais) ...
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, CableBlockEntity pBlockEntity) {
        // A lógica do cabo só roda se ele tiver energia.
        if(pBlockEntity.energyStorage.getEnergyStored() > 0) {
            pBlockEntity.distributeEnergy();
        }
    }

    private void distributeEnergy() {
        int energyAvailableToSend = Math.min(this.energyStorage.getEnergyStored(), TRANSFER_RATE);
        if (energyAvailableToSend <= 0) return;

        // --- PASSO 1: Separar os vizinhos em duas listas: máquinas e cabos ---
        List<IEnergyStorage> machineSinks = new ArrayList<>();
        List<IEnergyStorage> cableSinks = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockEntity be = level.getBlockEntity(worldPosition.relative(direction));
            if (be == null) continue;

            LazyOptional<IEnergyStorage> lazyNeighbor = be.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());
            if (lazyNeighbor.isPresent()) {
                IEnergyStorage neighborStorage = lazyNeighbor.resolve().get();
                if (!neighborStorage.canReceive()) continue;

                // Se o vizinho NÃO é um cabo, é uma máquina/armazenador
                if (!(be instanceof CableBlockEntity)) {
                    if (neighborStorage.getEnergyStored() < neighborStorage.getMaxEnergyStored()) {
                        machineSinks.add(neighborStorage);
                    }
                } 
                // Se o vizinho É um cabo, usamos a lógica de balanceamento
                else {
                    if (this.energyStorage.getEnergyStored() > neighborStorage.getEnergyStored()) {
                        cableSinks.add(neighborStorage);
                    }
                }
            }
        }

        // --- PASSO 2: PRIORIDADE - Enviar energia para as máquinas primeiro ---
        if (!machineSinks.isEmpty()) {
            // Distribui a energia de forma justa entre todas as máquinas que precisam
            int energyPerMachine = energyAvailableToSend / machineSinks.size();
            int remainder = energyAvailableToSend % machineSinks.size();

            for (IEnergyStorage machine : machineSinks) {
                if (energyAvailableToSend <= 0) break; // Para se a energia do tick acabar

                int amountForThisMachine = energyPerMachine;
                if (remainder > 0) {
                    amountForThisMachine++;
                    remainder--;
                }

                int accepted = machine.receiveEnergy(amountForThisMachine, false);
                this.energyStorage.extractEnergy(accepted, false);
                energyAvailableToSend -= accepted;
            }
        }

        // --- PASSO 3: Usar a energia que sobrou para balancear com outros cabos ---
        if (energyAvailableToSend > 0 && !cableSinks.isEmpty()) {
            for (IEnergyStorage cableNeighbor : cableSinks) {
                if (energyAvailableToSend <= 0) break;

                // Lógica de balanceamento: envia metade da diferença
                int diff = this.energyStorage.getEnergyStored() - cableNeighbor.getEnergyStored();
                int amountToSend = (diff + 1) / 2;
                amountToSend = Math.min(amountToSend, energyAvailableToSend); // Limita ao que ainda podemos enviar

                if (amountToSend > 0) {
                    int accepted = cableNeighbor.receiveEnergy(amountToSend, false);
                    this.energyStorage.extractEnergy(accepted, false);
                    energyAvailableToSend -= accepted;
                }
            }
        }
    }

    // --- MÉTODOS DE CAPABILITY E NBT (agora corrigidos) ---

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("energy", energyStorage.getEnergyStored());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        // AGORA O MÉTODO EXISTE E O ERRO VAI SUMIR!
        energyStorage.setEnergy(pTag.getInt("energy"));
    }
}