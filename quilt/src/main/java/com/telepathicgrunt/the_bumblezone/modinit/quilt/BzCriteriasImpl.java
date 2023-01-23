package com.telepathicgrunt.the_bumblezone.modinit.quilt;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

public class BzCriteriasImpl {
    public static <T extends CriterionTrigger<?>> T register(T criterionTrigger) {
        return CriteriaTriggers.register(criterionTrigger);
    }
}
