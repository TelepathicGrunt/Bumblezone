package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.ContainerCraftingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BzRecipes {
    public static final RecipeSerializer<ContainerCraftingRecipe> CONTAINER_CRAFTING_RECIPE = new ContainerCraftingRecipe.Serializer();
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(Bumblezone.MODID, "container_shapeless_recipe_bz"), CONTAINER_CRAFTING_RECIPE);
    }
}
