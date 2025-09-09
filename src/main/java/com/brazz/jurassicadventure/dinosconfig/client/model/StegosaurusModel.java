package com.brazz.jurassicadventure.dinosconfig.client.model;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.dinosconfig.entity.StegosaurusEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StegosaurusModel extends GeoModel<StegosaurusEntity> {
    @Override
    public ResourceLocation getModelResource(StegosaurusEntity animatable) {
        // Aponta para o ficheiro da forma 3D
        return new ResourceLocation(JurassicAdventure.MODID, "geo/stegosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StegosaurusEntity animatable) {
        // Aponta para o ficheiro da textura
        return new ResourceLocation(JurassicAdventure.MODID, "textures/entity/stegosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StegosaurusEntity animatable) {
        // Aponta para o ficheiro das animações
        return new ResourceLocation(JurassicAdventure.MODID, "animations/stegosaurus.animation.json");
    }
}
