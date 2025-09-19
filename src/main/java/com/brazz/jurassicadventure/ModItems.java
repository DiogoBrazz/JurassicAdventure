package com.brazz.jurassicadventure;

import com.brazz.jurassicadventure.item.EmbryoSyringeItem;
import com.brazz.jurassicadventure.item.GeneticInfoItem;
import com.brazz.jurassicadventure.item.LupaItem;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            JurassicAdventure.MODID);

    // ==================================================
    // ======== ITENS DE CRAFTING E UTILIDADE ===========
    // ==================================================

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

    public static final RegistryObject<Item> LUPA = ITEMS.register("lupa",
            () -> new LupaItem(new Item.Properties()));

    public static final RegistryObject<Item> JURASSIC_GUIDE = ITEMS.register("jurassic_guide",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BASIC_CHIP = ITEMS.register("basic_chip",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ADVANCED_CHIP = ITEMS.register("advanced_chip",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRON_WIRE = ITEMS.register("iron_wire",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GOLD_WIRE = ITEMS.register("gold_wire",
            () -> new Item(new Item.Properties()));

    // ==================================================
    // ======== COMIDAS =================================
    // ==================================================

    //  Método auxiliar para criar um item de comida (carne) de forma simplificada.

    private static Supplier<Item> createMeatFood(int nutrition, float saturationModifier) {
        return () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationMod(saturationModifier)
                .meat()
                .build()));
    }

    // --- Carnes Comuns ---
    public static final RegistryObject<Item> FROG_MEAT = ITEMS.register("frog_meat", createMeatFood(2, 0.3F));
    public static final RegistryObject<Item> COOKED_FROG_MEAT = ITEMS.register("cooked_frog_meat",
            createMeatFood(5, 0.7F));

    // --- Carnes de Dinossauros Grandes ---
    public static final RegistryObject<Item> REX_MEAT = ITEMS.register("rex_meat", createMeatFood(4, 0.5F));
    public static final RegistryObject<Item> COOKED_REX_MEAT = ITEMS.register("cooked_rex_meat",
            createMeatFood(10, 1.6F));
    public static final RegistryObject<Item> SPINOSAURUS_MEAT = ITEMS.register("spinosaurus_meat",
            createMeatFood(4, 0.5F));
    public static final RegistryObject<Item> COOKED_SPINOSAURUS_MEAT = ITEMS.register("cooked_spinosaurus_meat",
            createMeatFood(10, 1.6F));
    public static final RegistryObject<Item> MEGALANIA_MEAT = ITEMS.register("megalania_meat", createMeatFood(4, 0.5F));
    public static final RegistryObject<Item> COOKED_MEGALANIA_MEAT = ITEMS.register("cooked_megalania_meat",
            createMeatFood(10, 1.6F));
    public static final RegistryObject<Item> BRACHIOSAURUS_MEAT = ITEMS.register("brachiosaurus_meat",
            createMeatFood(4, 0.5F));
    public static final RegistryObject<Item> COOKED_BRACHIOSAURUS_MEAT = ITEMS.register("cooked_brachiosaurus_meat",
            createMeatFood(10, 1.6F));
    public static final RegistryObject<Item> DIPLODOCUS_MEAT = ITEMS.register("diplodocus_meat",
            createMeatFood(4, 0.5F));
    public static final RegistryObject<Item> COOKED_DIPLODOCUS_MEAT = ITEMS.register("cooked_diplodocus_meat",
            createMeatFood(10, 1.6F));
    public static final RegistryObject<Item> MOSASSAURO_MEAT = ITEMS.register("mosassauro_meat",
            createMeatFood(4, 0.5F));
    public static final RegistryObject<Item> COOKED_MOSASSAURO_MEAT = ITEMS.register("cooked_mosassauro_meat",
            createMeatFood(10, 1.6F));
    public static final RegistryObject<Item> MAMUTE_MEAT = ITEMS.register("mamute_meat", createMeatFood(4, 0.5F));
    public static final RegistryObject<Item> COOKED_MAMUTE_MEAT = ITEMS.register("cooked_mamute_meat",
            createMeatFood(10, 1.6F));

    // --- Carnes de Dinossauros Médios ---
    public static final RegistryObject<Item> TRICERATOPS_MEAT = ITEMS.register("triceratops_meat",
            createMeatFood(3, 0.4F));
    public static final RegistryObject<Item> COOKED_TRICERATOPS_MEAT = ITEMS.register("cooked_triceratops_meat",
            createMeatFood(8, 1.2F));
    public static final RegistryObject<Item> CARNOTAURUS_MEAT = ITEMS.register("carnotaurus_meat",
            createMeatFood(3, 0.4F));
    public static final RegistryObject<Item> COOKED_CARNOTAURUS_MEAT = ITEMS.register("cooked_carnotaurus_meat",
            createMeatFood(8, 1.2F));
    public static final RegistryObject<Item> ALLOSAURUS_MEAT = ITEMS.register("allosaurus_meat",
            createMeatFood(3, 0.4F));
    public static final RegistryObject<Item> COOKED_ALLOSAURUS_MEAT = ITEMS.register("cooked_allosaurus_meat",
            createMeatFood(8, 1.2F));
    public static final RegistryObject<Item> YUTYRANNUS_MEAT = ITEMS.register("yutyrannus_meat",
            createMeatFood(3, 0.4F));
    public static final RegistryObject<Item> COOKED_YUTYRANNUS_MEAT = ITEMS.register("cooked_yutyrannus_meat",
            createMeatFood(8, 1.2F));
    public static final RegistryObject<Item> PARASAUROLOPHUS_MEAT = ITEMS.register("parasaurolophus_meat",
            createMeatFood(3, 0.4F));
    public static final RegistryObject<Item> COOKED_PARASAUROLOPHUS_MEAT = ITEMS.register("cooked_parasaurolophus_meat",
            createMeatFood(8, 1.2F));
    public static final RegistryObject<Item> STEGOSAURUS_MEAT = ITEMS.register("stegosaurus_meat",
            createMeatFood(3, 0.4F));
    public static final RegistryObject<Item> COOKED_STEGOSAURUS_MEAT = ITEMS.register("cooked_stegosaurus_meat",
            createMeatFood(8, 1.2F));
    public static final RegistryObject<Item> ANKYLOSAURUS_MEAT = ITEMS.register("ankylosaurus_meat",
            createMeatFood(3, 0.4F));
    public static final RegistryObject<Item> COOKED_ANKYLOSAURUS_MEAT = ITEMS.register("cooked_ankylosaurus_meat",
            createMeatFood(8, 1.2F));

    // --- Carnes de Dinossauros Pequenos ---
    public static final RegistryObject<Item> VELOCIRAPTOR_MEAT = ITEMS.register("velociraptor_meat",
            createMeatFood(2, 0.3F));
    public static final RegistryObject<Item> COOKED_VELOCIRAPTOR_MEAT = ITEMS.register("cooked_velociraptor_meat",
            createMeatFood(5, 0.7F));
    public static final RegistryObject<Item> DILOPHOSAURUS_MEAT = ITEMS.register("dilophosaurus_meat",
            createMeatFood(2, 0.3F));
    public static final RegistryObject<Item> COOKED_DILOPHOSAURUS_MEAT = ITEMS.register("cooked_dilophosaurus_meat",
            createMeatFood(5, 0.7F));
    public static final RegistryObject<Item> DEINONYCHUS_MEAT = ITEMS.register("deinonychus_meat",
            createMeatFood(2, 0.3F));
    public static final RegistryObject<Item> COOKED_DEINONYCHUS_MEAT = ITEMS.register("cooked_deinonychus_meat",
            createMeatFood(5, 0.7F));
    public static final RegistryObject<Item> GALLIMIMUS_MEAT = ITEMS.register("gallimimus_meat",
            createMeatFood(2, 0.3F));
    public static final RegistryObject<Item> COOKED_GALLIMIMUS_MEAT = ITEMS.register("cooked_gallimimus_meat",
            createMeatFood(5, 0.7F));
    public static final RegistryObject<Item> PACHYCEPHALOSAURUS_MEAT = ITEMS.register("pachycephalosaurus_meat",
            createMeatFood(2, 0.3F));
    public static final RegistryObject<Item> COOKED_PACHYCEPHALOSAURUS_MEAT = ITEMS
            .register("cooked_pachycephalosaurus_meat", createMeatFood(5, 0.7F));

    // ==================================================
    // ======== SPAWN EGGS ==============================
    // ==================================================

    public static final RegistryObject<Item> RIVER_FROG_SPAWN_EGG = ITEMS.register("river_frog_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.RIVER_FROG.get(), 0x348C31, 0xC8B732, new Item.Properties()));

    public static final RegistryObject<Item> REX_SPAWN_EGG = ITEMS.register("rex_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.REX.get(), 0x8B4513, 0x5C2E2E, new Item.Properties()));

    public static final RegistryObject<Item> VELOCIRAPTOR_SPAWN_EGG = ITEMS.register("velociraptor_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.VELOCIRAPTOR.get(), 0x9F8C76, 0x785A46,
                    new Item.Properties()));

    public static final RegistryObject<Item> BRACHIOSAURUS_SPAWN_EGG = ITEMS.register("brachiosaurus_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.BRACHIOSAURUS.get(), 0x79828A, 0xD1D1D1,
                    new Item.Properties()));

    public static final RegistryObject<Item> MOSASSAURO_SPAWN_EGG = ITEMS.register("mosassauro_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.MOSASSAURO.get(), 0x3B5998, 0x888888, new Item.Properties()));

    public static final RegistryObject<Item> ALLOSAURUS_SPAWN_EGG = ITEMS.register("allosaurus_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.ALLOSAURUS.get(), 0x7d6650, 0x4a3c2f, new Item.Properties()));

    public static final RegistryObject<Item> ANKYLOSAURUS_SPAWN_EGG = ITEMS.register("ankylosaurus_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.ANKYLOSAURUS.get(), 0x8a8a8a, 0x5e5e5e,
                    new Item.Properties()));

    public static final RegistryObject<Item> GALLIMIMUS_SPAWN_EGG = ITEMS.register("gallimimus_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.GALLIMIMUS.get(), 0xc2a878, 0x7a633a, new Item.Properties()));

    public static final RegistryObject<Item> TRICERATOPS_SPAWN_EGG = ITEMS.register("triceratops_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.TRICERATOPS.get(), 0x637063, 0x4a544a,
                    new Item.Properties()));

    public static final RegistryObject<Item> STEGOSAURUS_SPAWN_EGG = ITEMS.register("stegosaurus_spawn_egg",
            () -> new ForgeSpawnEggItem(() -> ModEntities.STEGOSAURUS.get(), 0x556b2f, 0x8b4513,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
