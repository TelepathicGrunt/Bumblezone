package com.telepathicgrunt.the_bumblezone.modCompat;

import com.mojang.datafixers.util.Pair;
import com.resourcefulbees.resourcefulbees.block.multiblocks.apiary.ApiaryBlock;
import com.resourcefulbees.resourcefulbees.block.multiblocks.apiary.ApiaryBreederBlock;
import com.resourcefulbees.resourcefulbees.block.multiblocks.apiary.ApiaryStorageBlock;
import com.resourcefulbees.resourcefulbees.registry.ModBlocks;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.features.BzConfiguredFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.apache.logging.log4j.Level;

import java.util.*;

public class ResourcefulBeesCompat {

	private static final String RESOURCEFUL_BEES_NAMESPACE = "resourcefulbees";
	private static final List<EntityType<?>> RESOURCEFUL_BEES_LIST = new ArrayList<>();
	private static final Map<ResourceLocation, Block> RESOURCEFUL_HONEYCOMBS_MAP = new HashMap<>();
	private static final List<Block> ORE_BASED_HONEYCOMB_VARIANTS = new ArrayList<>();
	private static final List<Block> SPIDER_DUNGEON_HONEYCOMBS = new ArrayList<>();

	public static void setupResourcefulBees() {
		ModChecker.resourcefulBeesPresent = true;
		Bumblezone.LOGGER.log(Level.WARN, "Compat initiated");

		for(Map.Entry<RegistryKey<EntityType<?>>, EntityType<?>> entry : Registry.ENTITY_TYPE.getEntries()){
			if(entry.getKey().getValue().getNamespace().equals(RESOURCEFUL_BEES_NAMESPACE)){
				RESOURCEFUL_BEES_LIST.add(entry.getValue());
			}
		}

		for(Map.Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()){
			if(entry.getKey().getValue().getNamespace().equals(RESOURCEFUL_BEES_NAMESPACE) && entry.getKey().getValue().getPath().contains("honeycomb")){
				RESOURCEFUL_HONEYCOMBS_MAP.put(entry.getKey().getValue(), entry.getValue());
			}
		}
	}

	public static void RBAddWorldgen(BiomeLoadingEvent event) {
		if(Bumblezone.BzModCompatibilityConfig.RBBeesWaxWorldgen.get()){
			event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() -> BzConfiguredFeatures.BZ_BEES_WAX_PILLAR_CONFIGURED_FEATURE);
		}

