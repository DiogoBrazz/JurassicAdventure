package com.brazz.jurassicadventure.dinosconfig.client.model;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.dinosconfig.entity.GallimimusEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GallimimusModel extends GeoModel<GallimimusEntity> {
    @Override
    public ResourceLocation getModelResource(GallimimusEntity animatable) {
        // Aponta para o ficheiro da forma 3D
        return new ResourceLocation(JurassicAdventure.MODID, "geo/gallimimus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GallimimusEntity animatable) {
        // Aponta para o ficheiro da textura
        return new ResourceLocation(JurassicAdventure.MODID, "textures/entity/gallimimus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GallimimusEntity animatable) {
        // Aponta para o ficheiro das animações
        return new ResourceLocation(JurassicAdventure.MODID, "animations/gallimimus.animation.json");
    }
}
