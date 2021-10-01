package com.telepathicgrunt.bumblezone.modinit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

public class BzConfiguredFeatures {
    private static final SpringFeatureConfig SUGAR_WATER_SPRING_CONFIG = new SpringFeatureConfig(BzBlocks.SUGAR_WATER_FLUID.getDefaultState(), true, 4, 1, ImmutableSet.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK));
    public static final ConfiguredFeature<?,?> SUGAR_WATERFALL_HIGH = Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.aboveBottom(128), YOffset.belowTop(8))))).spreadHorizontally().repeat(1);
    public static final ConfiguredFeature<?,?> SUGAR_WATERFALL_LOW = Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.aboveBottom(88), YOffset.belowTop(56))))).spreadHorizontally().repeat(3);
    public static final ConfiguredFeature<?,?> SUGAR_WATERFALL_FULL_RANGE = Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.aboveBottom(40), YOffset.belowTop(88))))).spreadHorizontally().repeat(1);
    public static final ConfiguredFeature<?,?> HONEYCOMB_CAVES = BzFeatures.HONEYCOMB_CAVES.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(DecoratorConfig.DEFAULT));
    public static final ConfiguredFeature<?,?> POLLINATED_CAVES = BzFeatures.POLLINATED_CAVES.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(DecoratorConfig.DEFAULT));
    public static final ConfiguredFeature<?,?> HONEY_CRYSTALS_COMMON = BzFeatures.HONEY_CRYSTAL_FEATURE.configure(FeatureConfig.DEFAULT).decorate(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.configure(new CountConfig(4)));
    public static final ConfiguredFeature<?,?> HONEY_CRYSTALS_UNCOMMON = BzFeatures.HONEY_CRYSTAL_FEATURE.configure(FeatureConfig.DEFAULT).decorate(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.configure(new CountConfig(2)));
    public static final ConfiguredFeature<?,?> HONEY_CRYSTALS_RARE = BzFeatures.HONEY_CRYSTAL_FEATURE.configure(FeatureConfig.DEFAULT).decorate(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.configure(new CountConfig(1)));
    public static final ConfiguredFeature<?,?> CAVE_SUGAR_WATERFALL = BzFeatures.CAVE_SUGAR_WATERFALL.configure(FeatureConfig.DEFAULT).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.aboveBottom(8), YOffset.belowTop(8))))).spreadHorizontally().repeat(75);

    public static final ConfiguredFeature<?, ?> HONEYCOMB_HOLE = BzFeatures.HONEYCOMB_HOLE.configure(
            new NbtFeatureConfig(
                    new Identifier(Bumblezone.MODID, "honeycomb_hole_processors"),
                    new Identifier("empty"),
                    ImmutableList.of(Pair.of(new Identifier(Bumblezone.MODID, "honeycomb_hole"), 1)),
                    0
            )).decorate(BzPlacements.HONEYCOMB_HOLE_PLACER.configure(DecoratorConfig.DEFAULT));


    public static final ConfiguredFeature<?, ?> BEE_DUNGEON = BzFeatures.BEE_DUNGEON.configure(
            new NbtFeatureConfig(
                    new Identifier(Bumblezone.MODID, "bee_dungeon_processors"),
                    new Identifier("empty"),
                    ImmutableList.of(Pair.of(new Identifier(Bumblezone.MODID, "bee_dungeon"), 1)),
                    -4
            )).decorate(BzPlacements.BEE_DUNGEON_PLACER.configure(DecoratorConfig.DEFAULT));

    public static final ConfiguredFeature<?, ?> SPIDER_INFESTED_BEE_DUNGEON = BzFeatures.SPIDER_INFESTED_BEE_DUNGEON.configure(
            new NbtFeatureConfig(
                    new Identifier(Bumblezone.MODID, "spider_infested_bee_dungeon_processors"),
                    new Identifier("empty"),
                    ImmutableList.of(Pair.of(new Identifier(Bumblezone.MODID, "bee_dungeon"), 1)),
                    -4
            )).decorate(BzPlacements.BEE_DUNGEON_PLACER.configure(DecoratorConfig.DEFAULT));


    public static void registerConfiguredFeatures(){
        Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;
     
        Registry.register(registry, new Identifier(Bumblezone.MODID, "sugar_waterfall_high"), SUGAR_WATERFALL_HIGH);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "sugar_waterfall_low"), SUGAR_WATERFALL_LOW);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "sugar_waterfall_full_range"), SUGAR_WATERFALL_FULL_RANGE);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "honeycomb_caves"), HONEYCOMB_CAVES);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "pollinated_caves"), POLLINATED_CAVES);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "honeycomb_holes"), HONEYCOMB_HOLE);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "bee_dungeon"), BEE_DUNGEON);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "spider_infested_bee_dungeon"), SPIDER_INFESTED_BEE_DUNGEON);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "honey_crystals_common"), HONEY_CRYSTALS_COMMON);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "honey_crystals_uncommon"), HONEY_CRYSTALS_UNCOMMON);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "honey_crystals_rare"), HONEY_CRYSTALS_RARE);
        Registry.register(registry, new Identifier(Bumblezone.MODID, "cave_sugar_waterfall"), CAVE_SUGAR_WATERFALL);
    }
}
