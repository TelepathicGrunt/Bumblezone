package com.telepathicgrunt.the_bumblezone.modcompat;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.WeightedTradeResult;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei.JEIQueenRandomizerTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei.JEIQueenTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei.QueenRandomizeTradesJEICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei.QueenTradesJEICategory;
import com.telepathicgrunt.the_bumblezone.modinit.BzCreativeTabs;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

@JeiPlugin
public class JEIIntegration implements IModPlugin {

	public static final RecipeType<JEIQueenTradesInfo> QUEEN_TRADES = RecipeType.create(Bumblezone.MODID, "queen_trades", JEIQueenTradesInfo.class);
	public static final RecipeType<JEIQueenRandomizerTradesInfo> QUEEN_RANDOMIZE_TRADES = RecipeType.create(Bumblezone.MODID, "queen_color_randomizer_trades", JEIQueenRandomizerTradesInfo.class);

	@Override
    public ResourceLocation getPluginUid() {
		return new ResourceLocation(Bumblezone.MODID, "jei_plugin");
    }
    
    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
	  BzCreativeTabs.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(registration, item.get()));
      addInfo(registration, BzItems.PILE_OF_POLLEN.get());
      addInfo(registration, BzFluids.SUGAR_WATER_FLUID.get());
      addInfo(registration, BzFluids.ROYAL_JELLY_FLUID.get());
      addInfo(registration, BzFluids.HONEY_FLUID.get());

      ClientLevel level = Minecraft.getInstance().level;
		if (level == null)
			return;
		level.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle_from_super_candles"))
				.ifPresent(recipe -> registerExtraRecipes(recipe, registration, true));
		level.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
				.ifPresent(recipe -> registerExtraRecipes(recipe, registration, false));

		List<JEIQueenTradesInfo> trades = new LinkedList<>();
		if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades.isEmpty()) {
			for (Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades) {
				for (WeightedTradeResult weightedTradeResult : trade.getSecond().unwrap()) {
					trades.add(new JEIQueenTradesInfo(trade.getFirst(), weightedTradeResult));
				}
			}
		}
		registration.addRecipes(QUEEN_TRADES, trades);

		List<JEIQueenRandomizerTradesInfo> randomizerTrades = new LinkedList<>();
		if (!QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades.isEmpty()) {
			for (QueensTradeManager.TradeWantEntry tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades) {
				List<ItemStack> randomizeStack = tradeEntry.wantItems().stream().map(e -> e.value().getDefaultInstance()).toList();
				for (ItemStack input : randomizeStack) {
					randomizerTrades.add(new JEIQueenRandomizerTradesInfo(input, tradeEntry.tagKey(), randomizeStack));
				}
			}
		}
		registration.addRecipes(QUEEN_RANDOMIZE_TRADES, randomizerTrades);
	}

    private static void addInfo(IRecipeRegistration registration, Item item) {
	registration.addIngredientInfo(
		new ItemStack(item),
		VanillaTypes.ITEM_STACK,
		Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".description"));
	}

	private static void addInfo(IRecipeRegistration registration, Fluid fluid) {
		addFluidInfo(registration, fluid, registration.getJeiHelpers().getPlatformFluidHelper());
	}

	private static <T> void addFluidInfo(IRecipeRegistration registration, Fluid fluid, IPlatformFluidHelper<T> platformFluidHelper) {
		registration.addIngredientInfo(
				platformFluidHelper.create(fluid, 1),
				platformFluidHelper.getFluidIngredientType(),
				Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".description"));
	}

	private static void registerExtraRecipes(Recipe<?> baseRecipe, IRecipeRegistration registration, boolean oneRecipeOnly) {
		if (baseRecipe instanceof IncenseCandleRecipe incenseCandleRecipe) {
			List<CraftingRecipe> extraRecipes = FakeIncenseCandleRecipeCreator.constructFakeRecipes(incenseCandleRecipe, oneRecipeOnly);
			registration.addRecipes(RecipeTypes.CRAFTING, extraRecipes);
		}
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new QueenTradesJEICategory(registration.getJeiHelpers().getGuiHelper()));
		registration.addRecipeCategories(new QueenRandomizeTradesJEICategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(BzItems.BEE_QUEEN_SPAWN_EGG.get().getDefaultInstance(), QUEEN_TRADES);
		registration.addRecipeCatalyst(BzItems.BEE_QUEEN_SPAWN_EGG.get().getDefaultInstance(), QUEEN_RANDOMIZE_TRADES);
	}
}
