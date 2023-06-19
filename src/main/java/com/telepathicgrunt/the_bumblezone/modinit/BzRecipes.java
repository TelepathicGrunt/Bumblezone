package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.items.recipes.ContainerCraftingRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.NbtKeepingShapelessRecipe;
import com.telepathicgrunt.the_bumblezone.items.recipes.RecipeDiscoveredHookedShapedRecipe;
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
    public static final RegistryObject<RecipeSerializer<IncenseCandleRecipe>> INCENSE_CANDLE_RECIPE = RECIPES.register("incense_candle_recipe", IncenseCandleRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<RecipeDiscoveredHookedShapedRecipe>> RECIPE_DISCOVERED_HOOKED_RECIPE = RECIPES.register("recipe_discovered_hooked_recipe", RecipeDiscoveredHookedShapedRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<NbtKeepingShapelessRecipe>> NBT_KEEPING_SHAPELESS_RECIPE = RECIPES.register("nbt_keeping_shapeless_recipe", NbtKeepingShapelessRecipe.Serializer::new);

    public static void registerBrewingStandRecipes() {
        if (BzGeneralConfigs.glisteringHoneyBrewingRecipe.get()) {
            PotionBrewingAccessor.callAddMix(Potions.AWKWARD, BzItems.GLISTERING_HONEY_CRYSTAL.get(), Potions.LUCK);
        }
        if (BzGeneralConfigs.beeStingerBrewingRecipe.get()) {
            PotionBrewingAccessor.callAddMix(Potions.AWKWARD, BzItems.BEE_STINGER.get(), Potions.LONG_POISON);
        }
    }
}
