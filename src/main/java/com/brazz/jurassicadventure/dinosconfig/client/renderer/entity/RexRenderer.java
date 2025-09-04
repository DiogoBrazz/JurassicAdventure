package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.entity.AllDinos;
import com.brazz.jurassicadventure.dinosconfig.entity.RexEntity;
import com.brazz.jurassicadventure.dinosconfig.client.model.RexModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RexRenderer extends GeoEntityRenderer<RexEntity> {
    
    public RexRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RexModel());
    }

    @Override
    public void render(RexEntity entity, float entityYaw, float partialTick, PoseStack poseStack, 
                      MultiBufferSource bufferSource, int packedLight) {
        float scale = entity.getScale();
        
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        
        if (entity.isBaby()) {
            poseStack.translate(0, (1.0 - scale) * 0.5, 0);
        }
        
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}