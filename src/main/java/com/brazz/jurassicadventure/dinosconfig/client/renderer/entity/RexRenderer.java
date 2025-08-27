package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.client.model.RexModel;
import com.brazz.jurassicadventure.dinosconfig.entity.RexEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RexRenderer extends GeoEntityRenderer<RexEntity> {
    public RexRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RexModel());
        this.shadowRadius = 1.5f; // A sombra do T-Rex no chão
    }
}