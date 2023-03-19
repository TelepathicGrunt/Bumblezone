package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.EMICompat;
import com.telepathicgrunt.the_bumblezone.modcompat.REICompat;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei.REIQueenTradesInfo;
import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.render.EmiTexture;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class QueenTradesEMICategory extends EmiRecipeCategory {

	public QueenTradesEMICategory() {
		super(new ResourceLocation(Bumblezone.MODID, "queen_trades"), EMICompat.WORKSTATION, new EmiTexture(new ResourceLocation(Bumblezone.MODID, "textures/gui/bee_queen_trades.png"), 0, 0, 16, 16));
	}

	public Component getName() {
		return EmiPort.translatable("the_bumblezone.jei.bee_queen_trades");
	}
}