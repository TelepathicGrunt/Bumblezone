package com.telepathicgrunt.bumblezone.modinit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class BzConfiguredFeatures {
    private static final SpringConfiguration SUGAR_WATER_SPRING_CONFIG = new SpringConfiguration(BzFluids.SUGAR_WATER_FLUID.defaultFluidState(), true, 4, 1, ImmutableSet.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK));
    public static final ConfiguredFeature<?,?> SUGAR_WATERFALL_HIGH = Feature.SPRING.configured(SUGAR_WATER_SPRING_CONFIG).decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.aboveBottom(128), VerticalAnchor.belowTop(8))))).squared().count(1);
    public static final ConfiguredFeature<?,?> SUGAR_WATERFALL_LOW = Feature.SPRING.configured(SUGAR_WATER_SPRING_CONFIG).decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.aboveBottom(88), VerticalAnchor.belowTop(56))))).squared().count(3);
    public static final ConfiguredFeature<?,?> SUGAR_WATERFALL_FULL_RANGE = Feature.SPRING.configured(SUGAR_WATER_SPRING_CONFIG).decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.aboveBottom(40), VerticalAnchor.belowTop(88))))).squared().count(1);
    public static final ConfiguredFeature<?,?> HONEYCOMB_CAVES = BzFeatures.HONEYCOMB_CAVES.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.NOPE.configured(DecoratorConfiguration.NONE));
    public static final ConfiguredFeature<?,?> POLLINATED_CAVES = BzFeatures.POLLINATED_CAVES.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.NOPE.configured(DecoratorConfiguration.NONE));
    public static final ConfiguredFeature<?,?> HONEY_CRYSTALS_COMMON = BzFeatures.HONEY_CRYSTAL_FEATURE.configured(FeatureConfiguration.NONE).decorated(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.configured(new CountConfiguration(4)));
    public static final ConfiguredFeature<?,?> HONEY_CRYSTALS_UNCOMMON = BzFeatures.HONEY_CRYSTAL_FEATURE.configured(FeatureConfiguration.NONE).decorated(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.configured(new CountConfiguration(2)));
    public static final ConfiguredFeature<?,?> HONEY_CRYSTALS_RARE = BzFeatures.HONEY_CRYSTAL_FEATURE.configured(FeatureConfiguration.NONE).decorated(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.configured(new CountConfiguration(1)));
    public static final ConfiguredFeature<?,?> CAVE_SUGAR_WATERFALL = BzFeatures.CAVE_SUGAR_WATERFALL.configured(FeatureConfiguration.NONE).decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.belowTop(8))))).squared().count(75);

    public static final ConfiguredFeature<?, ?> HONEYCOMB_HOLE = BzFeatures.HONEYCOMB_HOLE.configured(
            new NbtFeatureConfig(
                    new ResourceLocation(Bumblezone.MODID, "honeycomb_hole_processors"),
                    new ResourceLocation("empty"),
                    ImmutableList.of(Pair.of(new ResourceLocation(Bumblezone.MODID, "honeycomb_hole"), 1)),
                    0
            )).decorated(BzPlacements.HONEYCOMB_HOLE_PLACER.configured(DecoratorConfiguration.NONE));


    public static final ConfiguredFeature<?, ?> BEE_DUNGEON = BzFeatures.BEE_DUNGEON.configured(
            new NbtFeatureConfig(
                    new ResourceLocation(Bumblezone.MODID, "bee_dungeon_processors"),
                    new ResourceLocation("empty"),
                    ImmutableList.of(Pair.of(new ResourceLocation(Bumblezone.MODID, "bee_dungeon"), 1)),
                    -4
            )).decorated(BzPlacements.BEE_DUNGEON_PLACER.configured(DecoratorConfiguration.NONE));

    public static final ConfiguredFeature<?, ?> SPIDER_INFESTED_BEE_DUNGEON = BzFeatures.SPIDER_INFESTED_BEE_DUNGEON.configured(
            new NbtFeatureConfig(
                    new ResourceLocation(Bumblezone.MODID, "spider_infested_bee_dungeon_processors"),
                    new ResourceLocation("empty"),
                    ImmutableList.of(Pair.of(new ResourceLocation(Bumblezone.MODID, "bee_dungeon"), 1)),
                    -4
            )).decorated(BzPlacements.BEE_DUNGEON_PLACER.configured(DecoratorConfiguration.NONE));


    public static void registerConfiguredFeatures(){
        Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;
     
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "sugar_waterfall_high"), SUGAR_WATERFALL_HIGH);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "sugar_waterfall_low"), SUGAR_WATERFALL_LOW);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "sugar_waterfall_full_range"), SUGAR_WATERFALL_FULL_RANGE);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honeycomb_caves"), HONEYCOMB_CAVES);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "pollinated_caves"), POLLINATED_CAVES);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honeycomb_holes"), HONEYCOMB_HOLE);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "bee_dungeon"), BEE_DUNGEON);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "spider_infested_bee_dungeon"), SPIDER_INFESTED_BEE_DUNGEON);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honey_crystals_common"), HONEY_CRYSTALS_COMMON);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honey_crystals_uncommon"), HONEY_CRYSTALS_UNCOMMON);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "honey_crystals_rare"), HONEY_CRYSTALS_RARE);
        Registry.register(registry, new ResourceLocation(Bumblezone.MODID, "cave_sugar_waterfall"), CAVE_SUGAR_WATERFALL);
    }
}
