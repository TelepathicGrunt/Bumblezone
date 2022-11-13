package com.telepathicgrunt.the_bumblezone.modcompat;

import com.teamabnormals.buzzier_bees.core.registry.BBBlocks;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.MatchingBlocksPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class BuzzierBeesCompatRegs {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Bumblezone.MODID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Bumblezone.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> CRYSTALIZED_HONEY_CF = CONFIGURED_FEATURES.register("buzzier_bees_crystallized_honey_cf", () ->
        new ConfiguredFeature<>(Feature.ORE,
            new OreConfiguration(
                new BlockMatchTest(BzBlocks.POROUS_HONEYCOMB.get()),
                BBBlocks.CRYSTALLIZED_HONEY_BLOCK.get().defaultBlockState(),
                33,
                0
            )
        )
    );

    public static final RegistryObject<ConfiguredFeature<?, ?>> HONEYCOMB_TILES_CF = CONFIGURED_FEATURES.register("honeycomb_tiles_cf", () ->
        new ConfiguredFeature<>(Feature.ORE,
            new OreConfiguration(
                new TagMatchTest(BzTags.CAVE_EDGE_BLOCKS_FOR_MODDED_COMPATS),
                BBBlocks.HONEYCOMB_TILES.get().defaultBlockState(),
                33,
                0
            )
        )
    );

    public static final RegistryObject<PlacedFeature> CRYSTALIZED_HONEY_PF = PLACED_FEATURES.register("buzzier_bees_crystallized_honey_pf", () ->
        new PlacedFeature(Holder.direct(CRYSTALIZED_HONEY_CF.get()),
            List.of(
                CountPlacement.of(30),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(
                    VerticalAnchor.aboveBottom(55),
                    VerticalAnchor.belowTop(16)),
                BlockPredicateFilter.forPredicate(new MatchingBlocksPredicate(
                    new Vec3i(0,0,0),
                    HolderSet.direct(BzBlocks.POROUS_HONEYCOMB.getHolder().get())
                )),
                BiomeFilter.biome()
            )
        )
    );

    public static final RegistryObject<PlacedFeature> HONEYCOMB_TILES_PF = PLACED_FEATURES.register("buzzier_bees_honeycomb_tiles_pf", () ->
        new PlacedFeature(Holder.direct(HONEYCOMB_TILES_CF.get()),
            List.of(
                CountPlacement.of(45),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(
                    VerticalAnchor.aboveBottom(41),
                    VerticalAnchor.belowTop(16)),
                BlockPredicateFilter.forPredicate(new MatchingBlocksPredicate(
                    new Vec3i(0,0,0),
                    HolderSet.direct(ForgeRegistries.BLOCKS.getHolder(Blocks.CAVE_AIR).get())
                )),
                BiomeFilter.biome()
            )
        )
    );
}
