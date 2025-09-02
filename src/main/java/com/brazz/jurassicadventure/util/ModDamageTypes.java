// Em ModDamageTypes.java
package com.brazz.jurassicadventure.util;

import com.brazz.jurassicadventure.JurassicAdventure;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> ELECTRIC_SHOCK = ResourceKey.create(Registries.DAMAGE_TYPE,
            new ResourceLocation(JurassicAdventure.MODID, "electric_shock"));
}