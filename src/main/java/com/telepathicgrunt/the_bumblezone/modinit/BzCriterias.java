package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.advancements.BeeDropPollenPuffTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.CleanupStickyHoneyResidueTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.FallingOnPollenPileTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.HoneyBucketBeeGrowTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.HoneyBucketBeeLoveTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.HoneyBucketBroodTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.HoneyBucketPorousHoneycombTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.HoneyCrystalShieldBlockIneffectivelyTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.HoneySlimeCreationTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.HoneySlimeHarvestTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.PollenPuffFireballTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.PollenPuffPandaTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.PollenPuffPollinateBeeTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.PollenPuffPollinateTallFlowerTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.QueenBeehemothTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.TeleportOutOfBumblezoneFallTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.TeleportToBumblezoneByPearlTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.TeleportToBumblezoneByPistonTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class BzCriterias {
    // CRITERIA TRIGGERS
    public static final QueenBeehemothTrigger QUEEN_BEEHEMOTH_TRIGGER = new QueenBeehemothTrigger();
    public static final FallingOnPollenPileTrigger FALLING_ON_POLLEN_BLOCK_TRIGGER = new FallingOnPollenPileTrigger();
    public static final PollenPuffPollinateBeeTrigger POLLEN_PUFF_POLLINATED_BEE_TRIGGER = new PollenPuffPollinateBeeTrigger();
    public static final PollenPuffPollinateTallFlowerTrigger POLLEN_PUFF_POLLINATED_TALL_FLOWER_TRIGGER = new PollenPuffPollinateTallFlowerTrigger();
    public static final PollenPuffPandaTrigger POLLEN_PUFF_PANDA_TRIGGER = new PollenPuffPandaTrigger();
    public static final PollenPuffFireballTrigger POLLEN_PUFF_ARROW_TRIGGER = new PollenPuffFireballTrigger();
    public static final BeeDropPollenPuffTrigger BEE_DROP_POLLEN_PUFF_TRIGGER = new BeeDropPollenPuffTrigger();
    public static final TeleportToBumblezoneByPearlTrigger TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER = new TeleportToBumblezoneByPearlTrigger();
    public static final TeleportOutOfBumblezoneFallTrigger TELEPORT_OUT_OF_BUMBLEZONE_FALL_TRIGGER = new TeleportOutOfBumblezoneFallTrigger();
    public static final TeleportToBumblezoneByPistonTrigger TELEPORT_TO_BUMBLEZONE_PISTON_TRIGGER = new TeleportToBumblezoneByPistonTrigger();
    public static final HoneySlimeCreationTrigger HONEY_SLIME_CREATION_TRIGGER = new HoneySlimeCreationTrigger();
    public static final HoneySlimeHarvestTrigger HONEY_SLIME_HARVEST_TRIGGER = new HoneySlimeHarvestTrigger();
    public static final HoneyBucketBeeLoveTrigger HONEY_BUCKET_BEE_LOVE_TRIGGER = new HoneyBucketBeeLoveTrigger();
    public static final HoneyBucketBeeGrowTrigger HONEY_BUCKET_BEE_GROW_TRIGGER = new HoneyBucketBeeGrowTrigger();
    public static final HoneyBucketPorousHoneycombTrigger HONEY_BUCKET_POROUS_HONEYCOMB_TRIGGER = new HoneyBucketPorousHoneycombTrigger();
    public static final HoneyBucketBroodTrigger HONEY_BUCKET_BROOD_TRIGGER = new HoneyBucketBroodTrigger();
    public static final HoneyCrystalShieldBlockIneffectivelyTrigger HONEY_CRYSTAL_SHIELD_BLOCK_INEFFECTIVELY_TRIGGER = new HoneyCrystalShieldBlockIneffectivelyTrigger();
    public static final CleanupStickyHoneyResidueTrigger CLEANUP_STICKY_HONEY_RESIDUE_TRIGGER = new CleanupStickyHoneyResidueTrigger();

    public static void init() {
        CriteriaTriggers.register(QUEEN_BEEHEMOTH_TRIGGER);
        CriteriaTriggers.register(FALLING_ON_POLLEN_BLOCK_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_POLLINATED_BEE_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_POLLINATED_TALL_FLOWER_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_PANDA_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_ARROW_TRIGGER);
        CriteriaTriggers.register(BEE_DROP_POLLEN_PUFF_TRIGGER);
        CriteriaTriggers.register(TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER);
        CriteriaTriggers.register(TELEPORT_OUT_OF_BUMBLEZONE_FALL_TRIGGER);
        CriteriaTriggers.register(TELEPORT_TO_BUMBLEZONE_PISTON_TRIGGER);
        CriteriaTriggers.register(HONEY_SLIME_CREATION_TRIGGER);
        CriteriaTriggers.register(HONEY_SLIME_HARVEST_TRIGGER);
        CriteriaTriggers.register(HONEY_BUCKET_BEE_LOVE_TRIGGER);
        CriteriaTriggers.register(HONEY_BUCKET_BEE_GROW_TRIGGER);
        CriteriaTriggers.register(HONEY_BUCKET_POROUS_HONEYCOMB_TRIGGER);
        CriteriaTriggers.register(HONEY_BUCKET_BROOD_TRIGGER);
        CriteriaTriggers.register(HONEY_CRYSTAL_SHIELD_BLOCK_INEFFECTIVELY_TRIGGER);
        CriteriaTriggers.register(CLEANUP_STICKY_HONEY_RESIDUE_TRIGGER);
    }
}
