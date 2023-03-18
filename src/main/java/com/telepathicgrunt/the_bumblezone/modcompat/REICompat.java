package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.TradeEntryReducedObj;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
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
import java.util.Map;

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

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.isEmpty()) {
            for (Map.Entry<Item, WeightedRandomList<TradeEntryReducedObj>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.entrySet()) {
                for (TradeEntryReducedObj tradeResult : trade.getValue().unwrap()) {
                    if (!tradeResult.randomizerTrade()) {
                        registry.add(new REIQueenTradesInfo(List.of(EntryIngredients.of(trade.getKey())), List.of(EntryIngredients.of(tradeResult.item(), tradeResult.count())), tradeResult.xpReward(), tradeResult.weight(), tradeResult.totalGroupWeight()), QUEEN_TRADES);
                    }
                }
            }
        }

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.isEmpty()) {
            for (List<TradeEntryReducedObj> tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.tradeRandomizer) {
                List<ItemStack> randomizeStack = tradeEntry.stream().map(e -> e.item().getDefaultInstance()).toList();
                for (ItemStack input : randomizeStack) {
                    registry.add(new REIQueenRandomizerTradesInfo(List.of(EntryIngredients.of(input)), List.of(EntryIngredients.ofItemStacks(randomizeStack)), 1, randomizeStack.size()), QUEEN_RANDOMIZE_TRADES);
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
}

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new QueenTradesREICategory());
        registry.add(new QueenRandomizerTradesREICategory());

        registry.addWorkstations(QUEEN_TRADES, EntryStacks.of(BzItems.BEE_QUEEN_SPAWN_EGG.get()));
        registry.addWorkstations(QUEEN_RANDOMIZE_TRADES, EntryStacks.of(BzItems.BEE_QUEEN_SPAWN_EGG.get()));
    }
}
