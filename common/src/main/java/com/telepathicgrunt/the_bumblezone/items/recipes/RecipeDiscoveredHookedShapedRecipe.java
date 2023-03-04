package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.mixin.containers.ShapedRecipeAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2CharOpenHashMap;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Map;

public class RecipeDiscoveredHookedShapedRecipe extends ShapedRecipe {

    public RecipeDiscoveredHookedShapedRecipe(ResourceLocation id, String group, CraftingBookCategory craftingBookCategory, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(id, group, craftingBookCategory, width, height, ingredients, result);
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
        return BzRecipes.RECIPE_DISCOVERED_HOOKED_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<RecipeDiscoveredHookedShapedRecipe>, BzRecipeSerializer<RecipeDiscoveredHookedShapedRecipe> {
        @Override
        public RecipeDiscoveredHookedShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = GsonHelper.getAsString(json, "group", "");
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", (String)null), CraftingBookCategory.MISC);
            Map<String, Ingredient> map = ShapedRecipeAccessor.callKeyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] astring = ShapedRecipeAccessor.callShrink(ShapedRecipeAccessor.callPatternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = ShapedRecipeAccessor.callDissolvePattern(astring, map, i, j);
            ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new RecipeDiscoveredHookedShapedRecipe(recipeId, s, craftingbookcategory, i, j, nonnulllist, itemstack);
        }

        public JsonObject toJson(RecipeDiscoveredHookedShapedRecipe recipe) {
            JsonObject json = new JsonObject();

            json.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(BzRecipes.RECIPE_DISCOVERED_HOOKED_RECIPE.get()).toString());
            json.addProperty("group", recipe.getGroup());

            NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
            var ingredients = new Object2CharOpenHashMap<Ingredient>();
            var inputs = new Char2ObjectOpenHashMap<Ingredient>();
            ingredients.defaultReturnValue(' ');
            char currentChar = 'A';
            for (Ingredient ingredient : recipeIngredients) {
                if (!ingredient.isEmpty()
                        && ingredients.putIfAbsent(ingredient, currentChar) == ingredients.defaultReturnValue()) {
                    inputs.putIfAbsent(currentChar, ingredient);
                    currentChar++;
                }
            }

            var pattern = new ArrayList<String>();
            var patternLine = new StringBuilder();
            for (int i = 0; i < recipeIngredients.size(); i++) {
                if (i != 0 && i % recipe.getWidth() == 0) {
                    pattern.add(patternLine.toString());
                    patternLine.setLength(0);
                }

                Ingredient ingredient = recipeIngredients.get(i);
                patternLine.append(ingredients.getChar(ingredient));
            }
            pattern.add(patternLine.toString());

            JsonArray jsonArray = new JsonArray();
            for(String string : pattern) {
                jsonArray.add(string);
            }
            json.add("pattern", jsonArray);

            JsonObject jsonObject = new JsonObject();
            for(Map.Entry<Character, Ingredient> entry : inputs.entrySet()) {
                jsonObject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
            }
            json.add("key", jsonObject);


            JsonObject result = new JsonObject();
            result.addProperty("item", BuiltInRegistries.ITEM.getKey(recipe.getResultItem().getItem()).toString());
            result.addProperty("count", recipe.getResultItem().getCount());
            json.add("result", result);

            return json;
        }

        @Override
        public RecipeDiscoveredHookedShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            String s = buffer.readUtf();
            CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new RecipeDiscoveredHookedShapedRecipe(recipeId, s, craftingbookcategory, i, j, nonnulllist, itemstack);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipeDiscoveredHookedShapedRecipe recipe) {
            buffer.writeVarInt(recipe.getWidth());
            buffer.writeVarInt(recipe.getHeight());
            buffer.writeUtf(recipe.getGroup());
            buffer.writeEnum(recipe.category());

            for(Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem());
        }
    }
}