package net.telepathicgrunt.bumblezone.biome;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.telepathicgrunt.bumblezone.biome.surfacebuilders.HoneySurfaceBuilder;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;

public class BzBaseBiome extends Biome {

    protected static final BlockState POROUS_HONEYCOMB = BzBlocks.POROUS_HONEYCOMB.getDefaultState();
    public static final TernarySurfaceConfig HONEY_CONFIG = new TernarySurfaceConfig(POROUS_HONEYCOMB, POROUS_HONEYCOMB, POROUS_HONEYCOMB);
    public static final SurfaceBuilder<TernarySurfaceConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(TernarySurfaceConfig.CODEC);

    protected BzBaseBiome(Biome.Settings biomeBuilder) {
        super(biomeBuilder);
    }


    public static void addSprings() {
        SpringFeatureConfig SUGAR_WATER_SPRING_CONFIG = new SpringFeatureConfig(BzBlocks.SUGAR_WATER_FLUID.getDefaultState(), true, 4, 1, ImmutableSet.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK));
        BzBiomes.HIVE_PILLAR.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(1, 128, 0, 128))));
        BzBiomes.HIVE_PILLAR.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(5, 16, 0, 72))));


        BzBiomes.HIVE_WALL.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(1, 128, 0, 128))));
        BzBiomes.HIVE_WALL.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(5, 16, 0, 72))));


        BzBiomes.SUGAR_WATER.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(1, 128, 0, 256))));
        BzBiomes.SUGAR_WATER.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(7, 16, 0, 128))));
    }
}
