package com.brazz.jurassicadventure.machines.incubator;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.network.ModMessages;
import com.brazz.jurassicadventure.network.packets.HatchEggPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class IncubatorScreen extends AbstractContainerScreen<IncubatorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassicAdventure.MODID, "textures/gui/incubator_gui.png");
    private static final ResourceLocation ENERGY_TEXTURE = new ResourceLocation(JurassicAdventure.MODID, "textures/gui/energy.png");

    private Button hatchButton;

    public IncubatorScreen(IncubatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
    
    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000; // Esconde o label "Inventory"
        
        this.hatchButton = this.addRenderableWidget(
            Button.builder(Component.literal("Chocar"), (button) -> {
                ModMessages.sendToServer(new HatchEggPacket(this.menu.blockEntity.getBlockPos()));
            })
            .bounds(this.leftPos + 62, this.topPos + 58, 52, 20)
            .build()
        );
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        
        // Atualiza o estado do botão a cada frame
        this.hatchButton.active = this.menu.isReadyToHatch();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Barra de Progresso
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 76, y + 14, 176, 0, menu.getScaledProgress() + 1, 16);
        }
        // Barra de Energia
        int energyHeight = menu.getEnergyHeight();
        if (energyHeight > 0) {
            guiGraphics.blit(ENERGY_TEXTURE, x + 152, y + 13 + (56 - energyHeight), 0, 56 - energyHeight, 9, energyHeight, 9, 56);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        // Tooltip de Energia
        if (isHovering(152, 13, 9, 56, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font,
                Component.literal(menu.getEnergyStored() + " / " + menu.getMaxEnergyStored() + " FE"),
                mouseX - leftPos, mouseY - topPos);
        }
    }
}