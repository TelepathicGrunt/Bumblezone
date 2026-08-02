package com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.rrv;

import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewScreen;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class RRVQueenRandomizerTradesRecipe implements ReliableClientRecipe {

    private final Identifier id;
    private final TagKey<Item> tagInAndOut;
    private final Ingredient ingredientInAndOut;
    private final int tagSize;
    private final List<ItemStack> output;
    private final SlotContent inputSlotContent;
    private final SlotContent ouputSlotContent;

    public RRVQueenRandomizerTradesRecipe(Identifier id, TagKey<Item> tagInAndOut, Ingredient ingredientInAndOut, int tagSize, List<ItemStack> output) {
        this.tagInAndOut = tagInAndOut;
        this.ingredientInAndOut = ingredientInAndOut;
        this.tagSize = tagSize;
        this.output = output;
        this.id = id;
        this.inputSlotContent = tagInAndOut != null ? SlotContent.of(tagInAndOut) : SlotContent.of(ingredientInAndOut);
        this.ouputSlotContent = SlotContent.of(output);
    }

    @Override
    public ReliableClientRecipeType getType() {
        return RRVQueenRandomizerTradesRecipeType.INSTANCE;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public void bindSlots(RecipeViewMenu.SlotFillContext slotFillContext) {
        slotFillContext.bindSlot(0, this.inputSlotContent);
        slotFillContext.bindSlot(1, this.ouputSlotContent);
    }

    @Override
    public List<SlotContent> getIngredients() {
        return List.of(this.inputSlotContent);
    }

    @Override
    public List<SlotContent> getResults() {
        return List.of(this.ouputSlotContent);
    }

    @Override
    public void renderRecipe(RecipeViewScreen screen, RecipePosition recipePosition, GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.text(Minecraft.getInstance().font, Component.translatable("the_bumblezone.recipe_viewers.queen_trade_colors", this.tagSize), 86, 11, 0xFF808080, false);

        if (this.tagInAndOut != null) {
            guiGraphics.blit(RRVQueenRandomizerTradesRecipeType.TAG_ICON, 11, 11, 27, 27, 0, 16, 0, 16);
            guiGraphics.blit(RRVQueenRandomizerTradesRecipeType.TAG_ICON, 69, 11, 85, 27, 0, 16, 0, 16);
        }
    }
}