package com.brazz.jurassicadventure.dinosconfig.client.model;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.dinosconfig.entity.MosassauroEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MosassauroModel extends GeoModel<MosassauroEntity> {
    @Override
    public ResourceLocation getModelResource(MosassauroEntity animatable) {
        // Aponta para o ficheiro da forma 3D
        return new ResourceLocation(JurassicAdventure.MODID, "geo/mosassauro.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MosassauroEntity animatable) {
        // Aponta para o ficheiro da textura
        return new ResourceLocation(JurassicAdventure.MODID, "textures/entity/mosassauro.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MosassauroEntity animatable) {
        // Aponta para o ficheiro das animações
        return new ResourceLocation(JurassicAdventure.MODID, "animations/mosassauro.animation.json");
    }
}