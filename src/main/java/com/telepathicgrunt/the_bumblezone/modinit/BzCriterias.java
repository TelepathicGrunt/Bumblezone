package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.advancements.*;
import net.minecraft.advancements.CriteriaTriggers;

public class BzCriterias {
    // CRITERIA TRIGGERS
    public static final BeeDropPollenPuffTrigger BEE_DROP_POLLEN_PUFF_TRIGGER = new BeeDropPollenPuffTrigger();
    public static final CleanupStickyHoneyResidueTrigger CLEANUP_STICKY_HONEY_RESIDUE_TRIGGER = new CleanupStickyHoneyResidueTrigger();
    public static final CombCutterExtraDropsTrigger COMB_CUTTER_EXTRA_DROPS_TRIGGER = new CombCutterExtraDropsTrigger();
    public static final ExtendedWrathOfTheHiveTrigger EXTENDED_WRATH_OF_THE_HIVE_TRIGGER = new ExtendedWrathOfTheHiveTrigger();
    public static final FallingOnPollenPileTrigger FALLING_ON_POLLEN_BLOCK_TRIGGER = new FallingOnPollenPileTrigger();
    public static final FoodRemovedWrathOfTheHiveTrigger FOOD_REMOVED_WRATH_OF_THE_HIVE_TRIGGER = new FoodRemovedWrathOfTheHiveTrigger();
    public static final HoneyBucketBeeGrowTrigger HONEY_BUCKET_BEE_GROW_TRIGGER = new HoneyBucketBeeGrowTrigger();
    public static final HoneyBucketBeeLoveTrigger HONEY_BUCKET_BEE_LOVE_TRIGGER = new HoneyBucketBeeLoveTrigger();
    public static final HoneyBucketBroodTrigger HONEY_BUCKET_BROOD_TRIGGER = new HoneyBucketBroodTrigger();
    public static final HoneyBucketPorousHoneycombTrigger HONEY_BUCKET_POROUS_HONEYCOMB_TRIGGER = new HoneyBucketPorousHoneycombTrigger();
    public static final HoneyCrystalInWaterTrigger HONEY_CRYSTAL_IN_WATER_TRIGGER = new HoneyCrystalInWaterTrigger();
    public static final HoneyCrystalShieldBlockIneffectivelyTrigger HONEY_CRYSTAL_SHIELD_BLOCK_INEFFECTIVELY_TRIGGER = new HoneyCrystalShieldBlockIneffectivelyTrigger();
    public static final HoneySlimeCreationTrigger HONEY_SLIME_CREATION_TRIGGER = new HoneySlimeCreationTrigger();
    public static final HoneySlimeHarvestTrigger HONEY_SLIME_HARVEST_TRIGGER = new HoneySlimeHarvestTrigger();
    public static final PollenPuffFireballTrigger POLLEN_PUFF_FIREBALL_TRIGGER = new PollenPuffFireballTrigger();
    public static final PollenPuffPandaTrigger POLLEN_PUFF_PANDA_TRIGGER = new PollenPuffPandaTrigger();
    public static final PollenPuffPollinateBeeTrigger POLLEN_PUFF_POLLINATED_BEE_TRIGGER = new PollenPuffPollinateBeeTrigger();
    public static final PollenPuffPollinateTallFlowerTrigger POLLEN_PUFF_POLLINATED_TALL_FLOWER_TRIGGER = new PollenPuffPollinateTallFlowerTrigger();
    public static final ProtectionOfTheHiveDefenseTrigger PROTECTION_OF_THE_HIVE_DEFENSE_TRIGGER = new ProtectionOfTheHiveDefenseTrigger();
    public static final QueenBeehemothTrigger QUEEN_BEEHEMOTH_TRIGGER = new QueenBeehemothTrigger();
    public static final RecipeDiscoveredTrigger RECIPE_DISCOVERED_TRIGGER = new RecipeDiscoveredTrigger();
    public static final SugarWaterNextToSugarCaneTrigger SUGAR_WATER_NEXT_TO_SUGAR_CANE_TRIGGER = new SugarWaterNextToSugarCaneTrigger();
    public static final TeleportOutOfBumblezoneFallTrigger TELEPORT_OUT_OF_BUMBLEZONE_FALL_TRIGGER = new TeleportOutOfBumblezoneFallTrigger();
    public static final TeleportToBumblezoneByPearlTrigger TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER = new TeleportToBumblezoneByPearlTrigger();
    public static final TeleportToBumblezoneByPistonTrigger TELEPORT_TO_BUMBLEZONE_PISTON_TRIGGER = new TeleportToBumblezoneByPistonTrigger();

    public static void init() {
        CriteriaTriggers.register(BEE_DROP_POLLEN_PUFF_TRIGGER);
        CriteriaTriggers.register(CLEANUP_STICKY_HONEY_RESIDUE_TRIGGER);
        CriteriaTriggers.register(COMB_CUTTER_EXTRA_DROPS_TRIGGER);
        CriteriaTriggers.register(EXTENDED_WRATH_OF_THE_HIVE_TRIGGER);
        CriteriaTriggers.register(FALLING_ON_POLLEN_BLOCK_TRIGGER);
        CriteriaTriggers.register(FOOD_REMOVED_WRATH_OF_THE_HIVE_TRIGGER);
        CriteriaTriggers.register(HONEY_BUCKET_BEE_GROW_TRIGGER);
        CriteriaTriggers.register(HONEY_BUCKET_BEE_LOVE_TRIGGER);
        CriteriaTriggers.register(HONEY_BUCKET_BROOD_TRIGGER);
        CriteriaTriggers.register(HONEY_BUCKET_POROUS_HONEYCOMB_TRIGGER);
        CriteriaTriggers.register(HONEY_CRYSTAL_IN_WATER_TRIGGER);
        CriteriaTriggers.register(HONEY_CRYSTAL_SHIELD_BLOCK_INEFFECTIVELY_TRIGGER);
        CriteriaTriggers.register(HONEY_SLIME_CREATION_TRIGGER);
        CriteriaTriggers.register(HONEY_SLIME_HARVEST_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_FIREBALL_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_PANDA_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_POLLINATED_BEE_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_POLLINATED_TALL_FLOWER_TRIGGER);
        CriteriaTriggers.register(PROTECTION_OF_THE_HIVE_DEFENSE_TRIGGER);
        CriteriaTriggers.register(QUEEN_BEEHEMOTH_TRIGGER);
        CriteriaTriggers.register(RECIPE_DISCOVERED_TRIGGER);
        CriteriaTriggers.register(SUGAR_WATER_NEXT_TO_SUGAR_CANE_TRIGGER);
        CriteriaTriggers.register(TELEPORT_OUT_OF_BUMBLEZONE_FALL_TRIGGER);
        CriteriaTriggers.register(TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER);
        CriteriaTriggers.register(TELEPORT_TO_BUMBLEZONE_PISTON_TRIGGER);
    }
}
