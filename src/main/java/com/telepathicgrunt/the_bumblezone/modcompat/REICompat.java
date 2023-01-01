package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

@REIPluginClient
public class REICompat implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        BzItems.CUSTOM_CREATIVE_TAB_ITEMS.forEach(item -> addInfo(item.get()));
        addInfo(BzItems.PILE_OF_POLLEN.get());
        addInfo(BzFluids.SUGAR_WATER_FLUID.get());
        addInfo(BzFluids.ROYAL_JELLY_FLUID.get());
        addInfo(BzFluids.HONEY_FLUID.get());

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle_from_super_candles"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, false));
    }

    private static void addInfo(Item item) {
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(item),
                Component.translatable(Registry.ITEM.getKey(item).toString()),
                (text) -> {
                    text.add(Component.translatable(Bumblezone.MODID + "." + Registry.ITEM.getKey(item).getPath() + ".jei_description"));
                    return text;
                });
    }

    private static void addInfo(Fluid fluid) {
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(fluid, 1),
                Component.translatable(Registry.FLUID.getKey(fluid).toString()),
                (text) -> {
                    text.add(Component.translatable(Bumblezone.MODID + "." + Registry.FLUID.getKey(fluid).getPath() + ".jei_description"));
                    return text;
                });
    }

    private static void registerExtraRecipes(Recipe<?> baseRecipe, DisplayRegistry registry, boolean oneRecipeOnly) {
        if (baseRecipe instanceof IncenseCandleRecipe incenseCandleRecipe) {
            List<CraftingRecipe> extraRecipes = FakeIncenseCandleRecipeCreator.constructFakeRecipes(incenseCandleRecipe, oneRecipeOnly);
            extraRecipes.forEach(registry::add);
        }
    }
}