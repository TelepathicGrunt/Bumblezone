package com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.rrv;

import cc.cassian.rrv.api.ReliableRecipeViewerClientPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.client.recipe.ClientRecipeManager;
import cc.cassian.rrv.common.builtin.crafting.CraftingClientRecipe;
import cc.cassian.rrv.common.builtin.info.InfoClientRecipe;
import cc.cassian.rrv.common.extra.FluidStack;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.entities.datamanagers.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.datamanagers.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.items.recipes.PotionCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.FakePotionCandleRecipeCreator;
import com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.MainTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.RandomizeTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.jei.datamanager.PotionCandleRecipeSyncData;
import com.telepathicgrunt.the_bumblezone.modinit.BzCreativeTabs;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RRVIntegration implements ReliableRecipeViewerClientPlugin {
    @Override
    public void onIntegrationInitialize() {
        ItemView.addClientRecipeProvider(recipeList -> {
            if (PotionCandleRecipeSyncData.POTION_CANDLE_FROM_SUPER_CANDLES != null) {
                registerExtraRecipes(PotionCandleRecipeSyncData.POTION_CANDLE_FROM_SUPER_CANDLES, recipeList, true);
            }

            if (PotionCandleRecipeSyncData.POTION_CANDLE_FROM_STRING_AND_CARVABLE_WAX != null) {
                registerExtraRecipes(PotionCandleRecipeSyncData.POTION_CANDLE_FROM_STRING_AND_CARVABLE_WAX, recipeList, false);
            }

            int beeQueenTradeIndex = 0;
            if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades.isEmpty()) {
                for (Pair<MainTradeRowInput, WeightedList<WeightedTradeResult>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades) {
                    for (Weighted<WeightedTradeResult> weightedTradeResult : trade.getSecond().unwrap()) {
                        beeQueenTradeIndex++;
                        recipeList.add(new RRVQueenTradesRecipe(
                                Identifier.fromNamespaceAndPath(Bumblezone.MODID, "/bee_queen_trade_" + beeQueenTradeIndex),
                                trade.getFirst(),
                                weightedTradeResult
                        ));
                    }
                }
            }

            beeQueenTradeIndex = 0;
            if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades.isEmpty()) {
                Map<TagKey<Item>, TagData> cacheRvvData = new Object2ObjectOpenHashMap<>();

                for (RandomizeTradeRowInput tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades) {
                    TagKey<Item> itemTagKey = tradeEntry.tagKey().get();
                    TagData tagData = cacheRvvData.getOrDefault(itemTagKey, null);
                    if (tagData == null) {
                        HolderSet.Named<Item> tagItems = BuiltInRegistries.ITEM.get(itemTagKey).get();
                        List<ItemStack> randomizeStack = tradeEntry.getWantItems().stream().map(e -> e.value().getDefaultInstance()).toList();
                        tagData = new TagData(randomizeStack.size(), randomizeStack, Ingredient.of(tagItems));
                        cacheRvvData.put(itemTagKey, tagData);
                    }

                    recipeList.add(new RRVQueenRandomizerTradesRecipe(
                            Identifier.fromNamespaceAndPath(Bumblezone.MODID, "/bee_queen_randomizer_trade_" + beeQueenTradeIndex),
                            itemTagKey,
                            tagData.rrvIngredient(),
                            tagData.listSize(),
                            tagData.rrvItems()));
                }
            }

            List<ItemStack> hangingGardensFlowers = GeneralUtils.convertBlockTagsToItemStacks(BzTags.HANGING_GARDEN_ALLOWED_FLOWERS_BLOCKS, BzTags.HANGING_GARDEN_FORCED_DISALLOWED_FLOWERS_BLOCKS);
            hangingGardensFlowers.addAll(GeneralUtils.convertBlockTagsToItemStacks(BzTags.HANGING_GARDEN_ALLOWED_TALL_FLOWERS_BLOCKS, BzTags.HANGING_GARDEN_FORCED_DISALLOWED_TALL_FLOWERS_BLOCKS));

            addComplexBlockTagInfo(
                    Pair.of(".hanging_gardens_flowers.description", hangingGardensFlowers),
                    Pair.of(".crystalline_flower_can_be_placed_on.description",
                            GeneralUtils.convertBlockTagsToItemStacks(BzTags.CRYSTALLINE_FLOWER_CAN_SURVIVE_ON, null))
            );
        });

        BzCreativeTabs.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(item.get()));
        addInfo(BzFluids.SUGAR_WATER_FLUID.get());
        addInfo(BzFluids.ROYAL_JELLY_FLUID.get());
        if (BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid.isEmpty()) {
            addInfo(BzFluids.HONEY_FLUID.get());
        }
    }

    private static void addInfo(Item item) {
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        ItemView.addInfoRecipe(new InfoClientRecipe(
                Identifier.fromNamespaceAndPath(Bumblezone.MODID, itemPath),
                SlotContent.of(item),
                Component.translatable(Bumblezone.MODID + "." + itemPath + ".description")));
    }

    private static void addInfo(Fluid fluid) {
        String fluidPath = BuiltInRegistries.FLUID.getKey(fluid).getPath();
        ItemView.addInfoRecipe(new InfoClientRecipe(
                Identifier.fromNamespaceAndPath(Bumblezone.MODID, fluidPath),
                SlotContent.of(new FluidStack(fluid, 1000)),
                Component.translatable(Bumblezone.MODID + "." + fluidPath + ".description")));
    }

    @SafeVarargs
    private static void addComplexBlockTagInfo(Pair<String, List<ItemStack>>... structureInfo) {
        for (Pair<String, List<ItemStack>> predicatePair : structureInfo) {
            for (ItemStack item : predicatePair.getSecond())
            {
                String itemPath = BuiltInRegistries.ITEM.getKey(item.getItem()).getPath();
                ItemView.addInfoRecipe(new InfoClientRecipe(
                        Identifier.fromNamespaceAndPath(Bumblezone.MODID, itemPath + "_" + predicatePair.getFirst()),
                        SlotContent.of(item),
                        Component.translatable(Bumblezone.MODID + predicatePair.getFirst())));
            }
        }
    }

    private static void registerExtraRecipes(RecipeHolder<?> baseRecipe, List<ReliableClientRecipe> recipeList, boolean oneRecipeOnly) {
        ClientRecipeManager.INSTANCE.getRecipesForType(RecipeType.CRAFTING)
            .forEach(_ -> {
                if (baseRecipe.value() instanceof PotionCandleRecipe potionCandleRecipe) {
                    List<ShapedRecipe> extraRecipes = FakePotionCandleRecipeCreator.constructFakeRecipes(potionCandleRecipe, oneRecipeOnly);
                    for (int i = 0; i < extraRecipes.size(); i++) {
                        Identifier identifier =  Identifier.fromNamespaceAndPath(baseRecipe.id().identifier().getNamespace(), baseRecipe.id().identifier().getPath() + "_" + i);
                        ShapedRecipe shapedRecipe = extraRecipes.get(i);

                        HashMap<Integer, SlotContent> ingredients = new HashMap<>();
                        int ingredientIndex = 0;
                        for (int y = 0; y < 3; y++) {
                            for (int x = 0; x < 3; x++) {

                                if (x >= shapedRecipe.getWidth() || y >= shapedRecipe.getHeight()) {
                                    continue;
                                }

                                if (shapedRecipe.getIngredients().get(ingredientIndex).isPresent())
                                    ingredients.put(x + y * 3, SlotContent.of(shapedRecipe.getIngredients().get(ingredientIndex).get()));

                                ingredientIndex++;
                            }
                        }
                        recipeList.add(new CraftingClientRecipe
                                .Builder(identifier, ingredients)
                                .setSize(shapedRecipe.getWidth(), shapedRecipe.getHeight())
                                .setResult(shapedRecipe.result)
                                .build()
                        );
                    }
                }
            });
    }

    record TagData(int listSize, List<ItemStack> rrvItems, Ingredient rrvIngredient){}

}
