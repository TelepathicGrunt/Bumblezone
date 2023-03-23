package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei;

import com.mojang.math.Matrix4f;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.REICompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class QueenTradesREICategory implements DisplayCategory<REIQueenTradesInfo> {

	public static final int RECIPE_WIDTH = 124;
	public static final int RECIPE_HEIGHT = 28;

	private final Renderer icon;
	private final Component localizedName;

	public QueenTradesREICategory() {
		this.icon = new QueenEggIconRenderer(new ResourceLocation(Bumblezone.MODID, "textures/gui/bee_queen_trades.png"));
		this.localizedName = Component.translatable("the_bumblezone.jei.bee_queen_trades");
	}

	@Override
	public CategoryIdentifier<? extends REIQueenTradesInfo> getCategoryIdentifier() {
		return REICompat.QUEEN_TRADES;
	}

	@Override
	public Component getTitle() {
		return this.localizedName;
	}

	@Override
	public Renderer getIcon() {
		return this.icon;
	}

	@Override
	public int getDisplayWidth(REIQueenTradesInfo display) {
		return RECIPE_WIDTH + 8;
	}

	@Override
	public int getDisplayHeight() {
		return RECIPE_HEIGHT + 8;
	}

	@Override
	public List<Widget> setupDisplay(REIQueenTradesInfo display, Rectangle origin) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(origin));
		Rectangle bounds = origin.getBounds();
		bounds.translate(4, 4);

		widgets.add(Widgets.createTexturedWidget(new ResourceLocation(Bumblezone.MODID, "textures/gui/queen_trades_layout.png"), new Rectangle(bounds.getX(), bounds.getY(), RECIPE_WIDTH, RECIPE_HEIGHT)));
		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 5, bounds.getY() + 5, 18, 18)).entries(display.getInputEntries().get(0)).markInput().disableBackground());
		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 63, bounds.getY() + 5, 18, 18)).entries(display.getOutputEntries().get(0)).markInput().disableBackground());

		if (display.getInputTag() != null) {
			widgets.add(Widgets.withTranslate(Widgets.createTexturedWidget(
					new ResourceLocation(Bumblezone.MODID, "textures/gui/tag_icon.png"),
					new Rectangle(bounds.getX() + 8, bounds.getY() + 8, 16, 16),
					0, 0, 16, 16), Matrix4f.createTranslateMatrix(0, 0, 101)));
		}
		if (display.getOutputTag() != null) {
			widgets.add(Widgets.withTranslate(Widgets.createTexturedWidget(
					new ResourceLocation(Bumblezone.MODID, "textures/gui/tag_icon.png"),
					new Rectangle(bounds.getX() + 66, bounds.getY() + 8, 16, 16),
					0, 0, 16, 16), Matrix4f.createTranslateMatrix(0, 0, 101)));
		}

		widgets.add(Widgets.createLabel(new Point(bounds.getX() + 100, bounds.getY() + 10), Component.translatable("the_bumblezone.jei.queen_trade_xp", display.getXpReward())).leftAligned().noShadow().color(0xFF404040, 0xFFBBBBBB));

		double percentValue = (double)(display.getWeight()) / (display.getGroupWeight()) * 100;
		String percent = String.valueOf(percentValue);
		String percentRounded = String.valueOf(Math.max(Math.round(percentValue), 1));

		widgets.add(Widgets.createTooltip(new Rectangle(bounds.getX() + 32, bounds.getY() + 2, 22, 20), Component.translatable("the_bumblezone.jei.queen_trade_chance_tooltip", percent.substring(0, Math.min(percent.length(), 5)))));
		widgets.add(Widgets.createLabel(new Point(bounds.getX() + (38 - (percentRounded.length() * 3)), bounds.getY() + 11), Component.translatable("the_bumblezone.jei.queen_trade_chance_text", percentRounded)).leftAligned().noShadow().color(0xFF404040, 0xFFBBBBBB));
		return widgets;
	}
}