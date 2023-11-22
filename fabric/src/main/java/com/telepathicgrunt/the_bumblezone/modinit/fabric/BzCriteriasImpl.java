package com.telepathicgrunt.the_bumblezone.modinit.fabric;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.resources.ResourceLocation;

public class BzCriteriasImpl {
    public static <T extends CriterionTrigger<?>> T register(ResourceLocation registryName, T criterionTrigger) {
        return CriteriaTriggers.register(registryName.toString(), criterionTrigger);
    }
}
