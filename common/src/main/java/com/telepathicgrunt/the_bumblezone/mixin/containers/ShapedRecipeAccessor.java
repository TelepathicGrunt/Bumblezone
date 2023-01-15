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
    @Invoker
    static NonNullList<Ingredient> callDissolvePattern(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static String[] callPatternFromJson(JsonArray patternArray) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static Map<String, Ingredient> callKeyFromJson(JsonObject keyEntry) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static String[] callShrink(String... toShrink) {
        throw new UnsupportedOperationException();
    }
}
