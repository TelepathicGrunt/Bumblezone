package com.telepathicgrunt.the_bumblezone.modcompat;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi.EMIQueenRandomizerTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi.EMIQueenTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi.QueenRandomizerTradesEMICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi.QueenTradesEMICategory;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.stream.Collectors;

public class EMICompat implements EmiPlugin {
    public static final EmiStack WORKSTATION = EmiStack.of(BzItems.BEE_QUEEN_SPAWN_EGG);
    public static final EmiRecipeCategory QUEEN_TRADES = new QueenTradesEMICategory();
    public static final EmiRecipeCategory QUEEN_RANDOMIZE_TRADES = new QueenRandomizerTradesEMICategory();

    @Override
    public void register(EmiRegistry registry) {
        BzItems.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(registry, item));
        addInfo(registry, BzItems.PILE_OF_POLLEN);
        addInfo(registry, BzFluids.SUGAR_WATER_FLUID);
        addInfo(registry, BzFluids.ROYAL_JELLY_FLUID);
        addInfo(registry, BzFluids.HONEY_FLUID);

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle_from_super_candles"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));
        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, false));

        registry.addCategory(QUEEN_TRADES);
        registry.addCategory(QUEEN_RANDOMIZE_TRADES);

        registry.addWorkstation(QUEEN_TRADES, WORKSTATION);
        registry.addWorkstation(QUEEN_RANDOMIZE_TRADES, WORKSTATION);

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades.isEmpty()) {
            for (Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades) {
                for (WeightedTradeResult weightedTradeResult : trade.getSecond().unwrap()) {
                    List<EmiStack> rewardCollection = weightedTradeResult.items.stream().map(e -> EmiStack.of(new ItemStack(e, weightedTradeResult.count))).toList();
                    registry.addRecipe(new EMIQueenTradesInfo(
                            EmiIngredient.of(trade.getFirst().tagKey() != null ? Ingredient.of(trade.getFirst().tagKey()) : Ingredient.of(trade.getFirst().item())),
                            trade.getFirst().tagKey(),
                            rewardCollection,
                            weightedTradeResult.tagKey,
                            weightedTradeResult.xpReward,
                            weightedTradeResult.weight,
                            weightedTradeResult.getTotalWeight()));
                }
            }
        }

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades.isEmpty()) {
            for (QueensTradeManager.TradeWantEntry tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades) {
                List<ItemStack> randomizeStack = tradeEntry.wantItems().stream().map(e -> e.value().getDefaultInstance()).toList();
                for (ItemStack input : randomizeStack) {
                    registry.addRecipe(new EMIQueenRandomizerTradesInfo(
                            EmiIngredient.of(Ingredient.of(input)),
                            randomizeStack.stream().map(EmiStack::of).collect(Collectors.toList()),
                            tradeEntry.tagKey(),
                            1,
                            randomizeStack.size()));
                }
            }
        }
    }

    private static void registerExtraRecipes(Recipe<?> baseRecipe, EmiRegistry registry, boolean oneRecipeOnly) {
        if (baseRecipe instanceof IncenseCandleRecipe incenseCandleRecipe) {
            List<CraftingRecipe> extraRecipes = FakeIncenseCandleRecipeCreator.constructFakeRecipes(incenseCandleRecipe, oneRecipeOnly);
            extraRecipes.forEach(r -> registry.addRecipe(
                    new EmiCraftingRecipe(
                            r.getIngredients().stream().map(EmiIngredient::of).toList(),
                            EmiStack.of(r.getResultItem()),
                            r.getId(),
                            false)));
        }
    }
    
    private static void addInfo(EmiRegistry registry, Item item) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiIngredient.of(Ingredient.of(new ItemStack(item)))),
                List.of(Component.translatable(Bumblezone.MODID + "." + Registry.ITEM.getKey(item).getPath() + ".description")),
                new ResourceLocation(Bumblezone.MODID, Registry.ITEM.getKey(item).getPath() + "_info")
        ));
    }

    private static void addInfo(EmiRegistry registry, Fluid fluid) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiStack.of(fluid)),
                List.of(Component.translatable(Bumblezone.MODID + "." + Registry.FLUID.getKey(fluid).getPath() + ".description")),
                new ResourceLocation(Bumblezone.MODID, Registry.FLUID.getKey(fluid).getPath() + "_info")
        ));
    }
}
