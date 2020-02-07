package net.telepathicgrunt.bumblezone.world.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.telepathicgrunt.bumblezone.world.biome.BzBaseBiome;

public final class HiveWallBiome extends BzBaseBiome
{
	public HiveWallBiome()
	{
		super((new Builder()).surfaceBuilder(new ConfiguredSurfaceBuilder<>(HONEY_SURFACE_BUILDER, HONEY_CONFIG)).precipitation(Biome.RainType.NONE).category(Biome.Category.JUNGLE).depth(0.1F).scale(0.2F).temperature(1.85F).downfall(0.5F).waterColor(16167168).waterFogColor(13528064).parent((String) null));

		
	    this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(WATER_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(4, 128, 0, 256))));
	    this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.configure(WATER_SPRING_CONFIG).createDecoratedFeature(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(10, 16, 0, 128))));
		
	    this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.field_226289_e_, 1, 2, 4));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SNOWBALL, 2000, 1, 1)); //Used to make monsters even less common
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SPIDER, 25, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 6, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.PHANTOM, 2, 1, 1));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SLIME, 350, 4, 8));
	}

	/**
	 * returns the chance a creature has to spawn.
	 */
	public float getSpawningChance()
	{
		return 0.4F;
	}

	
	/*
	 * Set sky color
	 */
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
