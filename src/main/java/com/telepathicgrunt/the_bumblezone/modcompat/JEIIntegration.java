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
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.fabric.constants.FabricTypes;
import mezz.jei.api.fabric.ingredients.fluids.IJeiFluidIngredient;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        BzItems.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(registration, item));
        addInfo(registration, BzItems.PILE_OF_POLLEN);
        addInfo(registration, BzFluids.SUGAR_WATER_FLUID);
        addInfo(registration, BzFluids.ROYAL_JELLY_FLUID);
        addInfo(registration, BzFluids.HONEY_FLUID);

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
                Component.translatable(Bumblezone.MODID + "." + Registry.ITEM.getKey(item).getPath() + ".jei_description"));
    }

    private static void addInfo(IRecipeRegistration registration, Fluid fluid) {
        registration.addIngredientInfo(
                new FluidStack(fluid, 1),
                FabricTypes.FLUID_STACK,
                Component.translatable(Bumblezone.MODID + "." + Registry.FLUID.getKey(fluid).getPath() + ".jei_description"));
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
        registration.addRecipeCatalyst(BzItems.BEE_QUEEN_SPAWN_EGG.getDefaultInstance(), QUEEN_TRADES);
        registration.addRecipeCatalyst(BzItems.BEE_QUEEN_SPAWN_EGG.getDefaultInstance(), QUEEN_RANDOMIZE_TRADES);
    }

    private static class FluidStack implements IJeiFluidIngredient {

        private final Fluid fluid;
        private final int count;

        private FluidStack(Fluid fluid, int count) {
            this.fluid = fluid;
            this.count = count;
        }


        @Override
        public Fluid getFluid() {
            return fluid;
        }

        @Override
        public long getAmount() {
            return count;
        }

        @Override
        public Optional<CompoundTag> getTag() {
            return Optional.empty();
        }
    }
}
