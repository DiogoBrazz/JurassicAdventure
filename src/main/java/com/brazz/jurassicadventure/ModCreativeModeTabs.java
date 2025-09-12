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

    // 2. Define a nossa nova aba.
    public static final RegistryObject<CreativeModeTab> JURASSIC_TAB = CREATIVE_MODE_TABS.register("jurassic_tab",
            () -> CreativeModeTab.builder()
                    // Define o ícone que vai aparecer para a aba. Vamos usar o nosso Âmbar Bruto!
                    .icon(() -> new ItemStack(ModItems.RAW_AMBER.get()))
                    // Define o título da aba, que será lido do ficheiro de idioma.
                    .title(Component.translatable("creativetab.jurassic_tab"))
                    // Define quais itens aparecerão nesta aba.
                    .displayItems((pParameters, pOutput) -> {
                        //ITEMS
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

                        pOutput.accept(ModItems.FROG_MEAT.get());
                        pOutput.accept(ModItems.COOKED_FROG_MEAT.get());

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

                        //BLOCKS
                        pOutput.accept(ModBlocks.AMBER_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_AMBER_ORE.get());
                        pOutput.accept(ModBlocks.ELECTRIC_PILLAR.get());
                        pOutput.accept(ModBlocks.ELECTRIC_FENCE_WIRE.get());
                        pOutput.accept(ModBlocks.GENERATOR.get());
                        pOutput.accept(ModBlocks.CABLE.get());
                        pOutput.accept(ModBlocks.ANALYZER.get());
                        pOutput.accept(ModBlocks.SEQUENCER.get());
                        pOutput.accept(ModBlocks.ASSEMBLER.get());
                        pOutput.accept(ModBlocks.INJECTOR.get());
                        pOutput.accept(ModBlocks.MIXER.get());
                        pOutput.accept(ModBlocks.INCUBATOR.get());
                        
                    })
                    .build());


    // 3. Método padrão para registar tudo no Forge.
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}