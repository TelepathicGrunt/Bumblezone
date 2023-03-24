package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.JEIIntegration;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class QueenTradesJEICategory implements IRecipeCategory<JEIQueenTradesInfo> {

    public static final int RECIPE_WIDTH = 124;
    public static final int RECIPE_HEIGHT = 28;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final IDrawable tagIcon;

    public QueenTradesJEICategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(Bumblezone.MODID, "textures/gui/queen_trades_layout.png"), 0, 0, RECIPE_WIDTH, RECIPE_HEIGHT);
        this.localizedName = Component.translatable("the_bumblezone.recipe_viewers.bee_queen_trades");

        DrawableBuilder iconBuilder = new DrawableBuilder(new ResourceLocation("the_bumblezone", "textures/gui/bee_queen_trades.png"), 0, 0, 16, 16);
        iconBuilder.setTextureSize(16, 16);
        this.icon = iconBuilder.build();

        DrawableBuilder tagIconBuilder = new DrawableBuilder(new ResourceLocation("the_bumblezone", "textures/gui/tag_icon.png"), 0, 0, 16, 16);
        tagIconBuilder.setTextureSize(16, 16);
        this.tagIcon = tagIconBuilder.build();
    }

    @Override
    public RecipeType<JEIQueenTradesInfo> getRecipeType() {
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
    public void draw(JEIQueenTradesInfo recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Minecraft.getInstance().font.draw(stack, Component.translatable("the_bumblezone.recipe_viewers.queen_trade_xp", recipe.reward().xpReward), 100, 10, 0xFF808080);

        double percentValue = ((double)(recipe.reward().weight) / recipe.reward().getTotalWeight()) * 100D;
        String percentRounded = String.valueOf(Math.max(Math.round(percentValue), 1));
        Minecraft.getInstance().font.draw(stack, Component.translatable("the_bumblezone.recipe_viewers.queen_trade_chance_text", percentRounded), 38 - (percentRounded.length() * 3), 11, 0xFF808080);

        if (recipe.input().tagKey().isPresent()) {
            tagIcon.draw(stack, 11, 11);
        }

        if (recipe.reward().tagKey.isPresent()) {
            tagIcon.draw(stack, 69, 11);
        }
    }

    @Override
    public List<Component> getTooltipStrings(JEIQueenTradesInfo recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX > 32 && mouseX < 54 && mouseY > 4 && mouseY < 24) {
            String percent = String.valueOf((double)(recipe.reward().weight) / (recipe.reward().getTotalWeight()) * 100);
            return List.of(Component.translatable("the_bumblezone.recipe_viewers.queen_trade_chance_tooltip", percent.substring(0, Math.min(percent.length(), 5))));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JEIQueenTradesInfo recipe, IFocusGroup focuses) {

        if (recipe.input().tagKey().isPresent()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 6, 6).addItemStacks(Registry.ITEM.getTag(recipe.input().tagKey().get()).get().stream().map(e -> e.value().getDefaultInstance()).toList());
        }
        else {
            builder.addSlot(RecipeIngredientRole.INPUT, 6, 6).addItemStack(recipe.input().item().getDefaultInstance());
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 6).addItemStacks(recipe.reward().items.stream().map(e -> new ItemStack(e, recipe.reward().count)).toList());
    }
}