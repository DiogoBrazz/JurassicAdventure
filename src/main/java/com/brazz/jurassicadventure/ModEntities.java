package com.brazz.jurassicadventure;

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

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, JurassicAdventure.MODID);

    public static final RegistryObject<EntityType<RiverFrogEntity>> RIVER_FROG =
        ENTITY_TYPES.register("river_frog", () -> EntityType.Builder.of(RiverFrogEntity::new, MobCategory.CREATURE)
                .sized(0.4f, 0.5f) // Largura e Altura da hitbox
                .build("river_frog"));

    public static final RegistryObject<EntityType<RexEntity>> REX =
        ENTITY_TYPES.register("rex", () -> EntityType.Builder.of(RexEntity::new, MobCategory.CREATURE)
                .sized(RexEntity.ADULT_WIDTH, RexEntity.ADULT_HEIGHT)
                .build("rex"));

    public static final RegistryObject<EntityType<VelociraptorEntity>> VELOCIRAPTOR =
        ENTITY_TYPES.register("velociraptor", () -> EntityType.Builder.of(VelociraptorEntity::new, MobCategory.CREATURE)
                .sized(VelociraptorEntity.ADULT_WIDTH, VelociraptorEntity.ADULT_HEIGHT)
                .build("velociraptor"));

    public static final RegistryObject<EntityType<BrachiosaurusEntity>> BRACHIOSAURUS =
        ENTITY_TYPES.register("brachiosaurus", () -> EntityType.Builder.of(BrachiosaurusEntity::new, MobCategory.CREATURE)
                .sized(BrachiosaurusEntity.ADULT_WIDTH, BrachiosaurusEntity.ADULT_HEIGHT)
                .build("brachiosaurus"));

    public static final RegistryObject<EntityType<MosassauroEntity>> MOSASSAURO =
        ENTITY_TYPES.register("mosassauro", () -> EntityType.Builder.of(MosassauroEntity::new, MobCategory.CREATURE)
                .sized(MosassauroEntity.ADULT_WIDTH, MosassauroEntity.ADULT_HEIGHT)
                .build("mosassauro"));

    //--------------

    public static final RegistryObject<EntityType<AllosaurusEntity>> ALLOSAURUS =
    ENTITY_TYPES.register("allosaurus", () -> EntityType.Builder.of(AllosaurusEntity::new, MobCategory.CREATURE)
            .sized(AllosaurusEntity.ADULT_WIDTH, AllosaurusEntity.ADULT_HEIGHT)
            .build("allosaurus"));

    public static final RegistryObject<EntityType<AnkylosaurusEntity>> ANKYLOSAURUS =
    ENTITY_TYPES.register("ankylosaurus", () -> EntityType.Builder.of(AnkylosaurusEntity::new, MobCategory.CREATURE)
            .sized(AnkylosaurusEntity.ADULT_WIDTH, AnkylosaurusEntity.ADULT_HEIGHT)
            .build("ankylosaurus"));

    public static final RegistryObject<EntityType<GallimimusEntity>> GALLIMIMUS =
    ENTITY_TYPES.register("gallimimus", () -> EntityType.Builder.of(GallimimusEntity::new, MobCategory.CREATURE)
            .sized(GallimimusEntity.ADULT_WIDTH, GallimimusEntity.ADULT_HEIGHT)
            .build("gallimimus"));

    public static final RegistryObject<EntityType<TriceratopsEntity>> TRICERATOPS =
    ENTITY_TYPES.register("triceratops", () -> EntityType.Builder.of(TriceratopsEntity::new, MobCategory.CREATURE)
            .sized(TriceratopsEntity.ADULT_WIDTH, TriceratopsEntity.ADULT_HEIGHT)
            .build("triceratops"));

    public static final RegistryObject<EntityType<StegosaurusEntity>> STEGOSAURUS =
    ENTITY_TYPES.register("stegosaurus", () -> EntityType.Builder.of(StegosaurusEntity::new, MobCategory.CREATURE)
            .sized(StegosaurusEntity.ADULT_WIDTH, StegosaurusEntity.ADULT_HEIGHT)
            .build("stegosaurus"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}