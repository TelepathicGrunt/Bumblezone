package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.EMICompat;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.TextureWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EMIQueenRandomizerTradesInfo implements EmiRecipe {

	private final EmiIngredient input;
	private final List<EmiStack> outputs;
	private final TagKey<Item> outputTag;
	private final EmiIngredient visualOutputs;
	private final int weight;
	private final int groupWeight;

	public EMIQueenRandomizerTradesInfo(EmiIngredient input, List<EmiStack> outputs, TagKey<Item> outputTag, int weight, int groupWeight) {
		super();
		this.input = input;
		this.outputTag = outputTag;
		this.outputs = outputs;
		this.visualOutputs = EmiIngredient.of(outputs);
		this.weight = weight;
		this.groupWeight = groupWeight;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getGroupWeight() {
		return this.groupWeight;
	}

	public TagKey<Item> getOutputTag() {
		return this.outputTag;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return EMICompat.QUEEN_RANDOMIZE_TRADES;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return null;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return List.of(input);
	}

	@Override
	public List<EmiStack> getOutputs() {
		return outputs;
	}

	@Override
	public int getDisplayWidth() {
		return 136;
	}

	@Override
	public int getDisplayHeight() {
		return 28;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.add(new TextureWidget(new ResourceLocation(Bumblezone.MODID, "textures/gui/queen_randomizer_trades_layout.png"), 0, 0, getDisplayWidth(), getDisplayHeight(), 0, 0));

		widgets.add(new SlotWidget(input, 5, 5));
		widgets.add(new SlotWidget(visualOutputs, 63, 5));

		if (this.getOutputTag() != null) {
			widgets.add(new TextureWidget(new ResourceLocation(Bumblezone.MODID, "textures/gui/tag_icon.png"), 69, 11, 16, 16, 0, 0, 16, 16, 16, 16));
		}

		widgets.add(new TextWidget(Component.translatable("the_bumblezone.recipe_viewers.queen_trade_colors", getOutputs().size()).getVisualOrderText(), 86,  10, 0xFF404040, false));
	}
}