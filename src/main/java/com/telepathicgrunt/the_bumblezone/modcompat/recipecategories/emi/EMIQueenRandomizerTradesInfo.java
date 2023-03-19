package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi;

import com.telepathicgrunt.the_bumblezone.modcompat.REICompat;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;

import java.util.List;

public class EMIQueenRandomizerTradesInfo extends BasicDisplay {

	private final int weight;
	private final int groupWeight;

	public EMIQueenRandomizerTradesInfo(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int weight, int groupWeight) {
		super(inputs, outputs);
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