package com.telepathicgrunt.the_bumblezone.mixin.containers;

import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ShapedRecipePattern.class)
public interface ShapedRecipePatternAccessor {
    @Invoker("shrink")
    static String[] callShrink(List<String> list) {
        throw new UnsupportedOperationException();
    }
}
