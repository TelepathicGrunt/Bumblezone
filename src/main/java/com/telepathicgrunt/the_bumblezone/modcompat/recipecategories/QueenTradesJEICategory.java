package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.JEIIntegration;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class QueenTradesJEICategory implements IRecipeCategory<QueenTradesInfo> {

    public static final int RECIPE_WIDTH = 124;
    public static final int RECIPE_HEIGHT = 28;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public QueenTradesJEICategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(Bumblezone.MODID, "textures/gui/queen_trades_jei.png"), 0, 0, RECIPE_WIDTH, RECIPE_HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(BzItems.BEE_QUEEN_SPAWN_EGG.get().getDefaultInstance());
        this.localizedName = Component.translatable("the_bumblezone.jei.bee_queen_trades");
    }

    @Override
    public RecipeType<QueenTradesInfo> getRecipeType() {
        return JEIIntegration.QUEEN_TRADES;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void draw(QueenTradesInfo recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Minecraft.getInstance().font.draw(stack, Component.translatable("the_bumblezone.jei.queen_trade_xp", recipe.xp()), 100, 10, 0xFF808080);
    }

    @Override
    public List<Component> getTooltipStrings(QueenTradesInfo recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX > 32 && mouseX < 54 && mouseY > 6 && mouseY < 22) {
            String percent = String.valueOf((double)(recipe.weight()) / (recipe.totalGroupWeight()) * 100);
            return List.of(Component.translatable("the_bumblezone.jei.queen_trade_chance", percent.substring(0, Math.min(percent.length(), 5))));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, QueenTradesInfo recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 6).addIngredient(VanillaTypes.ITEM_STACK, recipe.wantItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 6).addIngredient(VanillaTypes.ITEM_STACK, recipe.reward());
    }
}