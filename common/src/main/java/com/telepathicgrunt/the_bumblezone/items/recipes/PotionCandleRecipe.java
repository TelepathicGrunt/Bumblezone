package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.PotionCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.mixin.containers.ShapedRecipePatternAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class PotionCandleRecipe extends CustomRecipe implements CraftingRecipe {
    private final String group;
    private final CraftingBookCategory category;
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

    public PotionCandleRecipe(
            CraftingBookCategory category,
            String group,
            int outputCount,
            int maxAllowedPotions,
            NonNullList<Ingredient> shapedRecipeItems,
            NonNullList<Ingredient> shapelessRecipeItems,
            int width,
            int height,
            boolean allowNormalPotions,
            boolean allowSplashPotions,
            boolean allowLingeringPotions,
            int maxLevelCap)
    {
        super(category);
        this.group = group;
        this.category = category;
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
        ItemStack stack = BzItems.POTION_CANDLE.get().getDefaultInstance();
        stack.setCount(outputCountIn);
        return stack;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
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
        List<MobEffect> filteredMobEffects = setPicker.stream().filter(e -> !GeneralUtils.isInTag(BuiltInRegistries.MOB_EFFECT, BzTags.DISALLOWED_POTION_CANDLE_EFFECTS, e)).toList();
        chosenEffect = filteredMobEffects.get(new Random().nextInt(filteredMobEffects.size()));
        if (chosenEffect == null) {
            return getResultStack(this.outputCount);
        }

        balanceMainStats(chosenEffect, maxDuration, amplifier, potionEffectsFound);
        amplifier.set(Math.min(amplifier.get(), this.maxLevelCap));

        return createTaggedPotionCandle(chosenEffect, maxDuration, amplifier, splashCount, lingerCount, this.outputCount);
    }

    public static void balanceMainStats(MobEffect chosenEffect, AtomicInteger maxDuration, AtomicInteger amplifier, AtomicInteger potionEffectsFound) {
        amplifier.set(amplifier.get() / potionEffectsFound.get());

        float durationBaseMultiplier = ((0.4f / (0.9f * potionEffectsFound.get())) + (amplifier.get() * 0.22f));
        float durationAdjustment = (potionEffectsFound.get() * durationBaseMultiplier);
        maxDuration.set((int)(maxDuration.get() / durationAdjustment));
        if (chosenEffect.isInstantenous()) {
            long thresholdTime = PotionCandleBlockEntity.getInstantEffectThresholdTime(amplifier.intValue());
            int activationAmounts = (int)Math.ceil((double) maxDuration.intValue() / thresholdTime);
            maxDuration.set((int) (activationAmounts * thresholdTime));
        }
        else {
            if (GeneralUtils.isInTag(BuiltInRegistries.MOB_EFFECT, BzTags.TEN_SECONDS_POTION_CANDLE_EFFECTS, chosenEffect)) {
                maxDuration.set(Math.min(200, maxDuration.get()));
            }
            else if (GeneralUtils.isInTag(BuiltInRegistries.MOB_EFFECT, BzTags.ONE_MINUTE_POTION_CANDLE_EFFECTS, chosenEffect)) {
                maxDuration.set(Math.min(1200, maxDuration.get()));
            }
        }
    }

    public static ItemStack createTaggedPotionCandle(MobEffect chosenEffect,
                                                     AtomicInteger maxDuration,
                                                     AtomicInteger amplifier,
                                                     int splashCount,
                                                     int lingerCount,
                                                     int outputCount) {
        ItemStack resultStack = getResultStack(outputCount);

        CompoundTag tag = resultStack.getOrCreateTag();
        CompoundTag blockEntityTag = new CompoundTag();
        tag.put("BlockEntityTag", blockEntityTag);
        blockEntityTag.putInt(PotionCandleBlockEntity.COLOR_TAG, chosenEffect.getColor());
        blockEntityTag.putInt(PotionCandleBlockEntity.AMPLIFIER_TAG, amplifier.intValue());
        blockEntityTag.putInt(PotionCandleBlockEntity.MAX_DURATION_TAG, maxDuration.intValue());
        blockEntityTag.putString(PotionCandleBlockEntity.STATUS_EFFECT_TAG, BuiltInRegistries.MOB_EFFECT.getKey(chosenEffect).toString());
        blockEntityTag.putBoolean(PotionCandleBlockEntity.INFINITE_TAG, false);
        blockEntityTag.putInt(PotionCandleBlockEntity.RANGE_TAG, 3 + (splashCount * 2));
        if (chosenEffect.isInstantenous()) {
            blockEntityTag.putInt(PotionCandleBlockEntity.LINGER_TIME_TAG, 1);
        }
        else if (chosenEffect == MobEffects.NIGHT_VISION) {
            setLingerTime(chosenEffect, lingerCount, blockEntityTag, PotionCandleBlockEntity.DEFAULT_NIGHT_VISION_LINGER_TIME);
        }
        else {
            setLingerTime(chosenEffect, lingerCount, blockEntityTag, PotionCandleBlockEntity.DEFAULT_LINGER_TIME);
        }
        return resultStack;
    }

    private static void setLingerTime(MobEffect chosenEffect, int lingerCount, CompoundTag blockEntityTag, int baseLingerTime) {
        int lingerTime = baseLingerTime + (lingerCount * baseLingerTime * 2);

        if (GeneralUtils.isInTag(BuiltInRegistries.MOB_EFFECT, BzTags.TEN_SECONDS_POTION_CANDLE_EFFECTS, chosenEffect)) {
            lingerTime = Math.min(200, lingerTime);
        }
        else if (GeneralUtils.isInTag(BuiltInRegistries.MOB_EFFECT, BzTags.ONE_MINUTE_POTION_CANDLE_EFFECTS, chosenEffect)) {
            lingerTime = Math.min(1200, lingerTime);
        }

        blockEntityTag.putInt(PotionCandleBlockEntity.LINGER_TIME_TAG, lingerTime);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    /**
     * These are used in the neoforge mixin.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * These are used in the neoforge mixin.
     */
    public int getHeight() {
        return this.height;
    }


    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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
        List<ItemStack> secondaryIngredientsFound = new ArrayList<>();
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
                            secondaryIngredientsFound.add(itemStack);
                        }
                    }
                }
                else if (!ingredient.test(itemStack)) {
                    return false;
                }
            }
        }

        if (mobEffects.stream().allMatch(e -> GeneralUtils.isInTag(BuiltInRegistries.MOB_EFFECT, BzTags.DISALLOWED_POTION_CANDLE_EFFECTS, e.getEffect()))) {
            return false;
        }

        return potionCount > 0 && GeneralUtils.listMatches(secondaryIngredientsFound, this.shapelessRecipeItems);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.POTION_CANDLE_RECIPE.get();
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
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public CraftingBookCategory category() {
        return category;
    }

    public static class Serializer implements RecipeSerializer<PotionCandleRecipe> {

        private static final Codec<PotionCandleRecipe> CODEC = PotionCandleRecipe.Serializer.RawPotionRecipe.CODEC.flatXmap(rawShapedRecipe -> {
            String[] strings = ShapedRecipePatternAccessor.callShrink(rawShapedRecipe.shapedPattern);
            int width = strings[0].length();
            int height = strings.length;
            NonNullList<Ingredient> shapedRecipeItems = NonNullList.withSize(width * height, Ingredient.EMPTY);
            HashSet<String> set = Sets.newHashSet(rawShapedRecipe.shapedKey.keySet());
            for (int k = 0; k < strings.length; ++k) {
                String string = strings[k];
                for (int l = 0; l < string.length(); ++l) {
                    String string2 = string.substring(l, l + 1);
                    Ingredient ingredient = string2.equals(" ") ? Ingredient.EMPTY : rawShapedRecipe.shapedKey.get(string2);
                    if (ingredient == null) {
                        return DataResult.error(() -> "Pattern references symbol '" + string2 + "' but it's not defined in the shapedKey");
                    }
                    set.remove(string2);
                    shapedRecipeItems.set(l + width * k, ingredient);
                }
            }
            if (!set.isEmpty()) {
                return DataResult.error(() -> "Key defines symbols that aren't used in shapedPattern: " + set);
            }
            PotionCandleRecipe potionCandleRecipe = new PotionCandleRecipe(
                    rawShapedRecipe.category,
                    rawShapedRecipe.group,
                    rawShapedRecipe.resultCount(),
                    rawShapedRecipe.maxAllowedPotions(),
                    shapedRecipeItems,
                    rawShapedRecipe.shapelessIngredients(),
                    width,
                    height,
                    rawShapedRecipe.allowNormalPotions(),
                    rawShapedRecipe.allowSplashPotions(),
                    rawShapedRecipe.allowLingeringPotions(),
                    rawShapedRecipe.maxLevelCap());

            return DataResult.success(potionCandleRecipe);
        },
        potionCandleRecipe -> {
            throw new NotImplementedException("Serializing potionCandleRecipe is not implemented yet.");
        });

        @Override
        public Codec<PotionCandleRecipe> codec() {
            return CODEC;
        }

        @Override
        public PotionCandleRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            String category = buffer.readUtf(32767);

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
            return new PotionCandleRecipe(CraftingBookCategory.valueOf(category.toUpperCase(Locale.ROOT)), group, resultCountRead, maxPotionRead, shapedRecipe, shapelessRecipe, width, height, allowNormalPotionsRead, allowSplashPotionsRead, allowLingeringPotionsRead, maxLevelRead);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PotionCandleRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeUtf(recipe.category.getSerializedName());

            buffer.writeVarInt(recipe.width);
            buffer.writeVarInt(recipe.height);
            for(Ingredient ingredient : recipe.shapedRecipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeVarInt(recipe.shapelessRecipeItems.size());
            for (Ingredient ingredient : recipe.shapelessRecipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeVarInt(recipe.maxAllowedPotions);
            buffer.writeBoolean(recipe.allowNormalPotions);
            buffer.writeBoolean(recipe.allowSplashPotions);
            buffer.writeBoolean(recipe.allowLingeringPotions);
            buffer.writeVarInt(recipe.maxLevelCap);
            buffer.writeVarInt(recipe.outputCount);
        }

        record RawPotionRecipe(String group,
                               CraftingBookCategory category,
                               Map<String, Ingredient> shapedKey,
                               List<String> shapedPattern,
                               NonNullList<Ingredient> shapelessIngredients,
                               int maxAllowedPotions,
                               boolean allowNormalPotions,
                               boolean allowSplashPotions,
                               boolean allowLingeringPotions,
                               int maxLevelCap,
                               int resultCount)
        {
            static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap((list) -> {
                if (list.size() > 3) {
                    return DataResult.error(() -> "Invalid pattern: too many rows, 3 is maximum");
                }
                else if (list.isEmpty()) {
                    return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
                }
                else {
                    int length = list.get(0).length();
                    Iterator<String> var2 = list.iterator();
                    String string;
                    do {
                        if (!var2.hasNext()) {
                            return DataResult.success(list);
                        }

                        string = var2.next();
                        if (string.length() > 3) {
                            return DataResult.error(() -> "Invalid pattern: too many columns, 3 is maximum");
                        }
                    }
                    while(length == string.length());

                    return DataResult.error(() -> "Invalid pattern: each row must be the same width");
                }
            }, Function.identity());

            static final Codec<String> SINGLE_CHARACTER_STRING_CODEC = Codec.STRING.comapFlatMap((string) -> {
                if (string.length() != 1) {
                    return DataResult.error(() -> "Invalid key entry: '" + string + "' is an invalid symbol (must be 1 character only).");
                }
                else {
                    return " ".equals(string) ?
                            DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") :
                            DataResult.success(String.valueOf(string.charAt(0)));
                }
            }, String::valueOf);

            public static final Codec<RawPotionRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(potionRecipe -> potionRecipe.group),
                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(potionRecipe -> potionRecipe.category),
                    ExtraCodecs.strictUnboundedMap(SINGLE_CHARACTER_STRING_CODEC, Ingredient.CODEC_NONEMPTY).fieldOf("shapedKey").forGetter(potionRecipe -> potionRecipe.shapedKey),
                    PATTERN_CODEC.fieldOf("shapedPattern").forGetter(potionRecipe -> potionRecipe.shapedPattern),
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("shapelessExtraIngredients").flatXmap(list -> {
                        Ingredient[] ingredients = list.toArray(Ingredient[]::new);
                        if (ingredients.length == 0) {
                            return DataResult.error(() -> "No ingredients for shapeless recipe");
                        }
                        return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                    }, DataResult::success).forGetter(shapelessRecipe -> shapelessRecipe.shapelessIngredients),
                    Codec.intRange(1, 6).fieldOf("maxAllowedPotions").forGetter(potionRecipe -> potionRecipe.maxAllowedPotions),
                    Codec.BOOL.fieldOf("allowNormalPotions").forGetter(potionRecipe -> potionRecipe.allowNormalPotions),
                    Codec.BOOL.fieldOf("allowSplashPotions").forGetter(potionRecipe -> potionRecipe.allowSplashPotions),
                    Codec.BOOL.fieldOf("allowLingeringPotions").forGetter(potionRecipe -> potionRecipe.allowLingeringPotions),
                    Codec.intRange(1, 1000000).fieldOf("maxLevelCap").forGetter(potionRecipe -> potionRecipe.maxLevelCap),
                    Codec.intRange(1, 64).fieldOf("resultCount").forGetter(potionRecipe -> potionRecipe.resultCount)
            ).apply(instance, RawPotionRecipe::new));
        }
    }
}