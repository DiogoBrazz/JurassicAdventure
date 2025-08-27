package com.brazz.jurassicadventure.machines.generator;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GeneratorScreen extends AbstractContainerScreen<GeneratorMenu> {
    
    // Textura do fundo da GUI
    private static final ResourceLocation TEXTURE = new ResourceLocation("jurassicadventure", "textures/gui/generator_gui.png");
    // Texturas SEPARADAS para as barras (usando os nomes que você especificou)
    private static final ResourceLocation PROGRESS_TEXTURE = new ResourceLocation("jurassicadventure", "textures/gui/progress.png");
    private static final ResourceLocation ENERGY_TEXTURE = new ResourceLocation("jurassicadventure", "textures/gui/energy_progress.png");

    public GeneratorScreen(GeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Fundo da GUI
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Barra de progresso de queima (esquerda para direita) - posição: x+72, y+38
        int progress = this.menu.getBurnProgress();
        if (progress > 0) {
            RenderSystem.setShaderTexture(0, PROGRESS_TEXTURE);
            guiGraphics.blit(PROGRESS_TEXTURE, 
                x + 72, y + 38,     // Posição na tela
                0, 0,               // Coordenadas UV na textura
                progress, 4,        // Largura e altura a renderizar
                36, 4               // Tamanho total da textura
            );
        }

        // Barra de energia (baixo para cima) - posição: x+113, y+11, tamanho: 32x56
        int energy = this.menu.getEnergyHeight();
        if (energy > 0) {
            RenderSystem.setShaderTexture(0, ENERGY_TEXTURE);
            guiGraphics.blit(ENERGY_TEXTURE, 
                x + 113, y + 12 + (56 - energy), // Posição Y ajustada para crescer de baixo para cima
                0, 56 - energy,                  // Coordenada V ajustada na textura
                32, energy,                      // Largura e altura a renderizar
                32, 56                           // Tamanho total da textura
            );
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
        
        // Tooltip de energia se o mouse estiver sobre a barra (ajustado para a nova posição)
        if (isHovering(113, 11, 32, 56, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font, 
                Component.literal(menu.getEnergyStored() + " / " + menu.getMaxEnergyStored() + " FE"), 
                mouseX - leftPos, mouseY - topPos);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}