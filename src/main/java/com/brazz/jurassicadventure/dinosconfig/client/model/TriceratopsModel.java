package com.brazz.jurassicadventure.dinosconfig.client.model;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.dinosconfig.entity.TriceratopsEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TriceratopsModel extends GeoModel<TriceratopsEntity> {
    @Override
    public ResourceLocation getModelResource(TriceratopsEntity animatable) {
        // Aponta para o ficheiro da forma 3D
        return new ResourceLocation(JurassicAdventure.MODID, "geo/triceratops.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TriceratopsEntity animatable) {
        // Aponta para o ficheiro da textura
        return new ResourceLocation(JurassicAdventure.MODID, "textures/entity/triceratops.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TriceratopsEntity animatable) {
        // Aponta para o ficheiro das animações
        return new ResourceLocation(JurassicAdventure.MODID, "animations/triceratops.animation.json");
    }
}
