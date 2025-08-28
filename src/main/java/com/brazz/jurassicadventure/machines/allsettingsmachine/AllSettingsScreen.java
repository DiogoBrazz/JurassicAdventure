package com.brazz.jurassicadventure.machines.allsettingsmachine;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class AllSettingsScreen<T extends AllSettingsMenu> extends AbstractContainerScreen<T> {

    // Guarda a textura de FUNDO da máquina filha
    private final ResourceLocation backgroundTexture;
    
    // << ADICIONADO: Textura da BARRA DE ENERGIA, compartilhada por todas as máquinas
    private static final ResourceLocation ENERGY_TEXTURE = new ResourceLocation("jurassicadventure", "textures/gui/energy.png");

    public AllSettingsScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, ResourceLocation backgroundTexture) {
        super(pMenu, pPlayerInventory, pTitle);
        this.backgroundTexture = backgroundTexture;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Desenha o fundo da GUI específica da máquina
        guiGraphics.blit(this.backgroundTexture, x, y, 0, 0, imageWidth, imageHeight);

        // << ADICIONADO: Desenha a BARRA DE ENERGIA (lógica do Gerador adaptada)
        int energyHeight = this.menu.getEnergyHeight();
        if (energyHeight > 0) {
            // Usa as coordenadas e tamanho que você especificou (9x42, na posição x=153, y=7 a 48)
            guiGraphics.blit(ENERGY_TEXTURE, 
                    x + 153, y + 7 + (42 - energyHeight), // Posição Y ajustada para crescer de baixo para cima
                    0, 42 - energyHeight,            // Coordenada V ajustada na textura
                    9, energyHeight,                 // Largura e altura a renderizar
                    9, 42);                          // Tamanho total da textura de energia
        }

        // Chama o método para desenhar as barras de progresso específicas de cada máquina
        renderProgressBars(guiGraphics, x, y);
    }
    
    // Este método continua 'abstract' para que cada máquina desenhe sua própria seta de progresso
    protected abstract void renderProgressBars(GuiGraphics guiGraphics, int x, int y);

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Deixamos os títulos para as classes filhas, se quiserem.
        // Mas o TOOLTIP da energia, que é global, vem para cá.

        // << ADICIONADO: Lógica do Tooltip de Energia
        // Usa as coordenadas da área da barra de energia (x=153, y=7, w=9, h=42)
        if (isHovering(153, 7, 9, 42, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font,
                Component.literal(menu.getEnergyStored() + " / " + menu.getMaxEnergyStored() + " FE"),
                mouseX - leftPos, mouseY - topPos);
        }
    }
}