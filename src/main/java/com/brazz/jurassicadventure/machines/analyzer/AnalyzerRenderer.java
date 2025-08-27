package com.brazz.jurassicadventure.machines.analyzer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class AnalyzerRenderer implements BlockEntityRenderer<AnalyzerBlockEntity> {

    public AnalyzerRenderer(BlockEntityRendererProvider.Context context) {
        // O construtor geralmente fica vazio ou guarda referências, se necessário.
    }

    @Override
    public void render(AnalyzerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                    MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getItemHandler().getStackInSlot(0);

        // A CONDIÇÃO FOI SIMPLIFICADA:
        // Agora, só verificamos se o slot não está vazio.
        if (!itemStack.isEmpty()) {
            pPoseStack.pushPose();

            pPoseStack.translate(0.5d, 2.1d, 0.5d);
            pPoseStack.scale(0.35f, 0.35f, 0.35f);

            float rotation = (pBlockEntity.getLevel().getGameTime() + pPartialTick) * 2;
            pPoseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            // 1. Define a posição para calcular a luz (um bloco acima do Analyzer)
            BlockPos lightPos = pBlockEntity.getBlockPos().above();
            // 2. Obtém o nível de luz combinado (bloco + céu) nessa nova posição
            int light = LevelRenderer.getLightColor(pBlockEntity.getLevel(), lightPos);

            // 3. Usa o novo valor de luz 'light' em vez do 'pPackedLight' antigo
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, light, pPackedOverlay, pPoseStack, pBuffer, pBlockEntity.getLevel(), (int)pBlockEntity.getBlockPos().asLong());

            pPoseStack.popPose();
        }
    }
}
