package com.telepathicgrunt.the_bumblezone.modcompat;

import com.minecraftabnormals.buzzier_bees.core.registry.BBVillagers;
import com.mojang.datafixers.util.Pair;
import com.resourcefulbees.resourcefulbees.api.beedata.CustomBeeData;
import com.resourcefulbees.resourcefulbees.block.multiblocks.apiary.ApiaryBlock;
import com.resourcefulbees.resourcefulbees.block.multiblocks.apiary.ApiaryBreederBlock;
import com.resourcefulbees.resourcefulbees.block.multiblocks.apiary.ApiaryStorageBlock;
import com.resourcefulbees.resourcefulbees.registry.BeeRegistry;
import com.resourcefulbees.resourcefulbees.registry.ModBlocks;
import com.resourcefulbees.resourcefulbees.registry.ModVillagerProfessions;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzConfiguredFeatures;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.tags.BZBlockTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Items;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

import static com.telepathicgrunt.the_bumblezone.modinit.BzFeatures.HONEYCOMB_BUMBLEZONE;

public class ResourcefulBeesCompat {

    private static final String RESOURCEFUL_BEES_NAMESPACE = "resourcefulbees";
    private static final List<EntityType<?>> RESOURCEFUL_BEES_LIST = new ArrayList<>();
    private static final Map<ResourceLocation, Block> RESOURCEFUL_HONEYCOMBS_MAP = new HashMap<>();
    private static final List<Block> ORE_BASED_HONEYCOMB_VARIANTS = new ArrayList<>();
    private static final List<Block> SPIDER_DUNGEON_HONEYCOMBS = new ArrayList<>();
    private static final List<Pair<Block, ConfiguredFeature<?, ?>>> RESOURCEFUL_BEES_CFS = new ArrayList<>();
    private static final List<Block> HONEY_BLOCKS = new ArrayList<>();

    public static void setupResourcefulBees() {

    	// get all bees
        Map<CustomBeeData, EntityType<?>> tempBeeMap = new HashMap<>();
        BeeRegistry.getRegistry().getBees().forEach((s, b) -> tempBeeMap.put(b, ForgeRegistries.ENTITIES.getValue(b.getEntityTypeRegistryID())));

        // remove bees that RB will not spawn in the world
        if (Bumblezone.BzModCompatibilityConfig.useSpawnInWorldConfigFromRB.get()) {
            tempBeeMap.forEach((b, e) -> {
                if (!b.getSpawnData().canSpawnInWorld()){
                	tempBeeMap.remove(b);
				}
            });
        }

        // remove bees that RB blacklisted from any bumblezone biome
        if (Bumblezone.BzModCompatibilityConfig.useSpawnInWorldConfigFromRB.get()) {
            tempBeeMap.forEach((b, e) -> {
                if (!b.getSpawnData().getBiomeBlacklist().contains(Bumblezone.MODID)){
                    tempBeeMap.remove(b);
                }
            });
        }

        // remove bees that bumblezone tag blacklists
        tempBeeMap.forEach((b, e) -> {
            Set<ResourceLocation> blacklistedBees = Arrays.stream(Bumblezone.BzModCompatibilityConfig.RBBlacklistedBees.get().split(",")).map(String::trim).map(ResourceLocation::new).collect(Collectors.toSet());
            if(blacklistedBees.contains(e.getRegistryName())){
				tempBeeMap.remove(b);
			}
		});

        // Now create list of bees and honecombs to spawn
        tempBeeMap.forEach((b, e) -> {
            // check if comb block exist as oreo bee has an item instead which means getCombBlockRegistryObject is null
        	if (b.hasHoneycomb() && b.getCombBlockRegistryObject() != null) {
        		RESOURCEFUL_HONEYCOMBS_MAP.put(b.getCombBlockRegistryObject().getId(), b.getCombBlockRegistryObject().get());
			}
        	RESOURCEFUL_BEES_LIST.add(e);
		});


        for (Map.Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()) {
            ResourceLocation rl = entry.getKey().getLocation();
            if(rl.getNamespace().equals(RESOURCEFUL_BEES_NAMESPACE)){
                if (rl.getPath().contains("honeycomb")) {
                    RESOURCEFUL_HONEYCOMBS_MAP.put(entry.getKey().getLocation(), entry.getValue());
                }
                else if(rl.getPath().contains("honey") && rl.getPath().contains("block") &&
                        !rl.getPath().contains("comb") && !rl.getPath().contains("fluid"))
                {
                    HONEY_BLOCKS.add(entry.getValue());
                }
            }
        }

        //Set up lists/maps for adding the worldgen compat stuff to bee dungeons and biomes
        Map<ResourceLocation, Block> unusedHoneycombs = new HashMap<>(RESOURCEFUL_HONEYCOMBS_MAP);
        if (Bumblezone.BzModCompatibilityConfig.spawnResourcefulBeesHoneycombVariants.get()) {
            // Multiple entries influences changes of them being picked. Those in back of list is rarest to be picked
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "coal_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "coal_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "coal_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "iron_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "iron_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "zombie_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "zombie_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "pigman_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "pigman_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "skeleton_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "nether_quartz_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "creeper_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "creeper_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "wither_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "netherite_honeycomb_block"));
            addToSpiderDungeonList(unusedHoneycombs, new ResourceLocation("resourcefulbees", "rgbee_honeycomb_block"));

