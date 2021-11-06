package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.advancements.QueenBeehemothTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class BzCriterias {
    // CRITERIA TRIGGERS
    public static final QueenBeehemothTrigger QUEEN_BEEHEMOTH = new QueenBeehemothTrigger();

    public static void init() {
        CriteriaTriggers.register(QUEEN_BEEHEMOTH);
    }
}
