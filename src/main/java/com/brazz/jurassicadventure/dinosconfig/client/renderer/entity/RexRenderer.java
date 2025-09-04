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
            // << CORRIGIDO: Usando a constante 'MAX_BABY_AGE' da classe AllDinos >>
            float progress = 1.0f + ((float) entity.getAge() / (float) -AllDinos.MAX_BABY_AGE);
            progress = Math.max(0, Math.min(1, progress));
            scale = 0.3F + (0.7F * progress);
        } else {
            scale = 1.0F;
        }

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}