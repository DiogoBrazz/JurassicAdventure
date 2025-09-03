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
    public void render(RexEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float scale;
        
        if (entity.isBaby()) {
            // << LÓGICA DE CÁLCULO DE ESCALA CORRIGIDA >>
            // A idade de um bebê vai de -24000 (nascimento) até 0 (adulto).
            // Esta fórmula converte essa contagem para um progresso de 0.0 a 1.0.
            float progress = (float)(AllDinos.BABY_TO_JUVENILE_AGE - entity.getAge()) / (float)AllDinos.BABY_TO_JUVENILE_AGE;
            
            // Interpolação suave de 30% para 100% do tamanho.
            scale = 0.3F + (0.7F * progress);
        } else {
            scale = 1.0F; // Tamanho adulto
        }

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}