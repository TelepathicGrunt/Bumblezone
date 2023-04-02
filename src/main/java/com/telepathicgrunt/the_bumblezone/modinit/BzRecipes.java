package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.ContainerCraftingRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.mixin.containers.PotionBrewingAccessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BzRecipes {
    public static final RecipeSerializer<ContainerCraftingRecipe> CONTAINER_CRAFTING_RECIPE = new ContainerCraftingRecipe.Serializer();
    public static final IncenseCandleRecipe.Serializer INCENSE_CANDLE_RECIPE = new IncenseCandleRecipe.Serializer();

    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(Bumblezone.MODID, "container_shapeless_recipe_bz"), CONTAINER_CRAFTING_RECIPE);
        Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(Bumblezone.MODID, "incense_candle_recipe"), INCENSE_CANDLE_RECIPE);
    }

    public static void registerBrewingStandRecipes() {
        PotionBrewingAccessor.callAddMix(Potions.AWKWARD, BzItems.GLISTERING_HONEY_CRYSTAL, Potions.LUCK);
        PotionBrewingAccessor.callAddMix(Potions.AWKWARD, BzItems.BEE_STINGER, Potions.LONG_POISON);
    }
}
