package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei;

import com.telepathicgrunt.the_bumblezone.modcompat.REICompat;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.Collections;
import java.util.List;

public class REIQueenTradesInfo extends BasicDisplay {

	private final TagKey<Item> inputTag;
	private final TagKey<Item> outputTag;
	private final int xpReward;
	private final int weight;
	private final int groupWeight;

	public REIQueenTradesInfo(EntryIngredient inputs, TagKey<Item> inputTag, EntryIngredient outputs, TagKey<Item> outputTag, int xp, int weight, int groupWeight) {
		super(Collections.singletonList(inputs), Collections.singletonList(outputs));
		this.inputTag = inputTag;
		this.outputTag = outputTag;
		this.xpReward = xp;
		this.weight = weight;
		this.groupWeight = groupWeight;
	}

	public int getXpReward() {
		return this.xpReward;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getGroupWeight() {
		return this.groupWeight;
	}

	public TagKey<Item> getInputTag() {
		return this.inputTag;
	}

	public TagKey<Item> getOutputTag() {
		return this.outputTag;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICompat.QUEEN_TRADES;
	}
}
