package net.telepathicgrunt.bumblezone.biome.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.telepathicgrunt.bumblezone.biome.BzBaseBiome;
import net.telepathicgrunt.bumblezone.features.BzFeatures;

public final class HiveWallBiome extends BzBaseBiome
{
	public HiveWallBiome()
	{
		super((new Builder()).surfaceBuilder(new ConfiguredSurfaceBuilder<>(HONEY_SURFACE_BUILDER, HONEY_CONFIG)).precipitation(Biome.RainType.NONE).category(Biome.Category.JUNGLE).depth(0.1F).scale(0.2F).temperature(1.85F).downfall(0.5F).waterColor(16167168).waterFogColor(13528064).parent((String) null));

		this.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, BzFeatures.HONEYCOMB_HOLE.configure(IFeatureConfig.NO_FEATURE_CONFIG).createDecoratedFeature(HONEYCOMB_HOLE_PLACER.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		
	    this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(WATER_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(4, 128, 0, 128))));
	    this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(WATER_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(7, 16, 0, 72))));

	    this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.field_226289_e_, 10, 8, 12));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SNOWBALL, 3000, 1, 1)); //Used to make monsters even less common
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SPIDER, 30, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 10, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.PHANTOM, 4, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SLIME, 350, 4, 8));
	}

	/**
	 * returns the chance a creature has to spawn.
	 */
	@Override
	public float getSpawningChance()
	{
		return 0.6F;
	}

	
	/*
	 * Set sky color
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public int getSkyColor()
	{
		return 16759808;
	}


	/*
	 * set grass color
	 */
	@OnlyIn(Dist.CLIENT)
	public int func_225528_a_(double p_225528_1_, double p_225528_3_)
	{
		return 7600187;
	}


	/*
	 * set foliage/plant color
	 */
	@OnlyIn(Dist.CLIENT)
	public int func_225527_a_()
	{
		return 7593531;
	}

}
