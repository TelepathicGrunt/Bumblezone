package com.telepathicgrunt.the_bumblezone.mixin.containers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {
    @Invoker("dissolvePattern")
    static NonNullList<Ingredient> bumblezone$callDissolvePattern(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
        throw new UnsupportedOperationException();
    }

    @Invoker("patternFromJson")
    static String[] bumblezone$callPatternFromJson(JsonArray patternArray) {
        throw new UnsupportedOperationException();
    }

    @Invoker("keyFromJson")
    static Map<String, Ingredient> bumblezone$callKeyFromJson(JsonObject keyEntry) {
        throw new UnsupportedOperationException();
    }

    @Invoker("shrink")
    static String[] bumblezone$callShrink(String... toShrink) {
        throw new UnsupportedOperationException();
    }
}
