package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.SuperIncenseCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.mixin.containers.ShapedRecipeAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.IShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SuperIncenseCandleRecipe implements CraftingRecipe, IShapedRecipe<CraftingContainer> {
    private final ResourceLocation id;
    private final String group;
    private final int outputCount;
    private final int maxAllowedPotions;
    private final NonNullList<Ingredient> shapedRecipeItems;
    private final NonNullList<Ingredient> shapelessRecipeItems;
    private final ItemStack result;
    private final int width;
    private final int height;

    public SuperIncenseCandleRecipe(ResourceLocation id, String group, int outputCount, int maxAllowedPotions, NonNullList<Ingredient> shapedRecipeItems, NonNullList<Ingredient> shapelessRecipeItems, int width, int height) {
        this.id = id;
        this.group = group;
        this.outputCount = outputCount;
        this.maxAllowedPotions = maxAllowedPotions;
        this.shapedRecipeItems = shapedRecipeItems;
        this.shapelessRecipeItems = shapelessRecipeItems;
        this.result = getResultStack(outputCount);
        this.width = width;
        this.height = height;
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
                   amplifier.addAndGet(me.getAmplifier() + 1);
                   potionEffectsFound.getAndIncrement();
                });
            }
        }

        if (effects.isEmpty()) {
            return getResultStack(outputCount);
        }

        amplifier.set(amplifier.get() / potionEffectsFound.get());
        maxDuration.set((int)(maxDuration.get() / (potionEffectsFound.get() * (0.4f + (amplifier.get() * 0.12f)))));
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
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public int getRecipeWidth() {
        return getWidth();
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public int getRecipeHeight() {
        return getHeight();
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        boolean shapedMatch = false;

        for(int column = 0; column <= inv.getWidth() - this.width; ++column) {
            for(int row = 0; row <= inv.getHeight() - this.height; ++row) {
                if (this.matches(inv, column, row, true)) {
                    shapedMatch = true;
                }

                if (this.matches(inv, column, row, false)) {
                    shapedMatch = true;
                }
            }
        }
        return shapedMatch;
    }

    private boolean matches(CraftingContainer craftingInventory, int width, int height, boolean mirrored) {
        int potionCount = 0;
        for(int column = 0; column < craftingInventory.getWidth(); ++column) {
            for(int row = 0; row < craftingInventory.getHeight(); ++row) {
                ItemStack itemStack = craftingInventory.getItem(column + row * craftingInventory.getWidth());
                int k = column - width;
                int l = row - height;
                Ingredient ingredient = null;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (mirrored) {
                        ingredient = this.shapedRecipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.shapedRecipeItems.get(k + l * this.width);
                    }
                }

                if (ingredient == null) {
                    if (!itemStack.isEmpty()) {
                        if (itemStack.is(Items.POTION)) {
                            if(PotionUtils.getMobEffects(itemStack).isEmpty()) {
                                return false;
                            }
                            potionCount++;
                            if (potionCount > this.maxAllowedPotions) {
                                return false;
                            }
                        }
                        else if (this.shapelessRecipeItems.stream().noneMatch(i -> i.test(itemStack))) {
                            return false;
                        }
                    }
                }
                else if (!ingredient.test(itemStack)) {
                    return false;
                }
            }
        }

        return potionCount > 0;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.SUPER_INCENSE_CANDLE_RECIPE.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.addAll(shapelessRecipeItems);
        ingredients.addAll(shapedRecipeItems);
        return ingredients;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<SuperIncenseCandleRecipe> {
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
        public SuperIncenseCandleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");

            //shaped
            Map<String, Ingredient> map = ShapedRecipeAccessor.callKeyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] astring = ShapedRecipeAccessor.callShrink(ShapedRecipeAccessor.callPatternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            int width = astring[0].length();
            int height = astring.length;
            NonNullList<Ingredient> shapedRecipeItems = ShapedRecipeAccessor.callDissolvePattern(astring, map, width, height);

            //shapeless
            NonNullList<Ingredient> shapelessRecipeItems = getIngredients(GsonHelper.getAsJsonArray(json, "shapelessExtraIngredients"));
            if (shapelessRecipeItems.isEmpty()) {
                throw new JsonParseException("No shapeless ingredients for Super Incense Candle recipe");
            }

            int maxPotions = json.get("maxAllowedPotions").getAsInt();
            int resultCount = json.get("resultCount").getAsInt();

            return new SuperIncenseCandleRecipe(recipeId, group, resultCount, maxPotions, shapedRecipeItems, shapelessRecipeItems, width, height);
        }

        @Override
        public SuperIncenseCandleRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);

            int width = buffer.readVarInt();
            int height = buffer.readVarInt();
            NonNullList<Ingredient> shapedRecipe = NonNullList.withSize(width * height, Ingredient.EMPTY);
            shapedRecipe.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> shapelessRecipe = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            shapelessRecipe.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            int maxPotionRead = buffer.readVarInt();
            int resultCountRead = buffer.readVarInt();
            return new SuperIncenseCandleRecipe(recipeId, group, resultCountRead, maxPotionRead, shapedRecipe, shapelessRecipe, width, height);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SuperIncenseCandleRecipe recipe) {
            buffer.writeUtf(recipe.group);

            buffer.writeVarInt(recipe.width);
            buffer.writeVarInt(recipe.height);
            for(Ingredient ingredient : recipe.shapedRecipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeVarInt(recipe.shapelessRecipeItems.size());
            for (Ingredient ingredient : recipe.shapelessRecipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeInt(recipe.maxAllowedPotions);
            buffer.writeInt(recipe.outputCount);
        }
    }
}