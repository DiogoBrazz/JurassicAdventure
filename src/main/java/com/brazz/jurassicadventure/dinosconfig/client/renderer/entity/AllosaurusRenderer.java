package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.client.model.AllosaurusModel;
import com.brazz.jurassicadventure.dinosconfig.entity.AllosaurusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AllosaurusRenderer extends GeoEntityRenderer<AllosaurusEntity> {
    public AllosaurusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AllosaurusModel());
        // Define o raio da sombra base (quando o dino é 100% adulto)
        this.shadowRadius = AllosaurusEntity.ADULT_WIDTH * 0.7f; 
    }

    @Override
    public void render(AllosaurusEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        float scale = entity.getScale();
        
        // CORREÇÃO: A sombra é calculada a partir do raio base e da escala atual
        // para evitar que ela diminua exponencialmente a cada frame.
        this.shadowRadius = (AllosaurusEntity.ADULT_WIDTH * 0.7f) * scale; 

        poseStack.pushPose(); 
        poseStack.scale(scale, scale, scale); 
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose(); 
    }
}
