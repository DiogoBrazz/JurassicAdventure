package com.brazz.jurassicadventure;

import com.brazz.jurassicadventure.item.EmbryoSyringeItem;
import com.brazz.jurassicadventure.item.GeneticInfoItem;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
        public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, JurassicAdventure.MODID);

        public static final RegistryObject<Item> RAW_AMBER = ITEMS.register("raw_amber",
            () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> TUBE_GENOME_FRAGMENT = ITEMS.register("tube_genome_fragment",
            () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> ERLENMEYER_FLASK = ITEMS.register("erlenmeyer_flask",
            () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> SYRINGE = ITEMS.register("syringe",
            () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> BROKEN_SYRINGE = ITEMS.register("broken_syringe",
            () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> FAILED_SYRINGE = ITEMS.register("failed_syringe",
            () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> NUTRITIONAL_SYRINGE = ITEMS.register("nutritional_syringe",
            () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> EMBRYO_SYRINGE = ITEMS.register("embryo_syringe",
            () -> new EmbryoSyringeItem(new Item.Properties().stacksTo(1)));

         public static final RegistryObject<Item> DNA_SYRINGE = ITEMS.register("dna_syringe",
            () -> new GeneticInfoItem(new Item.Properties()));

        public static final RegistryObject<Item> TUBE = ITEMS.register("tube",
            () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> FAILED_TUBE = ITEMS.register("failed_tube",
            () -> new Item(new Item.Properties()));

        public static final RegistryObject<Item> PARTIAL_GENOME_TUBE = ITEMS.register("partial_genome_tube",
            () -> new GeneticInfoItem(new Item.Properties()));

        public static final RegistryObject<Item> COMPLETE_GENOME_TUBE = ITEMS.register("complete_genome_tube",
            () -> new GeneticInfoItem(new Item.Properties()));

        public static final RegistryObject<Item> DINOSAUR_EGG = ITEMS.register("dinosaur_egg",
            () -> new GeneticInfoItem(new Item.Properties().stacksTo(1)));

        public static final RegistryObject<Item> JURASSIC_GUIDE = ITEMS.register("jurassic_guide",
        () -> new Item(new Item.Properties()));


        public static final RegistryObject<Item> FROG_MEAT = ITEMS.register("frog_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                .nutrition(2) 
                .saturationMod(0.3F) 
                .meat() 
                .build())));

        public static final RegistryObject<Item> COOKED_FROG_MEAT = ITEMS.register("cooked_frog_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                .nutrition(5) 
                .saturationMod(0.7F) 
                .meat()
                .build())));

        public static final RegistryObject<Item> RIVER_FROG_SPAWN_EGG = ITEMS.register("river_frog_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.RIVER_FROG.get(), 0x567d46, 0x2c3e2e, new Item.Properties()));

        public static final RegistryObject<Item> REX_SPAWN_EGG = ITEMS.register("rex_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.REX.get(), 0x567d46, 0x2c3e2e, new Item.Properties()));

        public static final RegistryObject<Item> VELOCIRAPTOR_SPAWN_EGG = ITEMS.register("velociraptor_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.VELOCIRAPTOR.get(), 0x567d46, 0x2c3e2e, new Item.Properties()));

        public static final RegistryObject<Item> BRACHIOSAURUS_SPAWN_EGG = ITEMS.register("brachiosaurus_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.BRACHIOSAURUS.get(), 0x567d46, 0x2c3e2e, new Item.Properties()));

        public static final RegistryObject<Item> MOSASSAURO_SPAWN_EGG = ITEMS.register("mosassauro_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.MOSASSAURO.get(), 0x567d46, 0x2c3e2e, new Item.Properties()));

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
    }
}