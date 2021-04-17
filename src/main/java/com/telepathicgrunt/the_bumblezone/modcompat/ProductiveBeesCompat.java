package com.telepathicgrunt.the_bumblezone.modcompat;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.tags.BZBlockTags;
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
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

import static com.telepathicgrunt.the_bumblezone.modinit.BzFeatures.HONEYCOMB_BUMBLEZONE;

public class ProductiveBeesCompat {

	private static final String PRODUCTIVE_BEES_NAMESPACE = "productivebees";
	private static final List<Block> ORE_BASED_HONEYCOMB_VARIANTS = new ArrayList<>();
	private static final List<Block> SPIDER_DUNGEON_HONEYCOMBS = new ArrayList<>();
	private static List<String> PRODUCTIVE_BEES_LIST = new ArrayList<>();
	private static final Map<ResourceLocation, Block> PRODUCTIVE_BEES_HONEYCOMBS_MAP = new HashMap<>();
	private static final List<Pair<Block, ConfiguredFeature<?,?>>> PRODUCTIVE_BEES_CFS = new ArrayList<>();

	public static void setupProductiveBees() {
		for(Map.Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()){
			ResourceLocation rl = entry.getKey().getLocation();
			if(rl.getNamespace().equals(PRODUCTIVE_BEES_NAMESPACE) && rl.getPath().contains("comb")){
				PRODUCTIVE_BEES_HONEYCOMBS_MAP.put(entry.getKey().getLocation(), entry.getValue());
			}
		}
		Set<Block> unusedHoneycombs = new HashSet<>(PRODUCTIVE_BEES_HONEYCOMBS_MAP.values());

		if (Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get()) {
			// Multiple entries influences changes of them being picked. Those in back of list is rarest to be picked
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_ROTTEN.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_BAUXITE.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_BRAZEN.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_BRONZE.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_COPPER.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_EXPERIENCE.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_FOSSILISED.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_BISMUTH.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_CINNABAR.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_SLIMY.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_OBSIDIAN.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_REFINED_OBSIDIAN.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_RADIOACTIVE.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_URANINITE.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_WITHERED.get());
			addToSpiderDungeonList(unusedHoneycombs, ModBlocks.COMB_NETHERITE.get());

			// Basic combs that that are mostly based on vanilla ores.
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_GOLD.get(), 34, 3, 6, 230, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_IRON.get(), 26, 2, 30, 210, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_REDSTONE.get(), 22, 1, 30, 210, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_LAPIS.get(), 22, 1, 6, 30, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_EMERALD.get(), 5, 1, 6, 244, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_ENDER.get(), 5, 1, 200, 50, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_PROSPERITY.get(), 5, 1, 200, 50, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_EXPERIENCE.get(), 1, 0, 0, 1, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_DIAMOND.get(), 7, 1, 6, 244, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_NETHERITE.get(), 5, 1, 6, 244, true);

