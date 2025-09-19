package com.brazz.jurassicadventure.machines.assembler;

import com.brazz.jurassicadventure.JurassicAdventure;
import com.brazz.jurassicadventure.machines.allsettingsmachine.AllSettingsScreen; // Importa a nossa nova classe base
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

// AQUI ESTÁ A PARTE IMPORTANTE:
// A classe agora herda de AllSettingsScreen, ganhando toda a sua funcionalidade.
public class AssemblerScreen extends AllSettingsScreen<AssemblerMenu> {

    // A textura específica para o Assembler.
    @SuppressWarnings("removal")
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassicAdventure.MODID, "textures/gui/assembler_gui.png");

    // O construtor é muito simples.
    public AssemblerScreen(AssemblerMenu menu, Inventory playerInventory, Component title) {
        // AQUI ESTÁ A LIGAÇÃO:
        // Ele "chama" o construtor da classe mãe (super) e passa-lhe
        // todas as informações necessárias, incluindo a SUA textura específica.
        super(menu, playerInventory, title, TEXTURE);
    }

    // AQUI CUMPRIMOS O "CONTRATO":
    // Somos obrigados a criar este método. A sua única função é desenhar
    // as barras de progresso específicas do Assembler.
    @Override
    protected void renderProgressBars(GuiGraphics guiGraphics, int x, int y) {
        // A sua lógica para desenhar a barra de progresso do Assembler.
        if(menu.isCrafting()) {
            // Os valores aqui (114, 13, etc.) são únicos para a sua textura do assembler_gui.png
            guiGraphics.blit(TEXTURE, x + 34, y + 7, 176, 0, menu.getScaledProgress(), 12);
        }
    }
}