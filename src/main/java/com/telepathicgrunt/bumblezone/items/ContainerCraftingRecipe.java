package com.telepathicgrunt.bumblezone.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telepathicgrunt.bumblezone.modinit.BzRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class ContainerCraftingRecipe extends ShapelessRecipe {
    private final String group;
    private final ItemStack recipeOutput;
    private final DefaultedList<Ingredient> recipeItems;
    public ContainerCraftingRecipe(Identifier idIn, String groupIn, ItemStack recipeOutputIn, DefaultedList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
        this.group = groupIn;
        this.recipeOutput = recipeOutputIn;
        this.recipeItems = recipeItemsIn;
    }

    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.CONTAINER_CRAFTING_RECIPE;
    }

    @Override
    public DefaultedList<ItemStack> getRemainingStacks(CraftingInventory inv) {
        return DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
    }

    public static class Serializer implements RecipeSerializer<ContainerCraftingRecipe> {
        public ContainerCraftingRecipe read(Identifier recipeId, JsonObject json) {
            String s = JsonHelper.getString(json, "group", "");
            DefaultedList<Ingredient> DefaultedList = getIngredients(JsonHelper.getArray(json, "ingredients"));
            if (DefaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            else {
                ItemStack itemstack = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
                return new ContainerCraftingRecipe(recipeId, s, itemstack, DefaultedList);
            }
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray p_199568_0_) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();

            for (int i = 0; i < p_199568_0_.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(p_199568_0_.get(i));
                if (!ingredient.isEmpty()) {
                    defaultedList.add(ingredient);
                }
            }

            return defaultedList;
        }

        public ContainerCraftingRecipe read(Identifier recipeId, PacketByteBuf buffer) {
            String s = buffer.readString(32767);
            int i = buffer.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(buffer));
            }

            ItemStack itemstack = buffer.readItemStack();
            return new ContainerCraftingRecipe(recipeId, s, itemstack, defaultedList);
        }

        public void write(PacketByteBuf buffer, ContainerCraftingRecipe recipe) {
            buffer.writeString(recipe.group);
            buffer.writeVarInt(recipe.recipeItems.size());

            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.recipeOutput);
        }
    }
}