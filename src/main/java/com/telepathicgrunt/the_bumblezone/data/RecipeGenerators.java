package com.telepathicgrunt.the_bumblezone.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RecipeGenerators extends RecipeProvider implements IDataProvider {

    public RecipeGenerators(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {

        consumer.accept(shapedRecipeResult(
                Items.BEE_NEST,
                new ResourceLocation(Bumblezone.MODID, "beehive_beeswax_to_bee_nest"),
                1,
                ImmutableList.of(
                        "bbb",
                        "bcb",
                        "bbb"),
                ImmutableMap.<Character,Ingredient>builder()
                        .put('b', Ingredient.of(BzItems.BEEHIVE_BEESWAX.get()))
                        .put('c', Ingredient.of(Items.HONEYCOMB))
                        .build())
        );

        consumer.accept(shapedRecipeResult(
                BzItems.HONEY_CRYSTAL_SHIELD.get(),
                null,
                1,
                ImmutableList.of(
                        "sss",
                        "sss",
                        " s "),
                ImmutableMap.<Character,Ingredient>builder()
                        .put('s', Ingredient.of(BzItems.HONEY_CRYSTAL_SHARDS.get()))
                        .build())
        );

        consumer.accept(shapelessRecipeResult(
                BzItems.STICKY_HONEY_REDSTONE.get(),
                null,
                1,
                ImmutableList.of(
                        Ingredient.of(BzItems.STICKY_HONEY_RESIDUE.get()),
                        Ingredient.of(Items.REDSTONE)))
        );

        consumer.accept(bzContainerShapelessRecipeBuilder(
                BzItems.SUGAR_WATER_BUCKET.get(),
                null,
                1,
                ImmutableList.of(
                        Ingredient.of(Items.WATER_BUCKET),
                        Ingredient.of(Items.SUGAR)))
        );

        consumer.accept(cookingRecipeBuilderResult(
                Ingredient.of(BzItems.SUGAR_INFUSED_STONE.get()),
                Items.STONE,
                0.05f,
                50,
                new ResourceLocation(Bumblezone.MODID, "stone_smelting_sugar_infused_stone"),
                IRecipeSerializer.SMELTING_RECIPE)
        );

        consumer.accept(cookingRecipeBuilderResult(
                Ingredient.of(BzItems.SUGAR_INFUSED_COBBLESTONE.get()),
                Items.COBBLESTONE,
                0.05f,
                50,
                new ResourceLocation(Bumblezone.MODID, "cobblestone_smelting_sugar_infused_cobblestone"),
                IRecipeSerializer.SMELTING_RECIPE)
        );

        ForgeSmeltingRecipeBuilder.smelting(
                Ingredient.of(Items.HONEY_BLOCK),
                BzItems.STICKY_HONEY_RESIDUE.get(),
                6,
                4.0f,
                520)
                .save(consumer, Bumblezone.MODID + ":sticky_honey_residue_smelting_honey_block");

        ForgeSmeltingRecipeBuilder.smelting(
                Ingredient.of(BzItems.HONEY_CRYSTAL.get()),
                BzItems.STICKY_HONEY_RESIDUE.get(),
                4,
                3.7f,
                420)
                .save(consumer, Bumblezone.MODID + ":sticky_honey_residue_smelting_honey_crystal");

        consumer.accept(cookingRecipeBuilderResult(
                Ingredient.of(BzItems.HONEY_CRYSTAL_SHARDS.get()),
                BzItems.STICKY_HONEY_RESIDUE.get(),
                0.25f,
                125,
                new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue_smelting_honey_crystal_shards"),
                IRecipeSerializer.SMELTING_RECIPE)
        );

        ForgeSmeltingRecipeBuilder.smelting(
                Ingredient.of(BzItems.HONEY_CRYSTAL_SHIELD.get()),
                BzItems.STICKY_HONEY_RESIDUE.get(),
                5,
                1.50f,
                600)
                .save(consumer, Bumblezone.MODID + ":sticky_honey_residue_smelting_honey_crystal_shield");

    }

    public static ShapedRecipeBuilder.Result shapedRecipeResult(IItemProvider iItemProvider, ResourceLocation recipeRL,
                                                                int outputNum, List<String> recipe,
                                                                Map<Character, Ingredient> recipeMapKey) {
        return ShapedRecipeBuilder.shaped(iItemProvider).new Result(
                recipeRL == null ? iItemProvider.asItem().getRegistryName() : recipeRL,
                iItemProvider.asItem(),
                outputNum,
                Bumblezone.MODID,
                recipe,
                recipeMapKey,
                null,
                null
        ) {
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }
        };
    }

    public static ShapelessRecipeBuilder.Result shapelessRecipeResult(IItemProvider iItemProvider, ResourceLocation recipeRL,
                                                                int outputNum, List<Ingredient> recipe) {
        return new ShapelessRecipeBuilder.Result(
                recipeRL == null ? iItemProvider.asItem().getRegistryName() : recipeRL,
                iItemProvider.asItem(),
                outputNum,
                Bumblezone.MODID,
                recipe,
                null,
                null
        ) {
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }
        };
    }

    public static BzContainerShapelessRecipeBuilder.Result bzContainerShapelessRecipeBuilder(IItemProvider iItemProvider, ResourceLocation recipeRL,
                                                                                             int outputNum, List<Ingredient> recipe) {
        return new BzContainerShapelessRecipeBuilder.Result(
                recipeRL == null ? iItemProvider.asItem().getRegistryName() : recipeRL,
                iItemProvider.asItem(),
                outputNum,
                Bumblezone.MODID,
                recipe,
                null,
                null
        ) {
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }
        };
    }


    public static CookingRecipeBuilder.Result cookingRecipeBuilderResult(Ingredient ingredient, IItemProvider result,
                                                                         float cookTime, int exp,
                                                                         ResourceLocation recipeRL,
                                                                         IRecipeSerializer<? extends AbstractCookingRecipe> serializer) {
        return new CookingRecipeBuilder.Result(
                recipeRL == null ? new ResourceLocation(result.asItem().getRegistryName() + "_cooking") : recipeRL,
                Bumblezone.MODID,
                ingredient,
                result.asItem(),
                cookTime,
                exp,
                null,
                null,
                serializer
        ) {
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }
        };
    }

    @Override
    public String getName() {
        return "The Bumblezone Recipes";
    }
}
