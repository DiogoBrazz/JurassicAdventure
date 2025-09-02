package com.brazz.jurassicadventure;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import com.brazz.jurassicadventure.machines.analyzer.AnalyzerBlock;
import com.brazz.jurassicadventure.machines.assembler.AssemblerBlock;
import com.brazz.jurassicadventure.machines.cables.CableBlock;
import com.brazz.jurassicadventure.machines.electricfence.ElectricFencePillarBlock;
import com.brazz.jurassicadventure.machines.electricfence.ElectricFenceWireBlock;
import com.brazz.jurassicadventure.machines.injector.InjectorBlock;
import com.brazz.jurassicadventure.machines.mixer.MixerBlock;
import com.brazz.jurassicadventure.machines.sequencer.SequencerBlock;
import com.brazz.jurassicadventure.machines.generator.GeneratorBlock;
import com.brazz.jurassicadventure.machines.incubator.IncubatorBlock;

public class ModBlocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
                        JurassicAdventure.MODID);

        // --- BLOCOS ---
        public static final RegistryObject<Block> AMBER_ORE = registerBlock("amber_ore",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)));

        // --- MÁQUINAS ---

        public static final RegistryObject<Block> GENERATOR = registerBlock("generator",
                        () -> new GeneratorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                        .requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> CABLE = registerBlock("cable",
                        () -> new CableBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                        .requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> ANALYZER = registerBlock("analyzer",
                        () -> new AnalyzerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                        .requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> SEQUENCER = registerBlock("sequencer",
                        () -> new SequencerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                        .requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> ASSEMBLER = registerBlock("assembler",
                        () -> new AssemblerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                        .requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> INJECTOR = registerBlock("injector",
                        () -> new InjectorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                        .requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> MIXER = registerBlock("mixer",
                        () -> new MixerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                        .requiresCorrectToolForDrops()));
        public static final RegistryObject<Block> INCUBATOR = registerBlock("incubator",
                        () -> new IncubatorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                        .requiresCorrectToolForDrops()));

        public static final RegistryObject<Block> ELECTRIC_FENCE_PILLAR = registerBlock("electric_fence_pillar",
        () -> new ElectricFencePillarBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

        public static final RegistryObject<Block> ELECTRIC_FENCE_WIRE = registerBlock("electric_fence_wire",
        () -> new ElectricFenceWireBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).noOcclusion()));

        // --- MÉTODO AUXILIAR SIMPLIFICADO ---
        private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
                // Agora ele só regista o bloco. O item será registado em ModItems.
                RegistryObject<T> toReturn = BLOCKS.register(name, block);
                registerBlockItem(name, toReturn); // A chamada para o método que regista o item
                return toReturn;
        }

        // O método que realmente regista o item do bloco, mas na lista de ITENS.
        private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
                ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        }

        public static void register(IEventBus eventBus) {
                BLOCKS.register(eventBus);
        }
}
