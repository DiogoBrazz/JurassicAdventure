package com.brazz.jurassicadventure.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Este ficheiro técnico do Forge sabe como criar e ligar a nossa mochila.
public class PregnancyProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<IPregnancy> PREGNANCY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    private final IPregnancy pregnancy = new Pregnancy();
    private final LazyOptional<IPregnancy> optional = LazyOptional.of(() -> pregnancy);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PREGNANCY_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }
    
    @Override public CompoundTag serializeNBT() { return pregnancy.serializeNBT(); }
    @Override public void deserializeNBT(CompoundTag nbt) { pregnancy.deserializeNBT(nbt); }
}