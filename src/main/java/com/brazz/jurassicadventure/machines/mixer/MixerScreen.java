package com.brazz.jurassicadventure.machines.mixer;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MixerScreen extends AllSettingsScreen<MixerMenu>{
    @SuppressWarnings("removal")
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassicAdventure.MODID, "textures/gui/mixer_gui.png");

    public MixerScreen(MixerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
    }

    @Override
    protected void renderProgressBars(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 114, y + 13, 176, 0, menu.getScaledProgress(), 12);
        }
    }
}
