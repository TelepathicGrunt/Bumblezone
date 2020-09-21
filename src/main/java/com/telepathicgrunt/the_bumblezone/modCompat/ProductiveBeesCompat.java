package com.telepathicgrunt.the_bumblezone.modCompat;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.features.BzBEOreFeatureConfig;
import com.telepathicgrunt.the_bumblezone.features.BzFeatures;
import cy.jdkdigital.productivebees.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.block.ExpansionBox;
import cy.jdkdigital.productivebees.init.ModBlocks;
import cy.jdkdigital.productivebees.setup.BeeReloadListener;
import cy.jdkdigital.productivebees.state.properties.VerticalHive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public class ProductiveBeesCompat {

	private static final String PRODUCTIVE_BEES_NAMESPACE = "productivebees";
	private static final List<String> ORE_BASED_HONEYCOMB_VARIANTS = new ArrayList<>();
	public static List<EntityType<?>> PRODUCTIVE_BEES_LIST = new ArrayList<>();
	private static final List<String> SPIDER_DUNGEON_HONEYCOMBS = new ArrayList<>();
	public static final RuleTest HONEYCOMB_BUMBLEZONE = new TagMatchRuleTest(BlockTags.makeWrapperTag(Bumblezone.MODID+":honeycombs"));

	public static void setupProductiveBees() {
		ModChecker.productiveBeesPresent = true;

		// Dynamically create list of all Productive Bees' bees
		for (Map.Entry<RegistryKey<EntityType<?>>, EntityType<?>> productiveBeeEntry : ForgeRegistries.ENTITIES.getEntries()) {
			if (productiveBeeEntry.getKey().getValue().getNamespace().equals(PRODUCTIVE_BEES_NAMESPACE)
					&& productiveBeeEntry.getKey().getValue().getPath().contains("bee")) {
				PRODUCTIVE_BEES_LIST.add(productiveBeeEntry.getValue());
			}
		}

		if (Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get()) {
			SPIDER_DUNGEON_HONEYCOMBS.add(PRODUCTIVE_BEES_NAMESPACE + ":bauxite");
			SPIDER_DUNGEON_HONEYCOMBS.add(PRODUCTIVE_BEES_NAMESPACE + ":brazen");
			SPIDER_DUNGEON_HONEYCOMBS.add(PRODUCTIVE_BEES_NAMESPACE + ":bronze");
			SPIDER_DUNGEON_HONEYCOMBS.add(PRODUCTIVE_BEES_NAMESPACE + ":copper");
			SPIDER_DUNGEON_HONEYCOMBS.add(PRODUCTIVE_BEES_NAMESPACE + ":rotten");
			SPIDER_DUNGEON_HONEYCOMBS.add(PRODUCTIVE_BEES_NAMESPACE + ":slimy");
			SPIDER_DUNGEON_HONEYCOMBS.add(PRODUCTIVE_BEES_NAMESPACE + ":radioactive");
			SPIDER_DUNGEON_HONEYCOMBS.add(PRODUCTIVE_BEES_NAMESPACE + ":withered");
		}
	}
	
	public static void PBAddHoneycombs(Biome biome) {

		// Basic combs that that are mostly based on vanilla ores.
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":gold", 34, 3, 6, 230, true);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":iron", 26, 2, 30, 210, true);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":redstone", 22, 1, 30, 210, true);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":lapis", 22, 1, 6, 30, true);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":emerald", 5, 1, 6, 244, true);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":ender", 5, 1, 200, 50, true);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":diamond", 7, 1, 6, 244, true);

		// Other combs unique to Productive Bees
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":blazing", 34, 1, 40, 200, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":glowing", 34, 1, 40, 200, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":bone", 22, 1, 6, 25, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":fossilised", 18, 1, 4, 20, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":draconic", 5, 1, 200, 50, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":draconic", 5, 1, 2, 10, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":powdery", 7, 1, 60, 244, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":quartz", 7, 1, 60, 244, false);
		
		//0.1.13 productive bees
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":magmatic", 34, 1, 40, 200, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":amber", 34, 1, 40, 200, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":electrum", 30, 1, 40, 200, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":invar", 10, 1, 2, 244, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":leaden", 10, 1, 1, 30, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":nickel", 10, 1, 1, 30, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":osmium", 9, 1, 1, 30, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":platinum", 5, 1, 1, 30, true);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":silver", 9, 1, 1, 30, true);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":steel", 9, 1, 1, 200, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":tin", 9, 1, 1, 200, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":titanium", 6, 1, 1, 30, true);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":tungsten", 9, 1, 1, 200, false);
		addCombToWorldgen(biome, PRODUCTIVE_BEES_NAMESPACE + ":zinc", 9, 1, 1, 200, false);
	}

	private static void addCombToWorldgen(Biome biome, String combBlockType, int veinSize, int count, int bottomOffset, int range, boolean addToHoneycombList) {

		biome.getGenerationSettings().getFeatures().get(GenerationStage.Decoration.UNDERGROUND_ORES.ordinal())
			.add(() -> BzFeatures.BZ_BE_ORE_FEATURE.configure(new BzBEOreFeatureConfig(HONEYCOMB_BUMBLEZONE, ModBlocks.CONFIGURABLE_COMB.get().getDefaultState(), veinSize, combBlockType))
				.decorate(Placement.RANGE.configure(new TopSolidRangeConfig(bottomOffset, 0, range)))
					.spreadHorizontally()
					.repeat(count));

		if (addToHoneycombList)
			ORE_BASED_HONEYCOMB_VARIANTS.add(combBlockType);
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

		if(productiveBeeEntity == null) return;

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
	public static Pair<BlockState, String> PBGetRottenedHoneycomb(Random random) {
		return new Pair<>(ModBlocks.CONFIGURABLE_COMB.get().getDefaultState(), SPIDER_DUNGEON_HONEYCOMBS.get(random.nextInt(random.nextInt(SPIDER_DUNGEON_HONEYCOMBS.size())+1)));
	}

	/**
	 * Picks a random Productive Bees Honeycomb with lower index of
	 * ORE_BASED_HONEYCOMB_VARIANTS list being highly common
	 */
	public static Pair<BlockState, String> PBGetRandomHoneycomb(Random random, int lowerBoundBias) {
		int index = ORE_BASED_HONEYCOMB_VARIANTS.size() - 1;

		for (int i = 0; i < lowerBoundBias && index != 0; i++) {
			index = random.nextInt(index + 1);
		}

		return new Pair<>(ModBlocks.CONFIGURABLE_COMB.get().getDefaultState(), ORE_BASED_HONEYCOMB_VARIANTS.get(index));
	}
}
