package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.advancements.BeeDropPollenPuffTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.FallingOnPollenPileTrigger;
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
    }
}
