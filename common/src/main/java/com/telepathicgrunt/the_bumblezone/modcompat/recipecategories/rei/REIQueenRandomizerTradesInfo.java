package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei;

import com.telepathicgrunt.the_bumblezone.modcompat.REICompat;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;

public class REIQueenRandomizerTradesInfo extends BasicDisplay {

	private final TagKey<Item> inOutTag;
	private final int weight;
	private final int groupWeight;

	public REIQueenRandomizerTradesInfo(List<EntryIngredient> inputs, List<EntryIngredient> outputs, TagKey<Item> inOutTag, int weight, int groupWeight) {
		super(inputs, outputs);
		this.inOutTag = inOutTag;
		this.weight = weight;
		this.groupWeight = groupWeight;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getGroupWeight() {
		return this.groupWeight;
	}

	public TagKey<Item> getInOutTag() {
		return this.inOutTag;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICompat.QUEEN_RANDOMIZE_TRADES;
	}
}