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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class JEIQueenTradesCategory implements IRecipeCategory<JEIQueenTradesInfo> {

    public static final int RECIPE_WIDTH = 124;
    public static final int RECIPE_HEIGHT = 28;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final IDrawable tagIcon;

    public JEIQueenTradesCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/gui/recipe_viewer/queen_trades_layout.png"), 0, 0, RECIPE_WIDTH, RECIPE_HEIGHT);
        this.localizedName = Component.translatable("the_bumblezone.recipe_viewers.bee_queen_trades");

        DrawableBuilder iconBuilder = new DrawableBuilder(Identifier.fromNamespaceAndPath("the_bumblezone", "textures/gui/recipe_viewer/bee_queen_trades.png"), 0, 0, 16, 16);
        iconBuilder.setTextureSize(16, 16);
        this.icon = iconBuilder.build();

        DrawableBuilder tagIconBuilder = new DrawableBuilder(Identifier.fromNamespaceAndPath("the_bumblezone", "textures/gui/recipe_viewer/tag_icon.png"), 0, 0, 16, 16);
        tagIconBuilder.setTextureSize(16, 16);
        this.tagIcon = tagIconBuilder.build();
    }

    @Override
    public IRecipeType<JEIQueenTradesInfo> getRecipeType() {
        return JEIIntegration.QUEEN_TRADES;
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
    public void draw(JEIQueenTradesInfo recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
        guiGraphics.text(Minecraft.getInstance().font, Component.translatable("the_bumblezone.recipe_viewers.queen_trade_xp", recipe.reward.value().xpReward), 100, 11, 0xFF808080, false);

        double percentValue = ((double)(recipe.reward.value().weight) / recipe.reward.value().getTotalWeight()) * 100D;
        if (recipe.reward.value().tagKey.isPresent() && recipe.outputFocused) {
            percentValue *= ((double)(recipe.reward.value().weight) / BuiltInRegistries.ITEM.get(recipe.reward.value().tagKey.get()).get().size());
        }

        String percentRounded;
        if (percentValue < 1) {
            percentRounded = String.valueOf(Math.max(Math.round(percentValue * 10D) / 10D, 0.1D));
        }
        else {
            percentRounded = String.valueOf(Math.max(Math.round(percentValue), 1));
        }
        guiGraphics.text(Minecraft.getInstance().font, Component.translatable("the_bumblezone.recipe_viewers.queen_trade_chance_text", percentRounded), 38 - (percentValue < 1 ? 6 : (percentRounded.length() * 3)), 11, 0xFF808080, false);

        if (recipe.input.tagKey().isPresent()) {
            tagIcon.draw(guiGraphics, 11, 11);
        }

        if (recipe.reward.value().tagKey.isPresent() && !recipe.outputFocused) {
            tagIcon.draw(guiGraphics, 69, 11);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, JEIQueenTradesInfo recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX > 32 && mouseX < 54 && mouseY > 4 && mouseY < 24) {
            double percent = (double)(recipe.reward.value().weight) / (recipe.reward.value().getTotalWeight()) * 100;
            if (recipe.reward.value().tagKey.isPresent() && recipe.outputFocused) {
                percent *= ((double)(recipe.reward.value().weight) / BuiltInRegistries.ITEM.get(recipe.reward.value().tagKey.get()).get().size());
            }
            String percentString =  String.valueOf(percent);
            tooltip.add(Component.translatable("the_bumblezone.recipe_viewers.queen_trade_chance_tooltip", percentString.substring(0, Math.min(percentString.length(), 5))));
        }
        IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JEIQueenTradesInfo recipe, IFocusGroup focuses) {

        if (recipe.input.tagKey().isPresent()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 6, 6).addItemStacks(BuiltInRegistries.ITEM.get(recipe.input.tagKey().get()).get().stream().map(e -> e.value().getDefaultInstance()).toList());
        }
        else {
            builder.addSlot(RecipeIngredientRole.INPUT, 6, 6).add(recipe.input.item().getDefaultInstance());
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 6).addItemStacks(recipe.reward.value().getItems());
        recipe.outputFocused = !focuses.isEmpty() && focuses.getAllFocuses().get(0).getRole() == RecipeIngredientRole.OUTPUT;
    }
}