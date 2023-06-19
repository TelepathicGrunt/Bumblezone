package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

public class NbtKeepingShapelessRecipe extends ShapelessRecipe {

    private final Item itemToKeepNbtOf;

    public NbtKeepingShapelessRecipe(ResourceLocation resourceLocation, String string, CraftingBookCategory craftingBookCategory, ItemStack itemStack, NonNullList<Ingredient> nonNullList, Item itemToKeepNbtOf) {
        super(resourceLocation, string, craftingBookCategory, itemStack, nonNullList);
        this.itemToKeepNbtOf = itemToKeepNbtOf;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        return super.matches(inv, level);
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack resultItem = super.getResultItem(registryAccess).copy();
        for (ItemStack input : craftingContainer.getItems()) {
            if (input.is(this.itemToKeepNbtOf)) {
                if (input.hasTag()) {
                    resultItem.setTag(input.getTag());
                    break;
                }
            }
        }

        return resultItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.NBT_KEEPING_SHAPELESS_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<NbtKeepingShapelessRecipe>, BzRecipeSerializer<NbtKeepingShapelessRecipe> {

        @Override
        public NbtKeepingShapelessRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String string = GsonHelper.getAsString(jsonObject, "group", "");
            CraftingBookCategory craftingBookCategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", null), CraftingBookCategory.MISC);
            NonNullList<Ingredient> nonNullList = NbtKeepingShapelessRecipe.Serializer.itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (nonNullList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            if (nonNullList.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            }
            ItemStack itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            Item item = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(jsonObject, "keep_nbt_of"));
            return new NbtKeepingShapelessRecipe(resourceLocation, string, craftingBookCategory, itemStack, nonNullList, item);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> nonNullList = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (ingredient.isEmpty()) continue;
                nonNullList.add(ingredient);
            }
            return nonNullList;
        }

        public JsonObject toJson(NbtKeepingShapelessRecipe recipe) {
            JsonObject json = new JsonObject();
            json.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(BzRecipes.NBT_KEEPING_SHAPELESS_RECIPE.get()).toString());
            json.addProperty("group", recipe.getGroup());

            JsonArray ingredients = new JsonArray();
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredients.add(ingredient.toJson());
            }
            json.add("ingredients", ingredients);

            JsonObject result = new JsonObject();
            result.addProperty("item", BuiltInRegistries.ITEM.getKey(recipe.getResultItem(RegistryAccess.EMPTY).getItem()).toString());
            result.addProperty("count", recipe.getResultItem(RegistryAccess.EMPTY).getCount());
            json.add("result", result);
            JsonObject itemNbtToKeep = new JsonObject();
            itemNbtToKeep.addProperty("item", BuiltInRegistries.ITEM.getKey(recipe.itemToKeepNbtOf).toString());
            json.add("keep_nbt_of", itemNbtToKeep);
            return json;
        }

        @Override
        public NbtKeepingShapelessRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            String string = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            int i = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
            nonNullList.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));
            ItemStack itemStack = friendlyByteBuf.readItem();
            Item item = friendlyByteBuf.readById(BuiltInRegistries.ITEM);
            return new NbtKeepingShapelessRecipe(resourceLocation, string, craftingBookCategory, itemStack, nonNullList, item);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, NbtKeepingShapelessRecipe shapelessRecipe) {
            friendlyByteBuf.writeUtf(shapelessRecipe.getGroup());
            friendlyByteBuf.writeEnum(shapelessRecipe.category());
            friendlyByteBuf.writeVarInt(shapelessRecipe.getIngredients().size());
            for (Ingredient ingredient : shapelessRecipe.getIngredients()) {
                ingredient.toNetwork(friendlyByteBuf);
            }
            friendlyByteBuf.writeItem(shapelessRecipe.getResultItem(RegistryAccess.EMPTY));
            friendlyByteBuf.writeId(BuiltInRegistries.ITEM, shapelessRecipe.itemToKeepNbtOf);
        }
    }
}