package com.telepathicgrunt.the_bumblezone.modinit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;

public class BzConfiguredFeatures {
    private static final LiquidsConfig SUGAR_WATER_SPRING_CONFIG = new LiquidsConfig(BzFluids.SUGAR_WATER_FLUID.get().defaultFluidState(), true, 4, 1, ImmutableSet.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK));
    public static final ConfiguredFeature<?, ?> SUGAR_WATERFALL_HIGH = Feature.SPRING.configured(SUGAR_WATER_SPRING_CONFIG).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(128, 0, 128)).squared().count(1));
    public static final ConfiguredFeature<?, ?> SUGAR_WATERFALL_LOW = Feature.SPRING.configured(SUGAR_WATER_SPRING_CONFIG).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(128, 40, 72)).squared().count(3));
    public static final ConfiguredFeature<?, ?> SUGAR_WATERFALL_FULL_RANGE = Feature.SPRING.configured(SUGAR_WATER_SPRING_CONFIG).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(40, 0, 128)).squared().count(1));
    public static final ConfiguredFeature<?, ?> HONEYCOMB_CAVES = BzFeatures.HONEYCOMB_CAVES.get().configured(IFeatureConfig.NONE).decorated(Placement.NOPE.configured(IPlacementConfig.NONE));
    public static final ConfiguredFeature<?, ?> HONEYCOMB_HOLE = BzFeatures.HONEYCOMB_HOLE.get().configured(IFeatureConfig.NONE).decorated(BzPlacements.HONEYCOMB_HOLE_PLACER.get().configured(IPlacementConfig.NONE));
    public static final ConfiguredFeature<?, ?> HONEY_CRYSTALS_COMMON = BzFeatures.HONEY_CRYSTAL_FEATURE.get().configured(IFeatureConfig.NONE).decorated(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.get().configured(new FeatureSpreadConfig(4)));
    public static final ConfiguredFeature<?, ?> HONEY_CRYSTALS_UNCOMMON = BzFeatures.HONEY_CRYSTAL_FEATURE.get().configured(IFeatureConfig.NONE).decorated(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.get().configured(new FeatureSpreadConfig(2)));
    public static final ConfiguredFeature<?, ?> HONEY_CRYSTALS_RARE = BzFeatures.HONEY_CRYSTAL_FEATURE.get().configured(IFeatureConfig.NONE).decorated(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.get().configured(new FeatureSpreadConfig(1)));
    public static final ConfiguredFeature<?, ?> CAVE_SUGAR_WATERFALL = BzFeatures.CAVE_SUGAR_WATERFALL.get().configured(IFeatureConfig.NONE).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(8, 0, 248)).squared().count(75));
    public static ConfiguredFeature<?, ?> BZ_BEES_WAX_PILLAR_CONFIGURED_FEATURE = BzFeatures.BZ_BEES_WAX_PILLAR_FEATURE.get().configured(IFeatureConfig.NONE).range(256).squared().count(30);

    public static final ConfiguredFeature<?, ?> BEE_DUNGEON = BzFeatures.BEE_DUNGEON.get().configured(
            new NbtFeatureConfig(
                    new ResourceLocation(Bumblezone.MODID, "bee_dungeon_processors"),
                    new ResourceLocation("empty"),
                    ImmutableList.of(Pair.of(EntityType.BEE, 1)),
                    ImmutableList.of(Pair.of(new ResourceLocation(Bumblezone.MODID, "bee_dungeon"), 1)),
                    -4
            )).decorated(BzPlacements.BEE_DUNGEON_PLACER.get().configured(IPlacementConfig.NONE));

    public static final ConfiguredFeature<?, ?> SPIDER_INFESTED_BEE_DUNGEON = BzFeatures.SPIDER_INFESTED_BEE_DUNGEON.get().configured(
            new NbtFeatureConfig(
                    new ResourceLocation(Bumblezone.MODID, "spider_infested_bee_dungeon_processors"),
                    new ResourceLocation("empty"),
                    ImmutableList.of(Pair.of(EntityType.BEE, 1)),
                    ImmutableList.of(Pair.of(new ResourceLocation(Bumblezone.MODID, "bee_dungeon"), 1)),
                    -4
            )).decorated(BzPlacements.BEE_DUNGEON_PLACER.get().configured(IPlacementConfig.NONE));

    public static void registerConfiguredFeatures() {
        Registry<ConfiguredFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_FEATURE;

        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "sugar_waterfall_high"), SUGAR_WATERFALL_HIGH);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "sugar_waterfall_low"), SUGAR_WATERFALL_LOW);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "sugar_waterfall_full_range"), SUGAR_WATERFALL_FULL_RANGE);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honeycomb_caves"), HONEYCOMB_CAVES);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honeycomb_holes"), HONEYCOMB_HOLE);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "bee_dungeon"), BEE_DUNGEON);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "spider_infested_bee_dungeon"), SPIDER_INFESTED_BEE_DUNGEON);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honey_crystals_common"), HONEY_CRYSTALS_COMMON);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honey_crystals_uncommon"), HONEY_CRYSTALS_UNCOMMON);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honey_crystals_rare"), HONEY_CRYSTALS_RARE);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "cave_sugar_waterfall"), CAVE_SUGAR_WATERFALL);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "bz_bees_wax_pillar_configured_feature"), BZ_BEES_WAX_PILLAR_CONFIGURED_FEATURE);
    }
}
