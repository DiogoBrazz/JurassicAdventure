package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.client.model.MosassauroModel;
import com.brazz.jurassicadventure.dinosconfig.entity.MosassauroEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MosassauroRenderer extends GeoEntityRenderer<MosassauroEntity> {
    public MosassauroRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MosassauroModel());
        this.shadowRadius = 1.0f; 
    }
}