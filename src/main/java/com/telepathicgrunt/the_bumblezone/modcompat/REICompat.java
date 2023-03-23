package com.telepathicgrunt.the_bumblezone.modcompat;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei.QueenRandomizerTradesREICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei.QueenTradesREICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei.REIQueenRandomizerTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei.REIQueenTradesInfo;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public class REICompat implements REIClientPlugin {

    public static final CategoryIdentifier<REIQueenTradesInfo> QUEEN_TRADES = CategoryIdentifier.of(Bumblezone.MODID, "queen_trades");
    public static final CategoryIdentifier<REIQueenRandomizerTradesInfo> QUEEN_RANDOMIZE_TRADES = CategoryIdentifier.of(Bumblezone.MODID, "queen_color_randomizer_trades");

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        BzItems.CUSTOM_CREATIVE_TAB_ITEMS.forEach(REICompat::addInfo);
        addInfo(BzItems.PILE_OF_POLLEN);
        addInfo(BzFluids.SUGAR_WATER_FLUID);
        addInfo(BzFluids.ROYAL_JELLY_FLUID);
        addInfo(BzFluids.HONEY_FLUID);

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle_from_super_candles"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle_from_super_candles"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, false));

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades.isEmpty()) {
            for (Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades) {
                for (WeightedTradeResult weightedTradeResult : trade.getSecond().unwrap()) {
                    List<ItemStack> rewardCollection = weightedTradeResult.items.stream().map(e -> new ItemStack(e, weightedTradeResult.count)).toList();
                    registry.add(new REIQueenTradesInfo(
                            trade.getFirst().tagKey() != null ? EntryIngredients.ofItemTag(trade.getFirst().tagKey()) : EntryIngredients.of(trade.getFirst().item()),
                            trade.getFirst().tagKey(),
                            weightedTradeResult.tagKey != null ? EntryIngredients.ofItemTag(weightedTradeResult.tagKey) : EntryIngredients.ofItemStacks(rewardCollection),
                            weightedTradeResult.tagKey,
                            weightedTradeResult.xpReward,
                            weightedTradeResult.weight,
                            weightedTradeResult.getTotalWeight()
                    ), QUEEN_TRADES);
                }
            }
        }

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades.isEmpty()) {
            for (QueensTradeManager.TradeWantEntry tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades) {
                List<ItemStack> randomizeStack = tradeEntry.wantItems().stream().map(e -> e.value().getDefaultInstance()).toList();
                for (ItemStack input : randomizeStack) {
                    registry.add(new REIQueenRandomizerTradesInfo(
                            EntryIngredients.of(input),
                            tradeEntry.tagKey() != null ? EntryIngredients.ofItemTag(tradeEntry.tagKey()) : EntryIngredients.ofItemStacks(randomizeStack),
                            tradeEntry.tagKey(),
                            1,
                            randomizeStack.size()
                    ), QUEEN_RANDOMIZE_TRADES);
                }
            }
        }
    }

    private static void addInfo(Item item) {
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(item),
                Component.translatable(Registry.ITEM.getKey(item).toString()),
                (text) -> {
                    text.add(Component.translatable(Bumblezone.MODID + "." + Registry.ITEM.getKey(item).getPath() + ".jei_description"));
                    return text;
                });
    }

    private static void addInfo(Fluid fluid) {
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(fluid, 1),
                Component.translatable(Registry.FLUID.getKey(fluid).toString()),
                (text) -> {
                    text.add(Component.translatable(Bumblezone.MODID + "." + Registry.FLUID.getKey(fluid).getPath() + ".jei_description"));
                    return text;
                });
    }

    private static void registerExtraRecipes(Recipe<?> baseRecipe, DisplayRegistry registry, boolean oneRecipeOnly) {
        if (baseRecipe instanceof IncenseCandleRecipe incenseCandleRecipe) {
            List<CraftingRecipe> extraRecipes = FakeIncenseCandleRecipeCreator.constructFakeRecipes(incenseCandleRecipe, oneRecipeOnly);
            extraRecipes.forEach(registry::add);
        }
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new QueenTradesREICategory());
        registry.add(new QueenRandomizerTradesREICategory());

        registry.addWorkstations(QUEEN_TRADES, EntryStacks.of(BzItems.BEE_QUEEN_SPAWN_EGG));
        registry.addWorkstations(QUEEN_RANDOMIZE_TRADES, EntryStacks.of(BzItems.BEE_QUEEN_SPAWN_EGG));
    }
}
