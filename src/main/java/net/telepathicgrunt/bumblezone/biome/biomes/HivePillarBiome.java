package net.telepathicgrunt.bumblezone.biome.biomes;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.telepathicgrunt.bumblezone.biome.BzBaseBiome;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.features.BzFeatures;
import net.telepathicgrunt.bumblezone.features.decorators.BzPlacements;

public final class HivePillarBiome extends BzBaseBiome {
    public HivePillarBiome() {
        super((new Settings())
                .surfaceBuilder(new ConfiguredSurfaceBuilder<>(HONEY_SURFACE_BUILDER, HONEY_CONFIG))
                .precipitation(Biome.Precipitation.NONE)
                .category(Biome.Category.NONE)
                .depth(5.5F)
                .scale(0.5F)
                .temperature(1.85F)
                .downfall(0.5F)
                .effects((new BiomeEffects.Builder())
                        .waterColor(14402413)
                        .waterFogColor(11700268)
                        .fogColor(12638463)
                        .loopSound(SoundEvents.BLOCK_BEEHIVE_WORK)
                        .build())
                .parent(null));

        this.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, BzFeatures.HONEYCOMB_CAVES.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT)));
        this.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, BzFeatures.HONEYCOMB_HOLE.configure(FeatureConfig.DEFAULT).createDecoratedFeature(BzPlacements.HONEYCOMB_HOLE_PLACER.configure(DecoratorConfig.DEFAULT)));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, BzFeatures.BEE_DUNGEON.configure(FeatureConfig.DEFAULT).createDecoratedFeature(BzPlacements.BEE_DUNGEON_PLACER.configure(DecoratorConfig.DEFAULT)));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, BzFeatures.SPIDER_INFESTED_BEE_DUNGEON.configure(FeatureConfig.DEFAULT).createDecoratedFeature(BzPlacements.BEE_DUNGEON_PLACER.configure(DecoratorConfig.DEFAULT)));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, BzFeatures.HONEY_CRYSTAL_FEATURE.configure(FeatureConfig.DEFAULT).createDecoratedFeature(BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.configure(new CountDecoratorConfig(2))));
        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, BzFeatures.CAVE_SUGAR_WATERFALL.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(100, 8, 0, 248))));

        SpringFeatureConfig SUGAR_WATER_SPRING_CONFIG = new SpringFeatureConfig(BzBlocks.SUGAR_WATER_FLUID.getDefaultState(), true, 4, 1, ImmutableSet.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK));
        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(1, 128, 0, 128))));
        this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(SUGAR_WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(5, 16, 0, 72))));

        this.addSpawn(SpawnGroup.CREATURE, new Biome.SpawnEntry(EntityType.BEE, 20, 5, 8));
        this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.SNOWBALL, 3000, 1, 1)); //Used to make monsters even less common
        this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.SPIDER, 10, 1, 1));
        this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.CAVE_SPIDER, 2, 1, 1));
        this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.ENDERMAN, 10, 1, 1));
        this.addSpawn(SpawnGroup.MONSTER, new Biome.SpawnEntry(EntityType.PHANTOM, 1, 1, 1));
    }

    /**
     * returns the chance a creature has to spawn.
     */
    @Override
    public float getMaxSpawnChance() {
        return 0.45F;
    }


    /*
     * Set sky color
     */
    @Override
    @Environment(EnvType.CLIENT)
    public int getSkyColor() {
        return 16759808;
    }


    /*
     * set grass color
     */
    @Override
    @Environment(EnvType.CLIENT)
    public int getGrassColorAt(double x, double z) {
        return 7600187;
    }


    /*
     * set foliage/plant color
     */
    @Override
    @Environment(EnvType.CLIENT)
    public int getFoliageColor() {
        return 7593531;
    }

}
