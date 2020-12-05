package com.telepathicgrunt.the_bumblezone.modCompat;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.common.block.ExpansionBox;
import cy.jdkdigital.productivebees.common.entity.bee.ConfigurableBeeEntity;
import cy.jdkdigital.productivebees.init.ModBlocks;
import cy.jdkdigital.productivebees.init.ModEntities;
import cy.jdkdigital.productivebees.setup.BeeReloadListener;
import cy.jdkdigital.productivebees.state.properties.VerticalHive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ProductiveBeesCompat {

	private static final List<Block> ORE_BASED_HONEYCOMB_VARIANTS = new ArrayList<>();
	private static final List<Block> SPIDER_DUNGEON_HONEYCOMBS = new ArrayList<>();
	private static List<String> PRODUCTIVE_BEES_LIST = new ArrayList<>();
	public static final RuleTest HONEYCOMB_BUMBLEZONE = new TagMatchRuleTest(BlockTags.makeWrapperTag(Bumblezone.MODID+":honeycombs"));
	private static final List<ConfiguredFeature<?,?>> PRODUCTIVE_BEES_CFS = new ArrayList<>();

	public static void setupProductiveBees() {
		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.productiveBeesPresent = true;


		if (Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get()) {
			// Multiple entries influences changes of them being picked. Those in back of list is rarest to be picked
			addToSpiderDungeonList(ModBlocks.COMB_ROTTEN.get());
			addToSpiderDungeonList(ModBlocks.COMB_BAUXITE.get());
			addToSpiderDungeonList(ModBlocks.COMB_BRAZEN.get());
			addToSpiderDungeonList(ModBlocks.COMB_BRONZE.get());
			addToSpiderDungeonList(ModBlocks.COMB_COPPER.get());
			addToSpiderDungeonList(ModBlocks.COMB_EXPERIENCE.get());
			addToSpiderDungeonList(ModBlocks.COMB_FOSSILISED.get());
			addToSpiderDungeonList(ModBlocks.COMB_BISMUTH.get());
			addToSpiderDungeonList(ModBlocks.COMB_CINNABAR.get());
			addToSpiderDungeonList(ModBlocks.COMB_SLIMY.get());
			addToSpiderDungeonList(ModBlocks.COMB_OBSIDIAN.get());
			addToSpiderDungeonList(ModBlocks.COMB_REFINED_OBSIDIAN.get());
			addToSpiderDungeonList(ModBlocks.COMB_RADIOACTIVE.get());
			addToSpiderDungeonList(ModBlocks.COMB_URANINITE.get());
			addToSpiderDungeonList(ModBlocks.COMB_WITHERED.get());
			addToSpiderDungeonList(ModBlocks.COMB_NETHERITE.get());
		}

		// Basic combs that that are mostly based on vanilla ores.
		addCombToWorldgen(ModBlocks.COMB_GOLD.get(), 34, 3, 6, 230, true);
		addCombToWorldgen(ModBlocks.COMB_IRON.get(), 26, 2, 30, 210, true);
		addCombToWorldgen(ModBlocks.COMB_REDSTONE.get(), 22, 1, 30, 210, true);
		addCombToWorldgen(ModBlocks.COMB_LAPIS.get(), 22, 1, 6, 30, true);
		addCombToWorldgen(ModBlocks.COMB_EMERALD.get(), 5, 1, 6, 244, true);
		addCombToWorldgen(ModBlocks.COMB_ENDER.get(), 5, 1, 200, 50, true);
		addCombToWorldgen(ModBlocks.COMB_PROSPERITY.get(), 5, 1, 200, 50, true);
		addCombToWorldgen(ModBlocks.COMB_EXPERIENCE.get(), 1, 0, 0, 1, true);
		addCombToWorldgen(ModBlocks.COMB_DIAMOND.get(), 7, 1, 6, 244, true);
		addCombToWorldgen(ModBlocks.COMB_NETHERITE.get(), 5, 1, 6, 244, true);

		// Other combs unique to Productive Bees
		addCombToWorldgen(ModBlocks.COMB_AMBER.get(), 34, 1, 40, 200, false);
		addCombToWorldgen(ModBlocks.COMB_BLAZING.get(), 34, 1, 40, 200, false);
		addCombToWorldgen(ModBlocks.COMB_BONE.get(), 22, 1, 6, 25, false);
		addCombToWorldgen(ModBlocks.COMB_CONSTANTAN.get(), 9, 1, 1, 200, false);
		addCombToWorldgen(ModBlocks.COMB_DRACONIC.get(), 5, 1, 200, 50, false);
		addCombToWorldgen(ModBlocks.COMB_DRACONIC.get(), 5, 1, 2, 10, false);
		addCombToWorldgen(ModBlocks.COMB_ENDERIUM.get(), 5, 1, 200, 50, false);
		addCombToWorldgen(ModBlocks.COMB_ELECTRUM.get(), 30, 1, 40, 200, false);
		addCombToWorldgen(ModBlocks.COMB_ELEMENTIUM.get(), 10, 1, 40, 200, false);
		addCombToWorldgen(ModBlocks.COMB_FOSSILISED.get(), 18, 1, 4, 20, false);
		addCombToWorldgen(ModBlocks.COMB_GHOSTLY.get(), 5, 1, 2, 10, false);
		addCombToWorldgen(ModBlocks.COMB_GLOWING.get(), 34, 1, 40, 200, false);
		addCombToWorldgen(ModBlocks.COMB_IMPERIUM.get(), 10, 1, 2, 244, false);
		addCombToWorldgen(ModBlocks.COMB_INFERIUM.get(), 10, 1, 2, 244, false);
		addCombToWorldgen(ModBlocks.COMB_INSANIUM.get(), 10, 1, 2, 244, false);
		addCombToWorldgen(ModBlocks.COMB_INVAR.get(), 10, 1, 2, 244, false);
		addCombToWorldgen(ModBlocks.COMB_LEADEN.get(), 10, 1, 1, 30, false);
		addCombToWorldgen(ModBlocks.COMB_LUMIUM.get(), 10, 1, 1, 150, false);
		addCombToWorldgen(ModBlocks.COMB_MAGMATIC.get(), 34, 1, 40, 200, false);
		addCombToWorldgen(ModBlocks.COMB_MANASTEEL.get(), 10, 1, 2, 244, false);
		addCombToWorldgen(ModBlocks.COMB_MILKY.get(), 10, 1, 2, 244, false);
		addCombToWorldgen(ModBlocks.COMB_NICKEL.get(), 10, 1, 1, 30, false);
		addCombToWorldgen(ModBlocks.COMB_OSMIUM.get(), 9, 1, 1, 30, false);
		addCombToWorldgen(ModBlocks.COMB_PLASTIC.get(), 10, 1, 1, 150, false);
		addCombToWorldgen(ModBlocks.COMB_PLATINUM.get(), 5, 1, 1, 30, true);
		addCombToWorldgen(ModBlocks.COMB_PRUDENTIUM.get(), 10, 1, 2, 244, false);
		addCombToWorldgen(ModBlocks.COMB_POWDERY.get(), 7, 1, 60, 244, false);
		addCombToWorldgen(ModBlocks.COMB_QUARTZ.get(), 7, 1, 60, 244, false);
		addCombToWorldgen(ModBlocks.COMB_REFINED_GLOWSTONE.get(), 25, 1, 60, 170, false);
		addCombToWorldgen(ModBlocks.COMB_SIGNALUM.get(), 10, 1, 1, 244, false);
		addCombToWorldgen(ModBlocks.COMB_SILICON.get(), 10, 1, 1, 244, false);
		addCombToWorldgen(ModBlocks.COMB_SILVER.get(), 9, 1, 1, 30, true);
		addCombToWorldgen(ModBlocks.COMB_SOULIUM.get(), 10, 1, 1, 244, false);
		addCombToWorldgen(ModBlocks.COMB_STEEL.get(), 9, 1, 1, 200, false);
		addCombToWorldgen(ModBlocks.COMB_TERRASTEEL.get(), 10, 1, 1, 244, false);
		addCombToWorldgen(ModBlocks.COMB_TERTIUM.get(), 10, 1, 1, 244, false);
		addCombToWorldgen(ModBlocks.COMB_TIN.get(), 9, 1, 1, 200, false);
		addCombToWorldgen(ModBlocks.COMB_TITANIUM.get(), 6, 1, 1, 30, true);
		addCombToWorldgen(ModBlocks.COMB_TUNGSTEN.get(), 9, 1, 1, 200, false);
		addCombToWorldgen(ModBlocks.COMB_ZINC.get(), 9, 1, 1, 200, false);

		addCombToWorldgen(ModBlocks.COMB_ENDER_BIOTITE.get(), 5, 1, 1, 300, false);
		addCombToWorldgen(ModBlocks.COMB_SUPREMIUM.get(), 5, 1, 1, 300, false);
		addCombToWorldgen(ModBlocks.COMB_SPACIAL.get(), 5, 1, 1, 300, false);
		addCombToWorldgen(ModBlocks.COMB_VIBRANIUM.get(), 5, 1, 1, 300, false);
		addCombToWorldgen(ModBlocks.COMB_ALLTHEMODIUM.get(), 5, 1, 1, 400, false);
		addCombToWorldgen(ModBlocks.COMB_UNOBTAINIUM.get(), 5, 1, 1, 500, false);
	}
	
	public static void PBAddWorldgen(BiomeLoadingEvent event) {
		HashMap<String, CompoundNBT> PB_DATA = new HashMap<>(BeeReloadListener.INSTANCE.getData());
		PRODUCTIVE_BEES_LIST = new ArrayList<>(PB_DATA.keySet());

		// Add all the comb cfs that are registered.
		// We ignore the datapack combs as that's too much work to support tbh.
		for(ConfiguredFeature<?,?> cf : PRODUCTIVE_BEES_CFS){
			event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> cf);
		}
	}

	/**
	 * Add comb to spider dungeon comb list
	 */
	private static void addToSpiderDungeonList(Block combBlock){
		SPIDER_DUNGEON_HONEYCOMBS.add(combBlock);
	}

	/**
	 * Creates a configured feature of the combtype and add it to the biome and/or Bee Dungeon comb list
	 */
	private static void addCombToWorldgen(Block combBlock, int veinSize, int count, int bottomOffset, int range, boolean addToHoneycombList) {
		if(combBlock == null || combBlock == Blocks.AIR)
			return;

		ResourceLocation blockRL = ForgeRegistries.BLOCKS.getKey(combBlock);
		String cfRL = Bumblezone.MODID + ":" + blockRL.getNamespace() + blockRL.getPath();

		// Prevent registry replacements
		int idOffset = 0;
		while(WorldGenRegistries.CONFIGURED_FEATURE.containsKey(new ResourceLocation(cfRL + idOffset))){
			idOffset++;
		}

		ConfiguredFeature<?, ?> cf = Feature.ORE.configure(new OreFeatureConfig(HONEYCOMB_BUMBLEZONE, combBlock.getDefaultState(), veinSize))
				.decorate(Placement.RANGE.configure(new TopSolidRangeConfig(bottomOffset, 0, range)))
				.spreadHorizontally()
				.repeat(count);

		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(cfRL + idOffset), cf);
		PRODUCTIVE_BEES_CFS.add(cf);

		if (addToHoneycombList)
			ORE_BASED_HONEYCOMB_VARIANTS.add(combBlock);
	}

	/**
	 * Is block is a ProductiveBees nest or beenest block
	 */
	public static boolean PBIsExpandedBeehiveBlock(BlockState block) {

		if (block.getBlock() instanceof ExpansionBox && block.get(AdvancedBeehive.EXPANDED) != VerticalHive.NONE) {
			return true; // expansion boxes only count as beenest when they expand a hive.
		}
		else if(BlockTags.getCollection().getTagOrEmpty(new ResourceLocation("productivebees:solitary_overworld_nests")).contains(block.getBlock())){
			// Solitary nests are technically AdvancedBeehiveAbstract and will pass the next check.
			// But this is still done in case they do change that in the future to extend something else or something.
			return true;
		}
		else {
			return block.getBlock() instanceof AdvancedBeehiveAbstract; // all other nests/hives we somehow missed here so return true
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

		// randomly pick a productive bee (the nbt determines the bee)
		ConfigurableBeeEntity productiveBeeEntity = ModEntities.CONFIGURABLE_BEE.get().create(entity.world);
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

		productiveBeeEntity.setBeeType(PRODUCTIVE_BEES_LIST.get(world.getRandom().nextInt(PRODUCTIVE_BEES_LIST.size())));

		world.addEntity(productiveBeeEntity);
	}

	/**
	 * Safely get Rottened Honeycomb. If Rottened Honeycomb wasn't found, return
	 * Vanilla's Honeycomb
	 */
	public static Pair<BlockState, String> PBGetRottenedHoneycomb(Random random) {
		if(SPIDER_DUNGEON_HONEYCOMBS.size() == 0){
			return new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), null);
		}
		else{
			return new Pair<>(SPIDER_DUNGEON_HONEYCOMBS.get(random.nextInt(random.nextInt(SPIDER_DUNGEON_HONEYCOMBS.size())+1)).getDefaultState(), null);
		}
	}

	/**
	 * Picks a random Productive Bees Honeycomb with lower index of
	 * ORE_BASED_HONEYCOMB_VARIANTS list being highly common
	 */
	public static Pair<BlockState, String> PBGetRandomHoneycomb(Random random, int lowerBoundBias) {
		if (ORE_BASED_HONEYCOMB_VARIANTS.size() == 0) {
			return new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), null);
		}
		else {
			int index = ORE_BASED_HONEYCOMB_VARIANTS.size() - 1;

			for (int i = 0; i < lowerBoundBias && index != 0; i++) {
				index = random.nextInt(index + 1);
			}

			return new Pair<>(ORE_BASED_HONEYCOMB_VARIANTS.get(index).getDefaultState(), null);
		}
	}
}
