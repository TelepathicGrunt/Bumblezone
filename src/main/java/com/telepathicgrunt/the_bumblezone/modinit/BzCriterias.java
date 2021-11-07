package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.advancements.FallingOnPollenPileTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.PollenPuffFireballTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.PollenPuffPandaTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.PollenPuffPollinateBeeTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.PollenPuffPollinateTallFlowerTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.QueenBeehemothTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class BzCriterias {
    // CRITERIA TRIGGERS
    public static final QueenBeehemothTrigger QUEEN_BEEHEMOTH_TRIGGER = new QueenBeehemothTrigger();
    public static final FallingOnPollenPileTrigger FALLING_ON_POLLEN_BLOCK_TRIGGER = new FallingOnPollenPileTrigger();
    public static final PollenPuffPollinateBeeTrigger POLLEN_PUFF_POLLINATED_BEE_TRIGGER = new PollenPuffPollinateBeeTrigger();
    public static final PollenPuffPollinateTallFlowerTrigger POLLEN_PUFF_POLLINATED_TALL_FLOWER_TRIGGER = new PollenPuffPollinateTallFlowerTrigger();
    public static final PollenPuffPandaTrigger POLLEN_PUFF_PANDA_TRIGGER = new PollenPuffPandaTrigger();
    public static final PollenPuffFireballTrigger POLLEN_PUFF_ARROW_TRIGGER = new PollenPuffFireballTrigger();

    public static void init() {
        CriteriaTriggers.register(QUEEN_BEEHEMOTH_TRIGGER);
        CriteriaTriggers.register(FALLING_ON_POLLEN_BLOCK_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_POLLINATED_BEE_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_POLLINATED_TALL_FLOWER_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_PANDA_TRIGGER);
        CriteriaTriggers.register(POLLEN_PUFF_ARROW_TRIGGER);
    }
}
