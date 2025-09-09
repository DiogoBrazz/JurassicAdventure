package com.brazz.jurassicadventure.dinosconfig.client.model;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.dinosconfig.entity.AnkylosaurusEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AnkylosaurusModel extends GeoModel<AnkylosaurusEntity> {
    @Override
    public ResourceLocation getModelResource(AnkylosaurusEntity animatable) {
        // Aponta para o ficheiro da forma 3D
        return new ResourceLocation(JurassicAdventure.MODID, "geo/ankylosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AnkylosaurusEntity animatable) {
        // Aponta para o ficheiro da textura
        return new ResourceLocation(JurassicAdventure.MODID, "textures/entity/ankylosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AnkylosaurusEntity animatable) {
        // Aponta para o ficheiro das animações
        return new ResourceLocation(JurassicAdventure.MODID, "animations/ankylosaurus.animation.json");
    }
}
