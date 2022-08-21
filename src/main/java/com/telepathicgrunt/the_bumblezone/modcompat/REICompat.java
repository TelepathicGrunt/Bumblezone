package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.Fluid;
import org.quiltmc.loader.api.QuiltLoader;

import java.util.ArrayList;
import java.util.List;

public class REICompat implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        if (QuiltLoader.isModLoaded("jei")) {
            return;
        }

        addInfo(BzItems.EMPTY_HONEYCOMB_BROOD);
        addInfo(BzItems.FILLED_POROUS_HONEYCOMB);
        addInfo(BzItems.HONEY_CRYSTAL);
        addInfo(BzItems.HONEY_CRYSTAL_SHARDS);
        addInfo(BzItems.HONEY_CRYSTAL_SHIELD);
        addInfo(BzItems.HONEYCOMB_BROOD);
        addInfo(BzItems.POROUS_HONEYCOMB);
        addInfo(BzItems.STICKY_HONEY_REDSTONE);
        addInfo(BzItems.STICKY_HONEY_RESIDUE);
        addInfo(BzItems.SUGAR_INFUSED_COBBLESTONE);
        addInfo(BzItems.SUGAR_INFUSED_STONE);
        addInfo(BzItems.SUGAR_WATER_BOTTLE);
        addInfo(BzItems.SUGAR_WATER_BUCKET);
        addInfo(BzItems.BEEHIVE_BEESWAX);
        addInfo(BzItems.HONEY_SLIME_SPAWN_EGG);
        addInfo(BzItems.BEEHEMOTH_SPAWN_EGG);
        addInfo(BzItems.BEE_QUEEN_SPAWN_EGG);
        addInfo(BzFluids.SUGAR_WATER_FLUID);
        addInfo(BzFluids.ROYAL_JELLY_FLUID);
        addInfo(BzItems.ROYAL_JELLY_BOTTLE);
        addInfo(BzItems.ROYAL_JELLY_BUCKET);
        addInfo(BzItems.ROYAL_JELLY_BLOCK);
        addInfo(BzItems.POLLEN_PUFF);
        addInfo(BzItems.BEE_BREAD);
        addInfo(BzFluids.HONEY_FLUID);
        addInfo(BzItems.HONEY_BUCKET);
        addInfo(BzItems.HONEY_WEB);
        addInfo(BzItems.REDSTONE_HONEY_WEB);
        addInfo(BzItems.HONEY_COCOON);
        addInfo(BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
        addInfo(BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
        addInfo(BzItems.STINGER_SPEAR);
        addInfo(BzItems.HONEY_COMPASS);
        addInfo(BzItems.BEE_STINGER);
        addInfo(BzItems.BEE_CANNON);
        addInfo(BzItems.CRYSTAL_CANNON);
        addInfo(BzItems.HONEY_BEE_LEGGINGS_1);
        addInfo(BzItems.HONEY_BEE_LEGGINGS_2);
        addInfo(BzItems.BUMBLE_BEE_CHESTPLATE_1);
        addInfo(BzItems.BUMBLE_BEE_CHESTPLATE_2);
        addInfo(BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1);
        addInfo(BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2);
        addInfo(BzItems.STINGLESS_BEE_HELMET_1);
        addInfo(BzItems.STINGLESS_BEE_HELMET_2);
        addInfo(BzItems.CARPENTER_BEE_BOOTS_1);
        addInfo(BzItems.CARPENTER_BEE_BOOTS_2);
        addInfo(BzItems.ESSENCE_OF_THE_BEES);
        addInfo(BzItems.GLISTERING_HONEY_CRYSTAL);
        addInfo(BzItems.SUPER_CANDLE);
        addInfo(BzItems.SUPER_CANDLE_BLACK);
        addInfo(BzItems.SUPER_CANDLE_BLUE);
        addInfo(BzItems.SUPER_CANDLE_BROWN);
        addInfo(BzItems.SUPER_CANDLE_CYAN);
        addInfo(BzItems.SUPER_CANDLE_GRAY);
        addInfo(BzItems.SUPER_CANDLE_GREEN);
        addInfo(BzItems.SUPER_CANDLE_LIGHT_BLUE);
        addInfo(BzItems.SUPER_CANDLE_LIGHT_GRAY);
        addInfo(BzItems.SUPER_CANDLE_LIME);
        addInfo(BzItems.SUPER_CANDLE_MAGENTA);
        addInfo(BzItems.SUPER_CANDLE_ORANGE);
        addInfo(BzItems.SUPER_CANDLE_PINK);
        addInfo(BzItems.SUPER_CANDLE_PURPLE);
        addInfo(BzItems.SUPER_CANDLE_RED);
        addInfo(BzItems.SUPER_CANDLE_WHITE);
        addInfo(BzItems.SUPER_CANDLE_YELLOW);
        addInfo(BzItems.INCENSE_CANDLE);

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return;
        level.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry));
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

    private static void registerExtraRecipes(Recipe<?> baseRecipe, DisplayRegistry registry) {
        if (baseRecipe instanceof IncenseCandleRecipe incenseCandleRecipe) {
            List<CraftingRecipe> extraRecipes = new ArrayList<>();
            int currentRecipe = 0;
            for (Potion potion : Registry.POTION) {
                addRecipeIfValid(extraRecipes, FakeIncenseCandleRecipeCreator.getFakeShapedRecipe(incenseCandleRecipe, potion, Items.POTION.getDefaultInstance(), currentRecipe));
                currentRecipe++;
                addRecipeIfValid(extraRecipes, FakeIncenseCandleRecipeCreator.getFakeShapedRecipe(incenseCandleRecipe, potion, Items.SPLASH_POTION.getDefaultInstance(), currentRecipe));
                currentRecipe++;
                addRecipeIfValid(extraRecipes, FakeIncenseCandleRecipeCreator.getFakeShapedRecipe(incenseCandleRecipe, potion, Items.LINGERING_POTION.getDefaultInstance(), currentRecipe));
                currentRecipe++;
            }
            registry.add(extraRecipes);
        }
    }

    private static void addRecipeIfValid(List<CraftingRecipe> extraRecipes, ShapedRecipe recipe) {
        if (!recipe.getResultItem().isEmpty()) {
            extraRecipes.add(recipe);
        }
    }
}
