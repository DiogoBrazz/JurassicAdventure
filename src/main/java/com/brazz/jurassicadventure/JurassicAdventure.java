package com.brazz.jurassicadventure;

import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.BrachiosaurusRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.RexRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.RiverFrogRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.VelociraptorRenderer;
import com.brazz.jurassicadventure.dinosconfig.entity.BrachiosaurusEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.RexEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.RiverFrogEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.VelociraptorEntity;
import com.brazz.jurassicadventure.machines.analyzer.AnalyzerRenderer;
import com.brazz.jurassicadventure.machines.analyzer.AnalyzerScreen;
import com.brazz.jurassicadventure.machines.assembler.AssemblerScreen;
import com.brazz.jurassicadventure.machines.generator.GeneratorScreen;
import com.brazz.jurassicadventure.machines.injector.InjectorScreen;
import com.brazz.jurassicadventure.machines.sequencer.SequencerScreen;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent; // <-- NOVA IMPORTAÇÃO
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

@Mod(JurassicAdventure.MODID)
public class JurassicAdventure {
    public static final String MODID = "jurassicadventure";

    @SuppressWarnings("removal")
    public JurassicAdventure() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GeckoLib.initialize();
        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        // regista os atributos do mob.
        modEventBus.addListener(this::entityAttributeEvent);
    }

    private void entityAttributeEvent(final EntityAttributeCreationEvent event) {
        event.put(ModEntities.RIVER_FROG.get(), RiverFrogEntity.createAttributes().build());
        event.put(ModEntities.REX.get(), RexEntity.createAttributes().build());
        event.put(ModEntities.VELOCIRAPTOR.get(), VelociraptorEntity.createAttributes().build());
        event.put(ModEntities.BRACHIOSAURUS.get(), BrachiosaurusEntity.createAttributes().build());
    }

    // A classe de eventos do cliente
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Regista os ecrãs das nossas máquinas
            MenuScreens.register(ModMenuTypes.GENERATOR_MENU.get(), GeneratorScreen::new);
            MenuScreens.register(ModMenuTypes.ANALYZER_MENU.get(), AnalyzerScreen::new);
            //Carregar o holograma do item
            BlockEntityRenderers.register(ModBlockEntities.ANALYZER_BLOCK_ENTITY.get(), AnalyzerRenderer::new);
            MenuScreens.register(ModMenuTypes.SEQUENCER_MENU.get(), SequencerScreen::new);
            MenuScreens.register(ModMenuTypes.ASSEMBLER_MENU.get(), AssemblerScreen::new);
            MenuScreens.register(ModMenuTypes.INJECTOR_MENU.get(), InjectorScreen::new);

            
            EntityRenderers.register(ModEntities.RIVER_FROG.get(), RiverFrogRenderer::new);
            EntityRenderers.register(ModEntities.REX.get(), RexRenderer::new);
            EntityRenderers.register(ModEntities.VELOCIRAPTOR.get(), VelociraptorRenderer::new);
            EntityRenderers.register(ModEntities.BRACHIOSAURUS.get(), BrachiosaurusRenderer::new);
        }
        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
            
        }

        
    }
}