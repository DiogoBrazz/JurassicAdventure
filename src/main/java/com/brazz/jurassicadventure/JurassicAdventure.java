package com.brazz.jurassicadventure;

import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.AllosaurusRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.AnkylosaurusRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.BrachiosaurusRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.GallimimusRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.MosassauroRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.RexRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.RiverFrogRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.StegosaurusRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.TriceratopsRenderer;
import com.brazz.jurassicadventure.dinosconfig.client.renderer.entity.VelociraptorRenderer;
import com.brazz.jurassicadventure.dinosconfig.entity.AllosaurusEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.AnkylosaurusEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.BrachiosaurusEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.GallimimusEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.MosassauroEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.RexEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.RiverFrogEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.StegosaurusEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.TriceratopsEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.VelociraptorEntity;
import com.brazz.jurassicadventure.machines.analyzer.AnalyzerRenderer;
import com.brazz.jurassicadventure.machines.analyzer.AnalyzerScreen;
import com.brazz.jurassicadventure.machines.assembler.AssemblerScreen;
import com.brazz.jurassicadventure.machines.generator.GeneratorScreen;
import com.brazz.jurassicadventure.machines.incubator.IncubatorScreen;
import com.brazz.jurassicadventure.machines.injector.InjectorScreen;
import com.brazz.jurassicadventure.machines.mixer.MixerScreen;
import com.brazz.jurassicadventure.machines.sequencer.SequencerScreen;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent; // <-- NOVA IMPORTAÇÃO
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;
import com.brazz.jurassicadventure.network.ModMessages;



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
        ModMessages.register();

        // regista os atributos do mob.
        modEventBus.addListener(this::entityAttributeEvent);

    }

    private void entityAttributeEvent(final EntityAttributeCreationEvent event) {
        event.put(ModEntities.RIVER_FROG.get(), RiverFrogEntity.createAttributes().build());
        event.put(ModEntities.REX.get(), RexEntity.createAttributes().build());
        event.put(ModEntities.VELOCIRAPTOR.get(), VelociraptorEntity.createAttributes().build());
        event.put(ModEntities.BRACHIOSAURUS.get(), BrachiosaurusEntity.createAttributes().build());
        event.put(ModEntities.MOSASSAURO.get(), MosassauroEntity.createAttributes().build());
        event.put(ModEntities.ALLOSAURUS.get(), AllosaurusEntity.createAttributes().build());
        event.put(ModEntities.ANKYLOSAURUS.get(), AnkylosaurusEntity.createAttributes().build());
        event.put(ModEntities.GALLIMIMUS.get(), GallimimusEntity.createAttributes().build());
        event.put(ModEntities.TRICERATOPS.get(), TriceratopsEntity.createAttributes().build());
        event.put(ModEntities.STEGOSAURUS.get(), StegosaurusEntity.createAttributes().build());
    }

    // A classe de eventos do cliente
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // << CORRETO >>
            // Este método agora é responsável apenas pelo registro de Menus (Screens)
            // e Renderers de Block Entities.
            MenuScreens.register(ModMenuTypes.GENERATOR_MENU.get(), GeneratorScreen::new);
            MenuScreens.register(ModMenuTypes.ANALYZER_MENU.get(), AnalyzerScreen::new);
            MenuScreens.register(ModMenuTypes.SEQUENCER_MENU.get(), SequencerScreen::new);
            MenuScreens.register(ModMenuTypes.ASSEMBLER_MENU.get(), AssemblerScreen::new);
            MenuScreens.register(ModMenuTypes.INJECTOR_MENU.get(), InjectorScreen::new);
            MenuScreens.register(ModMenuTypes.MIXER_MENU.get(), MixerScreen::new);
            MenuScreens.register(ModMenuTypes.INCUBATOR_MENU.get(), IncubatorScreen::new);
            
            BlockEntityRenderers.register(ModBlockEntities.ANALYZER_BLOCK_ENTITY.get(), AnalyzerRenderer::new);

            // << AS LINHAS DE EntityRenderers FORAM MOVIDAS DAQUI >>
        }

        // << NOVO MÉTODO >>
        // Este é o evento CORRETO e específico para registrar renderers de entidades.
        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.RIVER_FROG.get(), RiverFrogRenderer::new);
            event.registerEntityRenderer(ModEntities.REX.get(), RexRenderer::new);
            event.registerEntityRenderer(ModEntities.VELOCIRAPTOR.get(), VelociraptorRenderer::new);
            event.registerEntityRenderer(ModEntities.BRACHIOSAURUS.get(), BrachiosaurusRenderer::new);
            event.registerEntityRenderer(ModEntities.MOSASSAURO.get(), MosassauroRenderer::new);
            event.registerEntityRenderer(ModEntities.ALLOSAURUS.get(), AllosaurusRenderer::new);
            event.registerEntityRenderer(ModEntities.ANKYLOSAURUS.get(), AnkylosaurusRenderer::new);
            event.registerEntityRenderer(ModEntities.GALLIMIMUS.get(), GallimimusRenderer::new);
            event.registerEntityRenderer(ModEntities.TRICERATOPS.get(), TriceratopsRenderer::new);
            event.registerEntityRenderer(ModEntities.STEGOSAURUS.get(), StegosaurusRenderer::new);
        }

        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
            // Este método é para outra coisa (layers de modelos), pode continuar vazio.
        }
    }
}