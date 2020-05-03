package net.telepathicgrunt.bumblezone.biome.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.telepathicgrunt.bumblezone.biome.BzBaseBiome;
import net.telepathicgrunt.bumblezone.biome.surfacebuilders.BzSurfaceBuilders;
import net.telepathicgrunt.bumblezone.features.BzFeatures;
import net.telepathicgrunt.bumblezone.features.placement.BzPlacements;

public final class SugarWaterBiome extends BzBaseBiome
{
	public SugarWaterBiome()
	{
		super((new Builder()).surfaceBuilder(new ConfiguredSurfaceBuilder<>(BzSurfaceBuilders.HONEY_SURFACE_BUILDER, BzSurfaceBuilders.HONEY_CONFIG)).depth(-2F).scale(0.01F).temperature(1.85F).downfall(0.5F));

		this.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, BzFeatures.HONEYCOMB_CAVES.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		this.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, BzFeatures.HONEYCOMB_HOLE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(BzPlacements.HONEYCOMB_HOLE_PLACER.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		this.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, BzFeatures.BEE_DUNGEON.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(BzPlacements.BEE_DUNGEON_PLACER.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, BzFeatures.CAVE_SUGAR_WATERFALL.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(100, 8, 0, 248))));
		
	    this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.BEE, 20, 5, 10));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SNOWBALL, 2100, 1, 1)); //Used to make monsters even less common
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SLIME, 20, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 2, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.PHANTOM, 1, 1, 1));
	}

	/**
	 * returns the chance a creature has to spawn.
	 */
	@Override
	public float getSpawningChance()
	{
		return 0.35F;
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
