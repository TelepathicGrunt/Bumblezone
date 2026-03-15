package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.PotionCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.datamanagers.PotionCandleDataManager;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.crafting.BannerDuplicateRecipe;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PotionCandleRecipe extends CustomRecipe implements CraftingRecipe {
    private final String group;
    private final List<Ingredient> shapelessRecipeItems;
    private final ShapedRecipePattern pattern;
    private final ItemStack result;
    private final int maxAllowedPotions;
    private final boolean allowNormalPotions;
    private final boolean allowSplashPotions;
    private final boolean allowLingeringPotions;
    private final int maxLevelCap;

    public PotionCandleRecipe(
            String group,
            ItemStack result,
            ShapedRecipePattern pattern,
            List<Ingredient> shapelessRecipeItems,
            int maxAllowedPotions,
            boolean allowNormalPotions,
            boolean allowSplashPotions,
            boolean allowLingeringPotions,
            int maxLevelCap)
    {
        super();
        this.group = group;
        this.result = result;
        this.maxAllowedPotions = maxAllowedPotions;
        this.shapelessRecipeItems = shapelessRecipeItems;
        this.pattern = pattern;
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

    public List<Optional<Ingredient>> getShapedRecipeItems() {
        return this.pattern.ingredients();
    }

    public List<Ingredient> getShapelessRecipeItems() {
        return this.shapelessRecipeItems;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput) {
        MobEffect chosenEffect;
        List<MobEffect> effects = new ArrayList<>();
        AtomicInteger maxDuration = new AtomicInteger();
        AtomicInteger effectLevel = new AtomicInteger();
        AtomicInteger potionEffectsFound = new AtomicInteger();
        int splashCount = 0;
        int lingerCount = 0;

        for(int j = 0; j < craftingInput.size(); ++j) {
            ItemStack itemStack = craftingInput.getItem(j);
            if (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION)) {
                itemStack.get(DataComponents.POTION_CONTENTS).getAllEffects().forEach(me -> {
                   effects.add(me.getEffect().value());
                   maxDuration.addAndGet(me.getEffect().value().isInstantenous() ? 200 : me.getDuration());
                   effectLevel.addAndGet(me.getAmplifier() + 1);
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
            return this.result.copy();
        }

        HashSet<MobEffect> setPicker = new HashSet<>(effects);
        List<MobEffect> filteredMobEffects = setPicker.stream().filter(e -> !GeneralUtils.isInTag(BuiltInRegistries.MOB_EFFECT, BzTags.DISALLOWED_POTION_CANDLE_EFFECTS, e)).toList();
        chosenEffect = filteredMobEffects.get(new Random().nextInt(filteredMobEffects.size()));
        if (chosenEffect == null) {
            return this.result.copy();
        }

        balanceMainStats(chosenEffect, maxDuration, effectLevel, potionEffectsFound);
        effectLevel.set(Math.min(effectLevel.get(), this.maxLevelCap));

        return createTaggedPotionCandle(chosenEffect, maxDuration, effectLevel, splashCount, lingerCount, this.result.copy()).create();
    }

    public static void balanceMainStats(MobEffect chosenEffect, AtomicInteger maxDuration, AtomicInteger effectLevel, AtomicInteger potionEffectsFound) {
        effectLevel.set(effectLevel.get() / potionEffectsFound.get());

        if (PotionCandleDataManager.POTION_CANDLE_DATA_MANAGER.effectToOverrideStats.containsKey(chosenEffect)) {
            PotionCandleDataManager.OverrideData overrideData = PotionCandleDataManager.POTION_CANDLE_DATA_MANAGER.effectToOverrideStats.get(chosenEffect);
            effectLevel.set(GeneralUtils.constrainToRange(effectLevel.get(), overrideData.minLevelCap(), overrideData.maxLevelCap()));
        }

        float durationBaseMultiplier = ((0.4f / (0.9f * potionEffectsFound.get())) + (effectLevel.get() * 0.22f));
        float durationAdjustment = (potionEffectsFound.get() * durationBaseMultiplier);
        maxDuration.set((int)(maxDuration.get() / durationAdjustment));
        if (chosenEffect.isInstantenous()) {
            long thresholdTime = PotionCandleBlockEntity.getInstantEffectThresholdTime(effectLevel.intValue());
            int activationAmounts = (int)Math.ceil((double) maxDuration.intValue() / thresholdTime);
            maxDuration.set((int) (activationAmounts * thresholdTime));
        }

        if (PotionCandleDataManager.POTION_CANDLE_DATA_MANAGER.effectToOverrideStats.containsKey(chosenEffect)) {
            PotionCandleDataManager.OverrideData overrideData = PotionCandleDataManager.POTION_CANDLE_DATA_MANAGER.effectToOverrideStats.get(chosenEffect);
            maxDuration.set(GeneralUtils.constrainToRange(maxDuration.get(), overrideData.minBurnDurationCap() * 20, overrideData.maxBurnDurationCap() * 20));
        }
    }

    public static ItemStackTemplate createTaggedPotionCandle(MobEffect chosenEffect,
                                                             AtomicInteger maxDuration,
                                                             AtomicInteger effectLevel,
                                                             int splashCount,
                                                             int lingerCount,
                                                             ItemStack resultStack)
    {
        TypedEntityData<BlockEntityType<?>> blockEntityTypeTypedEntityData = resultStack.get(DataComponents.BLOCK_ENTITY_DATA);
        CompoundTag blockEntityTag = blockEntityTypeTypedEntityData == null ? new CompoundTag() : blockEntityTypeTypedEntityData.copyTagWithoutId();
        blockEntityTag.putString("id", BzBlockEntities.POTION_CANDLE.getId().toString());
        blockEntityTag.putInt(PotionCandleBlockEntity.COLOR_TAG, chosenEffect.getColor());
        blockEntityTag.putInt(PotionCandleBlockEntity.EFFECT_LEVEL_TAG, effectLevel.intValue());
        blockEntityTag.putInt(PotionCandleBlockEntity.MAX_DURATION_TAG, maxDuration.intValue());
        blockEntityTag.putString(PotionCandleBlockEntity.STATUS_EFFECT_TAG, BuiltInRegistries.MOB_EFFECT.getKey(chosenEffect).toString());
        blockEntityTag.putBoolean(PotionCandleBlockEntity.INFINITE_TAG, false);
        blockEntityTag.putInt(PotionCandleBlockEntity.RANGE_TAG, 3 + (splashCount * 2));
        if (chosenEffect.isInstantenous()) {
            blockEntityTag.putInt(PotionCandleBlockEntity.LINGER_TIME_TAG, 1);
        }
        else {
            setLingerTime(chosenEffect, lingerCount, blockEntityTag, PotionCandleBlockEntity.DEFAULT_LINGER_TIME, effectLevel.get());
        }

        if (PotionCandleDataManager.POTION_CANDLE_DATA_MANAGER.effectToOverrideStats.containsKey(chosenEffect)) {
            PotionCandleDataManager.OverrideData overrideData = PotionCandleDataManager.POTION_CANDLE_DATA_MANAGER.effectToOverrideStats.get(chosenEffect);
            overrideData.baseLingerTime().ifPresent(baseLingerTime ->
                    setLingerTime(chosenEffect, lingerCount, blockEntityTag, baseLingerTime * 20, effectLevel.get()));
        }

        resultStack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(blockEntityTypeTypedEntityData == null ? BzBlockEntities.POTION_CANDLE.get() : blockEntityTypeTypedEntityData.type(), blockEntityTag));
        return ItemStackTemplate.fromNonEmptyStack(resultStack);
    }

    private static void setLingerTime(MobEffect chosenEffect, int lingerCount, CompoundTag blockEntityTag, int baseLingerTime, int effectLevel) {
        int lingerTime = baseLingerTime + (lingerCount * baseLingerTime * 2);

        if (PotionCandleDataManager.POTION_CANDLE_DATA_MANAGER.effectToOverrideStats.containsKey(chosenEffect)) {
            PotionCandleDataManager.OverrideData overrideData = PotionCandleDataManager.POTION_CANDLE_DATA_MANAGER.effectToOverrideStats.get(chosenEffect);
            lingerTime = Math.min(lingerTime, overrideData.maxBurnDurationCap() * 20);
        }

        if (!chosenEffect.isInstantenous()) {
            int intervalCalced = PotionCandleBlockEntity.createIntervalTimeForEffectApply(Holder.direct(chosenEffect), effectLevel, lingerTime);
            lingerTime += intervalCalced;
            blockEntityTag.putInt(PotionCandleBlockEntity.CALCULATED_EFFECT_APPLY_INTERVAL_TAG, intervalCalced);
        }

        blockEntityTag.putInt(PotionCandleBlockEntity.LINGER_TIME_TAG, lingerTime);
    }

    /**
     * These are used in the neoforge mixin.
     */
    public int getWidth() {
        return this.pattern.width();
    }

    /**
     * These are used in the neoforge mixin.
     */
    public int getHeight() {
        return this.pattern.height();
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        boolean shapedMatch = false;

        for(int column = 0; column <= craftingInput.width() - this.pattern.width(); ++column) {
            for(int row = 0; row <= craftingInput.height() - this.pattern.height(); ++row) {
                if (this.matches(craftingInput, column, row, true)) {
                    shapedMatch = true;
                }

                if (this.matches(craftingInput, column, row, false)) {
                    shapedMatch = true;
                }
            }
        }
        return shapedMatch;
    }

    private boolean matches(CraftingInput craftingInput, int width, int height, boolean mirrored) {
        int potionCount = 0;
        List<ItemStack> secondaryIngredientsFound = new ArrayList<>();
        List<MobEffectInstance> mobEffects = new ObjectArrayList<>();
        for(int column = 0; column < craftingInput.width(); ++column) {
            for(int row = 0; row < craftingInput.height(); ++row) {
                ItemStack itemStack = craftingInput.getItem(column + row * craftingInput.width());
                int k = column - width;
                int l = row - height;
                Optional<Ingredient> ingredient = Optional.empty();
                if (k >= 0 && l >= 0 && k < this.pattern.width() && l < this.pattern.height()) {
                    if (mirrored) {
                        ingredient = this.pattern.ingredients().get(this.pattern.width() - k - 1 + l * this.pattern.width());
                    }
                    else {
                        ingredient = this.pattern.ingredients().get(k + l * this.pattern.width());
                    }
                }

                if (ingredient.isEmpty()) {
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

                            List<MobEffectInstance> currentMobEffects = new ArrayList<>();
                            itemStack.get(DataComponents.POTION_CONTENTS).getAllEffects().forEach(currentMobEffects::add);
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
                else if (!ingredient.get().test(itemStack)) {
                    return false;
                }
            }
        }

        if (mobEffects.stream().allMatch(e -> e.getEffect().is(BzTags.DISALLOWED_POTION_CANDLE_EFFECTS))) {
            return false;
        }

        return potionCount > 0 && GeneralUtils.listMatches(secondaryIngredientsFound, this.shapelessRecipeItems);
    }

    private static final MapCodec<PotionCandleRecipe> CODEC = RecordCodecBuilder.mapCodec(
        r -> r.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(o -> o.group),
            ItemStack.CODEC.fieldOf("result").forGetter(o -> o.result),
            ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern),
            Ingredient.CODEC.listOf().fieldOf("shapelessExtraIngredients").forGetter(shapelessRecipe -> shapelessRecipe.shapelessRecipeItems),
            Codec.intRange(1, 6).fieldOf("maxAllowedPotions").forGetter(potionRecipe -> potionRecipe.maxAllowedPotions),
            Codec.BOOL.fieldOf("allowNormalPotions").forGetter(potionRecipe -> potionRecipe.allowNormalPotions),
            Codec.BOOL.fieldOf("allowSplashPotions").forGetter(potionRecipe -> potionRecipe.allowSplashPotions),
            Codec.BOOL.fieldOf("allowLingeringPotions").forGetter(potionRecipe -> potionRecipe.allowLingeringPotions),
            Codec.intRange(1, 1000000).fieldOf("maxLevelCap").forGetter(potionRecipe -> potionRecipe.maxLevelCap)
        )
        .apply(r, PotionCandleRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PotionCandleRecipe> STREAM_CODEC = StreamCodec.of(
            PotionCandleRecipe::toNetwork, PotionCandleRecipe::fromNetwork
    );

    public static PotionCandleRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
        ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
        List<Ingredient> shapelessRecipe = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
        int maxPotionRead = buffer.readVarInt();
        boolean allowNormalPotionsRead = buffer.readBoolean();
        boolean allowSplashPotionsRead = buffer.readBoolean();
        boolean allowLingeringPotionsRead = buffer.readBoolean();
        int maxLevelRead = buffer.readVarInt();
        return new PotionCandleRecipe(group, result, pattern, shapelessRecipe, maxPotionRead, allowNormalPotionsRead, allowSplashPotionsRead, allowLingeringPotionsRead, maxLevelRead);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, PotionCandleRecipe recipe) {
        buffer.writeUtf(recipe.group);
        ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.shapelessRecipeItems);
        buffer.writeVarInt(recipe.maxAllowedPotions);
        buffer.writeBoolean(recipe.allowNormalPotions);
        buffer.writeBoolean(recipe.allowSplashPotions);
        buffer.writeBoolean(recipe.allowLingeringPotions);
        buffer.writeVarInt(recipe.maxLevelCap);
    }

    public static final RecipeSerializer<PotionCandleRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public RecipeSerializer<PotionCandleRecipe> getSerializer() {
        return BzRecipes.POTION_CANDLE_RECIPE.get();
    }
}