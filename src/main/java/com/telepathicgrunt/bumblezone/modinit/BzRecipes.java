package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.blocks.PorousHoneycomb;
import com.telepathicgrunt.bumblezone.items.ContainerCraftingRecipe;
import net.minecraft.block.Block;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BzRecipes {
    public static final RecipeSerializer<ContainerCraftingRecipe> CONTAINER_CRAFTING_RECIPE = new ContainerCraftingRecipe.Serializer();
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Bumblezone.MODID, "container_shapeless_recipe_bz"), CONTAINER_CRAFTING_RECIPE);
    }
}
