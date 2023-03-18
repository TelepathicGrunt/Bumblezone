package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.TradeEntryReducedObj;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.QueenRandomizeTradesJEICategory;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.QueenTradesInfo;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.QueenTradesJEICategory;
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
import java.util.Map;

@JeiPlugin
public class JEIIntegration implements IModPlugin {

    public static final RecipeType<QueenTradesInfo> QUEEN_TRADES = RecipeType.create(Bumblezone.MODID, "queen_trades", QueenTradesInfo.class);
    public static final RecipeType<QueenTradesInfo> QUEEN_RANDOMIZE_TRADES = RecipeType.create(Bumblezone.MODID, "queen_color_randomizer_trades", QueenTradesInfo.class);

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

        List<QueenTradesInfo> trades = new LinkedList<>();
        List<QueenTradesInfo> randomizerTrades = new LinkedList<>();
        if (!QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.isEmpty()) {
            for (Map.Entry<Item, WeightedRandomList<TradeEntryReducedObj>> trade : QueensTradeManager.QUEENS_TRADE_MANAGER.tradeReduced.entrySet()) {
                for (TradeEntryReducedObj tradeResult : trade.getValue().unwrap()) {
                    if (tradeResult.randomizerTrade()) {
                        randomizerTrades.add(new QueenTradesInfo(trade.getKey().getDefaultInstance(), new ItemStack(tradeResult.item(), tradeResult.count()), tradeResult.xpReward(), tradeResult.weight(), tradeResult.totalGroupWeight()));
                    }
                    else {
                        trades.add(new QueenTradesInfo(trade.getKey().getDefaultInstance(), new ItemStack(tradeResult.item(), tradeResult.count()), tradeResult.xpReward(), tradeResult.weight(), tradeResult.totalGroupWeight()));
                    }
                }
            }
        }
        registration.addRecipes(QUEEN_TRADES, trades);
        registration.addRecipes(QUEEN_RANDOMIZE_TRADES, randomizerTrades);
    }

    private static void addInfo(IRecipeRegistration registration, Item item) {
        registration.addIngredientInfo(
                new ItemStack(item),
                VanillaTypes.ITEM_STACK,
                Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".jei_description"));
    }

    private static void addInfo(IRecipeRegistration registration, Fluid fluid) {
        addFluidInfo(registration, fluid, registration.getJeiHelpers().getPlatformFluidHelper());
    }

    private static <T> void addFluidInfo(IRecipeRegistration registration, Fluid fluid, IPlatformFluidHelper<T> platformFluidHelper) {
        registration.addIngredientInfo(
                platformFluidHelper.create(fluid, 1),
                platformFluidHelper.getFluidIngredientType(),
                Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".jei_description"));
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
