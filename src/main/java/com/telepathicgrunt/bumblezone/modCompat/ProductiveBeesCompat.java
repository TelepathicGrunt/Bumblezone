package com.telepathicgrunt.bumblezone.modCompat;

import com.telepathicgrunt.bumblezone.Bumblezone;
import cy.jdkdigital.productivebees.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.block.ExpansionBox;
import cy.jdkdigital.productivebees.state.properties.VerticalHive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductiveBeesCompat {

	private static final String PRODUCTIVE_BEES_NAMESPACE = "productivebees";
	private static final List<Block> ORE_BASED_HONEYCOMB_VARIANTS = new ArrayList<>();
	public static List<EntityType<?>> PRODUCTIVE_BEES_LIST = new ArrayList<>();
	private static final List<Block> SPIDER_DUNGEON_HONEYCOMBS = new ArrayList<>();
	public static final RuleTest HONEYCOMB_BUMBLEZONE = new TagMatchRuleTest(BlockTags.makeWrapperTag(Bumblezone.MODID+":honeycombs"));

	public static void setupProductiveBees() {
		ModChecker.productiveBeesPresent = true;

		// Dynamically create list of all Productive Bees' bees
		for (EntityType<?> productiveBeeType : ForgeRegistries.ENTITIES) {
			if (productiveBeeType.getRegistryName().getNamespace().equals(PRODUCTIVE_BEES_NAMESPACE)
					&& productiveBeeType.getRegistryName().getPath().contains("bee")) {
				PRODUCTIVE_BEES_LIST.add(productiveBeeType);
			}
		}

		if (Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get()) {
			PBAddHoneycombs();

			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_bauxite")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_brazen")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_bronze")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_copper")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_rotten")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_slimy")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_radioactive")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_withered")));

			// Warn user if a block specific to spider bee dungeons is missing
			for(Block block : SPIDER_DUNGEON_HONEYCOMBS) {
				isBlockMissing(block, block.getRegistryName());
			}
		}

		// Fluid still needs work to be usable and stuff
		//	Fluid honeyFluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(productivebeesRL + ":honey"));
		//	if (newEnoughVersion && honeyFluid != Fluids.EMPTY) {
		//	    LiquidsConfig honeySpringConfig = new LiquidsConfig(honeyFluid.getDefaultState(), false, 4, 1, ImmutableSet.of(Blocks.HONEY_BLOCK, Blocks.HONEYCOMB_BLOCK));
		//	    BzBiomes.HIVE_PILLAR.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(honeySpringConfig).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(Bumblezone.BzConfig.PBHoneyWaterfallRate.get(), 128, 0, 128))));
		//	    BzBiomes.HIVE_WALL.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(honeySpringConfig).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(Bumblezone.BzConfig.PBHoneyWaterfallRate.get(), 128, 0, 128))));
		//	    BzBiomes.SUGAR_WATER.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(honeySpringConfig).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(Bumblezone.BzConfig.PBHoneyWaterfallRate.get(), 16, 0, 128))));
		//	}
	}
	
	public static void PBAddHoneycombs() {

		// Basic combs that that are mostly based on vanilla ores.
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_gold"), 34, 3, 6, 230, true);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_iron"), 26, 2, 30, 210, true);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_redstone"), 22, 1, 30, 210, true);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_lapis"), 22, 1, 6, 30, true);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_emerald"), 5, 1, 6, 244, true);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_ender"), 5, 1, 200, 50, true);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_diamond"), 7, 1, 6, 244, true);

		// Other combs unique to Productive Bees
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_blazing"), 34, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_glowing"), 34, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_bone"), 22, 1, 6, 25, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_fossilised"), 18, 1, 4, 20, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_draconic"), 5, 1, 200, 50, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_draconic"), 5, 1, 2, 10, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_powdery"), 7, 1, 60, 244, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_quartz"), 7, 1, 60, 244, false);
		
		//0.1.13 productive bees
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_magmatic"), 34, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_amber"), 34, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_electrum"), 30, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_invar"), 10, 1, 2, 244, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_leaden"), 10, 1, 1, 30, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_nickel"), 10, 1, 1, 30, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_osmium"), 9, 1, 1, 30, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_platinum"), 5, 1, 1, 30, true);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_silver"), 9, 1, 1, 30, true);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_steel"), 9, 1, 1, 200, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_tin"), 9, 1, 1, 200, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_titanium"), 6, 1, 1, 30, true);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_tungsten"), 9, 1, 1, 200, false);
		addCombToWorldgen(new ResourceLocation(PRODUCTIVE_BEES_NAMESPACE + ":comb_zinc"), 9, 1, 1, 200, false);
	}

	private static boolean isBlockMissing(Block block, ResourceLocation blockRL) {
		if (block == Blocks.AIR) {
			Bumblezone.LOGGER.log(Level.INFO,"------------------------------------------------NOTICE-------------------------------------------------------------------------");
			Bumblezone.LOGGER.log(Level.INFO, " ");
			Bumblezone.LOGGER.log(Level.INFO, "BUMBLEZONE: Error trying to get the following block: " + blockRL.toString() + ". Please let The Bumblezone developer know about this!");
			Bumblezone.LOGGER.log(Level.INFO, " ");
			Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
			return true;
		}
		return false;
	}

	private static void addCombToWorldgen(ResourceLocation blockRL, int veinSize, int count, int bottomOffset, int range, boolean addToHoneycombList) {
		Block honeycombBlock = ForgeRegistries.BLOCKS.getValue(blockRL);
		if (isBlockMissing(honeycombBlock, blockRL)) return;

//		BzBiomes.biomes.forEach(biome -> ((BzBaseBiome) biome).addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
//			Feature.ORE.withConfiguration(HONEYCOMB_BUMBLEZONE)
//				.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(count, bottomOffset, 0, range)))));

		if (addToHoneycombList)
			ORE_BASED_HONEYCOMB_VARIANTS.add(honeycombBlock);
	}

	/**
	 * Is block is a ProductiveBees nest or beenest block
	 */
	public static boolean PBIsAdvancedBeehiveAbstractBlock(BlockState block) {

		if (block.getBlock() instanceof ExpansionBox && block.get(AdvancedBeehive.EXPANDED) != VerticalHive.NONE) {
			return true; // expansion boxes only count as beenest when they expand a hive.
		} else {
			return block.getBlock() instanceof AdvancedBeehiveAbstract; // all other nests/hives here so return true
		}
	}

	/**
	 * 1/15th of bees spawning will also spawn Productive Bees' bees
	 */
	public static void PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {

		if (PRODUCTIVE_BEES_LIST.size() == 0) {
			Bumblezone.LOGGER.warn(
					"Error! List of productive bees is empty! Cannot spawn their bees. " +
					"Please let TelepathicGrunt (The Bumblezone dev) know about this!");
			return;
		}

		MobEntity entity = (MobEntity) event.getEntity();
		IServerWorld world = (IServerWorld) event.getWorld();

		// randomly pick a productive bee
		MobEntity productiveBeeEntity = (MobEntity) PRODUCTIVE_BEES_LIST
				.get(world.getRandom().nextInt(PRODUCTIVE_BEES_LIST.size())).create(entity.world);

		BlockPos.Mutable blockpos = new BlockPos.Mutable().setPos(entity.getBlockPos());
		productiveBeeEntity.setLocationAndAngles(
				blockpos.getX(),
				blockpos.getY(),
				blockpos.getZ(),
				world.getRandom().nextFloat() * 360.0F,
				0.0F);

		productiveBeeEntity.onInitialSpawn(
				world,
				world.getDifficultyForLocation(productiveBeeEntity.getBlockPos()),
				event.getSpawnReason(),
				null,
				null);

		world.addEntity(productiveBeeEntity);
	}

	/**
	 * Safely get Rottened Honeycomb. If Rottened Honeycomb wasn't found, return
	 * Vanilla's Honeycomb
	 */
	public static Block PBGetRottenedHoneycomb(Random random) {
		Block replacementBlock = SPIDER_DUNGEON_HONEYCOMBS.get(random.nextInt(random.nextInt(SPIDER_DUNGEON_HONEYCOMBS.size())+1));
		return replacementBlock == Blocks.AIR ? Blocks.HONEYCOMB_BLOCK : replacementBlock;
	}

	/**
	 * Picks a random Productive Bees Honeycomb with lower index of
	 * ORE_BASED_HONEYCOMB_VARIANTS list being highly common
	 */
	public static Block PBGetRandomHoneycomb(Random random, int lowerBoundBias) {
		int index = ORE_BASED_HONEYCOMB_VARIANTS.size() - 1;

		for (int i = 0; i < lowerBoundBias && index != 0; i++) {
			index = random.nextInt(index + 1);
		}

		return ORE_BASED_HONEYCOMB_VARIANTS.get(index);
	}
}
