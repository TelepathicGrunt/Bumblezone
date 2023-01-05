package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
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
}
