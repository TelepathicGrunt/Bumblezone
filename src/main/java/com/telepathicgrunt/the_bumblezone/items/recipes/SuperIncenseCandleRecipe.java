package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.SuperIncenseCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SuperIncenseCandleRecipe extends ShapelessRecipe {
    private final String group;
    private final int outputCount;
    private final NonNullList<Ingredient> recipeItems;

    public SuperIncenseCandleRecipe(ResourceLocation idIn, String groupIn, int outputCount, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, getResultStack(outputCount), recipeItemsIn);
        this.group = groupIn;
        this.outputCount = outputCount;
        this.recipeItems = recipeItemsIn;
    }

    private static ItemStack getResultStack(int outputCountIn) {
        ItemStack stack = BzItems.SUPER_INCENSE_CANDLE.get().getDefaultInstance();
        stack.setCount(outputCountIn);
        return stack;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        MobEffect chosenEffect;
        List<MobEffect> effects = new ArrayList<>();
        AtomicInteger maxDuration = new AtomicInteger();
        AtomicInteger amplifier = new AtomicInteger();
        AtomicInteger potionEffectsFound = new AtomicInteger();

        for(int j = 0; j < inv.getContainerSize(); ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (itemstack.is(Items.POTION)) {
                PotionUtils.getMobEffects(itemstack).forEach(me -> {
                   effects.add(me.getEffect());
                   maxDuration.addAndGet(me.getDuration());
                   amplifier.addAndGet(me.getAmplifier());
                   potionEffectsFound.getAndIncrement();
                });
            }
        }

        if (effects.isEmpty()) {
            return getResultStack(outputCount);
        }

        maxDuration.set((int)(maxDuration.get() / (potionEffectsFound.get() * 0.33f)));
        amplifier.set((int)(amplifier.get() / (potionEffectsFound.get() * 0.5f)));
        chosenEffect = effects.get(new Random().nextInt(effects.size()));

        ItemStack resultStack = getResultStack(outputCount);
        CompoundTag tag = resultStack.getOrCreateTag();
        CompoundTag blockEntityTag = new CompoundTag();
        tag.put("BlockEntityTag", blockEntityTag);
        blockEntityTag.putInt(SuperIncenseCandleBlockEntity.COLOR_TAG, chosenEffect.getColor());
        blockEntityTag.putInt(SuperIncenseCandleBlockEntity.AMPLIFIER_TAG, amplifier.intValue());
        blockEntityTag.putInt(SuperIncenseCandleBlockEntity.MAX_DURATION_TAG, maxDuration.intValue());
        blockEntityTag.putString(SuperIncenseCandleBlockEntity.STATUS_EFFECT_TAG, Registry.MOB_EFFECT.getKey(chosenEffect).toString());
        blockEntityTag.putBoolean(SuperIncenseCandleBlockEntity.INFINITE_TAG, false);
        return resultStack;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        boolean validPotion = false;
        for(int j = 0; j < inv.getContainerSize(); ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (itemstack.is(Items.POTION) && !PotionUtils.getMobEffects(itemstack).isEmpty()) {
                validPotion = true;
                break;
            }
        }
        return validPotion && super.matches(inv, level);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.SUPER_INCENSE_CANDLE_RECIPE.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public static class Serializer implements RecipeSerializer<SuperIncenseCandleRecipe> {
        @Override
        public SuperIncenseCandleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            NonNullList<Ingredient> ingredients = getIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for Super Incense Candle shapeless recipe");
            }
            else {
                return new SuperIncenseCandleRecipe(recipeId, group, json.get("resultCount").getAsInt(), ingredients);
            }
        }

        private static NonNullList<Ingredient> getIngredients(JsonArray jsonElements) {
            NonNullList<Ingredient> defaultedList = NonNullList.create();

            for (int i = 0; i < jsonElements.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonElements.get(i));
                if (!ingredient.isEmpty()) {
                    defaultedList.add(ingredient);
                }
            }

            return defaultedList;
        }

        @Override
        public SuperIncenseCandleRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));
            int resultCountRead = buffer.readVarInt();
            return new SuperIncenseCandleRecipe(recipeId, group, resultCountRead, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SuperIncenseCandleRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.recipeItems.size());

            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeInt(recipe.outputCount);
        }
    }
}