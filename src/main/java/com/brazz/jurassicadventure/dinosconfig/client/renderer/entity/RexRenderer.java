package com.brazz.jurassicadventure.dinosconfig.client.renderer.entity;

import com.brazz.jurassicadventure.dinosconfig.client.model.RexModel;
import com.brazz.jurassicadventure.dinosconfig.entity.RexEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RexRenderer extends GeoEntityRenderer<RexEntity> {
    public RexRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RexModel());
        this.shadowRadius = RexEntity.SHADOW_RADIUS;
    }

    @Override
    public void render(RexEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        // 1. Pega a escala calculada na RexEntity
        float scale = entity.getScale();
        // 2. Atualiza o raio da sombra se quiser
        this.shadowRadius = this.shadowRadius * scale;
        // 3. Aplica a escala na "matriz de pose". Isso afeta tudo que for renderizado depois.
        poseStack.pushPose(); // Salva o estado atual da matriz
        poseStack.scale(scale, scale, scale); // Aplica a nossa escala
        // 4. Chama o método original da GeckoLib para renderizar o modelo, mas já com a escala aplicada
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose(); // Restaura a matriz para não afetar outros renders
    }
}