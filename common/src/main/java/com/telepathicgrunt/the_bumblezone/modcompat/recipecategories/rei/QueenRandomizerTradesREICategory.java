package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.REICompat;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class QueenRandomizerTradesREICategory implements DisplayCategory<REIQueenRandomizerTradesInfo> {

	public static final int RECIPE_WIDTH = 136;
	public static final int RECIPE_HEIGHT = 28;

	private final Renderer icon;
	private final Component localizedName;

	public QueenRandomizerTradesREICategory() {
		this.icon = new QueenEggIconRenderer(new ResourceLocation(Bumblezone.MODID, "textures/gui/bee_queen_randomize_trades.png"));
		this.localizedName = Component.translatable("the_bumblezone.jei.bee_queen_color_randomizing_trades");
	}

	@Override
	public CategoryIdentifier<? extends REIQueenRandomizerTradesInfo> getCategoryIdentifier() {
		return REICompat.QUEEN_RANDOMIZE_TRADES;
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
	public int getDisplayWidth(REIQueenRandomizerTradesInfo display) {
		return RECIPE_WIDTH + 8;
	}

	@Override
	public int getDisplayHeight() {
		return RECIPE_HEIGHT + 8;
	}

	@Override
	public List<Widget> setupDisplay(REIQueenRandomizerTradesInfo display, Rectangle origin) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(origin));
		Rectangle bounds = origin.getBounds();
		bounds.translate(4, 4);

		widgets.add(Widgets.createTexturedWidget(new ResourceLocation(Bumblezone.MODID, "textures/gui/queen_randomizer_trades_layout.png"), new Rectangle(bounds.getX(), bounds.getY(), RECIPE_WIDTH, RECIPE_HEIGHT)));
		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 5, bounds.getY() + 5, 18, 18)).entries(display.getInputEntries().get(0)).markInput().disableBackground());
		widgets.add(Widgets.createSlot(new Rectangle(bounds.getX() + 63, bounds.getY() + 5, 18, 18)).entries(display.getOutputEntries().get(0)).markInput().disableBackground());

		widgets.add(Widgets.createLabel(new Point(bounds.getX() + 86, bounds.getY() + 10), Component.translatable("the_bumblezone.jei.queen_trade_colors", display.getOutputEntries().get(0).size())).leftAligned().noShadow().color(0xFF404040, 0xFFBBBBBB));

		if (display.getOutputTag() != null) {
			widgets.add(Widgets.withTranslate(Widgets.createTexturedWidget(
					new ResourceLocation(Bumblezone.MODID, "textures/gui/tag_icon.png"),
					new Rectangle(bounds.getX() + 69, bounds.getY() + 11, 16, 16),
					0, 0, 16, 16), new Matrix4f().translate(0, 0, 301)));
		}

		return widgets;
	}
}