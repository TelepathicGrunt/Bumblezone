package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;

public record QueenTradesInfo(ItemStack wantItem, ItemStack reward, int xp, int weight, int totalGroupWeight) implements IRecipeCategoryExtension {

}
