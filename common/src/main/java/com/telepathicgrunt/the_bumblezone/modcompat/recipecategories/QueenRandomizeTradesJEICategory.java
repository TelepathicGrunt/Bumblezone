package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories;

import com.telepathicgrunt.the_bumblezone.modcompat.JEIIntegration;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.gui.elements.DrawableBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class QueenRandomizeTradesJEICategory extends QueenTradesJEICategory {

    private final IDrawable icon;
    private final Component localizedName;

    public QueenRandomizeTradesJEICategory(IGuiHelper guiHelper) {
        super(guiHelper);
        this.localizedName = Component.translatable("the_bumblezone.jei.bee_queen_color_randomizing_trades");

        DrawableBuilder iconBuilder = new DrawableBuilder(new ResourceLocation("the_bumblezone", "textures/gui/bee_queen_randomize_trades.png"), 0, 0, 16, 16);
        iconBuilder.setTextureSize(16, 16);
        this.icon = iconBuilder.build();
    }

    @Override
    public RecipeType<QueenTradesInfo> getRecipeType() {
        return JEIIntegration.QUEEN_RANDOMIZE_TRADES;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }
}