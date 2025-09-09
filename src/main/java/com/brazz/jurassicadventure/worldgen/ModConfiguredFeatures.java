package com.brazz.jurassicadventure.worldgen;

import com.brazz.jurassicadventure.ModBlocks; // <-- CORREÇÃO 1: Importe sua classe de blocos.
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration; // <-- CORREÇÃO 2: Import necessário
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMBER_ORE_KEY = registerKey("amber_ore");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Regras para substituição de blocos (pedra e ardósia)
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // Lista de alvos
        List<OreConfiguration.TargetBlockState> amberOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.AMBER_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_AMBER_ORE.get().defaultBlockState())
        );

        // Registra a configured feature
        register(context, AMBER_ORE_KEY, Feature.ORE, new OreConfiguration(amberOres, 7, 0.5f)); // Tamanho do veio = 9
    }

    // Métodos auxiliares
    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("jurassicadventure", name));
    }

    // <-- CORREÇÃO 3: Assinatura do método corrigida para evitar os erros de "Bound mismatch"
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                           ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}