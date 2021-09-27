package com.telepathicgrunt.the_bumblezone.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerCraftingRecipe extends ShapelessRecipe {
    private final String group;
    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    public ContainerCraftingRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
        this.group = groupIn;
        this.recipeOutput = recipeOutputIn;
        this.recipeItems = recipeItemsIn;
    }

    public IRecipeSerializer<?> getSerializer() {
        return BzItems.CONTAINER_CRAFTING_RECIPE.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        NonNullList<ItemStack> remainingInv = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        int containerOutput = recipeOutput.hasContainerItem() ? recipeOutput.getCount() : 0;

        for(int i = 0; i < remainingInv.size(); ++i) {
            ItemStack item = inv.getItem(i);
            if (item.hasContainerItem()) {
                if(containerOutput > 0 &&
                    (recipeOutput.getItem() == item.getContainerItem().getItem() ||
                     recipeOutput.getContainerItem().getItem() == item.getItem()))
                {
                    containerOutput--;
                }
                else {
                    remainingInv.set(i, item.getContainerItem());
                }
            }
        }

        return remainingInv;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<ContainerCraftingRecipe> {
        public ContainerCraftingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getAsString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            else {
                ItemStack itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
                return new ContainerCraftingRecipe(recipeId, s, itemstack, nonnulllist);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray jsonElements) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < jsonElements.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonElements.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        public ContainerCraftingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for (int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new ContainerCraftingRecipe(recipeId, s, itemstack, nonnulllist);
        }

        public void toNetwork(PacketBuffer buffer, ContainerCraftingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.recipeItems.size());

            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.recipeOutput);
        }
    }
}