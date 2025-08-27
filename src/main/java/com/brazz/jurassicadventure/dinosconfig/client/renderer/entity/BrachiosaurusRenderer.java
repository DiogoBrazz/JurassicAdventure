package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.client.model.BrachiosaurusModel;
import com.brazz.jurassicadventure.dinosconfig.entity.BrachiosaurusEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BrachiosaurusRenderer extends GeoEntityRenderer<BrachiosaurusEntity> {
    public BrachiosaurusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BrachiosaurusModel());
        this.shadowRadius = 1.0f; 
    }
}