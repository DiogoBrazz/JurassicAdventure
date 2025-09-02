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

    // << A CORREÇÃO FINAL: Sobrescrevendo o método 'render' principal >>
    @Override
    public void render(RexEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        
        // 1. Nossa lógica de cálculo de escala continua a mesma e está perfeita.
        float scale;
        AllDinos.AgeStage stage = entity.getAgeStage();

        if (stage == AllDinos.AgeStage.ADULTO) {
            scale = 1.0F; // Tamanho adulto final
        } else {
            // Interpolação para crescimento suave
            float progress = (float)entity.getAgeInTicks() / AllDinos.ADULT_AGE_TICKS;
            scale = 0.3F + (0.7F * progress);
        }

        // 2. Aplicamos a escala ANTES de qualquer outra coisa
        poseStack.pushPose(); // Salva o estado atual da matriz de transformações
        poseStack.scale(scale, scale, scale); // Aplica nossa escala

        // 3. Chamamos o método 'render' original da classe pai (super)
        // Isso diz ao GeckoLib para desenhar o dinossauro, mas usando a nossa PoseStack já modificada (escalada)
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        // 4. Restauramos a matriz de transformações
        // Isso é CRUCIAL para não afetar a renderização de outras entidades no jogo
        poseStack.popPose();
    }
}