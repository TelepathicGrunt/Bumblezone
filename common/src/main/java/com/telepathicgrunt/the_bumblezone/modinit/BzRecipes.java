package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.HolderRegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.events.item.BzRegisterBrewingRecipeEvent;
import com.telepathicgrunt.the_bumblezone.items.recipes.ContainerCraftingRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.ItemStackSmeltingRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.NbtKeepingShapelessRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.PotionCandleRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BzRecipes {
    public static final ResourcefulRegistry<RecipeSerializer<?>> RECIPES = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_SERIALIZER, Bumblezone.MODID);

    //Recipe
    public static final HolderRegistryEntry<RecipeSerializer<?>> CONTAINER_CRAFTING_RECIPE = RECIPES.registerHolder("container_shapeless_recipe_bz", ContainerCraftingRecipe.Serializer::new);
    public static final HolderRegistryEntry<RecipeSerializer<?>> POTION_CANDLE_RECIPE = RECIPES.registerHolder("potion_candle_recipe", PotionCandleRecipe.Serializer::new);
    public static final HolderRegistryEntry<RecipeSerializer<?>> NBT_KEEPING_SHAPELESS_RECIPE = RECIPES.registerHolder("nbt_keeping_shapeless_recipe", NbtKeepingShapelessRecipe.Serializer::new);
    public static final HolderRegistryEntry<RecipeSerializer<?>> ITEMSTACK_SMELTING_RECIPE = RECIPES.registerHolder("itemstack_smelting_recipe", ItemStackSmeltingRecipe.ItemStackSmeltingRecipeSerializer::new);

    public static void registerBrewingStandRecipes(BzRegisterBrewingRecipeEvent event) {
        if (BzGeneralConfigs.glisteringHoneyBrewingRecipe) {
            event.registrator().accept(Potions.AWKWARD, BzItems.GLISTERING_HONEY_CRYSTAL.get(), Potions.LUCK);
        }
        if (BzGeneralConfigs.beeStingerBrewingRecipe) {
            event.registrator().accept(Potions.AWKWARD, BzItems.BEE_STINGER.get(), Potions.LONG_POISON);
        }
        if (BzGeneralConfigs.beeSoupBrewingRecipe) {
            event.registrator().accept(Potions.AWKWARD, BzItems.BEE_SOUP.get(), BzPotions.NEUROTOXIN.holder());
            event.registrator().accept(BzPotions.NEUROTOXIN.holder(), Items.REDSTONE, BzPotions.LONG_NEUROTOXIN.holder());
        }
    }
}
