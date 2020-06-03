package net.telepathicgrunt.bumblezone.biome;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LiquidsConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.modcompatibility.ModChecking;

public class BzBaseBiome extends Biome {
	
	protected BzBaseBiome(Biome.Builder biomeBuilder) {
		super(biomeBuilder.precipitation(Biome.RainType.NONE).category(Biome.Category.JUNGLE).waterColor(14402413).waterFogColor(11700268).parent((String) null));
	}
	
	public void increaseVanillaSlimeMobsRates() {
		if(!ModChecking.buzzierBeesPresent) {
			this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SLIME, 350, 4, 8));
		}
	}
	
	public <T extends Entity> void addModMobs(EntityClassification classification, EntityType<T> mob, int weight, int minGroup, int maxGroup) {
		this.addSpawn(classification, new Biome.SpawnListEntry(mob, weight, minGroup, maxGroup));
	}
	
	/**
	 * We have to add springs after all registration because someone at Forge decided that biomes registry should fire
	 * before fluids so adding a spring with custom fluid in the biome constructor will crash the game.
	 * 
	 * gg Forge. gg.
	 */
	public static void addSprings() {
		LiquidsConfig sugarWaterSpringConfig = new LiquidsConfig(BzBlocks.SUGAR_WATER_FLUID.get().getDefaultState(), true, 4, 1, ImmutableSet.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK));
		BzBiomes.HIVE_PILLAR.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(sugarWaterSpringConfig).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(1, 128, 0, 128))));
		BzBiomes.HIVE_PILLAR.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(sugarWaterSpringConfig).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(5, 16, 0, 72))));
	    
		
		BzBiomes.HIVE_WALL.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(sugarWaterSpringConfig).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(1, 128, 0, 128))));
		BzBiomes.HIVE_WALL.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(sugarWaterSpringConfig).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(5, 16, 0, 72))));

		
		BzBiomes.SUGAR_WATER.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(sugarWaterSpringConfig).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(1, 128, 0, 256))));
		BzBiomes.SUGAR_WATER.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(sugarWaterSpringConfig).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(7, 16, 0, 128))));
	}
}
