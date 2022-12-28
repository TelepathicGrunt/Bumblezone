package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPlugin;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;

import java.util.List;

@REIPlugin(Dist.CLIENT)
public class REICompat implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        addInfo(BzItems.EMPTY_HONEYCOMB_BROOD.get());
        addInfo(BzItems.FILLED_POROUS_HONEYCOMB.get());
        addInfo(BzItems.HONEY_CRYSTAL.get());
        addInfo(BzItems.HONEY_CRYSTAL_SHARDS.get());
        addInfo(BzItems.HONEY_CRYSTAL_SHIELD.get());
        addInfo(BzItems.HONEYCOMB_BROOD.get());
        addInfo(BzItems.POROUS_HONEYCOMB.get());
        addInfo(BzItems.STICKY_HONEY_REDSTONE.get());
        addInfo(BzItems.STICKY_HONEY_RESIDUE.get());
        addInfo(BzItems.SUGAR_INFUSED_COBBLESTONE.get());
        addInfo(BzItems.SUGAR_INFUSED_STONE.get());
        addInfo(BzItems.SUGAR_WATER_BOTTLE.get());
        addInfo(BzItems.SUGAR_WATER_BUCKET.get());
        addInfo(BzItems.BEEHIVE_BEESWAX.get());
        addInfo(BzItems.HONEY_SLIME_SPAWN_EGG.get());
        addInfo(BzItems.BEEHEMOTH_SPAWN_EGG.get());
        addInfo(BzItems.BEE_QUEEN_SPAWN_EGG.get());
        addInfo(BzFluids.SUGAR_WATER_FLUID.get());
        addInfo(BzFluids.ROYAL_JELLY_FLUID.get());
        addInfo(BzItems.ROYAL_JELLY_BOTTLE.get());
        addInfo(BzItems.ROYAL_JELLY_BUCKET.get());
        addInfo(BzItems.ROYAL_JELLY_BLOCK.get());
        addInfo(BzItems.POLLEN_PUFF.get());
        addInfo(BzItems.BEE_BREAD.get());
        addInfo(BzFluids.HONEY_FLUID.get());
        addInfo(BzItems.HONEY_BUCKET.get());
        addInfo(BzItems.HONEY_WEB.get());
        addInfo(BzItems.REDSTONE_HONEY_WEB.get());
        addInfo(BzItems.HONEY_COCOON.get());
        addInfo(BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV.get());
        addInfo(BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY.get());
        addInfo(BzItems.MUSIC_DISC_LA_BEE_DA_LOCA.get());
        addInfo(BzItems.MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES.get());
        addInfo(BzItems.STINGER_SPEAR.get());
        addInfo(BzItems.HONEY_COMPASS.get());
        addInfo(BzItems.BEE_STINGER.get());
        addInfo(BzItems.BEE_CANNON.get());
        addInfo(BzItems.CRYSTAL_CANNON.get());
        addInfo(BzItems.HONEY_BEE_LEGGINGS_1.get());
        addInfo(BzItems.HONEY_BEE_LEGGINGS_2.get());
        addInfo(BzItems.BUMBLE_BEE_CHESTPLATE_1.get());
        addInfo(BzItems.BUMBLE_BEE_CHESTPLATE_2.get());
        addInfo(BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1.get());
        addInfo(BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2.get());
        addInfo(BzItems.STINGLESS_BEE_HELMET_1.get());
        addInfo(BzItems.STINGLESS_BEE_HELMET_2.get());
        addInfo(BzItems.CARPENTER_BEE_BOOTS_1.get());
        addInfo(BzItems.CARPENTER_BEE_BOOTS_2.get());
        addInfo(BzItems.ESSENCE_OF_THE_BEES.get());
        addInfo(BzItems.GLISTERING_HONEY_CRYSTAL.get());
        addInfo(BzItems.CARVABLE_WAX.get());
        addInfo(BzItems.CARVABLE_WAX_WAVY.get());
        addInfo(BzItems.CARVABLE_WAX_FLOWER.get());
        addInfo(BzItems.CARVABLE_WAX_CHISELED.get());
        addInfo(BzItems.CARVABLE_WAX_DIAMOND.get());
        addInfo(BzItems.CARVABLE_WAX_BRICKS.get());
        addInfo(BzItems.CARVABLE_WAX_CHAINS.get());
        addInfo(BzItems.SUPER_CANDLE.get());
        addInfo(BzItems.SUPER_CANDLE_BLACK.get());
        addInfo(BzItems.SUPER_CANDLE_BLUE.get());
        addInfo(BzItems.SUPER_CANDLE_BROWN.get());
        addInfo(BzItems.SUPER_CANDLE_CYAN.get());
        addInfo(BzItems.SUPER_CANDLE_GRAY.get());
        addInfo(BzItems.SUPER_CANDLE_GREEN.get());
        addInfo(BzItems.SUPER_CANDLE_LIGHT_BLUE.get());
        addInfo(BzItems.SUPER_CANDLE_LIGHT_GRAY.get());
        addInfo(BzItems.SUPER_CANDLE_LIME.get());
        addInfo(BzItems.SUPER_CANDLE_MAGENTA.get());
        addInfo(BzItems.SUPER_CANDLE_ORANGE.get());
        addInfo(BzItems.SUPER_CANDLE_PINK.get());
        addInfo(BzItems.SUPER_CANDLE_PURPLE.get());
        addInfo(BzItems.SUPER_CANDLE_RED.get());
        addInfo(BzItems.SUPER_CANDLE_WHITE.get());
        addInfo(BzItems.SUPER_CANDLE_YELLOW.get());
        addInfo(BzItems.INCENSE_CANDLE.get());
        addInfo(BzItems.CRYSTALLINE_FLOWER.get());
        addInfo(BzItems.STRING_CURTAIN_BLACK.get());
        addInfo(BzItems.STRING_CURTAIN_BLUE.get());
        addInfo(BzItems.STRING_CURTAIN_BROWN.get());
        addInfo(BzItems.STRING_CURTAIN_CYAN.get());
        addInfo(BzItems.STRING_CURTAIN_GRAY.get());
        addInfo(BzItems.STRING_CURTAIN_GREEN.get());
        addInfo(BzItems.STRING_CURTAIN_LIGHT_BLUE.get());
        addInfo(BzItems.STRING_CURTAIN_LIGHT_GRAY.get());
        addInfo(BzItems.STRING_CURTAIN_LIME.get());
        addInfo(BzItems.STRING_CURTAIN_MAGENTA.get());
        addInfo(BzItems.STRING_CURTAIN_ORANGE.get());
        addInfo(BzItems.STRING_CURTAIN_PINK.get());
        addInfo(BzItems.STRING_CURTAIN_PURPLE.get());
        addInfo(BzItems.STRING_CURTAIN_RED.get());
        addInfo(BzItems.STRING_CURTAIN_WHITE.get());
        addInfo(BzItems.STRING_CURTAIN_YELLOW.get());

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle_from_super_candles"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, true));

        registry.getRecipeManager().byKey(new ResourceLocation(Bumblezone.MODID, "incense_candle"))
                .ifPresent(recipe -> registerExtraRecipes(recipe, registry, false));
    }

    private static void addInfo(Item item) {
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(item),
                Component.translatable(BuiltInRegistries.ITEM.getKey(item).toString()),
                (text) -> {
                    text.add(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.ITEM.getKey(item).getPath() + ".jei_description"));
                    return text;
                });
    }

    private static void addInfo(Fluid fluid) {
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(fluid, 1),
                Component.translatable(BuiltInRegistries.FLUID.getKey(fluid).toString()),
                (text) -> {
                    text.add(Component.translatable(Bumblezone.MODID + "." + BuiltInRegistries.FLUID.getKey(fluid).getPath() + ".jei_description"));
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