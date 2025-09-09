package com.brazz.jurassicadventure.dinosconfig.client.model;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.dinosconfig.entity.AllosaurusEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AllosaurusModel extends GeoModel<AllosaurusEntity> {
    @Override
    public ResourceLocation getModelResource(AllosaurusEntity animatable) {
        // Aponta para o ficheiro da forma 3D
        return new ResourceLocation(JurassicAdventure.MODID, "geo/allosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AllosaurusEntity animatable) {
        // Aponta para o ficheiro da textura
        return new ResourceLocation(JurassicAdventure.MODID, "textures/entity/allosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AllosaurusEntity animatable) {
        // Aponta para o ficheiro das animações
        return new ResourceLocation(JurassicAdventure.MODID, "animations/allosaurus.animation.json");
    }
}
