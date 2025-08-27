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
import com.brazz.jurassicadventure.machines.injector.InjectorBlock;
import com.brazz.jurassicadventure.machines.sequencer.SequencerBlock;
import com.brazz.jurassicadventure.machines.generator.GeneratorBlock;

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
