package com.telepathicgrunt.the_bumblezone.modcompat;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.items.recipes.PotionCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.RandomizeTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi.EMIQueenRandomizerTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi.EMIQueenTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi.QueenRandomizerTradesEMICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.emi.QueenTradesEMICategory;
import com.telepathicgrunt.the_bumblezone.modinit.BzCreativeTabs;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import dev.emi.emi.api.EmiEntrypoint;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.stream.Collectors;

@EmiEntrypoint
public class EMICompat implements EmiPlugin {
    public static final EmiStack WORKSTATION = EmiStack.of(BzItems.BEE_QUEEN_SPAWN_EGG.get());
    public static final EmiRecipeCategory QUEEN_TRADES = new QueenTradesEMICategory();
    public static final EmiRecipeCategory QUEEN_RANDOMIZE_TRADES = new QueenRandomizerTradesEMICategory();

    @Override
    public void register(EmiRegistry registry) {
        BzCreativeTabs.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(registry, item.get()));
        addInfo(registry, BzFluids.SUGAR_WATER_FLUID.get());
        addInfo(registry, BzFluids.ROYAL_JELLY_FLUID.get());
        if (BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid.isEmpty()) {
            addInfo(registry, BzFluids.HONEY_FLUID.get());
        }

        registry.getRecipeManager().byKey(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "potion_candle/from_super_candles"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));

        registry.getRecipeManager().byKey(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "potion_candle/from_string_and_carvable_wax"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, false));

        registry.addCategory(QUEEN_TRADES);
        registry.addCategory(QUEEN_RANDOMIZE_TRADES);

        registry.addWorkstation(QUEEN_TRADES, WORKSTATION);
        registry.addWorkstation(QUEEN_RANDOMIZE_TRADES, WORKSTATION);

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades.isEmpty()) {
            for (Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades) {
                for (WeightedTradeResult weightedTradeResult : trade.getSecond().unwrap()) {
                    List<EmiStack> rewardCollection = weightedTradeResult.getItems().stream().map(EmiStack::of).toList();
                    registry.addRecipe(new EMIQueenTradesInfo(
                            EmiIngredient.of(trade.getFirst().tagKey().isPresent() ? Ingredient.of(trade.getFirst().tagKey().get()) : Ingredient.of(trade.getFirst().item())),
                            trade.getFirst().tagKey().orElse(null),
                            rewardCollection,
                            weightedTradeResult.tagKey.orElse(null),
                            weightedTradeResult.xpReward,
                            weightedTradeResult.weight,
                            weightedTradeResult.getTotalWeight()));
                }
            }
        }

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades.isEmpty()) {
            for (RandomizeTradeRowInput tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades) {
                List<ItemStack> randomizeStack = tradeEntry.getWantItems().stream().map(e -> e.value().getDefaultInstance()).toList();
                for (ItemStack input : randomizeStack) {
                    registry.addRecipe(new EMIQueenRandomizerTradesInfo(
                            EmiIngredient.of(Ingredient.of(input)),
                            randomizeStack.stream().map(EmiStack::of).collect(Collectors.toList()),
                            tradeEntry.tagKey().orElse(null),
                            1,
                            randomizeStack.size()));
                }
            }
        }
    }

    private static void registerExtraRecipes(RecipeHolder<?> baseRecipe, EmiRegistry registry, boolean oneRecipeOnly) {
        if (baseRecipe.value() instanceof PotionCandleRecipe potionCandleRecipe) {
            List<CraftingRecipe> extraRecipes = FakePotionCandleRecipeCreator.constructFakeRecipes(potionCandleRecipe, oneRecipeOnly);
            for (int i = 0 ; i < extraRecipes.size(); i++) {
                CraftingRecipe craftingRecipe = extraRecipes.get(i);
                registry.addRecipe(
                        new EmiCraftingRecipe(
                                craftingRecipe.getIngredients().stream().map(EmiIngredient::of).toList(),
                                EmiStack.of(craftingRecipe.getResultItem(RegistryAccess.EMPTY)),
                                ResourceLocation.fromNamespaceAndPath(baseRecipe.id().getNamespace(), baseRecipe.id().getPath() + "_" + i),
                                false));
            }
        }
    }
    
    private static void addInfo(EmiRegistry registry, Item item) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiIngredient.of(Ingredient.of(new ItemStack(item)))),
                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".description")),
                ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, BuiltInRegistries.ITEM.getKey(item).getPath() + "_info")
        ));
    }

    private static void addInfo(EmiRegistry registry, Fluid fluid) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiStack.of(fluid)),
                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".description")),
                ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, BuiltInRegistries.FLUID.getKey(fluid).getPath() + "_info")
        ));
    }
}