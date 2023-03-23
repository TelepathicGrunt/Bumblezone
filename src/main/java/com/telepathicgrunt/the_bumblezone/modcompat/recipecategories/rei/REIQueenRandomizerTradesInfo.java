package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei;

import com.telepathicgrunt.the_bumblezone.modcompat.REICompat;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class REIQueenRandomizerTradesInfo extends BasicDisplay {

	private final int weight;
	private final int groupWeight;

	public REIQueenRandomizerTradesInfo(EntryIngredient inputs, EntryIngredient outputs, TagKey<Item> outputTag, int weight, int groupWeight) {
		super(Collections.singletonList(inputs), Collections.singletonList(outputs));
		this.outputTag = outputTag;
		this.weight = weight;
		this.groupWeight = groupWeight;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getGroupWeight() {
		return this.groupWeight;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICompat.QUEEN_RANDOMIZE_TRADES;
	}
}