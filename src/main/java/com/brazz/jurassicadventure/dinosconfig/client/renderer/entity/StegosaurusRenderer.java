package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.client.model.StegosaurusModel;
import com.brazz.jurassicadventure.dinosconfig.entity.StegosaurusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StegosaurusRenderer extends GeoEntityRenderer<StegosaurusEntity> {
    public StegosaurusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StegosaurusModel());
        // Define o raio da sombra base (quando o dino é 100% adulto)
        this.shadowRadius = StegosaurusEntity.ADULT_WIDTH * 0.7f; 
    }

    @Override
    public void render(StegosaurusEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        float scale = entity.getScale();
        
        // A sombra é calculada a partir do raio base e da escala atual
        this.shadowRadius = (StegosaurusEntity.ADULT_WIDTH * 0.7f) * scale; 

        poseStack.pushPose(); 
        poseStack.scale(scale, scale, scale); 
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose(); 
    }
}
