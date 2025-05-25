package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.items.recipes.PotionCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.FakePotionCandleRecipeCreator;
import com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.emi.EMIQueenRandomizerTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.emi.EMIQueenTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.emi.QueenRandomizerTradesEMICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.fabricbase.emi.QueenTradesEMICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.RandomizeTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modinit.BzCreativeTabs;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "potion_candle/from_super_candles"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "potion_candle/from_string_and_carvable_wax"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, false));

        registry.addCategory(QUEEN_TRADES);
        registry.addCategory(QUEEN_RANDOMIZE_TRADES);

        registry.addWorkstation(QUEEN_TRADES, WORKSTATION);
        registry.addWorkstation(QUEEN_RANDOMIZE_TRADES, WORKSTATION);

        long recipeId = 1;
        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades.isEmpty()) {
            for (Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades) {
                for (WeightedTradeResult weightedTradeResult : trade.getSecond().unwrap()) {
                    List<EmiStack> rewardCollection = weightedTradeResult.getItems().stream().map(e -> EmiStack.of(new ItemStack(e, weightedTradeResult.count))).toList();
                    registry.addRecipe(new EMIQueenTradesInfo(
                            EmiIngredient.of(trade.getFirst().tagKey().isPresent() ? Ingredient.of(trade.getFirst().tagKey().get()) : Ingredient.of(trade.getFirst().item())),
                            trade.getFirst().tagKey().orElse(null),
                            rewardCollection,
                            weightedTradeResult.tagKey.orElse(null),
                            weightedTradeResult.xpReward,
                            weightedTradeResult.weight,
                            weightedTradeResult.getTotalWeight(),
                            new ResourceLocation(Bumblezone.MODID, "" + recipeId)));
                    recipeId++;
                }
            }
        }

        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades.isEmpty()) {
            Map<TagKey<Item>, TagData> cacheEmiData = new Object2ObjectOpenHashMap<>();

            for (RandomizeTradeRowInput tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades) {
                TagKey<Item> itemTagKey = tradeEntry.tagKey().get();
                TagData tagData = cacheEmiData.getOrDefault(itemTagKey, null);
                if (tagData == null) {
                    List<ItemStack> randomizeStack = tradeEntry.getWantItems().stream().map(e -> e.value().getDefaultInstance()).toList();
                    List<EmiStack> emiStackList = randomizeStack.stream().map(EmiStack::of).collect(Collectors.toList());
                    EmiIngredient emiIngredient = EmiIngredient.of(itemTagKey);

                    tagData = new TagData(randomizeStack.size(), emiStackList, emiIngredient);
                    cacheEmiData.put(itemTagKey, tagData);
                }

                registry.addRecipe(new EMIQueenRandomizerTradesInfo(
                        EmiIngredient.of(Ingredient.of(itemTagKey)),
                        true,
                        tagData.emiStack(),
                        itemTagKey,
                        tagData.emiIngredient(),
                        1,
                        tagData.listSize(),
                        new ResourceLocation(Bumblezone.MODID, "" + recipeId)));
                recipeId++;
            }
        }

        List<ItemStack> hangingGardensFlowers = GeneralUtils.convertBlockTagsToItemStacks(BzTags.HANGING_GARDEN_ALLOWED_FLOWERS_BLOCKS, BzTags.HANGING_GARDEN_FORCED_DISALLOWED_FLOWERS_BLOCKS);
        hangingGardensFlowers.addAll(GeneralUtils.convertBlockTagsToItemStacks(BzTags.HANGING_GARDEN_ALLOWED_TALL_FLOWERS_BLOCKS, BzTags.HANGING_GARDEN_FORCED_DISALLOWED_TALL_FLOWERS_BLOCKS));

        addComplexBlockTagInfo(registry,
                Pair.of(".hanging_gardens_flowers.description", hangingGardensFlowers),
                Pair.of(".crystalline_flower_can_be_placed_on.description",
                        GeneralUtils.convertBlockTagsToItemStacks(BzTags.CRYSTALLINE_FLOWER_CAN_SURVIVE_ON, null))
        );
    }

    @SafeVarargs
    private static void addComplexBlockTagInfo(@NotNull EmiRegistry registry, Pair<String, List<ItemStack>>... structureInfo) {
        for (Pair<String, List<ItemStack>> predicatePair : structureInfo) {
            registry.addRecipe(new EmiInfoRecipe(
                    List.of(EmiIngredient.of(Ingredient.of(predicatePair.getSecond().stream()))),
                    List.of(Component.translatable(Bumblezone.MODID + predicatePair.getFirst())),
                    new ResourceLocation(Bumblezone.MODID, predicatePair.getFirst())
            ));
        }
    }

    record TagData(int listSize, List<EmiStack> emiStack, EmiIngredient emiIngredient){}

    private static void registerExtraRecipes(Recipe<?> baseRecipe, EmiRegistry registry, boolean oneRecipeOnly) {
        if (baseRecipe instanceof PotionCandleRecipe potionCandleRecipe) {
            List<CraftingRecipe> extraRecipes = FakePotionCandleRecipeCreator.constructFakeRecipes(potionCandleRecipe, oneRecipeOnly);
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
                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".description")),
                new ResourceLocation(Bumblezone.MODID, BuiltInRegistries.ITEM.getKey(item).getPath() + "_info")
        ));
    }

    private static void addInfo(EmiRegistry registry, Fluid fluid) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiStack.of(fluid)),
                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".description")),
                new ResourceLocation(Bumblezone.MODID, BuiltInRegistries.FLUID.getKey(fluid).getPath() + "_info")
        ));
    }
}