package com.telepathicgrunt.the_bumblezone.mixin.containers;

import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {
    @Invoker("shrink")
    static String[] callShrink(List<String> list) {
        throw new UnsupportedOperationException();
    }
}
