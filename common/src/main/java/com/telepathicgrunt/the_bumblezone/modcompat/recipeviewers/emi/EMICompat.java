package com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.emi;

// TODO: Re-enable when EMI updates
//@EmiEntrypoint
//public class EMICompat implements EmiPlugin {
//    public static final EmiStack WORKSTATION = EmiStack.of(BzItems.BEE_QUEEN_SPAWN_EGG.get());
//    public static final EmiRecipeCategory QUEEN_TRADES = new QueenTradesEMICategory();
//    public static final EmiRecipeCategory QUEEN_RANDOMIZE_TRADES = new QueenRandomizerTradesEMICategory();
//
//    @Override
//    public void register(EmiRegistry registry) {
//        BzCreativeTabs.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(registry, item.get()));
//        addInfo(registry, BzFluids.SUGAR_WATER_FLUID.get());
//        addInfo(registry, BzFluids.ROYAL_JELLY_FLUID.get());
//        if (BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid.isEmpty()) {
//            addInfo(registry, BzFluids.HONEY_FLUID.get());
//        }
//
//        registry.getRecipeManager().byKey(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "potion_candle/from_super_candles"))
//                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));
//
//        registry.getRecipeManager().byKey(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "potion_candle/from_string_and_carvable_wax"))
//                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, false));
//
//        registry.addCategory(QUEEN_TRADES);
//        registry.addCategory(QUEEN_RANDOMIZE_TRADES);
//
//        registry.addWorkstation(QUEEN_TRADES, WORKSTATION);
//        registry.addWorkstation(QUEEN_RANDOMIZE_TRADES, WORKSTATION);
//
//        long recipeId = 1;
//        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades.isEmpty()) {
//            for (Pair<MainTradeRowInput, WeightedList<WeightedTradeResult>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades) {
//                for (WeightedTradeResult weightedTradeResult : trade.getSecond().unwrap()) {
//                    List<EmiStack> rewardCollection = weightedTradeResult.getItems().stream().map(EmiStack::of).toList();
//                    registry.addRecipe(new EMIQueenTradesInfo(
//                            EmiIngredient.of(trade.getFirst().tagKey().isPresent() ? Ingredient.of(trade.getFirst().tagKey().get()) : Ingredient.of(trade.getFirst().item())),
//                            trade.getFirst().tagKey().orElse(null),
//                            rewardCollection,
//                            weightedTradeResult.tagKey.orElse(null),
//                            weightedTradeResult.xpReward,
//                            weightedTradeResult.weight,
//                            weightedTradeResult.getTotalWeight(),
//                            Identifier.fromNamespaceAndPath(Bumblezone.MODID, "" + recipeId)));
//                    recipeId++;
//                }
//            }
//        }
//
//        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades.isEmpty()) {
//            Map<TagKey<Item>, TagData> cacheEmiData = new Object2ObjectOpenHashMap<>();
//
//            for (RandomizeTradeRowInput tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades) {
//                TagKey<Item> itemTagKey = tradeEntry.tagKey().get();
//                TagData tagData = cacheEmiData.getOrDefault(itemTagKey, null);
//                if (tagData == null) {
//                    List<ItemStack> randomizeStack = tradeEntry.getWantItems().stream().map(e -> e.value().getDefaultInstance()).toList();
//                    List<EmiStack> emiStackList = randomizeStack.stream().map(EmiStack::of).collect(Collectors.toList());
//                    EmiIngredient emiIngredient = EmiIngredient.of(itemTagKey);
//
//                    tagData = new TagData(randomizeStack.size(), emiStackList, emiIngredient);
//                    cacheEmiData.put(itemTagKey, tagData);
//                }
//
//                registry.addRecipe(new EMIQueenRandomizerTradesInfo(
//                        EmiIngredient.of(Ingredient.of(itemTagKey)),
//                        true,
//                        tagData.emiStack(),
//                        itemTagKey,
//                        tagData.emiIngredient(),
//                        1,
//                        tagData.listSize(),
//                        Identifier.fromNamespaceAndPath(Bumblezone.MODID, "" + recipeId)));
//                recipeId++;
//            }
//        }
//
//        List<ItemStack> hangingGardensFlowers = GeneralUtils.convertBlockTagsToItemStacks(BzTags.HANGING_GARDEN_ALLOWED_FLOWERS_BLOCKS, BzTags.HANGING_GARDEN_FORCED_DISALLOWED_FLOWERS_BLOCKS);
//        hangingGardensFlowers.addAll(GeneralUtils.convertBlockTagsToItemStacks(BzTags.HANGING_GARDEN_ALLOWED_TALL_FLOWERS_BLOCKS, BzTags.HANGING_GARDEN_FORCED_DISALLOWED_TALL_FLOWERS_BLOCKS));
//
//        addComplexBlockTagInfo(registry,
//                Pair.of(".hanging_gardens_flowers.description", hangingGardensFlowers),
//                Pair.of(".crystalline_flower_can_be_placed_on.description",
//                        GeneralUtils.convertBlockTagsToItemStacks(BzTags.CRYSTALLINE_FLOWER_CAN_SURVIVE_ON, null))
//        );
//    }
//
//    @SafeVarargs
//    private static void addComplexBlockTagInfo(@NotNull EmiRegistry registry, Pair<String, List<ItemStack>>... structureInfo) {
//        for (Pair<String, List<ItemStack>> predicatePair : structureInfo) {
//            registry.addRecipe(new EmiInfoRecipe(
//                    List.of(EmiIngredient.of(Ingredient.of(predicatePair.getSecond().stream()))),
//                    List.of(Component.translatable(Bumblezone.MODID + predicatePair.getFirst())),
//                    Identifier.fromNamespaceAndPath(Bumblezone.MODID, predicatePair.getFirst())
//            ));
//        }
//    }
//
//    record TagData(int listSize, List<EmiStack> emiStack, EmiIngredient emiIngredient){}
//
//    private static void registerExtraRecipes(RecipeHolder<?> baseRecipe, EmiRegistry registry, boolean oneRecipeOnly) {
//        if (baseRecipe.value() instanceof PotionCandleRecipe potionCandleRecipe) {
//            List<CraftingRecipe> extraRecipes = FakePotionCandleRecipeCreator.constructFakeRecipes(potionCandleRecipe, oneRecipeOnly);
//            for (int i = 0 ; i < extraRecipes.size(); i++) {
//                CraftingRecipe craftingRecipe = extraRecipes.get(i);
//                registry.addRecipe(
//                        new EmiCraftingRecipe(
//                                craftingRecipe.getIngredients().stream().map(EmiIngredient::of).toList(),
//                                EmiStack.of(craftingRecipe.getResultItem(RegistryAccess.EMPTY)),
//                                Identifier.fromNamespaceAndPath(baseRecipe.id().getNamespace(), baseRecipe.id().getPath() + "_" + i),
//                                false));
//            }
//        }
//    }
//
//    private static void addInfo(EmiRegistry registry, Item item) {
//        registry.addRecipe(new EmiInfoRecipe(
//                List.of(EmiIngredient.of(Ingredient.of(new ItemStack(item)))),
//                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".description")),
//                Identifier.fromNamespaceAndPath(Bumblezone.MODID, BuiltInRegistries.ITEM.getKey(item).getPath() + "_info")
//        ));
//    }
//
//    private static void addInfo(EmiRegistry registry, Fluid fluid) {
//        registry.addRecipe(new EmiInfoRecipe(
//                List.of(EmiStack.of(fluid)),
//                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".description")),
//                Identifier.fromNamespaceAndPath(Bumblezone.MODID, BuiltInRegistries.FLUID.getKey(fluid).getPath() + "_info")
//        ));
//    }
//}