package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.client.model.VelociraptorModel;
import com.brazz.jurassicadventure.dinosconfig.entity.VelociraptorEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VelociraptorRenderer extends GeoEntityRenderer<VelociraptorEntity> {
    public VelociraptorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VelociraptorModel());
        this.shadowRadius = 1.0f; 
    }
}