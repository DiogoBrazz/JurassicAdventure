package com.brazz.jurassicadventure.machines.allsettingsmachine;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

// <T extends AllSettingsMenu>: Diz que este ecrã genérico só funciona com menus que são do tipo AllSettingsMenu.
public abstract class AllSettingsScreen<T extends AllSettingsMenu> extends AbstractContainerScreen<T> {

    // Guarda a textura específica da máquina filha (Analyzer, Sequencer, etc.).
    private final ResourceLocation texture;

    // O construtor recebe a textura da classe filha e guarda-a.
    public AllSettingsScreen(T pMenu, Inventory pPlayerInventory, Component pTitle, ResourceLocation texture) {
        super(pMenu, pPlayerInventory, pTitle);
        this.texture = texture;
    }

    // Este método é o "pintor" principal e é partilhado por todos os ecrãs.
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // Usa a textura que foi passada pela classe filha.
        RenderSystem.setShaderTexture(0, this.texture);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Desenha o fundo da interface.
        guiGraphics.blit(this.texture, x, y, 0, 0, imageWidth, imageHeight);

        // AQUI ESTÁ A PARTE IMPORTANTE:
        // Em vez de desenhar a barra de progresso aqui, nós "delegamos" essa tarefa
        // para um método que a classe filha é obrigada a implementar.
        renderProgressBars(guiGraphics, x, y);
    }

    // ESTE É O "CONTRATO":
    // Ao ser 'abstract', este método obriga qualquer classe que herde de AllSettingsScreen
    // a criar a sua própria lógica para desenhar as suas barras de progresso únicas.
    protected abstract void renderProgressBars(GuiGraphics guiGraphics, int x, int y);

    // O resto dos métodos são boilerplate (código repetido) que tratam da renderização geral.
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        // Deixado em branco para não ter os títulos "Assembler" e "Inventory".
    }
}