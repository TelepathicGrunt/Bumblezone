package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.items.recipes.ContainerCraftingRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.ItemStackSmeltingRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.NbtKeepingShapelessRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.PotionCandleRecipe;
import com.telepathicgrunt.the_bumblezone.mixin.containers.PotionBrewingAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class BzRecipes {
    public static final ResourcefulRegistry<RecipeSerializer<?>> RECIPES = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_SERIALIZER, Bumblezone.MODID);

    //Recipe
    public static final RegistryEntry<RecipeSerializer<ContainerCraftingRecipe>> CONTAINER_CRAFTING_RECIPE = RECIPES.register("container_shapeless_recipe_bz", ContainerCraftingRecipe.Serializer::new);
    public static final RegistryEntry<RecipeSerializer<PotionCandleRecipe>> POTION_CANDLE_RECIPE = RECIPES.register("potion_candle_recipe", PotionCandleRecipe.Serializer::new);
    public static final RegistryEntry<RecipeSerializer<NbtKeepingShapelessRecipe>> NBT_KEEPING_SHAPELESS_RECIPE = RECIPES.register("nbt_keeping_shapeless_recipe", NbtKeepingShapelessRecipe.Serializer::new);
    public static final RegistryEntry<RecipeSerializer<ItemStackSmeltingRecipe>> ITEMSTACK_SMELTING_RECIPE = RECIPES.register("itemstack_smelting_recipe", ItemStackSmeltingRecipe.ItemStackSmeltingRecipeSerializer::new);

    public static void registerBrewingStandRecipes() {
        if (BzGeneralConfigs.glisteringHoneyBrewingRecipe) {
            PotionBrewingAccessor.callAddMix(Potions.AWKWARD, BzItems.GLISTERING_HONEY_CRYSTAL.get(), Potions.LUCK);
        }
        if (BzGeneralConfigs.beeStingerBrewingRecipe) {
            PotionBrewingAccessor.callAddMix(Potions.AWKWARD, BzItems.BEE_STINGER.get(), Potions.LONG_POISON);
        }
        if (BzGeneralConfigs.beeSoupBrewingRecipe) {
            PotionBrewingAccessor.callAddMix(Potions.AWKWARD, BzItems.BEE_SOUP.get(), BzPotions.NEUROTOXIN.get());
            PotionBrewingAccessor.callAddMix(BzPotions.NEUROTOXIN.get(), Items.REDSTONE, BzPotions.LONG_NEUROTOXIN.get());
        }
    }
}
