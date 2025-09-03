package com.brazz.jurassicadventure;

import com.brazz.jurassicadventure.dinosconfig.entity.BrachiosaurusEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.MosassauroEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.RexEntity;
import com.brazz.jurassicadventure.dinosconfig.entity.RiverFrogEntity;
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
                .sized(5f, 5f) // Largura e Altura da hitbox
                .build("rex"));

    public static final RegistryObject<EntityType<VelociraptorEntity>> VELOCIRAPTOR =
        ENTITY_TYPES.register("velociraptor", () -> EntityType.Builder.of(VelociraptorEntity::new, MobCategory.CREATURE)
                .sized(0.4f, 0.5f) // Largura e Altura da hitbox
                .build("velociraptor"));

    public static final RegistryObject<EntityType<BrachiosaurusEntity>> BRACHIOSAURUS =
        ENTITY_TYPES.register("brachiosaurus", () -> EntityType.Builder.of(BrachiosaurusEntity::new, MobCategory.CREATURE)
                .sized(6.5f, 10.0f) // Largura e Altura da hitbox
                .build("brachiosaurus"));

    public static final RegistryObject<EntityType<MosassauroEntity>> MOSASSAURO =
        ENTITY_TYPES.register("mosassauro", () -> EntityType.Builder.of(MosassauroEntity::new, MobCategory.CREATURE)
                .sized(3f, 4.0f) // Largura e Altura da hitbox
                .build("mosassauro"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}