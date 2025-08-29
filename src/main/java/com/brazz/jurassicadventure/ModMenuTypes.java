package com.brazz.jurassicadventure;

import com.brazz.jurassicadventure.machines.analyzer.AnalyzerMenu;
import com.brazz.jurassicadventure.machines.assembler.AssemblerMenu;
import com.brazz.jurassicadventure.machines.generator.GeneratorMenu;
import com.brazz.jurassicadventure.machines.incubator.IncubatorMenu;
import com.brazz.jurassicadventure.machines.injector.InjectorMenu;
import com.brazz.jurassicadventure.machines.mixer.MixerMenu;
import com.brazz.jurassicadventure.machines.sequencer.SequencerMenu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
        public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
                        JurassicAdventure.MODID);

        public static final RegistryObject<MenuType<GeneratorMenu>> GENERATOR_MENU = registerMenuType(
                        GeneratorMenu::new, "generator_menu");

        public static final RegistryObject<MenuType<AnalyzerMenu>> ANALYZER_MENU = registerMenuType(
                        AnalyzerMenu::new,"analyzer_menu");

        public static final RegistryObject<MenuType<SequencerMenu>> SEQUENCER_MENU = registerMenuType(
                        SequencerMenu::new, "sequencer_menu");

        public static final RegistryObject<MenuType<AssemblerMenu>> ASSEMBLER_MENU = registerMenuType(
                        AssemblerMenu::new, "assembler_menu");

        public static final RegistryObject<MenuType<InjectorMenu>> INJECTOR_MENU = registerMenuType(
                        InjectorMenu::new, "injector_menu");

        public static final RegistryObject<MenuType<MixerMenu>> MIXER_MENU = registerMenuType(
                        MixerMenu::new, "mixer_menu");

        public static final RegistryObject<MenuType<IncubatorMenu>> INCUBATOR_MENU = registerMenuType(
                        IncubatorMenu::new, "incubator_menu");

        private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(
                        IContainerFactory<T> factory, String name) {
                return MENUS.register(name, () -> IForgeMenuType.create(factory));
        }

        public static void register(IEventBus eventBus) {
                MENUS.register(eventBus);
        }
}