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
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMIQueenTradesInfo implements EmiRecipe {

	private final List<EmiIngredient> inputs;
	private final List<EmiStack> outputs;
	private final int xpReward;
	private final int weight;
	private final int groupWeight;

	public EMIQueenTradesInfo(List<EmiIngredient> inputs, List<EmiStack> outputs, int xp, int weight, int groupWeight) {
		super();
		this.inputs = inputs;
		this.outputs = outputs;
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

	@Override
	public EmiRecipeCategory getCategory() {
		return EMICompat.QUEEN_TRADES;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return null;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return inputs;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return outputs;
	}

	@Override
	public int getDisplayWidth() {
		return 124;
	}

	@Override
	public int getDisplayHeight() {
		return 28;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.add(new TextureWidget(new ResourceLocation(Bumblezone.MODID, "textures/gui/queen_trades_jei.png"), 0, 0, getDisplayWidth(), getDisplayHeight(), 0, 0));
		widgets.add(new SlotWidget(inputs.get(0), 5, 5));
		widgets.add(new SlotWidget(outputs.get(0), 63, 5));

		widgets.add(new TextWidget(Component.translatable("the_bumblezone.jei.queen_trade_xp", getXpReward()).getVisualOrderText(), 100,  110, 0xFF404040, false));

		String percent = String.valueOf((double)(getWeight()) / (getGroupWeight()) * 100);
		//widgets.add(Widgets.createTooltip(new Rectangle(bounds.getX() + 32, bounds.getY() + 6, 22, 16), Component.translatable("the_bumblezone.jei.queen_trade_chance", percent.substring(0, Math.min(percent.length(), 5)))));
	}
}
