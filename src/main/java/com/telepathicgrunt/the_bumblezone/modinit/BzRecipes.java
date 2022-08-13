package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.ContainerCraftingRecipe;
import com.telepathicgrunt.the_bumblezone.mixin.containers.PotionBrewingAccessor;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Bumblezone.MODID);

    //Recipe
    public static final RegistryObject<RecipeSerializer<ContainerCraftingRecipe>> CONTAINER_CRAFTING_RECIPE = RECIPES.register("container_shapeless_recipe_bz", ContainerCraftingRecipe.Serializer::new);

    public static void registerBrewingStandRecipes() {
        PotionBrewingAccessor.callAddMix(Potions.AWKWARD, BzItems.GLISTERING_HONEY_CRYSTAL.get(), Potions.LUCK);
    }
}
