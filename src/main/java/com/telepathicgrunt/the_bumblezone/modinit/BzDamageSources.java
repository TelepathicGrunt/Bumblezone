package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.advancements.BlockStateSpecificTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.CounterTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.EntitySpecificTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.GenericTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.ItemSpecificTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.KilledCounterTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.RecipeDiscoveredTrigger;
import com.telepathicgrunt.the_bumblezone.advancements.TargetAdvancementDoneTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;

public class BzDamageSources {
    public static final DamageSource CRYSTALLINE_FLOWER = new DamageSource("crystallineFlower");
}
