package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public class EMICompat implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        addInfo(registry, BzItems.EMPTY_HONEYCOMB_BROOD);
        addInfo(registry, BzItems.FILLED_POROUS_HONEYCOMB);
        addInfo(registry, BzItems.HONEY_CRYSTAL);
        addInfo(registry, BzItems.HONEY_CRYSTAL_SHARDS);
        addInfo(registry, BzItems.HONEY_CRYSTAL_SHIELD);
        addInfo(registry, BzItems.HONEYCOMB_BROOD);
        addInfo(registry, BzItems.POROUS_HONEYCOMB);
        addInfo(registry, BzItems.STICKY_HONEY_REDSTONE);
        addInfo(registry, BzItems.STICKY_HONEY_RESIDUE);
        addInfo(registry, BzItems.SUGAR_INFUSED_COBBLESTONE);
        addInfo(registry, BzItems.SUGAR_INFUSED_STONE);
        addInfo(registry, BzFluids.SUGAR_WATER_FLUID);
        addInfo(registry, BzItems.SUGAR_WATER_BOTTLE);
        addInfo(registry, BzItems.SUGAR_WATER_BUCKET);
        addInfo(registry, BzItems.BEEHIVE_BEESWAX);
        addInfo(registry, BzItems.HONEY_SLIME_SPAWN_EGG);
        addInfo(registry, BzItems.BEEHEMOTH_SPAWN_EGG);
        addInfo(registry, BzItems.BEE_QUEEN_SPAWN_EGG);
        addInfo(registry, BzFluids.ROYAL_JELLY_FLUID);
        addInfo(registry, BzItems.ROYAL_JELLY_BOTTLE);
        addInfo(registry, BzItems.ROYAL_JELLY_BUCKET);
        addInfo(registry, BzItems.ROYAL_JELLY_BLOCK);
        addInfo(registry, BzItems.POLLEN_PUFF);
        addInfo(registry, BzItems.BEE_BREAD);
        addInfo(registry, BzItems.HONEY_BUCKET);
        addInfo(registry, BzFluids.HONEY_FLUID);
        addInfo(registry, BzItems.HONEY_WEB);
        addInfo(registry, BzItems.REDSTONE_HONEY_WEB);
        addInfo(registry, BzItems.HONEY_COCOON);
        addInfo(registry, BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
        addInfo(registry, BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
        addInfo(registry, BzItems.MUSIC_DISC_LA_BEE_DA_LOCA);
        addInfo(registry, BzItems.MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES);
        addInfo(registry, BzItems.STINGER_SPEAR);
        addInfo(registry, BzItems.HONEY_COMPASS);
        addInfo(registry, BzItems.BEE_STINGER);
        addInfo(registry, BzItems.BEE_CANNON);
        addInfo(registry, BzItems.CRYSTAL_CANNON);
        addInfo(registry, BzItems.HONEY_BEE_LEGGINGS_1);
        addInfo(registry, BzItems.HONEY_BEE_LEGGINGS_2);
        addInfo(registry, BzItems.BUMBLE_BEE_CHESTPLATE_1);
        addInfo(registry, BzItems.BUMBLE_BEE_CHESTPLATE_2);
        addInfo(registry, BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1);
        addInfo(registry, BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2);
        addInfo(registry, BzItems.STINGLESS_BEE_HELMET_1);
        addInfo(registry, BzItems.STINGLESS_BEE_HELMET_2);
        addInfo(registry, BzItems.CARPENTER_BEE_BOOTS_1);
        addInfo(registry, BzItems.CARPENTER_BEE_BOOTS_2);
        addInfo(registry, BzItems.ESSENCE_OF_THE_BEES);
        addInfo(registry, BzItems.GLISTERING_HONEY_CRYSTAL);
        addInfo(registry, BzItems.CARVABLE_WAX);
        addInfo(registry, BzItems.CARVABLE_WAX_WAVY);
        addInfo(registry, BzItems.CARVABLE_WAX_FLOWER);
        addInfo(registry, BzItems.CARVABLE_WAX_CHISELED);
        addInfo(registry, BzItems.CARVABLE_WAX_DIAMOND);
        addInfo(registry, BzItems.CARVABLE_WAX_BRICKS);
        addInfo(registry, BzItems.CARVABLE_WAX_CHAINS);
        addInfo(registry, BzItems.SUPER_CANDLE);
        addInfo(registry, BzItems.SUPER_CANDLE_BLACK);
        addInfo(registry, BzItems.SUPER_CANDLE_BLUE);
        addInfo(registry, BzItems.SUPER_CANDLE_BROWN);
        addInfo(registry, BzItems.SUPER_CANDLE_CYAN);
        addInfo(registry, BzItems.SUPER_CANDLE_GRAY);
        addInfo(registry, BzItems.SUPER_CANDLE_GREEN);
        addInfo(registry, BzItems.SUPER_CANDLE_LIGHT_BLUE);
        addInfo(registry, BzItems.SUPER_CANDLE_LIGHT_GRAY);
        addInfo(registry, BzItems.SUPER_CANDLE_LIME);
        addInfo(registry, BzItems.SUPER_CANDLE_MAGENTA);
        addInfo(registry, BzItems.SUPER_CANDLE_ORANGE);
        addInfo(registry, BzItems.SUPER_CANDLE_PINK);
        addInfo(registry, BzItems.SUPER_CANDLE_PURPLE);
        addInfo(registry, BzItems.SUPER_CANDLE_RED);
        addInfo(registry, BzItems.SUPER_CANDLE_WHITE);
        addInfo(registry, BzItems.SUPER_CANDLE_YELLOW);
        addInfo(registry, BzItems.INCENSE_CANDLE);
        addInfo(registry, BzItems.CRYSTALLINE_FLOWER);

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle_from_super_candles"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));
        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, false));
    }

    private static void registerExtraRecipes(Recipe<?> baseRecipe, EmiRegistry registry, boolean oneRecipeOnly) {
        if (baseRecipe instanceof IncenseCandleRecipe incenseCandleRecipe) {
            List<CraftingRecipe> extraRecipes = FakeIncenseCandleRecipeCreator.constructFakeRecipes(incenseCandleRecipe, oneRecipeOnly);
            extraRecipes.forEach(r -> registry.addRecipe(
                    new EmiCraftingRecipe(
                            r.getIngredients().stream().map(EmiIngredient::of).toList(),
                            EmiStack.of(r.getResultItem()),
                            r.getId(),
                            false)));
        }
    }
    
    private static void addInfo(EmiRegistry registry, Item item) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiIngredient.of(Ingredient.of(new ItemStack(item)))),
                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".jei_description")),
                new ResourceLocation(Bumblezone.MODID, BuiltInRegistries.ITEM.getKey(item).getPath() + "_info")
        ));
    }

    private static void addInfo(EmiRegistry registry, Fluid fluid) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiStack.of(fluid)),
                List.of(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".jei_description")),
                new ResourceLocation(Bumblezone.MODID, BuiltInRegistries.FLUID.getKey(fluid).getPath() + "_info")
        ));
    }
}
