package com.brazz.jurassicadventure.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

// A interface define o que a nossa mochila PODE FAZER.
// É como uma lista de botões e visores que a mochila tem.
public interface IPregnancy {
    boolean isPregnant();
    void setPregnant(ResourceLocation dinoType, int ticks, boolean isOviparous);
    void tickDown();
    int getGestationTicks();
    ResourceLocation getDinoType();
    boolean isOviparous(); // Adicionámos o novo "visor"

    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag nbt);
}