            addCombToWorldgen(unusedHoneycombs, new ResourceLocation("resourcefulbees", "gold_honeycomb_block"), 34, 3, 6, 230, true);
            addCombToWorldgen(unusedHoneycombs, new ResourceLocation("resourcefulbees", "iron_honeycomb_block"), 26, 2, 30, 210, true);
            addCombToWorldgen(unusedHoneycombs, new ResourceLocation("resourcefulbees", "redstone_honeycomb_block"), 22, 1, 30, 210, true);
            addCombToWorldgen(unusedHoneycombs, new ResourceLocation("resourcefulbees", "lapis_honeycomb_block"), 22, 1, 6, 30, true);
            addCombToWorldgen(unusedHoneycombs, new ResourceLocation("resourcefulbees", "emerald_honeycomb_block"), 5, 1, 6, 244, true);
            addCombToWorldgen(unusedHoneycombs, new ResourceLocation("resourcefulbees", "ender_honeycomb_block"), 5, 1, 200, 50, true);
            addCombToWorldgen(unusedHoneycombs, new ResourceLocation("resourcefulbees", "diamond_honeycomb_block"), 7, 1, 6, 244, true);
            addCombToWorldgen(unusedHoneycombs, new ResourceLocation("resourcefulbees", "rgbee_honeycomb_block"), 7, 1, 6, 244, true);

