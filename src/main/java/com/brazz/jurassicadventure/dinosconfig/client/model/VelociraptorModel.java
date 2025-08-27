package com.brazz.jurassicadventure.dinosconfig.client.model;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.dinosconfig.entity.VelociraptorEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VelociraptorModel extends GeoModel<VelociraptorEntity> {
    @Override
    public ResourceLocation getModelResource(VelociraptorEntity animatable) {
        // Aponta para o ficheiro da forma 3D
        return new ResourceLocation(JurassicAdventure.MODID, "geo/velociraptor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VelociraptorEntity animatable) {
        // Aponta para o ficheiro da textura
        return new ResourceLocation(JurassicAdventure.MODID, "textures/entity/velociraptor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VelociraptorEntity animatable) {
        // Aponta para o ficheiro das animações
        return new ResourceLocation(JurassicAdventure.MODID, "animations/velociraptor.animation.json");
    }
}