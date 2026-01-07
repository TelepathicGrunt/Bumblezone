package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei;

import com.telepathicgrunt.the_bumblezone.entities.datamanagers.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.util.random.Weighted;

public class JEIQueenTradesInfo implements IRecipeCategoryExtension {
    public final MainTradeRowInput input;
    public final Weighted<WeightedTradeResult> reward;
    public boolean outputFocused = false;

    public JEIQueenTradesInfo(MainTradeRowInput input, Weighted<WeightedTradeResult> reward) {
        this.input = input;
        this.reward = reward;
    }
}