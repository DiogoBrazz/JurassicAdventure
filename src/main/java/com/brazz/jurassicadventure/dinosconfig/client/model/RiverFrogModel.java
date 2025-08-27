package com.brazz.jurassicadventure.dinosconfig.client.model;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.dinosconfig.entity.RiverFrogEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RiverFrogModel extends GeoModel<RiverFrogEntity> {
    @Override
    public ResourceLocation getModelResource(RiverFrogEntity animatable) {
        // Aponta para o ficheiro da forma 3D
        return new ResourceLocation(JurassicAdventure.MODID, "geo/river_frog.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RiverFrogEntity animatable) {
        // Aponta para o ficheiro da textura
        return new ResourceLocation(JurassicAdventure.MODID, "textures/entity/river_frog.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RiverFrogEntity animatable) {
        // Aponta para o ficheiro das animações
        return new ResourceLocation(JurassicAdventure.MODID, "animations/river_frog.animation.json");
    }
}