		Map<ResourceLocation, Block> unused_honeycombs = new HashMap<>(RESOURCEFUL_HONEYCOMBS_MAP);
		if (Bumblezone.BzModCompatibilityConfig.spawnResourcefulBeesHoneycombVariants.get()) {
			// Multiple entries influences changes of them being picked. Those in back of list is rarest to be picked
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "coal_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "coal_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "coal_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "iron_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "iron_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "zombie_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "zombie_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "pigman_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "pigman_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "skeleton_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "nether_quartz_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "creeper_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "creeper_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "wither_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "netherite_honeycomb_block"));
			addToSpiderDungeonList(unused_honeycombs, new ResourceLocation("resourcefulbees", "rgbee_honeycomb_block"));
		}

		addCombToWorldgen(event, unused_honeycombs, new ResourceLocation("resourcefulbees", "gold_honeycomb_block"), 34, 3, 6, 230, true);
		addCombToWorldgen(event, unused_honeycombs, new ResourceLocation("resourcefulbees", "iron_honeycomb_block"), 26, 2, 30, 210, true);
		addCombToWorldgen(event, unused_honeycombs, new ResourceLocation("resourcefulbees", "redstone_honeycomb_block"), 22, 1, 30, 210, true);
		addCombToWorldgen(event, unused_honeycombs, new ResourceLocation("resourcefulbees", "lapis_honeycomb_block"), 22, 1, 6, 30, true);
		addCombToWorldgen(event, unused_honeycombs, new ResourceLocation("resourcefulbees", "emerald_honeycomb_block"), 5, 1, 6, 244, true);
		addCombToWorldgen(event, unused_honeycombs, new ResourceLocation("resourcefulbees", "ender_honeycomb_block"), 5, 1, 200, 50, true);
		addCombToWorldgen(event, unused_honeycombs, new ResourceLocation("resourcefulbees", "diamond_honeycomb_block"), 7, 1, 6, 244, true);
		addCombToWorldgen(event, unused_honeycombs, new ResourceLocation("resourcefulbees", "rgbee_honeycomb_block"), 7, 1, 6, 244, true);

		// Remaining combs gets a generic spawning rate
		for(Map.Entry<ResourceLocation, Block> remainingCombs : unused_honeycombs.entrySet()){
			addCombToWorldgen(event, null, remainingCombs.getKey(), 18, 1, 1, 235, false);
		}
	}
	/**
	 * Creates a configured feature of the combtype and add it to the biome and/or Bee Dungeon comb list
	 */
	private static void addCombToWorldgen(BiomeLoadingEvent event, Map<ResourceLocation, Block> unused_honeycombs, ResourceLocation blockEntryRL, int veinSize, int count, int bottomOffset, int range, boolean addToBeeDungeon) {
		Block honeycomb = RESOURCEFUL_HONEYCOMBS_MAP.get(blockEntryRL);

		ConfiguredFeature<?, ?> cf = Feature.ORE.configure(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.HONEYCOMB_BLOCK), honeycomb.getDefaultState(), veinSize))
				.decorate(Placement.RANGE.configure(new TopSolidRangeConfig(bottomOffset, 0, range)))
				.spreadHorizontally()
				.repeat(count);

		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Bumblezone.MODID, blockEntryRL.getPath()), cf);
		event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> cf);

		if (addToBeeDungeon)
			ORE_BASED_HONEYCOMB_VARIANTS.add(honeycomb);

		if(unused_honeycombs != null)
			unused_honeycombs.remove(blockEntryRL);
	}

	/**
	 * Add comb to spider dungeon comb list
	 */
	private static void addToSpiderDungeonList(Map<ResourceLocation, Block> unused_honeycombs, ResourceLocation blockEntryRL){
		SPIDER_DUNGEON_HONEYCOMBS.add(RESOURCEFUL_HONEYCOMBS_MAP.get(blockEntryRL));
		unused_honeycombs.remove(blockEntryRL);
	}


	/**
	 * Is block is a Resourceful Bees Apairy block
	 */
	public static boolean RBIsApairyBlock(BlockState block) {

		return (block.getBlock() instanceof ApiaryBlock && block.get(ApiaryBlock.VALIDATED)) ||
				block.getBlock() instanceof ApiaryBreederBlock ||
				block.getBlock() instanceof ApiaryStorageBlock; // apairy boxes only count as beenest when they are validated
	}

	/**
	 * get bees wax block
	 */
	public static BlockState getRBBeesWaxBlock(){
		return ModBlocks.WAX_BLOCK.get().getDefaultState();
	}

	/**
	 * 1/15th of bees spawning will also spawn Resourceful Bees' bees
	 */
	public static void RBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {

		if (RESOURCEFUL_BEES_LIST.size() == 0) {
			Bumblezone.LOGGER.warn(
					"Error! List of Resourceful bees is empty! Cannot spawn their bees. " +
					"Please let TelepathicGrunt (The Bumblezone dev) know about this!");
			return;
		}

		MobEntity entity = (MobEntity) event.getEntity();
		IServerWorld world = (IServerWorld) event.getWorld();

		// randomly pick a Resourceful bee (the nbt determines the bee)
		MobEntity resourcefulBeeEntity = (MobEntity) RESOURCEFUL_BEES_LIST.get(world.getRandom().nextInt(RESOURCEFUL_BEES_LIST.size())).create(entity.world);
		if(resourcefulBeeEntity == null) return;

		BlockPos.Mutable blockpos = new BlockPos.Mutable().setPos(entity.getBlockPos());
		resourcefulBeeEntity.setLocationAndAngles(
				blockpos.getX(),
				blockpos.getY(),
				blockpos.getZ(),
				world.getRandom().nextFloat() * 360.0F,
				0.0F);

		resourcefulBeeEntity.onInitialSpawn(
				world,
				world.getDifficultyForLocation(resourcefulBeeEntity.getBlockPos()),
				event.getSpawnReason(),
				null,
				null);

		world.addEntity(resourcefulBeeEntity);
	}


	/**
	 * Safely get Spider dungeon Honeycomb. If Spider dungeon Honeycomb wasn't found, return
	 * Vanilla's Honeycomb
	 */
	public static Pair<BlockState, String> RBGetSpiderHoneycomb(Random random) {
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
	public static Pair<BlockState, String> RBGetRandomHoneycomb(Random random, int lowerBoundBias) {
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
