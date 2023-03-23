package com.telepathicgrunt.the_bumblezone.mixin.util;

import net.minecraft.util.random.WeightedRandomList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WeightedRandomList.class)
public interface WeightedRandomListAccessor {
    @Accessor("totalWeight")
    int getTotalWeight();
}
