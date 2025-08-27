package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.client.model.RiverFrogModel;
import com.brazz.jurassicadventure.dinosconfig.entity.RiverFrogEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RiverFrogRenderer extends GeoEntityRenderer<RiverFrogEntity> {
    public RiverFrogRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RiverFrogModel());
        this.shadowRadius = 0.3f; // A sombra do T-Rex no chão
    }
}