package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.client.model.VelociraptorModel;
import com.brazz.jurassicadventure.dinosconfig.entity.VelociraptorEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VelociraptorRenderer extends GeoEntityRenderer<VelociraptorEntity> {
    public VelociraptorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VelociraptorModel());
        this.shadowRadius = VelociraptorEntity.SHADOW_RADIUS; 
    }

    @Override
    public void render(VelociraptorEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        float scale = entity.getScale();
        this.shadowRadius = this.shadowRadius * scale;
        poseStack.pushPose(); 
        poseStack.scale(scale, scale, scale); 
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose(); 
    }
}