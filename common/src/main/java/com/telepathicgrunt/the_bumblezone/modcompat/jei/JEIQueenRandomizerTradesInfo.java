package com.telepathicgrunt.the_bumblezone.modcompat.jei;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record JEIQueenRandomizerTradesInfo(ItemStack input, List<ItemStack> randomizes, int weight, int totalGroupWeight) implements IRecipeCategoryExtension {

}
