package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.TradeEntryReducedObj;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei.QueenRandomizeTradesJEICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei.JEIQueenRandomizerTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei.JEIQueenTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei.QueenTradesJEICategory;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JEIIntegration implements IModPlugin {

	public static final RecipeType<JEIQueenTradesInfo> QUEEN_TRADES = RecipeType.create(Bumblezone.MODID, "queen_trades", JEIQueenTradesInfo.class);
	public static final RecipeType<JEIQueenRandomizerTradesInfo> QUEEN_RANDOMIZE_TRADES = RecipeType.create(Bumblezone.MODID, "queen_color_randomizer_trades", JEIQueenRandomizerTradesInfo.class);

	@Override
    public ResourceLocation getPluginUid() {
		return new ResourceLocation(Bumblezone.MODID, "jei_plugin");
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
      if (ModList.get().isLoaded("roughlyenoughitems")) {
        return;
      }

      BzItems.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(registration, item.get()));
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
		if (!QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.isEmpty()) {
			for (Map.Entry<Item, WeightedRandomList<TradeEntryReducedObj>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.entrySet()) {
				for (TradeEntryReducedObj tradeResult : trade.getValue().unwrap()) {
					if (!tradeResult.randomizerTrade()) {
						trades.add(new JEIQueenTradesInfo(trade.getKey().getDefaultInstance(), tradeResult.items().stream().map(e -> new ItemStack(e, tradeResult.count())).toList(), tradeResult.xpReward(), tradeResult.weight(), tradeResult.totalGroupWeight()));
					}
				}
			}
		}
		registration.addRecipes(QUEEN_TRADES, trades);

		List<JEIQueenRandomizerTradesInfo> randomizerTrades = new LinkedList<>();
		if (!QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.isEmpty()) {
			for (TradeEntryReducedObj tradeEntry : QueensTradeManager.QUEENS_TRADE_MANAGER.tradeRandomizer) {
				List<ItemStack> randomizeStack = tradeEntry.items().stream().map(Item::getDefaultInstance).toList();
				for (ItemStack input : randomizeStack) {
					randomizerTrades.add(new JEIQueenRandomizerTradesInfo(input, randomizeStack, 1, randomizeStack.size()));
				}
			}
		}
		registration.addRecipes(QUEEN_RANDOMIZE_TRADES, randomizerTrades);
	}

    private static void addInfo(IRecipeRegistration registration, Item item) {
	registration.addIngredientInfo(
		new ItemStack(item),
		VanillaTypes.ITEM_STACK,
		Component.translatable(Bumblezone.MODID + "." + ForgeRegistries.ITEMS.getKey(item).getPath() + ".jei_description"));
    }
    
    private static void addInfo(IRecipeRegistration registration, Fluid fluid) {
	registration.addIngredientInfo(
		new FluidStack(fluid, 1),
		ForgeTypes.FLUID_STACK,
		Component.translatable(Bumblezone.MODID + "." + ForgeRegistries.FLUIDS.getKey(fluid).getPath() + ".jei_description"));
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
