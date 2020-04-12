package net.telepathicgrunt.bumblezone.biome;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LiquidsConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.telepathicgrunt.bumblezone.biome.surfacebuilders.HoneySurfaceBuilder;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.features.placement.HoneycombHolePlacer;
import net.telepathicgrunt.bumblezone.modcompatibility.ModChecking;

public class BzBaseBiome extends Biome {

    protected static final BlockState CORAL = Blocks.HORN_CORAL_BLOCK.getDefaultState();
    protected static final BlockState HONEY = Blocks.HONEY_BLOCK.getDefaultState();
    protected static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState();
    protected static final BlockState POROUS_HONEYCOMB = BzBlocks.POROUS_HONEYCOMB.get().getDefaultState();
    
    public static final SurfaceBuilderConfig HONEY_CONFIG = new SurfaceBuilderConfig(POROUS_HONEYCOMB, POROUS_HONEYCOMB, POROUS_HONEYCOMB);
    public static final SurfaceBuilder<SurfaceBuilderConfig> HONEY_SURFACE_BUILDER = new HoneySurfaceBuilder(SurfaceBuilderConfig::deserialize);

	public static final Placement<NoPlacementConfig> HONEYCOMB_HOLE_PLACER = new HoneycombHolePlacer(NoPlacementConfig::deserialize);
	
	protected BzBaseBiome(Biome.Builder biomeBuilder) {
		super(biomeBuilder.precipitation(Biome.RainType.NONE).category(Biome.Category.JUNGLE).waterColor(14402413).waterFogColor(11700268).parent((String) null));
	}
	
	public void addVanillaSlimeMobs() {
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
		LiquidsConfig SUGAR_WATER_SPRING_CONFIG = new LiquidsConfig(BzBlocks.SUGAR_WATER_FLUID.get().getDefaultState(), true, 4, 1, ImmutableSet.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK));
		BzBiomes.HIVE_PILLAR.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(SUGAR_WATER_SPRING_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(4, 128, 0, 128))));
		BzBiomes.HIVE_PILLAR.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(SUGAR_WATER_SPRING_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(7, 16, 0, 72))));
	    
		
		BzBiomes.HIVE_WALL.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(SUGAR_WATER_SPRING_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(4, 128, 0, 128))));
		BzBiomes.HIVE_WALL.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(SUGAR_WATER_SPRING_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(7, 16, 0, 72))));

		
		BzBiomes.SUGAR_WATER.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(SUGAR_WATER_SPRING_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(4, 128, 0, 256))));
		BzBiomes.SUGAR_WATER.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(SUGAR_WATER_SPRING_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(10, 16, 0, 128))));
	}
}
