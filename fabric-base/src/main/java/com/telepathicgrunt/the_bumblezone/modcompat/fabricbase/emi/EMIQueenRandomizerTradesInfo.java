package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.emi;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.EMICompat;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.DrawableWidget;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.TextureWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EMIQueenRandomizerTradesInfo implements EmiRecipe {

	private final EmiIngredient input;
	private final List<EmiStack> outputs;
	private final EmiIngredient visualOutputs;
	private final int weight;
	private final int groupWeight;

	public EMIQueenRandomizerTradesInfo(EmiIngredient input, List<EmiStack> outputs, int weight, int groupWeight) {
		super();
		this.input = input;
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

		widgets.add(new TextWidget(Component.translatable("the_bumblezone.jei.queen_trade_colors", getOutputs().size()).getVisualOrderText(), 86,  10, 0xFF404040, false));
	}
}