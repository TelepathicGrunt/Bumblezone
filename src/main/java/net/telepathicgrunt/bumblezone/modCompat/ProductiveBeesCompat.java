package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import cy.jdkdigital.productivebees.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.block.ExpansionBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.biome.BzBaseBiome;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;

public class ProductiveBeesCompat {

	private static String productivebeesNamespace = "productivebees";
	private static List<Block> ORE_BASED_HONEYCOMB_VARIANTS = new ArrayList<>();
	public static List<EntityType<?>> productiveBeesList = new ArrayList<>();
	private static List<Block> SPIDER_DUNGEON_HONEYCOMBS = new ArrayList<>();
	private static boolean newEnoughVersion = false;

	public static void setupProductiveBees() {
		ModChecking.productiveBeesPresent = true;

		// create list of all Productive Bees' bees
		for (EntityType<?> productiveBeeType : ForgeRegistries.ENTITIES) {
			if (productiveBeeType.getRegistryName().getNamespace().equals(productivebeesNamespace)
					&& productiveBeeType.getRegistryName().getPath().contains("bee")) {
				productiveBeesList.add(productiveBeeType);
			}
		}

		// Only do the honeycombs when productive bee's version is new enough
		ArtifactVersion versionObj = ModList.get().getModContainerById(productivebeesNamespace).get().getModInfo()
				.getVersion();
		if (versionObj.getMajorVersion() != 0
				|| (versionObj.getMinorVersion() >= 1 && versionObj.getBuildNumber() >= 8))
			newEnoughVersion = true;

		if (Bumblezone.BzConfig.spawnProductiveBeesHoneycombVariants.get() && newEnoughVersion) {
			FillerBlockType.create("honeycomb_target".toUpperCase(), "honeycomb_target",
					new BlockMatcher(Blocks.HONEYCOMB_BLOCK));

			PBAddHoneycombs();

			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(productivebeesNamespace + ":comb_bauxite")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(productivebeesNamespace + ":comb_brazen")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(productivebeesNamespace + ":comb_bronze")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(productivebeesNamespace + ":comb_copper")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(productivebeesNamespace + ":comb_rotten")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(productivebeesNamespace + ":comb_slimy")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(productivebeesNamespace + ":comb_radioactive")));
			SPIDER_DUNGEON_HONEYCOMBS.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(productivebeesNamespace + ":comb_withered")));
			
			for(Block block : SPIDER_DUNGEON_HONEYCOMBS) {
				wasBlockFound(block, block.getRegistryName());
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

		// Basic combs that Beesourceful also has.
		// Only spawn these if beesourceful is off or their combs are off
		if (!(ModChecking.beesourcefulPresent && Bumblezone.BzConfig.spawnBeesourcefulHoneycombVariants.get())) {
			addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_gold"), 34, 3, 6, 230, true);
			addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_iron"), 26, 2, 30, 210, true);
			addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_redstone"), 22, 1, 30, 210, true);
			addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_lapis"), 22, 1, 6, 30, true);
			addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_emerald"), 5, 1, 6, 244, true);
			addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_ender"), 5, 1, 200, 50, true);
			addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_diamond"), 7, 1, 6, 244, true);
		}

		// Other combs unique to Productive Bees
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_blazing"), 34, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_glowing"), 34, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_bone"), 22, 1, 6, 25, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_fossilised"), 18, 1, 4, 20, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_draconic"), 5, 1, 200, 50, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_draconic"), 5, 1, 2, 10, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_powdery"), 7, 1, 60, 244, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_quartz"), 7, 1, 60, 244, false);
		
		//0.1.13 productive bees
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_magmatic"), 34, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_amber"), 34, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_electrum"), 30, 1, 40, 200, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_invar"), 10, 1, 2, 244, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_leaden"), 10, 1, 1, 30, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_nickel"), 10, 1, 1, 30, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_osmium"), 9, 1, 1, 30, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_platinum"), 5, 1, 1, 30, true);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_silver"), 9, 1, 1, 30, true);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_steel"), 9, 1, 1, 200, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_tin"), 9, 1, 1, 200, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_titanium"), 6, 1, 1, 30, true);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_tungsten"), 9, 1, 1, 200, false);
		addCombToWorldgen(new ResourceLocation(productivebeesNamespace + ":comb_zinc"), 9, 1, 1, 200, false);
	}

	private static boolean wasBlockFound(Block block, ResourceLocation blockRL) {
		if (block == Blocks.AIR) {
			Bumblezone.LOGGER.log(Level.INFO,"------------------------------------------------NOTICE-------------------------------------------------------------------------");
			Bumblezone.LOGGER.log(Level.INFO, " ");
			Bumblezone.LOGGER.log(Level.INFO, "BUMBLEZONE: Error trying to get the following block: " + blockRL.toString() + ". Please let The Bumblezone developer know about this!");
			Bumblezone.LOGGER.log(Level.INFO, " ");
			Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
			return false;
		}
		return true;
	}

	private static void addCombToWorldgen(ResourceLocation blockRL, int veinSize, int count, int bottomOffset, int range, boolean addToHoneycombList) {
		Block honeycombBlock = ForgeRegistries.BLOCKS.getValue(blockRL);
		if (!wasBlockFound(honeycombBlock, blockRL))
			return;

		BzBiomes.biomes.forEach(biome -> ((BzBaseBiome) biome).addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
			Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.byName("honeycomb_target"), honeycombBlock.getDefaultState(), veinSize))
				.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(count, bottomOffset, 0, range)))));

		if (addToHoneycombList)
			ORE_BASED_HONEYCOMB_VARIANTS.add(honeycombBlock);
	}

	/**
	 * Is block is a ProductiveBees nest or beenest block
	 */
	public static boolean PBIsAdvancedBeehiveAbstractBlock(BlockState block) {

		if (block.getBlock() instanceof ExpansionBox && block.get(AdvancedBeehive.EXPANDED)) {
			return true; // expansion boxes only count as beenest when they expand a hive.
		} else if (block.getBlock() instanceof AdvancedBeehiveAbstract) {
			return true; // nests/hives here so return true
		}

		return false;
	}

	/**
	 * 1/15th of bees spawning will also spawn Productive Bees' bees
	 */
	public static void PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {

		if (productiveBeesList.size() == 0) {
			Bumblezone.LOGGER.warn(
					"Error! List of productive bees is empty! Cannot spawn their bees. Please let TelepathicGrunt (The Bumblezone dev) know about this!");
			return;
		}

		MobEntity entity = (MobEntity) event.getEntity();
		IWorld world = event.getWorld();

		// randomly pick a productive bee
		MobEntity productiveBeeEntity = (MobEntity) productiveBeesList
				.get(world.getRandom().nextInt(productiveBeesList.size())).create(entity.world);

		BlockPos.Mutable blockpos = new BlockPos.Mutable(entity.getPosition());
		productiveBeeEntity.setLocationAndAngles(blockpos.getX(), blockpos.getY(), blockpos.getZ(),
				world.getRandom().nextFloat() * 360.0F, 0.0F);

		ILivingEntityData ilivingentitydata = null;
		ilivingentitydata = productiveBeeEntity.onInitialSpawn(world,
				world.getDifficultyForLocation(new BlockPos(productiveBeeEntity)), event.getSpawnReason(),
				ilivingentitydata, (CompoundNBT) null);

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
		if (newEnoughVersion) {
			int index = ORE_BASED_HONEYCOMB_VARIANTS.size() - 1;

			for (int i = 0; i < lowerBoundBias && index != 0; i++) {
				index = random.nextInt(index + 1);
			}

			return ORE_BASED_HONEYCOMB_VARIANTS.get(index);
		}

		return Blocks.HONEYCOMB_BLOCK;
	}
}
