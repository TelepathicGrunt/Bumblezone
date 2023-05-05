package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei;

import com.telepathicgrunt.the_bumblezone.entities.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;

public class JEIQueenTradesInfo implements IRecipeCategoryExtension {
    public final MainTradeRowInput input;
    public final WeightedTradeResult reward;
    public boolean outputFocused = false;

    public JEIQueenTradesInfo(MainTradeRowInput input, WeightedTradeResult reward) {
        this.input = input;
        this.reward = reward;
    }
}
