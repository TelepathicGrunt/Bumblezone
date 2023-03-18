package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record QueenRandomizerTradesInfo(ItemStack input, List<ItemStack> randomizes, int weight, int totalGroupWeight) implements IRecipeCategoryExtension {

}
