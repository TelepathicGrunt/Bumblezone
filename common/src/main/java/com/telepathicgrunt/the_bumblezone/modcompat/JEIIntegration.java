package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzCreativeTabs;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Bumblezone.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (PlatformHooks.isModLoaded("roughlyenoughitems")) {
            return;
        }

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
}
