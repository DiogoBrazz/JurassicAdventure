package com.brazz.jurassicadventure;

import com.brazz.jurassicadventure.util.DinoTypes;
import com.brazz.jurassicadventure.util.MobTypes;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    // 1. Cria um "Registrador" para as nossas abas criativas.
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JurassicAdventure.MODID);

    // 2. Define as nossas novas abas.

    // ABA PRINCIPAL COM TODOS OS ITENS
    public static final RegistryObject<CreativeModeTab> JURASSIC_ALL_ITEMS_TAB = CREATIVE_MODE_TABS.register("jurassic_all_items_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.JURASSIC_GUIDE.get()))
                    .title(Component.translatable("creativetab.jurassic_all_items_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        // ITENS DE UTILIDADE E GENÉTICA
                        pOutput.accept(ModItems.JURASSIC_GUIDE.get());
                        pOutput.accept(ModItems.RAW_AMBER.get());
                        pOutput.accept(ModItems.LUPA.get());
                        pOutput.accept(ModItems.SYRINGE.get());
                        pOutput.accept(ModItems.BROKEN_SYRINGE.get());
                        pOutput.accept(ModItems.FAILED_SYRINGE.get());
                        pOutput.accept(ModItems.NUTRITIONAL_SYRINGE.get());

                        for(ResourceLocation mobType : MobTypes.ALL_VANILLA_MOBS) {
                            ItemStack mobDnaSyringe = new ItemStack(ModItems.DNA_SYRINGE.get());
                            mobDnaSyringe.getOrCreateTag().putString("mob_type", mobType.toString());
                            pOutput.accept(mobDnaSyringe);
                        }
                        for(ResourceLocation dinoType : DinoTypes.ALL_DINOS) {
                            ItemStack dnaSyringe = new ItemStack(ModItems.DNA_SYRINGE.get());
                            dnaSyringe.getOrCreateTag().putString("dino_type", dinoType.toString());
                            pOutput.accept(dnaSyringe);
                        }

                        pOutput.accept(ModItems.TUBE.get());
                        pOutput.accept(ModItems.FAILED_TUBE.get());

                        for(ResourceLocation dinoType : DinoTypes.ALL_DINOS) {
                            ItemStack partialGenome = new ItemStack(ModItems.PARTIAL_GENOME_TUBE.get());
                            partialGenome.getOrCreateTag().putString("dino_type", dinoType.toString());
                            pOutput.accept(partialGenome);
                        }
                        for(ResourceLocation dinoType : DinoTypes.ALL_DINOS) {
                            ItemStack completeGenome = new ItemStack(ModItems.COMPLETE_GENOME_TUBE.get());
                            completeGenome.getOrCreateTag().putString("dino_type", dinoType.toString());
                            pOutput.accept(completeGenome);
                        }
                        for(ResourceLocation dinoType : DinoTypes.OVIPAROUS_DINOS) {
                            ItemStack embryoSyringe = new ItemStack(ModItems.EMBRYO_SYRINGE.get());
                            embryoSyringe.getOrCreateTag().putString("dino_type", dinoType.toString());
                            embryoSyringe.getOrCreateTag().putBoolean("is_oviparous", true);
                            pOutput.accept(embryoSyringe);
                        }
                        for(ResourceLocation dinoType : DinoTypes.MAMIFEROS_DINOS) {
                            ItemStack embryoSyringe = new ItemStack(ModItems.EMBRYO_SYRINGE.get());
                            embryoSyringe.getOrCreateTag().putString("dino_type", dinoType.toString());
                            embryoSyringe.getOrCreateTag().putBoolean("is_oviparous", false);
                            pOutput.accept(embryoSyringe);
                        }
                        for(ResourceLocation dinoType : DinoTypes.OVIPAROUS_DINOS) {
                            ItemStack dinosaurEgg = new ItemStack(ModItems.DINOSAUR_EGG.get());
                            dinosaurEgg.getOrCreateTag().putString("dino_type", dinoType.toString());
                            pOutput.accept(dinosaurEgg);
                        }

                        // COMIDAS
                        pOutput.accept(ModItems.FROG_MEAT.get());
                        pOutput.accept(ModItems.COOKED_FROG_MEAT.get());
                        pOutput.accept(ModItems.REX_MEAT.get());
                        pOutput.accept(ModItems.COOKED_REX_MEAT.get());
                        pOutput.accept(ModItems.SPINOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_SPINOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.MEGALANIA_MEAT.get());
                        pOutput.accept(ModItems.COOKED_MEGALANIA_MEAT.get());
                        pOutput.accept(ModItems.BRACHIOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_BRACHIOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.DIPLODOCUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_DIPLODOCUS_MEAT.get());
                        pOutput.accept(ModItems.MOSASSAURO_MEAT.get());
                        pOutput.accept(ModItems.COOKED_MOSASSAURO_MEAT.get());
                        pOutput.accept(ModItems.MAMUTE_MEAT.get());
                        pOutput.accept(ModItems.COOKED_MAMUTE_MEAT.get());
                        pOutput.accept(ModItems.TRICERATOPS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_TRICERATOPS_MEAT.get());
                        pOutput.accept(ModItems.CARNOTAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_CARNOTAURUS_MEAT.get());
                        pOutput.accept(ModItems.ALLOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_ALLOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.YUTYRANNUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_YUTYRANNUS_MEAT.get());
                        pOutput.accept(ModItems.PARASAUROLOPHUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_PARASAUROLOPHUS_MEAT.get());
                        pOutput.accept(ModItems.STEGOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_STEGOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.ANKYLOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_ANKYLOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.VELOCIRAPTOR_MEAT.get());
                        pOutput.accept(ModItems.COOKED_VELOCIRAPTOR_MEAT.get());
                        pOutput.accept(ModItems.DILOPHOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_DILOPHOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.DEINONYCHUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_DEINONYCHUS_MEAT.get());
                        pOutput.accept(ModItems.GALLIMIMUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_GALLIMIMUS_MEAT.get());
                        pOutput.accept(ModItems.PACHYCEPHALOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_PACHYCEPHALOSAURUS_MEAT.get());

                        // OVOS DE SPAWN
                        pOutput.accept(ModItems.RIVER_FROG_SPAWN_EGG.get());
                        pOutput.accept(ModItems.REX_SPAWN_EGG.get());
                        pOutput.accept(ModItems.VELOCIRAPTOR_SPAWN_EGG.get());
                        pOutput.accept(ModItems.BRACHIOSAURUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.MOSASSAURO_SPAWN_EGG.get());
                        pOutput.accept(ModItems.ALLOSAURUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.ANKYLOSAURUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.GALLIMIMUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.TRICERATOPS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.STEGOSAURUS_SPAWN_EGG.get());

                        // BLOCOS
                        pOutput.accept(ModBlocks.AMBER_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_AMBER_ORE.get());
                        pOutput.accept(ModBlocks.GENERATOR.get());
                        pOutput.accept(ModBlocks.CABLE.get());
                        pOutput.accept(ModBlocks.ANALYZER.get());
                        pOutput.accept(ModBlocks.SEQUENCER.get());
                        pOutput.accept(ModBlocks.ASSEMBLER.get());
                        pOutput.accept(ModBlocks.MIXER.get());
                        pOutput.accept(ModBlocks.INJECTOR.get());
                        pOutput.accept(ModBlocks.INCUBATOR.get());
                        pOutput.accept(ModBlocks.ELECTRIC_PILLAR.get());
                        pOutput.accept(ModBlocks.ELECTRIC_FENCE_WIRE.get());
                    })
                    .build());

    // ABA DE ITENS DE GENÉTICA
    public static final RegistryObject<CreativeModeTab> JURASSIC_GENETICS_TAB = CREATIVE_MODE_TABS.register("jurassic_genetics_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.COMPLETE_GENOME_TUBE.get()))
                    .title(Component.translatable("creativetab.jurassic_genetics_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.RAW_AMBER.get());
                        pOutput.accept(ModItems.LUPA.get());
                        pOutput.accept(ModItems.SYRINGE.get());
                        pOutput.accept(ModItems.BROKEN_SYRINGE.get());
                        pOutput.accept(ModItems.FAILED_SYRINGE.get());
                        pOutput.accept(ModItems.NUTRITIONAL_SYRINGE.get());

                        for(ResourceLocation mobType : MobTypes.ALL_VANILLA_MOBS) {
                            ItemStack mobDnaSyringe = new ItemStack(ModItems.DNA_SYRINGE.get());
                            mobDnaSyringe.getOrCreateTag().putString("mob_type", mobType.toString());
                            pOutput.accept(mobDnaSyringe);
                        }
                        for(ResourceLocation dinoType : DinoTypes.ALL_DINOS) {
                            ItemStack dnaSyringe = new ItemStack(ModItems.DNA_SYRINGE.get());
                            dnaSyringe.getOrCreateTag().putString("dino_type", dinoType.toString());
                            pOutput.accept(dnaSyringe);
                        }

                        pOutput.accept(ModItems.TUBE.get());
                        pOutput.accept(ModItems.FAILED_TUBE.get());

                        for(ResourceLocation dinoType : DinoTypes.ALL_DINOS) {
                            ItemStack partialGenome = new ItemStack(ModItems.PARTIAL_GENOME_TUBE.get());
                            partialGenome.getOrCreateTag().putString("dino_type", dinoType.toString());
                            pOutput.accept(partialGenome);
                        }
                        for(ResourceLocation dinoType : DinoTypes.ALL_DINOS) {
                            ItemStack completeGenome = new ItemStack(ModItems.COMPLETE_GENOME_TUBE.get());
                            completeGenome.getOrCreateTag().putString("dino_type", dinoType.toString());
                            pOutput.accept(completeGenome);
                        }
                        for(ResourceLocation dinoType : DinoTypes.OVIPAROUS_DINOS) {
                            ItemStack embryoSyringe = new ItemStack(ModItems.EMBRYO_SYRINGE.get());
                            embryoSyringe.getOrCreateTag().putString("dino_type", dinoType.toString());
                            embryoSyringe.getOrCreateTag().putBoolean("is_oviparous", true);
                            pOutput.accept(embryoSyringe);
                        }
                        for(ResourceLocation dinoType : DinoTypes.MAMIFEROS_DINOS) {
                            ItemStack embryoSyringe = new ItemStack(ModItems.EMBRYO_SYRINGE.get());
                            embryoSyringe.getOrCreateTag().putString("dino_type", dinoType.toString());
                            embryoSyringe.getOrCreateTag().putBoolean("is_oviparous", false);
                            pOutput.accept(embryoSyringe);
                        }
                        for(ResourceLocation dinoType : DinoTypes.OVIPAROUS_DINOS) {
                            ItemStack dinosaurEgg = new ItemStack(ModItems.DINOSAUR_EGG.get());
                            dinosaurEgg.getOrCreateTag().putString("dino_type", dinoType.toString());
                            pOutput.accept(dinosaurEgg);
                        }
                    })
                    .build());

    // ABA DE MÁQUINAS
    public static final RegistryObject<CreativeModeTab> JURASSIC_MACHINES_TAB = CREATIVE_MODE_TABS.register("jurassic_machines_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.ANALYZER.get()))
                    .title(Component.translatable("creativetab.jurassic_machines_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.GENERATOR.get());
                        pOutput.accept(ModBlocks.CABLE.get());
                        pOutput.accept(ModBlocks.ANALYZER.get());
                        pOutput.accept(ModBlocks.SEQUENCER.get());
                        pOutput.accept(ModBlocks.ASSEMBLER.get());
                        pOutput.accept(ModBlocks.MIXER.get());
                        pOutput.accept(ModBlocks.INJECTOR.get());
                        pOutput.accept(ModBlocks.INCUBATOR.get());
                        pOutput.accept(ModBlocks.ELECTRIC_PILLAR.get());
                        pOutput.accept(ModBlocks.ELECTRIC_FENCE_WIRE.get());
                        pOutput.accept(ModBlocks.AMBER_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_AMBER_ORE.get());
                    })
                    .build());

    // ABA DE COMIDAS
    public static final RegistryObject<CreativeModeTab> JURASSIC_FOODS_TAB = CREATIVE_MODE_TABS.register("jurassic_foods_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.COOKED_REX_MEAT.get()))
                    .title(Component.translatable("creativetab.jurassic_foods_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.FROG_MEAT.get());
                        pOutput.accept(ModItems.COOKED_FROG_MEAT.get());
                        pOutput.accept(ModItems.REX_MEAT.get());
                        pOutput.accept(ModItems.COOKED_REX_MEAT.get());
                        pOutput.accept(ModItems.SPINOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_SPINOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.MEGALANIA_MEAT.get());
                        pOutput.accept(ModItems.COOKED_MEGALANIA_MEAT.get());
                        pOutput.accept(ModItems.BRACHIOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_BRACHIOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.DIPLODOCUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_DIPLODOCUS_MEAT.get());
                        pOutput.accept(ModItems.MOSASSAURO_MEAT.get());
                        pOutput.accept(ModItems.COOKED_MOSASSAURO_MEAT.get());
                        pOutput.accept(ModItems.MAMUTE_MEAT.get());
                        pOutput.accept(ModItems.COOKED_MAMUTE_MEAT.get());
                        pOutput.accept(ModItems.TRICERATOPS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_TRICERATOPS_MEAT.get());
                        pOutput.accept(ModItems.CARNOTAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_CARNOTAURUS_MEAT.get());
                        pOutput.accept(ModItems.ALLOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_ALLOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.YUTYRANNUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_YUTYRANNUS_MEAT.get());
                        pOutput.accept(ModItems.PARASAUROLOPHUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_PARASAUROLOPHUS_MEAT.get());
                        pOutput.accept(ModItems.STEGOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_STEGOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.ANKYLOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_ANKYLOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.VELOCIRAPTOR_MEAT.get());
                        pOutput.accept(ModItems.COOKED_VELOCIRAPTOR_MEAT.get());
                        pOutput.accept(ModItems.DILOPHOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_DILOPHOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.DEINONYCHUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_DEINONYCHUS_MEAT.get());
                        pOutput.accept(ModItems.GALLIMIMUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_GALLIMIMUS_MEAT.get());
                        pOutput.accept(ModItems.PACHYCEPHALOSAURUS_MEAT.get());
                        pOutput.accept(ModItems.COOKED_PACHYCEPHALOSAURUS_MEAT.get());
                    })
                    .build());

    // ABA DE OVOS DE SPAWN
    public static final RegistryObject<CreativeModeTab> JURASSIC_SPAWN_EGGS_TAB = CREATIVE_MODE_TABS.register("jurassic_spawn_eggs_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.REX_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.jurassic_spawn_eggs_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.RIVER_FROG_SPAWN_EGG.get());
                        pOutput.accept(ModItems.REX_SPAWN_EGG.get());
                        pOutput.accept(ModItems.VELOCIRAPTOR_SPAWN_EGG.get());
                        pOutput.accept(ModItems.BRACHIOSAURUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.MOSASSAURO_SPAWN_EGG.get());
                        pOutput.accept(ModItems.ALLOSAURUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.ANKYLOSAURUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.GALLIMIMUS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.TRICERATOPS_SPAWN_EGG.get());
                        pOutput.accept(ModItems.STEGOSAURUS_SPAWN_EGG.get());
                        // Adicione novos ovos de spawn aqui
                    })
                    .build());


    // 3. Método padrão para registar tudo no Forge.
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

