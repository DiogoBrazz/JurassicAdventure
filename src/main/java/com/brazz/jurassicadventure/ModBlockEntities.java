package com.brazz.jurassicadventure;

import com.brazz.jurassicadventure.machines.analyzer.AnalyzerBlockEntity;
import com.brazz.jurassicadventure.machines.assembler.AssemblerBlockEntity;
import com.brazz.jurassicadventure.machines.cables.CableBlockEntity;
import com.brazz.jurassicadventure.machines.generator.GeneratorBlockEntity;
import com.brazz.jurassicadventure.machines.injector.InjectorBlockEntity;
import com.brazz.jurassicadventure.machines.sequencer.SequencerBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
                        .create(ForgeRegistries.BLOCK_ENTITY_TYPES, JurassicAdventure.MODID);

        public static final RegistryObject<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("generator_block_entity",
                                        () -> BlockEntityType.Builder
                                                        .<GeneratorBlockEntity>of(GeneratorBlockEntity::new,
                                                                        ModBlocks.GENERATOR.get())
                                                        .build(null));


        public static final RegistryObject<BlockEntityType<CableBlockEntity>> CABLE_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("cable_block_entity",
                                        () -> BlockEntityType.Builder
                                                        .<CableBlockEntity>of(CableBlockEntity::new,
                                                                        ModBlocks.CABLE.get())
                                                        .build(null));

        public static final RegistryObject<BlockEntityType<AnalyzerBlockEntity>> ANALYZER_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("analyzer_block_entity",
                                        () -> BlockEntityType.Builder
                                                        .<AnalyzerBlockEntity>of(AnalyzerBlockEntity::new,
                                                                        ModBlocks.ANALYZER.get())
                                                        .build(null));

        public static final RegistryObject<BlockEntityType<SequencerBlockEntity>> SEQUENCER_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("sequencer_block_entity",
                                        () -> BlockEntityType.Builder
                                                        .<SequencerBlockEntity>of(SequencerBlockEntity::new,
                                                                        ModBlocks.SEQUENCER.get())
                                                        .build(null));

        public static final RegistryObject<BlockEntityType<AssemblerBlockEntity>> ASSEMBLER_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("assembler_block_entity",
                                        () -> BlockEntityType.Builder
                                                        .<AssemblerBlockEntity>of(AssemblerBlockEntity::new,
                                                                        ModBlocks.ASSEMBLER.get())
                                                        .build(null));

        public static final RegistryObject<BlockEntityType<InjectorBlockEntity>> INJECTOR_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("injector_block_entity",
                                        () -> BlockEntityType.Builder
                                                        .<InjectorBlockEntity>of(InjectorBlockEntity::new,
                                                                        ModBlocks.INJECTOR.get())
                                                        .build(null));

        public static void register(IEventBus eventBus) {
                BLOCK_ENTITIES.register(eventBus);
        }
}
