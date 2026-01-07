package com.telepathicgrunt.the_bumblezone.modcompat;

// TODO: Re-enable when REI updates
//public class REICompat implements REIClientPlugin {
//
//    public static final CategoryIdentifier<REIQueenTradesInfo> QUEEN_TRADES = CategoryIdentifier.of(Bumblezone.MODID, "queen_trades");
//    public static final CategoryIdentifier<REIQueenRandomizerTradesInfo> QUEEN_RANDOMIZE_TRADES = CategoryIdentifier.of(Bumblezone.MODID, "queen_color_randomizer_trades");
//
//    @Override
//    public void registerDisplays(DisplayRegistry registry) {
//        BzCreativeTabs.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(item.get()));
//        addInfo(BzFluids.SUGAR_WATER_FLUID.get());
//        addInfo(BzFluids.ROYAL_JELLY_FLUID.get());
//        if (BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid.isEmpty()) {
//            addInfo(BzFluids.HONEY_FLUID.get());
//        }
//
//        registry.getRecipeManager().byKey(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "potion_candle/from_super_candles"))
//                .ifPresent(recipe -> registerExtraRecipes(recipe.value(), registry, true));
//
//        registry.getRecipeManager().byKey(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "potion_candle/from_string_and_carvable_wax"))
//                .ifPresent(recipe -> registerExtraRecipes(recipe.value(), registry, false));
//
//        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades.isEmpty()) {
//            for (Pair<MainTradeRowInput, WeightedList<WeightedTradeResult>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades) {
//                for (WeightedTradeResult weightedTradeResult : trade.getSecond().unwrap()) {
//                    List<ItemStack> rewardCollection = weightedTradeResult.getItems();
//                    registry.add(new REIQueenTradesInfo(
//                            trade.getFirst().tagKey().isPresent() ? EntryIngredients.ofItemTag(trade.getFirst().tagKey().get()) : EntryIngredients.of(trade.getFirst().item()),
//                            trade.getFirst().tagKey().orElse(null),
//                            EntryIngredients.ofItemStacks(rewardCollection),
//                            weightedTradeResult.tagKey.orElse(null),
//                            weightedTradeResult.xpReward,
//                            weightedTradeResult.weight,
//                            weightedTradeResult.getTotalWeight()
//                    ), QUEEN_TRADES);
//                }
//            }
//        }
//
//        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades.isEmpty()) {
//            Map<TagKey<Item>, TagData> cacheReiData = new Object2ObjectOpenHashMap<>();
//
//            for (RandomizeTradeRowInput tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades) {
//                TagKey<Item> itemTagKey = tradeEntry.tagKey().get();
//                TagData tagData = cacheReiData.getOrDefault(itemTagKey, null);
//                if (tagData == null) {
//                    List<ItemStack> randomizeStack = tradeEntry.getWantItems().stream().map(e -> e.value().getDefaultInstance()).toList();
//                    tagData = new TagData(randomizeStack.size(), Collections.singletonList(EntryIngredients.ofIngredient(Ingredient.of(itemTagKey))));
//                    cacheReiData.put(itemTagKey, tagData);
//                }
//
//                registry.add(new REIQueenRandomizerTradesInfo(
//                        tagData.reiIngredient(),
//                        tagData.reiIngredient(),
//                        itemTagKey,
//                        1,
//                        tagData.listSize()
//                ), QUEEN_RANDOMIZE_TRADES);
//            }
//        }
//
//        List<ItemStack> hangingGardensFlowers = GeneralUtils.convertBlockTagsToItemStacks(BzTags.HANGING_GARDEN_ALLOWED_FLOWERS_BLOCKS, BzTags.HANGING_GARDEN_FORCED_DISALLOWED_FLOWERS_BLOCKS);
//        hangingGardensFlowers.addAll(GeneralUtils.convertBlockTagsToItemStacks(BzTags.HANGING_GARDEN_ALLOWED_TALL_FLOWERS_BLOCKS, BzTags.HANGING_GARDEN_FORCED_DISALLOWED_TALL_FLOWERS_BLOCKS));
//
//        addComplexBlockTagInfo(
//                Pair.of(".hanging_gardens_flowers.description", hangingGardensFlowers),
//                Pair.of(".crystalline_flower_can_be_placed_on.description",
//                        GeneralUtils.convertBlockTagsToItemStacks(BzTags.CRYSTALLINE_FLOWER_CAN_SURVIVE_ON, null))
//        );
//    }
//
//    @SafeVarargs
//    private static void addComplexBlockTagInfo(Pair<String, List<ItemStack>>... structureInfo) {
//        for (Pair<String, List<ItemStack>> predicatePair : structureInfo) {
//            BuiltinClientPlugin.getInstance().registerInformation(
//                    EntryIngredient.of(predicatePair.getSecond().stream().map(EntryStacks::of).toList()),
//                    Component.translatable(predicatePair.toString()),
//                    (text) -> {
//                        text.add(Component.translatable(Bumblezone.MODID + predicatePair.getFirst()));
//                        return text;
//                    });
//        }
//    }
//
//    record TagData(int listSize, List<EntryIngredient> reiIngredient){}
//
//    private static void addInfo(Item item) {
//        BuiltinClientPlugin.getInstance().registerInformation(
//                EntryStacks.of(item),
//                Component.translatable(BuiltInRegistries.ITEM.getKey(item).toString()),
//                (text) -> {
//                    text.add(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".description"));
//                    return text;
//                });
//    }
//
//    private static void addInfo(Fluid fluid) {
//        BuiltinClientPlugin.getInstance().registerInformation(
//                EntryStacks.of(fluid, 1000),
//                Component.translatable(BuiltInRegistries.FLUID.getKey(fluid).toString()),
//                (text) -> {
//                    text.add(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".description"));
//                    return text;
//                });
//    }
//
//    private static void registerExtraRecipes(Recipe<?> baseRecipe, DisplayRegistry registry, boolean oneRecipeOnly) {
//        if (baseRecipe instanceof PotionCandleRecipe potionCandleRecipe) {
//            List<CraftingRecipe> extraRecipes = FakePotionCandleRecipeCreator.constructFakeRecipes(potionCandleRecipe, oneRecipeOnly);
//            extraRecipes.forEach(registry::add);
//        }
//    }
//
//    @Override
//    public void registerCategories(CategoryRegistry registry) {
//        registry.add(new QueenTradesREICategory());
//        registry.add(new QueenRandomizerTradesREICategory());
//
//        registry.addWorkstations(QUEEN_TRADES, EntryStacks.of(BzItems.BEE_QUEEN_SPAWN_EGG.get()));
//        registry.addWorkstations(QUEEN_RANDOMIZE_TRADES, EntryStacks.of(BzItems.BEE_QUEEN_SPAWN_EGG.get()));
//    }
//
//    @Override
//    public void registerScreens(ScreenRegistry registry) {
//        registry.registerDecider(new OverlayDecider() {
//            @Override
//            public <R extends Screen> boolean isHandingScreen(Class<R> screen) {
//                return true;
//            }
//
//            @Override
//            public <R extends Screen> InteractionResult shouldScreenBeOverlaid(R screen) {
//                return screen.getClass() == CrystallineFlowerScreen.class ?
//                        InteractionResult.FAIL : InteractionResult.PASS;
//            }
//        });
//    }
//}