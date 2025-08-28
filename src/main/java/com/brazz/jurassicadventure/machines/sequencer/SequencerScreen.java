package com.brazz.jurassicadventure.machines.sequencer;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsScreen; // Importa a nossa nova classe base
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SequencerScreen extends AllSettingsScreen<SequencerMenu> {

    @SuppressWarnings("removal")
    private static final ResourceLocation TEXTURE = 
            new ResourceLocation(JurassicAdventure.MODID, "textures/gui/sequencer_gui.png");

    public SequencerScreen(SequencerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, TEXTURE);
    }

    @Override
    protected void renderProgressBars(GuiGraphics guiGraphics, int x, int y) {
        if (this.menu.isCrafting()) {       
            guiGraphics.blit(TEXTURE, x + 61, y + 31, 176, 0, 54, menu.getScaledProgress());
        }
    }
}