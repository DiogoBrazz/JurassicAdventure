package com.brazz.jurassicadventure.util;

import net.minecraft.resources.ResourceLocation;
import java.util.List;

import com.brazz.jurassicadventure.JurassicAdventure;

public class MobTypes {
    public static final ResourceLocation COW = new ResourceLocation("minecraft", "cow");
    public static final ResourceLocation PIG = new ResourceLocation("minecraft", "pig");
    public static final ResourceLocation CHICKEN = new ResourceLocation("minecraft", "chicken");
    public static final ResourceLocation SHEEP = new ResourceLocation("minecraft", "sheep");
    public static final ResourceLocation SALMON = new ResourceLocation("minecraft", "salmon");
    public static final ResourceLocation RIVER_FROG = new ResourceLocation(JurassicAdventure.MODID, "river_frog");

    public static final List<ResourceLocation> ALL_VANILLA_MOBS = List.of(
            COW, PIG, CHICKEN, SHEEP, RIVER_FROG, SALMON
    );
}