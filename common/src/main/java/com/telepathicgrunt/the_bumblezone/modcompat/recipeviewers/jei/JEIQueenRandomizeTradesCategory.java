package com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.jei;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class JEIQueenRandomizeTradesCategory implements IRecipeCategory<JEIQueenRandomizerTradesInfo> {

    public static final int RECIPE_WIDTH = 136;
    public static final int RECIPE_HEIGHT = 28;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final IDrawable tagIcon;

    public JEIQueenRandomizeTradesCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/gui/queen_randomizer_trades_layout.png"), 0, 0, RECIPE_WIDTH, RECIPE_HEIGHT);
        this.localizedName = Component.translatable("the_bumblezone.recipe_viewers.bee_queen_color_randomizing_trades");

        DrawableBuilder iconBuilder = new DrawableBuilder(Identifier.fromNamespaceAndPath("the_bumblezone", "textures/gui/bee_queen_randomize_trades.png"), 0, 0, 16, 16);
        iconBuilder.setTextureSize(16, 16);
        this.icon = iconBuilder.build();

        DrawableBuilder tagIconBuilder = new DrawableBuilder(Identifier.fromNamespaceAndPath("the_bumblezone", "textures/gui/tag_icon.png"), 0, 0, 16, 16);
        tagIconBuilder.setTextureSize(16, 16);
        this.tagIcon = tagIconBuilder.build();
    }

    @Override
    public IRecipeType<JEIQueenRandomizerTradesInfo> getRecipeType() {
        return JEIIntegration.QUEEN_RANDOMIZE_TRADES;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public int getWidth() {
        return RECIPE_WIDTH;
    }

    @Override
    public int getHeight() {
        return RECIPE_HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(JEIQueenRandomizerTradesInfo recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
        guiGraphics.text(Minecraft.getInstance().font, Component.translatable("the_bumblezone.recipe_viewers.queen_trade_colors", recipe.tagSize()), 86, 10, 0xFF808080, false);

        if (recipe.tagInAndOut() != null) {
            tagIcon.draw(guiGraphics, 11, 11);
            tagIcon.draw(guiGraphics, 69, 11);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, JEIQueenRandomizerTradesInfo recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JEIQueenRandomizerTradesInfo recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 6).add(recipe.ingredientInAndOut());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 6).add(recipe.ingredientInAndOut());
    }
}