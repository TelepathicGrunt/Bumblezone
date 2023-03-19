package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.TradeEntryReducedObj;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.FakeIncenseCandleRecipeCreator;
import com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.emi.EMIQueenRandomizerTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.emi.EMIQueenTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.emi.QueenRandomizerTradesEMICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.emi.QueenTradesEMICategory;
import com.telepathicgrunt.the_bumblezone.modinit.BzCreativeTabs;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
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
import java.util.Map;
import java.util.stream.Collectors;

public class EMICompat implements EmiPlugin {
    public static final EmiStack WORKSTATION = EmiStack.of(BzItems.BEE_QUEEN_SPAWN_EGG.get());
    public static final EmiRecipeCategory QUEEN_TRADES = new QueenTradesEMICategory();
    public static final EmiRecipeCategory QUEEN_RANDOMIZE_TRADES = new QueenRandomizerTradesEMICategory();

    @Override
    public void register(EmiRegistry registry) {
        BzCreativeTabs.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(registry, item.get()));
        addInfo(registry, BzItems.PILE_OF_POLLEN.get());
        addInfo(registry, BzFluids.SUGAR_WATER_FLUID.get());
        addInfo(registry, BzFluids.ROYAL_JELLY_FLUID.get());
        addInfo(registry, BzFluids.HONEY_FLUID.get());

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle_from_super_candles"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));
        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, false));

        registry.addCategory(QUEEN_TRADES);
        registry.addCategory(QUEEN_RANDOMIZE_TRADES);

        registry.addWorkstation(QUEEN_TRADES, WORKSTATION);
        registry.addWorkstation(QUEEN_RANDOMIZE_TRADES, WORKSTATION);

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.isEmpty()) {
            for (Map.Entry<Item, WeightedRandomList<TradeEntryReducedObj>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.entrySet()) {
                for (TradeEntryReducedObj tradeResult : trade.getValue().unwrap()) {
                    if (!tradeResult.randomizerTrade()) {
                        List<EmiStack> rewardCollection = tradeResult.items().stream().map(e -> EmiStack.of(new ItemStack(e, tradeResult.count()))).toList();
                        registry.addRecipe(new EMIQueenTradesInfo(EmiIngredient.of(Ingredient.of(trade.getKey())), rewardCollection, tradeResult.xpReward(), tradeResult.weight(), tradeResult.totalGroupWeight()));
                    }
                }
            }
        }

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.isEmpty()) {
            for (TradeEntryReducedObj tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.tradeRandomizer) {
                List<ItemStack> randomizeStack = tradeEntry.items().stream().map(Item::getDefaultInstance).toList();
                for (ItemStack input : randomizeStack) {
                    registry.addRecipe(new EMIQueenRandomizerTradesInfo(EmiIngredient.of(Ingredient.of(input)), randomizeStack.stream().map(EmiStack::of).collect(Collectors.toList()), 1, randomizeStack.size()));
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
                            EmiStack.of(r.getResultItem(RegistryAccess.EMPTY)),
                            r.getId(),
                            false)));
        }
    }
    
    private static void addInfo(EmiRegistry registry, Item item) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiIngredient.of(Ingredient.of(new ItemStack(item)))),
                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".jei_description")),
                new ResourceLocation(Bumblezone.MODID, BuiltInRegistries.ITEM.getKey(item).getPath() + "_info")
        ));
    }

    private static void addInfo(EmiRegistry registry, Fluid fluid) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiStack.of(fluid)),
                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".jei_description")),
                new ResourceLocation(Bumblezone.MODID, BuiltInRegistries.FLUID.getKey(fluid).getPath() + "_info")
        ));
    }
}