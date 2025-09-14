package com.telepathicgrunt.the_bumblezone.mixin.util;

import net.minecraft.util.random.WeightedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WeightedList.class)
public interface WeightedRandomListAccessor {
    @Accessor("totalWeight")
    int bumblezone$getTotalWeight();
}
