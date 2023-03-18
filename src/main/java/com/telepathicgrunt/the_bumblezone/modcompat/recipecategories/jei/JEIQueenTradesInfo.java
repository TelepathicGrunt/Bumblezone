package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;

public record JEIQueenTradesInfo(ItemStack wantItem, ItemStack reward, int xp, int weight, int totalGroupWeight) implements IRecipeCategoryExtension {

}
