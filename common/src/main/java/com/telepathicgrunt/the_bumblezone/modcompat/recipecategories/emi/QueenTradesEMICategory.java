package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class QueenTradesEMICategory extends EmiRecipeCategory {

	public QueenTradesEMICategory() {
		super(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "queen_trades"), new EmiTexture(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "textures/gui/bee_queen_trades.png"), 0, 0, 16, 16, 16, 16, 16, 16));
	}

	public Component getName() {
		return Component.translatable("the_bumblezone.recipe_viewers.bee_queen_trades");
	}
}