			// Other combs unique to Productive Bees
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_AMBER.get(), 34, 1, 40, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_BLAZING.get(), 34, 1, 40, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_BONE.get(), 22, 1, 6, 25, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_CONSTANTAN.get(), 9, 1, 1, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_DRACONIC.get(), 5, 1, 200, 50, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_DRACONIC.get(), 5, 1, 2, 10, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_ENDERIUM.get(), 5, 1, 200, 50, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_ELECTRUM.get(), 30, 1, 40, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_ELEMENTIUM.get(), 10, 1, 40, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_FOSSILISED.get(), 18, 1, 4, 20, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_GHOSTLY.get(), 5, 1, 2, 10, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_GLOWING.get(), 34, 1, 40, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_IMPERIUM.get(), 10, 1, 2, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_INFERIUM.get(), 10, 1, 2, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_INSANIUM.get(), 10, 1, 2, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_INVAR.get(), 10, 1, 2, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_LEADEN.get(), 10, 1, 1, 30, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_LUMIUM.get(), 10, 1, 1, 150, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_MAGMATIC.get(), 34, 1, 40, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_MANASTEEL.get(), 10, 1, 2, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_MILKY.get(), 10, 1, 2, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_NICKEL.get(), 10, 1, 1, 30, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_OSMIUM.get(), 9, 1, 1, 30, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_PLASTIC.get(), 10, 1, 1, 150, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_PLATINUM.get(), 5, 1, 1, 30, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_PRUDENTIUM.get(), 10, 1, 2, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_POWDERY.get(), 7, 1, 60, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_QUARTZ.get(), 7, 1, 60, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_REFINED_GLOWSTONE.get(), 25, 1, 60, 170, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_SIGNALUM.get(), 10, 1, 1, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_SILICON.get(), 10, 1, 1, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_SILVER.get(), 9, 1, 1, 30, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_SOULIUM.get(), 10, 1, 1, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_STEEL.get(), 9, 1, 1, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_TERRASTEEL.get(), 10, 1, 1, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_TERTIUM.get(), 10, 1, 1, 244, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_TIN.get(), 9, 1, 1, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_TITANIUM.get(), 6, 1, 1, 30, true);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_TUNGSTEN.get(), 9, 1, 1, 200, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_ZINC.get(), 9, 1, 1, 200, false);

			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_ENDER_BIOTITE.get(), 5, 1, 1, 300, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_SUPREMIUM.get(), 5, 1, 1, 300, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_SPACIAL.get(), 5, 1, 1, 300, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_VIBRANIUM.get(), 5, 1, 1, 300, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_ALLTHEMODIUM.get(), 5, 1, 1, 400, false);
			addCombToWorldgen(unusedHoneycombs, ModBlocks.COMB_UNOBTAINIUM.get(), 5, 1, 1, 500, false);

			// Remaining combs gets a generic spawning rate
			for(Block remainingCombs : unusedHoneycombs){
				addCombToWorldgen(null, remainingCombs, 10, 1, 1, 235, false);
			}
		}

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.productiveBeesPresent = true;
	}
	
	public static void PBAddWorldgen(List<Biome> bumblezoneBiomes) {
		HashMap<String, CompoundNBT> PB_DATA = new HashMap<>(BeeReloadListener.INSTANCE.getData());
		PRODUCTIVE_BEES_LIST = new ArrayList<>(PB_DATA.keySet());
		Set<String> blacklistedBees = Arrays.stream(Bumblezone.BzModCompatibilityConfig.PBBlacklistedBees.get().split(",")).map(String::trim).collect(Collectors.toSet());
		PRODUCTIVE_BEES_LIST.removeIf(blacklistedBees::contains);

		for(Biome biome : bumblezoneBiomes) {
			// Add all the comb cfs that are registered.
			// We ignore the datapack combs as that's too much work to support tbh.
			for (Pair<Block, ConfiguredFeature<?, ?>> cf : PRODUCTIVE_BEES_CFS) {
				if (!BZBlockTags.BLACKLISTED_PRODUCTIVEBEES_COMBS.contains(cf.getFirst()))
					biome.getGenerationSettings().getFeatures().get(GenerationStage.Decoration.UNDERGROUND_ORES.ordinal()).add(cf::getSecond);
			}
		}

		SPIDER_DUNGEON_HONEYCOMBS.removeIf(BZBlockTags.BLACKLISTED_PRODUCTIVEBEES_COMBS::contains);
		ORE_BASED_HONEYCOMB_VARIANTS.removeIf(BZBlockTags.BLACKLISTED_PRODUCTIVEBEES_COMBS::contains);
	}

	/**
	 * Add comb to spider dungeon comb list
	 */
	private static void addToSpiderDungeonList(Set<Block> unusedHoneycombs, Block combBlock){
		SPIDER_DUNGEON_HONEYCOMBS.add(combBlock);
		unusedHoneycombs.remove(combBlock);
	}

	/**
	 * Creates a configured feature of the combtype and add it to the biome and/or Bee Dungeon comb list
	 */
	private static void addCombToWorldgen(Set<Block> unusedHoneycombs, Block combBlock, int veinSize, int count, int bottomOffset, int range, boolean addToHoneycombList) {
		if(combBlock == null || combBlock == Blocks.AIR)
			return;

		ResourceLocation blockRL = ForgeRegistries.BLOCKS.getKey(combBlock);
		String cfRL = Bumblezone.MODID + ":" + blockRL.getNamespace() + blockRL.getPath();

		// Prevent registry replacements
		int idOffset = 0;
		while(WorldGenRegistries.CONFIGURED_FEATURE.getOptional(new ResourceLocation(cfRL + idOffset)).isPresent()){
			idOffset++;
		}

		ConfiguredFeature<?, ?> cf = Feature.ORE.withConfiguration(new OreFeatureConfig(HONEYCOMB_BUMBLEZONE, combBlock.getDefaultState(), veinSize))
				.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(bottomOffset, 0, range)))
				.square()
				.func_242731_b(count);

		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(cfRL + idOffset), cf);
		PRODUCTIVE_BEES_CFS.add(Pair.of(combBlock, cf));
		if(unusedHoneycombs != null)
			unusedHoneycombs.remove(combBlock);

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
		else if(BlockTags.getCollection().getTagByID(new ResourceLocation("productivebees:solitary_overworld_nests")).contains(block.getBlock())){
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
	public static void PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {

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

		BlockPos.Mutable blockpos = new BlockPos.Mutable().setPos(entity.getPosition());
		productiveBeeEntity.setLocationAndAngles(
				blockpos.getX() + 0.5f,
				blockpos.getY() + 0.5f,
				blockpos.getZ() + 0.5f,
				world.getRandom().nextFloat() * 360.0F,
				0.0F);

		productiveBeeEntity.onInitialSpawn(
				world,
				world.getDifficultyForLocation(productiveBeeEntity.getPosition()),
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
	public static BlockState PBGetRottenedHoneycomb(Random random) {
		if(SPIDER_DUNGEON_HONEYCOMBS.size() == 0){
			return Blocks.HONEYCOMB_BLOCK.getDefaultState();
		}
		else{
			return SPIDER_DUNGEON_HONEYCOMBS.get(random.nextInt(random.nextInt(SPIDER_DUNGEON_HONEYCOMBS.size())+1)).getDefaultState();
		}
	}

	/**
	 * Picks a random Productive Bees Honeycomb with lower index of
	 * ORE_BASED_HONEYCOMB_VARIANTS list being highly common
	 */
	public static BlockState PBGetRandomHoneycomb(Random random, int lowerBoundBias) {
		if (ORE_BASED_HONEYCOMB_VARIANTS.size() == 0) {
			return Blocks.HONEYCOMB_BLOCK.getDefaultState();
		}
		else {
			int index = ORE_BASED_HONEYCOMB_VARIANTS.size() - 1;

			for (int i = 0; i < lowerBoundBias && index != 0; i++) {
				index = random.nextInt(index + 1);
			}

			return ORE_BASED_HONEYCOMB_VARIANTS.get(index).getDefaultState();
		}
	}
}
