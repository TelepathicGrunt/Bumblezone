package net.telepathicgrunt.bumblezone.biome.biomes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.telepathicgrunt.bumblezone.biome.BzBaseBiome;
import net.telepathicgrunt.bumblezone.features.BzFeatureInit;

public final class SugarWaterBiome extends BzBaseBiome
{
	public SugarWaterBiome()
	{
		super((new Settings()).surfaceBuilder(new ConfiguredSurfaceBuilder<>(HONEY_SURFACE_BUILDER, HONEY_CONFIG)).precipitation(Biome.Precipitation.NONE).category(Biome.Category.JUNGLE).depth(-2F).scale(0.01F).temperature(1.85F).downfall(0.5F).effects((new BiomeEffects.Builder()).waterColor(16167168).waterFogColor(13528064).fogColor(12638463).method_24943(SoundEvents.BLOCK_BEEHIVE_WORK).build()).parent((String) null));

		this.addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, BzFeatureInit.HONEYCOMB_HOLE.configure(FeatureConfig.DEFAULT).createDecoratedFeature(HONEYCOMB_HOLE_PLACER.configure(DecoratorConfig.DEFAULT)));
		
	    this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(4, 128, 0, 256))));
	    this.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(WATER_SPRING_CONFIG).createDecoratedFeature(Decorator.COUNT_BIASED_RANGE.configure(new RangeDecoratorConfig(10, 16, 0, 128))));

		this.addSpawn(EntityCategory.CREATURE, new Biome.SpawnEntry(EntityType.BEE, 100, 5, 10));
		this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.SNOWBALL, 2100, 1, 1)); //Used to make monsters even less common
		this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.SPIDER, 12, 1, 1));
		this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.ENDERMAN, 2, 1, 1));
		this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.PHANTOM, 1, 1, 1));
		this.addSpawn(EntityCategory.MONSTER, new Biome.SpawnEntry(EntityType.SLIME, 500, 4, 8));
	}

	/**
	 * returns the chance a creature has to spawn.
	 */
	public float getMaxSpawnLimit()
	{
		return 0.45F;
	}

	
	/*
	 * Set sky color
	 */
	@Environment(EnvType.CLIENT)
	public int getSkyColor()
	{
		return 16759808;
	}


	/*
	 * set grass color
	 */
	@Environment(EnvType.CLIENT)
	public int getGrassColorAt(double p_225528_1_, double p_225528_3_)
	{
		return 7600187;
	}


	/*
	 * set foliage/plant color
	 */
	@Environment(EnvType.CLIENT)
	public int getFoliageColor()
	{
		return 7593531;
	}

}
