package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record JEIQueenRandomizerTradesInfo(TagKey<Item> tagInAndOut, Ingredient ingredientInAndOut, int tagSize, List<ItemStack> output) implements IRecipeCategoryExtension {

}
