package com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.rrv;

import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewScreen;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import com.telepathicgrunt.the_bumblezone.entities.datamanagers.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.MainTradeRowInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.Weighted;

import java.util.List;

public class RRVQueenTradesRecipe implements ReliableClientRecipe {

    private final Identifier id;
    private final MainTradeRowInput input;
    private final Weighted<WeightedTradeResult> reward;
    private final SlotContent inputSlotContent;
    private final SlotContent ouputSlotContent;

    public RRVQueenTradesRecipe(Identifier id, MainTradeRowInput input, Weighted<WeightedTradeResult> reward) {
        this.input = input;
        this.reward = reward;
        this.id = id;
        this.inputSlotContent = input.tagKey().isPresent() ? SlotContent.of(input.tagKey().get()) : SlotContent.of(input.item());
        this.ouputSlotContent = reward.value().tagKey.isPresent() ? SlotContent.of(reward.value().tagKey.get()) : SlotContent.of(reward.value().getItems());
    }

    @Override
    public ReliableClientRecipeType getType() {
        return RRVQueenTradesRecipeType.INSTANCE;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public void bindSlots(RecipeViewMenu.SlotFillContext slotFillContext) {
        slotFillContext.bindSlot(0, this.inputSlotContent);
        slotFillContext.bindSlot(1, this.ouputSlotContent);

        slotFillContext.addAdditionalStackModifier(1, (_, tooltip) -> {
            double percent = (double)(this.reward.value().weight) / (this.reward.value().getTotalWeight()) * 100;
            if (this.reward.value().tagKey.isPresent()) {
                percent *= ((double)(this.reward.value().weight) / BuiltInRegistries.ITEM.get(this.reward.value().tagKey.get()).get().size());
            }
            String percentString =  String.valueOf(percent);
            tooltip.add(Component.translatable("the_bumblezone.recipe_viewers.queen_trade_chance_tooltip", percentString.substring(0, Math.min(percentString.length(), 5))));
        });
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
        guiGraphics.text(Minecraft.getInstance().font, Component.translatable("the_bumblezone.recipe_viewers.queen_trade_xp", this.reward.value().xpReward), 100, 11, 0xFF808080, false);

        double percentValue = ((double)(this.reward.value().weight) / this.reward.value().getTotalWeight()) * 100D;
        if (this.reward.value().tagKey.isPresent()) {
            percentValue *= ((double)(this.reward.value().weight) / BuiltInRegistries.ITEM.get(this.reward.value().tagKey.get()).get().size());
        }
        String percentRounded;
        if (percentValue < 1) {
            percentRounded = String.valueOf(Math.max(Math.round(percentValue * 10D) / 10D, 0.1D));
        }
        else {
            percentRounded = String.valueOf(Math.max(Math.round(percentValue), 1));
        }
        guiGraphics.text(Minecraft.getInstance().font, Component.translatable("the_bumblezone.recipe_viewers.queen_trade_chance_text", percentRounded), 38 - (percentValue < 1 ? 6 : (percentRounded.length() * 3)), 11, 0xFF808080, false);

        if (this.input.tagKey().isPresent()) {
            guiGraphics.blit(RRVQueenTradesRecipeType.TAG_ICON, 11, 11, 27, 27, 0, 16, 0, 16);
        }

        if (this.reward.value().tagKey.isPresent()) {
            guiGraphics.blit(RRVQueenTradesRecipeType.TAG_ICON, 69, 11, 85, 27, 0, 16, 0, 16);
        }
    }
}