package com.brazz.jurassicadventure.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

// A implementação que GUARDA os dados.
public class Pregnancy implements IPregnancy {
    private int gestationTicks = 0;
    private String dinoType = "";
    private boolean isOviparous = false; // O novo campo para guardar a informação

    @Override public boolean isPregnant() { return this.gestationTicks > 0; }
    @Override public int getGestationTicks() { return this.gestationTicks; }
    
    @SuppressWarnings("removal")
    @Override public ResourceLocation getDinoType() { return new ResourceLocation(this.dinoType); }
    @Override public boolean isOviparous() { return this.isOviparous; }

    @Override
    public void setPregnant(ResourceLocation dinoType, int ticks, boolean isOviparous) {
        this.dinoType = dinoType.toString();
        this.gestationTicks = ticks;
        this.isOviparous = isOviparous; // Guardamos a nova informação
    }

    @Override
    public void tickDown() {
        if (this.isPregnant()) {
            this.gestationTicks--;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("dinoType", this.dinoType);
        nbt.putInt("gestationTicks", this.gestationTicks);
        nbt.putBoolean("isOviparous", this.isOviparous); // Guardamos a nova informação
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.dinoType = nbt.getString("dinoType");
        this.gestationTicks = nbt.getInt("gestationTicks");
        this.isOviparous = nbt.getBoolean("isOviparous"); // Carregamos a nova informação
    }
}