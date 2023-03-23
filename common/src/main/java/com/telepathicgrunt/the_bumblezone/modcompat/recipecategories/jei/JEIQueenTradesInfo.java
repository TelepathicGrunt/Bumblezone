package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei;

import com.telepathicgrunt.the_bumblezone.entities.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;

public record JEIQueenTradesInfo(MainTradeRowInput input, WeightedTradeResult reward) implements IRecipeCategoryExtension {

}
