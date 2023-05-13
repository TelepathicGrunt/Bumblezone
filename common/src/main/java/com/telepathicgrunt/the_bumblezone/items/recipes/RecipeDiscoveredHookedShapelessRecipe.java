package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telepathicgrunt.the_bumblezone.mixin.containers.ShapedRecipeAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2CharOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Map;

public class RecipeDiscoveredHookedShapelessRecipe extends ShapelessRecipe {

    public RecipeDiscoveredHookedShapelessRecipe(ResourceLocation resourceLocation, String string, CraftingBookCategory craftingBookCategory, ItemStack itemStack, NonNullList<Ingredient> nonNullList) {
        super(resourceLocation, string, craftingBookCategory, itemStack, nonNullList);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        boolean matched = super.matches(inv, level);

        if (matched) {
            Player player = PlatformHooks.getCraftingPlayer();
            if (player instanceof ServerPlayer serverPlayer) {
                BzCriterias.RECIPE_DISCOVERED_TRIGGER.trigger(serverPlayer, this.getId());
            }
        }

        return matched;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.RECIPE_DISCOVERED_HOOKED_SHAPELESS_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<RecipeDiscoveredHookedShapelessRecipe>, BzRecipeSerializer<RecipeDiscoveredHookedShapelessRecipe> {

        @Override
        public RecipeDiscoveredHookedShapelessRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String string = GsonHelper.getAsString(jsonObject, "group", "");
            CraftingBookCategory craftingBookCategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", null), CraftingBookCategory.MISC);
            NonNullList<Ingredient> nonNullList = RecipeDiscoveredHookedShapelessRecipe.Serializer.itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (nonNullList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            if (nonNullList.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            }
            ItemStack itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new RecipeDiscoveredHookedShapelessRecipe(resourceLocation, string, craftingBookCategory, itemStack, nonNullList);
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

        public JsonObject toJson(RecipeDiscoveredHookedShapelessRecipe recipe) {
            JsonObject json = new JsonObject();
            json.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(BzRecipes.RECIPE_DISCOVERED_HOOKED_SHAPELESS_RECIPE.get()).toString());
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

            return json;
        }

        @Override
        public RecipeDiscoveredHookedShapelessRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            String string = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            int i = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < nonNullList.size(); ++j) {
                nonNullList.set(j, Ingredient.fromNetwork(friendlyByteBuf));
            }
            ItemStack itemStack = friendlyByteBuf.readItem();
            return new RecipeDiscoveredHookedShapelessRecipe(resourceLocation, string, craftingBookCategory, itemStack, nonNullList);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, RecipeDiscoveredHookedShapelessRecipe shapelessRecipe) {
            friendlyByteBuf.writeUtf(shapelessRecipe.getGroup());
            friendlyByteBuf.writeEnum(shapelessRecipe.category());
            friendlyByteBuf.writeVarInt(shapelessRecipe.getIngredients().size());
            for (Ingredient ingredient : shapelessRecipe.getIngredients()) {
                ingredient.toNetwork(friendlyByteBuf);
            }
            friendlyByteBuf.writeItem(shapelessRecipe.getResultItem(RegistryAccess.EMPTY));
        }
    }
}