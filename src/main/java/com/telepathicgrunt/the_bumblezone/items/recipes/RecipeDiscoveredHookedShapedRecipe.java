package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.mixin.containers.ShapedRecipeAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import java.util.Map;

public class RecipeDiscoveredHookedShapedRecipe extends ShapedRecipe {

    public RecipeDiscoveredHookedShapedRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(id, group, width, height, ingredients, result);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        boolean matched = super.matches(inv, level);

        if (matched) {
            Player player = ForgeHooks.getCraftingPlayer();
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

    public static class Serializer implements RecipeSerializer<RecipeDiscoveredHookedShapedRecipe> {
        @Override
        public RecipeDiscoveredHookedShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = GsonHelper.getAsString(json, "group", "");
            Map<String, Ingredient> map = ShapedRecipeAccessor.callKeyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] astring = ShapedRecipeAccessor.callShrink(ShapedRecipeAccessor.callPatternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = ShapedRecipeAccessor.callDissolvePattern(astring, map, i, j);
            ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new RecipeDiscoveredHookedShapedRecipe(recipeId, s, i, j, nonnulllist, itemstack);
        }

        @Override
        public RecipeDiscoveredHookedShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            String s = buffer.readUtf();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new RecipeDiscoveredHookedShapedRecipe(recipeId, s, i, j, nonnulllist, itemstack);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RecipeDiscoveredHookedShapedRecipe recipe) {
            buffer.writeVarInt(recipe.getWidth());
            buffer.writeVarInt(recipe.getHeight());
            buffer.writeUtf(recipe.getGroup());

            for(Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem());
        }
    }
}