            // Remaining combs gets a generic spawning rate
            for (Map.Entry<ResourceLocation, Block> remainingCombs : unusedHoneycombs.entrySet()) {
                addCombToWorldgen(null, remainingCombs.getKey(), 10, 1, 1, 235, false);
            }
        }


        // fires when server starts up so long after FMLCommonSetupEvent.
        // Thus it is safe to register this event here.
        // Need lowest priority to make sure we add trades after the other mod has created their trades.
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.LOWEST, ResourcefulBeesCompat::setupResourcefulBeesTrades);

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.resourcefulBeesPresent = true;
    }

    public static void setupResourcefulBeesTrades(VillagerTradesEvent event) {
        if (event.getType() == ModVillagerProfessions.BEEKEEPER.get()) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            List<VillagerTrades.ITrade> tradeList = new ArrayList<>(trades.get(2));
            tradeList.add(new GeneralUtils.BasicItemTrade(Items.EMERALD, 1, BzItems.STICKY_HONEY_RESIDUE.get(), 2));
            trades.put(2, tradeList);

            tradeList = new ArrayList<>(trades.get(3));
            tradeList.add(new GeneralUtils.BasicItemTrade(BzItems.HONEY_CRYSTAL_SHARDS.get(), 3, Items.EMERALD, 1));
            trades.put(3, tradeList);

            tradeList = new ArrayList<>(trades.get(4));
            tradeList.add(new GeneralUtils.BasicItemTrade(Items.EMERALD, 2, BzItems.HONEY_CRYSTAL.get(), 1));
            trades.put(4, tradeList);

            tradeList = new ArrayList<>(trades.get(5));
            tradeList.add(new GeneralUtils.BasicItemTrade(Items.EMERALD, 20, BzItems.HONEYCOMB_LARVA.get(), 1));
            trades.put(5, tradeList);
        }
    }

    public static void RBAddWorldgen(List<Biome> bumblezone_biomes) {
        for (Biome biome : bumblezone_biomes) {
            // beeswax block
            if (Bumblezone.BzModCompatibilityConfig.RBBeesWaxWorldgen.get()) {
                biome.getGenerationSettings().getFeatures().get(GenerationStage.Decoration.VEGETAL_DECORATION.ordinal()).add(() -> BzConfiguredFeatures.BZ_BEES_WAX_PILLAR_CONFIGURED_FEATURE);
            }

            // add all the comb cfs that are registered
            for (Pair<Block, ConfiguredFeature<?, ?>> cf : RESOURCEFUL_BEES_CFS) {
                if (!BZBlockTags.BLACKLISTED_RESOURCEFUL_COMBS.contains(cf.getFirst()))
                    biome.getGenerationSettings().getFeatures().get(GenerationStage.Decoration.UNDERGROUND_ORES.ordinal()).add(cf::getSecond);
            }
        }

        ORE_BASED_HONEYCOMB_VARIANTS.removeIf(BZBlockTags.BLACKLISTED_RESOURCEFUL_COMBS::contains);
        SPIDER_DUNGEON_HONEYCOMBS.removeIf(BZBlockTags.BLACKLISTED_RESOURCEFUL_COMBS::contains);
    }


    /**
     * Creates a configured feature of the combtype and add it to the cf list and/or Bee Dungeon comb list
     */
    private static void addCombToWorldgen(Map<ResourceLocation, Block> unusedHoneycombs, ResourceLocation blockEntryRL, int veinSize, int count, int bottomOffset, int range, boolean addToBeeDungeon) {
        Block honeycomb = RESOURCEFUL_HONEYCOMBS_MAP.get(blockEntryRL);
        if (honeycomb == null) return;

        String cfRL = Bumblezone.MODID + ":" + blockEntryRL.getNamespace() + blockEntryRL.getPath();

        // Prevent registry replacements
        int idOffset = 0;
        while (WorldGenRegistries.CONFIGURED_FEATURE.getOptional(new ResourceLocation(cfRL + idOffset)).isPresent()) {
            idOffset++;
        }

        ConfiguredFeature<?, ?> cf = Feature.ORE.withConfiguration(new OreFeatureConfig(HONEYCOMB_BUMBLEZONE, honeycomb.getDefaultState(), veinSize))
                .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(bottomOffset, 0, range)))
                .square()
                .func_242731_b(count);

        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(cfRL + idOffset), cf);
        RESOURCEFUL_BEES_CFS.add(Pair.of(honeycomb, cf));

        if (addToBeeDungeon)
            ORE_BASED_HONEYCOMB_VARIANTS.add(honeycomb);

        if (unusedHoneycombs != null)
            unusedHoneycombs.remove(blockEntryRL);
    }

    /**
     * Add comb to spider dungeon comb list
     */
    private static void addToSpiderDungeonList(Map<ResourceLocation, Block> unusedHoneycombs, ResourceLocation blockEntryRL) {
        Block block = RESOURCEFUL_HONEYCOMBS_MAP.get(blockEntryRL);
        if (block != null)
            SPIDER_DUNGEON_HONEYCOMBS.add(block);

        unusedHoneycombs.remove(blockEntryRL);
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
    public static BlockState getRBBeesWaxBlock() {
        return ModBlocks.WAX_BLOCK.get().getDefaultState();
    }

    /**
     * get either honey block from resourceful bees block
     */
    public static BlockState getRBHoneyBlock(Random random) {
        return HONEY_BLOCKS.get(random.nextInt(HONEY_BLOCKS.size())).getDefaultState();
    }


    /**
     * 1/15th of bees spawning will also spawn Resourceful Bees' bees
     */
    public static void RBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {

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
        if (resourcefulBeeEntity == null) return;

        resourcefulBeeEntity.setChild(isChild);
        BlockPos.Mutable blockpos = new BlockPos.Mutable().setPos(entity.getPosition());
        resourcefulBeeEntity.setLocationAndAngles(
                blockpos.getX() + 0.5f,
                blockpos.getY() + 0.5f,
                blockpos.getZ() + 0.5f,
                world.getRandom().nextFloat() * 360.0F,
                0.0F);

        resourcefulBeeEntity.onInitialSpawn(
                world,
                world.getDifficultyForLocation(resourcefulBeeEntity.getPosition()),
                event.getSpawnReason(),
                null,
                null);

        world.addEntity(resourcefulBeeEntity);
    }


    /**
     * Safely get Spider dungeon Honeycomb. If Spider dungeon Honeycomb wasn't found, return
     * Vanilla's Honeycomb
     */
    public static BlockState RBGetSpiderHoneycomb(Random random) {
        if (SPIDER_DUNGEON_HONEYCOMBS.size() == 0) {
            return Blocks.HONEYCOMB_BLOCK.getDefaultState();
        } else {
            return SPIDER_DUNGEON_HONEYCOMBS.get(random.nextInt(random.nextInt(SPIDER_DUNGEON_HONEYCOMBS.size()) + 1)).getDefaultState();
        }
    }

    /**
     * Picks a random Productive Bees Honeycomb with lower index of
     * ORE_BASED_HONEYCOMB_VARIANTS list being highly common
     */
    public static BlockState RBGetRandomHoneycomb(Random random, int lowerBoundBias) {
        if (ORE_BASED_HONEYCOMB_VARIANTS.size() == 0) {
            return Blocks.HONEYCOMB_BLOCK.getDefaultState();
        } else {
            int index = ORE_BASED_HONEYCOMB_VARIANTS.size() - 1;

            for (int i = 0; i < lowerBoundBias && index != 0; i++) {
                index = random.nextInt(index + 1);
            }

            return ORE_BASED_HONEYCOMB_VARIANTS.get(index).getDefaultState();
        }
    }
}
