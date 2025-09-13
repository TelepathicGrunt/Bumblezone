package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FoodData.class)
public interface FoodDataAccessor {
    @Accessor("exhaustionLevel")
    void the_bumblezone$setExhaustionLevel(float exhaustionLevel);
}
