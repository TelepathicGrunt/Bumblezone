package com.telepathicgrunt.the_bumblezone.modcompat.jei;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record JEIQueenTradesInfo(ItemStack wantItem, List<ItemStack> rewards, int xp, int weight, int totalGroupWeight) implements IRecipeCategoryExtension {

}
