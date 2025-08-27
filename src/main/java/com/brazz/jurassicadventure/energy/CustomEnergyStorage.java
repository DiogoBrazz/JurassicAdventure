package com.brazz.jurassicadventure.energy;

import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public void setEnergy(int energy) {
        this.energy = Math.min(capacity, energy);
    }

    // --- MÉTODO NOVO ADICIONADO AQUI ---
    /**
     * Adiciona energia ao buffer interno, ignorando o limite de 'maxReceive'.
     * Deve ser usado para a GERAÇÃO de energia, não para recebimento de outros blocos.
     */
    public void produceEnergy(int amount) {
        this.energy += amount;
        if (this.energy > capacity) {
            this.energy = capacity;
        }
    }
}