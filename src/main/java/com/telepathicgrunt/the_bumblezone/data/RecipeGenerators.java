package com.telepathicgrunt.the_bumblezone.data;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Consumer;

public class RecipeGenerators extends RecipeProvider implements IDataProvider {

    public RecipeGenerators(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(Items.BEE_NEST)
                .define('b', BzItems.BEESWAX_PLANKS.get())
                .define('c', Items.HONEYCOMB)
                .pattern("bbb")
                .pattern("bcb")
                .pattern("bbb")
                .unlockedBy("has_bees_wax_planks", has(BzItems.BEESWAX_PLANKS.get()))
                .save(consumer, Bumblezone.MODID + ":beeswax_planks_to_bee_nest");

        ShapedRecipeBuilder.shaped(BzItems.HONEY_CRYSTAL_SHIELD.get())
                .define('s', BzItems.HONEY_CRYSTAL_SHARDS.get())
                .pattern("sss")
                .pattern("sss")
                .pattern(" s ")
                .unlockedBy("has_honey_crystal_shards", has(BzItems.HONEY_CRYSTAL_SHARDS.get()))
                .save(consumer, Bumblezone.MODID + ":honey_crystal_shards");

        ShapelessRecipeBuilder.shapeless(BzItems.STICKY_HONEY_REDSTONE.get())
                .requires(BzItems.STICKY_HONEY_RESIDUE.get())
                .requires(Items.REDSTONE)
                .unlockedBy("has_stick_honey_residue", has(BzItems.STICKY_HONEY_RESIDUE.get()))
                .save(consumer, Bumblezone.MODID + ":stick_honey_residue");

        BzContainerShapelessRecipeBuilder.shapeless(BzItems.SUGAR_WATER_BUCKET.get())
                .requires(Items.WATER_BUCKET)
                .requires(Items.SUGAR)
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .save(consumer, Bumblezone.MODID + ":sugar_water_bucket");

        CookingRecipeBuilder.smelting(Ingredient.of(BzItems.SUGAR_INFUSED_STONE.get()), Items.STONE, 0.05f, 50)
                .unlockedBy("has_sugar_infused_stone", has(BzItems.SUGAR_INFUSED_STONE.get()))
                .save(consumer, Bumblezone.MODID + ":stone_smelting_sugar_infused_stone");

        CookingRecipeBuilder.smelting(Ingredient.of(BzItems.SUGAR_INFUSED_COBBLESTONE.get()), Items.COBBLESTONE, 0.05f, 50)
                .unlockedBy("has_sugar_infused_cobblestone", has(BzItems.SUGAR_INFUSED_COBBLESTONE.get()))
                .save(consumer, Bumblezone.MODID + ":cobblestone_smelting_sugar_infused_cobblestone");

        ForgeSmeltingRecipeBuilder.smelting(Ingredient.of(Items.HONEY_BLOCK), BzItems.STICKY_HONEY_RESIDUE.get(), 6, 4.0f, 520)
                .unlockedBy("has_honey_block", has(Items.HONEY_BLOCK))
                .save(consumer, Bumblezone.MODID + ":sticky_honey_residue_smelting_honey_block");

        ForgeSmeltingRecipeBuilder.smelting(Ingredient.of(BzItems.HONEY_CRYSTAL.get()), BzItems.STICKY_HONEY_RESIDUE.get(), 4, 3.7f, 420)
                .unlockedBy("has_honey_crystal", has(BzItems.HONEY_CRYSTAL.get()))
                .save(consumer, Bumblezone.MODID + ":sticky_honey_residue_smelting_honey_crystal");

        CookingRecipeBuilder.smelting(Ingredient.of(BzItems.HONEY_CRYSTAL_SHARDS.get()), BzItems.STICKY_HONEY_RESIDUE.get(), 0.25f, 125)
                .unlockedBy("has_honey_crystal_shards", has(BzItems.HONEY_CRYSTAL_SHARDS.get()))
                .save(consumer, Bumblezone.MODID + ":sticky_honey_residue_smelting_honey_crystal_shards");

        ForgeSmeltingRecipeBuilder.smelting(Ingredient.of(BzItems.HONEY_CRYSTAL_SHIELD.get()), BzItems.STICKY_HONEY_RESIDUE.get(), 5, 1.50f, 600)
                .unlockedBy("has_honey_crystal_shield", has(BzItems.HONEY_CRYSTAL_SHIELD.get()))
                .save(consumer, Bumblezone.MODID + ":sticky_honey_residue_smelting_honey_crystal_shield");

    }

    @Override
    public String getName() {
        return "The Bumblezone Recipes";
    }
}
