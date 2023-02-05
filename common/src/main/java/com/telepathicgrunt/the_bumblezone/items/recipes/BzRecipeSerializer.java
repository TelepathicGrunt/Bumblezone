package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.Recipe;

public interface BzRecipeSerializer<T extends Recipe<?>> {
    default JsonObject toJson(T recipe) {
        throw new IllegalStateException("This should be overridden by a mixin!");
    }
}
