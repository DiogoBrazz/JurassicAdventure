package com.brazz.jurassicadventure.dinosconfig.client.model;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.dinosconfig.entity.BrachiosaurusEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BrachiosaurusModel extends GeoModel<BrachiosaurusEntity> {
    @Override
    public ResourceLocation getModelResource(BrachiosaurusEntity animatable) {
        // Aponta para o ficheiro da forma 3D
        return new ResourceLocation(JurassicAdventure.MODID, "geo/brachiosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BrachiosaurusEntity animatable) {
        // Aponta para o ficheiro da textura
        return new ResourceLocation(JurassicAdventure.MODID, "textures/entity/brachiosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BrachiosaurusEntity animatable) {
        // Aponta para o ficheiro das animações
        return new ResourceLocation(JurassicAdventure.MODID, "animations/brachiosaurus.animation.json");
    }
}