package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.IncenseCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.mixin.containers.ShapedRecipeAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2CharOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class IncenseCandleRecipe implements CraftingRecipe, QuiltRecipeSerializer<IncenseCandleRecipe> {
    private final ResourceLocation id;
    private final String group;
    private final int outputCount;
    private final int maxAllowedPotions;
    private final NonNullList<Ingredient> shapedRecipeItems;
    private final NonNullList<Ingredient> shapelessRecipeItems;
    private final ItemStack result;
    private final int width;
    private final int height;
    private final boolean allowNormalPotions;
    private final boolean allowSplashPotions;
    private final boolean allowLingeringPotions;
    private final int maxLevelCap;

    public IncenseCandleRecipe(ResourceLocation id, String group, int outputCount, int maxAllowedPotions, NonNullList<Ingredient> shapedRecipeItems, NonNullList<Ingredient> shapelessRecipeItems, int width, int height, boolean allowNormalPotions, boolean allowSplashPotions, boolean allowLingeringPotions, int maxLevelCap) {
        this.id = id;
        this.group = group;
        this.outputCount = outputCount;
        this.maxAllowedPotions = maxAllowedPotions;
        this.shapedRecipeItems = shapedRecipeItems;
        this.shapelessRecipeItems = shapelessRecipeItems;
        this.result = getResultStack(outputCount);
        this.width = width;
        this.height = height;
        this.allowNormalPotions = allowNormalPotions;
        this.allowSplashPotions = allowSplashPotions;
        this.allowLingeringPotions = allowLingeringPotions;
        this.maxLevelCap = maxLevelCap;
    }

    public int getMaxAllowedPotions() {
        return this.maxAllowedPotions;
    }

    public boolean getAllowNormalPotions() {
        return this.allowNormalPotions;
    }

    public boolean getAllowSplashPotions() {
        return this.allowSplashPotions;
    }

    public boolean getAllowLingeringPotions() {
        return this.allowLingeringPotions;
    }

    public int getMaxLevelCap() {
        return this.maxLevelCap;
    }

    public NonNullList<Ingredient> getShapedRecipeItems() {
        return this.shapedRecipeItems;
    }

    public NonNullList<Ingredient> getShapelessRecipeItems() {
        return this.shapelessRecipeItems;
    }

    private static ItemStack getResultStack(int outputCountIn) {
        ItemStack stack = BzItems.INCENSE_CANDLE.getDefaultInstance();
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
        int splashCount = 0;
        int lingerCount = 0;

        for(int j = 0; j < inv.getContainerSize(); ++j) {
            ItemStack itemStack = inv.getItem(j);
            if (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION)) {
                PotionUtils.getMobEffects(itemStack).forEach(me -> {
                   effects.add(me.getEffect());
                   maxDuration.addAndGet(me.getEffect().isInstantenous() ? 200 : me.getDuration());
                   amplifier.addAndGet(me.getAmplifier() + 1);
                   potionEffectsFound.getAndIncrement();
                });

                if (itemStack.is(Items.SPLASH_POTION)) {
                    splashCount++;
                }

                if (itemStack.is(Items.LINGERING_POTION)) {
                    lingerCount++;
                }
            }
        }

        if (effects.isEmpty()) {
            return getResultStack(this.outputCount);
        }

        HashSet<MobEffect> setPicker = new HashSet<>(effects);
        List<MobEffect> filteredMobEffects = setPicker.stream().filter(e -> !Registry.MOB_EFFECT.getHolderOrThrow(Registry.MOB_EFFECT.getResourceKey(e).orElseThrow()).is(BzTags.BLACKLISTED_INCENSE_CANDLE_EFFECTS)).toList();
        chosenEffect = filteredMobEffects.get(new Random().nextInt(filteredMobEffects.size()));
        if (chosenEffect == null) {
            return getResultStack(this.outputCount);
        }

        balanceStats(chosenEffect, maxDuration, amplifier, potionEffectsFound);
        amplifier.set(Math.min(amplifier.get(), this.maxLevelCap));

        return createTaggedIncenseCandle(chosenEffect, maxDuration, amplifier, splashCount, lingerCount, this.outputCount);
    }

    public static void balanceStats(MobEffect chosenEffect, AtomicInteger maxDuration, AtomicInteger amplifier, AtomicInteger potionEffectsFound) {
        amplifier.set(amplifier.get() / potionEffectsFound.get());

        float durationBaseMultiplier = ((0.4f / (0.9f * potionEffectsFound.get())) + (amplifier.get() * 0.22f));
        float durationAdjustment = (potionEffectsFound.get() * durationBaseMultiplier);
        maxDuration.set((int)(maxDuration.get() / durationAdjustment));
        if (chosenEffect.isInstantenous()) {
            long thresholdTime = IncenseCandleBlockEntity.getInstantEffectThresholdTime(amplifier.intValue());
            int activationAmounts = (int)Math.ceil((double) maxDuration.intValue() / thresholdTime);
            maxDuration.set((int) (activationAmounts * thresholdTime));
        }
    }

    public static ItemStack createTaggedIncenseCandle(MobEffect chosenEffect, AtomicInteger maxDuration, AtomicInteger amplifier, int splashCount, int lingerCount, int outputCount) {
        ItemStack resultStack = getResultStack(outputCount);

        CompoundTag tag = resultStack.getOrCreateTag();
        CompoundTag blockEntityTag = new CompoundTag();
        tag.put("BlockEntityTag", blockEntityTag);
        blockEntityTag.putInt(IncenseCandleBlockEntity.COLOR_TAG, chosenEffect.getColor());
        blockEntityTag.putInt(IncenseCandleBlockEntity.AMPLIFIER_TAG, amplifier.intValue());
        blockEntityTag.putInt(IncenseCandleBlockEntity.MAX_DURATION_TAG, maxDuration.intValue());
        blockEntityTag.putString(IncenseCandleBlockEntity.STATUS_EFFECT_TAG, Registry.MOB_EFFECT.getKey(chosenEffect).toString());
        blockEntityTag.putBoolean(IncenseCandleBlockEntity.INFINITE_TAG, false);
        blockEntityTag.putInt(IncenseCandleBlockEntity.RANGE_TAG, 3 + (splashCount * 2));
        if (chosenEffect.isInstantenous()) {
            blockEntityTag.putInt(IncenseCandleBlockEntity.LINGER_TIME_TAG, 1);
        }
        else if (chosenEffect == MobEffects.NIGHT_VISION) {
            setLingerTime(lingerCount, blockEntityTag, IncenseCandleBlockEntity.DEFAULT_NIGHT_VISION_LINGER_TIME);
        }
        else {
            setLingerTime(lingerCount, blockEntityTag, IncenseCandleBlockEntity.DEFAULT_LINGER_TIME);
        }
        return resultStack;
    }

    private static void setLingerTime(int lingerCount, CompoundTag blockEntityTag, int baseLingerTime) {
        blockEntityTag.putInt(IncenseCandleBlockEntity.LINGER_TIME_TAG, baseLingerTime + (lingerCount * baseLingerTime * 2));
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getRecipeWidth() {
        return getWidth();
    }

    public int getHeight() {
        return this.height;
    }

    public int getRecipeHeight() {
        return getHeight();
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
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
        List<ItemStack> stackList = new ObjectArrayList<>();
        List<MobEffectInstance> mobEffects = new ObjectArrayList<>();
        for(int column = 0; column < craftingInventory.getWidth(); ++column) {
            for(int row = 0; row < craftingInventory.getHeight(); ++row) {
                ItemStack itemStack = craftingInventory.getItem(column + row * craftingInventory.getWidth());
                int k = column - width;
                int l = row - height;
                Ingredient ingredient = null;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (mirrored) {
                        ingredient = this.shapedRecipeItems.get(this.width - k - 1 + l * this.width);
                    }
                    else {
                        ingredient = this.shapedRecipeItems.get(k + l * this.width);
                    }
                }

                if (ingredient == null) {
                    if (!itemStack.isEmpty()) {
                        if (itemStack.is(Items.POTION) ||
                            itemStack.is(Items.SPLASH_POTION) ||
                            itemStack.is(Items.LINGERING_POTION)
                        ) {
                            if (itemStack.is(Items.POTION) && !this.allowNormalPotions) {
                                return false;
                            }
                            else if (itemStack.is(Items.SPLASH_POTION) && !this.allowSplashPotions) {
                                return false;
                            }
                            else if (itemStack.is(Items.LINGERING_POTION) && !this.allowLingeringPotions) {
                                return false;
                            }

                            List<MobEffectInstance> currentMobEffects = PotionUtils.getMobEffects(itemStack);
                            mobEffects.addAll(currentMobEffects);
                            if(currentMobEffects.isEmpty()) {
                                return false;
                            }
                            potionCount++;
                            if (potionCount > this.maxAllowedPotions) {
                                return false;
                            }
                        }
                        else {
                            stackList.add(itemStack);
                        }
                    }
                }
                else if (!ingredient.test(itemStack)) {
                    return false;
                }
            }
        }

        if (mobEffects.stream().allMatch(e -> Registry.MOB_EFFECT.getHolderOrThrow(Registry.MOB_EFFECT.getResourceKey(e.getEffect()).orElseThrow()).is(BzTags.BLACKLISTED_INCENSE_CANDLE_EFFECTS))) {
            return false;
        }

        boolean shapelessMatched = shapelessIngredientMatched(stackList);

        return potionCount > 0 && shapelessMatched;
    }

    public boolean shapelessIngredientMatched(List<ItemStack> stackList) {
        List<Ingredient> copiedRequiredIngredients = new ArrayList<>(this.shapelessRecipeItems);
        for (int i = copiedRequiredIngredients.size() - 1; i >= 0; i--) {
            boolean foundMatch = false;
            for (int k = stackList.size() - 1; k >= 0; k--) {
                if (copiedRequiredIngredients.get(i).test(stackList.get(k))) {
                    copiedRequiredIngredients.remove(i);
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                return false;
            }
        }
        return copiedRequiredIngredients.isEmpty();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.INCENSE_CANDLE_RECIPE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.addAll(this.shapelessRecipeItems);
        ingredients.addAll(this.shapedRecipeItems);
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public JsonObject toJson(IncenseCandleRecipe recipe) {
        return BzRecipes.INCENSE_CANDLE_RECIPE.toJson(recipe);
    }

    @Override
    public IncenseCandleRecipe fromJson(ResourceLocation id, JsonObject json) {
        return BzRecipes.INCENSE_CANDLE_RECIPE.fromJson(id, json);
    }

    @Override
    public IncenseCandleRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return BzRecipes.INCENSE_CANDLE_RECIPE.fromNetwork(id, buf);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, IncenseCandleRecipe recipe) {
        BzRecipes.INCENSE_CANDLE_RECIPE.toNetwork(buf, recipe);
    }

    public static class Serializer implements RecipeSerializer<IncenseCandleRecipe> {
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
        public IncenseCandleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
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
            boolean allowNormalPotionsRead = json.get("allowNormalPotions").getAsBoolean();
            boolean allowSplashPotionsRead = json.get("allowSplashPotions").getAsBoolean();
            boolean allowLingeringPotionsRead = json.get("allowLingeringPotions").getAsBoolean();
            int maxLevelRead = json.get("maxLevelCap").getAsInt();
            int resultCount = json.get("resultCount").getAsInt();

            return new IncenseCandleRecipe(recipeId, group, resultCount, maxPotions, shapedRecipeItems, shapelessRecipeItems, width, height, allowNormalPotionsRead, allowSplashPotionsRead, allowLingeringPotionsRead, maxLevelRead);
        }

        public JsonObject toJson(IncenseCandleRecipe recipe) {
            JsonObject json = new JsonObject();

            json.addProperty("type", Registry.RECIPE_SERIALIZER.getKey(BzRecipes.INCENSE_CANDLE_RECIPE).toString());
            json.addProperty("group", recipe.group);

            NonNullList<Ingredient> recipeIngredients = recipe.shapedRecipeItems;
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

            JsonArray shapelessRecipeJsonArray = new JsonArray();
            recipe.shapelessRecipeItems.stream().map(Ingredient::toJson).forEach(shapelessRecipeJsonArray::add);
            json.add("shapelessExtraIngredients", shapelessRecipeJsonArray);

            json.addProperty("maxAllowedPotions", recipe.maxAllowedPotions);
            json.addProperty("allowNormalPotions", recipe.allowNormalPotions);
            json.addProperty("allowSplashPotions", recipe.allowSplashPotions);
            json.addProperty("allowLingeringPotions", recipe.allowLingeringPotions);
            json.addProperty("maxLevelCap", recipe.maxLevelCap);
            json.addProperty("resultCount", recipe.result.getCount());

            return json;
        }

        @Override
        public IncenseCandleRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);

            int width = buffer.readVarInt();
            int height = buffer.readVarInt();
            NonNullList<Ingredient> shapedRecipe = NonNullList.withSize(width * height, Ingredient.EMPTY);
            shapedRecipe.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            int ingredientCount = buffer.readVarInt();
            NonNullList<Ingredient> shapelessRecipe = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            shapelessRecipe.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            int maxPotionRead = buffer.readVarInt();
            boolean allowNormalPotionsRead = buffer.readBoolean();
            boolean allowSplashPotionsRead = buffer.readBoolean();
            boolean allowLingeringPotionsRead = buffer.readBoolean();
            int maxLevelRead = buffer.readVarInt();
            int resultCountRead = buffer.readVarInt();
            return new IncenseCandleRecipe(recipeId, group, resultCountRead, maxPotionRead, shapedRecipe, shapelessRecipe, width, height, allowNormalPotionsRead, allowSplashPotionsRead, allowLingeringPotionsRead, maxLevelRead);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, IncenseCandleRecipe recipe) {
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
            buffer.writeBoolean(recipe.allowNormalPotions);
            buffer.writeBoolean(recipe.allowSplashPotions);
            buffer.writeBoolean(recipe.allowLingeringPotions);
            buffer.writeInt(recipe.maxLevelCap);
            buffer.writeInt(recipe.outputCount);
        }
    }
}