package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei;

import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record JEIQueenRandomizerTradesInfo(ItemStack input, TagKey<Item> tagOutput, List<ItemStack> output) implements IRecipeCategoryExtension {

}
