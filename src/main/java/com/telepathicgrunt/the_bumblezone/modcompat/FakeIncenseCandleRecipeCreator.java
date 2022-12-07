package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class FakeIncenseCandleRecipeCreator {

    public static List<CraftingRecipe> constructFakeRecipes(IncenseCandleRecipe incenseCandleRecipe, boolean oneRecipeOnly) {
        List<CraftingRecipe> extraRecipes = new ArrayList<>();
        int currentRecipe = 0;
        Set<MobEffect> effects = new HashSet<>();
        List<Potion> potions = new ArrayList<>();
        for (Potion potion : Registry.POTION) {
            if (oneRecipeOnly && potions.size() > 0) {
                break;
            }

            if (potion.getEffects().stream().allMatch(e -> effects.contains(e.getEffect()) || Registry.MOB_EFFECT.getHolderOrThrow(Registry.MOB_EFFECT.getResourceKey(e.getEffect()).orElseThrow()).is(BzTags.BLACKLISTED_INCENSE_CANDLE_EFFECTS))) {
                continue;
            }

            potion.getEffects().forEach(e -> effects.add(e.getEffect()));
            potions.add(potion);
        }
        potions.sort(Comparator.comparingInt(a -> a.getEffects().size()));
        for (Potion potion : potions) {
            if (potion.getEffects().stream().allMatch(e -> Registry.MOB_EFFECT.getHolderOrThrow(Registry.MOB_EFFECT.getResourceKey(e.getEffect()).orElseThrow()).is(BzTags.BLACKLISTED_INCENSE_CANDLE_EFFECTS))) {
                continue;
            }

            addRecipeIfValid(extraRecipes, FakeIncenseCandleRecipeCreator.getFakeShapedRecipe(incenseCandleRecipe, potion, Items.POTION.getDefaultInstance(), currentRecipe));
            currentRecipe++;
            addRecipeIfValid(extraRecipes, FakeIncenseCandleRecipeCreator.getFakeShapedRecipe(incenseCandleRecipe, potion, Items.SPLASH_POTION.getDefaultInstance(), currentRecipe));
            currentRecipe++;
            addRecipeIfValid(extraRecipes, FakeIncenseCandleRecipeCreator.getFakeShapedRecipe(incenseCandleRecipe, potion, Items.LINGERING_POTION.getDefaultInstance(), currentRecipe));
            currentRecipe++;
        }
        return extraRecipes;
    }

    private static void addRecipeIfValid(List<CraftingRecipe> extraRecipes, ShapedRecipe recipe) {
        if (!recipe.getResultItem().isEmpty()) {
            extraRecipes.add(recipe);
        }
    }

    private static ShapedRecipe getFakeShapedRecipe(IncenseCandleRecipe recipe, Potion potion, ItemStack potionItem, int currentRecipe) {
        ItemStack potionStack = PotionUtils.setPotion(potionItem, potion);

        List<Ingredient> fakedShapedIngredientsMutable = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            fakedShapedIngredientsMutable.add(Ingredient.EMPTY);
        }

        int currentShapedIndex = 0;
        int shapedRecipeSize = recipe.getShapedRecipeItems().size();
        for (int x = 0; x < recipe.getRecipeWidth(); x++) {
            for (int z = 0; z < recipe.getRecipeHeight(); z++) {
                if (currentShapedIndex >= shapedRecipeSize) {
                    continue;
                }

                Ingredient ingredient = recipe.getShapedRecipeItems().get(currentShapedIndex);
                fakedShapedIngredientsMutable.set(x + (z * 3), ingredient);
                currentShapedIndex++;
            }
        }

        int currentShapelessIndex = 0;
        int shapelessRecipeSize = recipe.getShapelessRecipeItems().size();
        for (int i = 0; i < 9; i++) {
            Ingredient ingredient = fakedShapedIngredientsMutable.get(i);
            if (ingredient.isEmpty()) {
                if (currentShapelessIndex >= shapelessRecipeSize) {
                    fakedShapedIngredientsMutable.set(i, Ingredient.of(potionStack));
                    break;
                }

                fakedShapedIngredientsMutable.set(i, recipe.getShapelessRecipeItems().get(currentShapelessIndex));
                currentShapelessIndex++;
            }
        }

        NonNullList<Ingredient> fakedShapedIngredients = NonNullList.create();
        fakedShapedIngredients.addAll(fakedShapedIngredientsMutable);

        return new ShapedRecipe(
                new ResourceLocation(Bumblezone.MODID, recipe.getId().getPath() + "_" + currentRecipe),
                Bumblezone.MODID,
                3,
                3,
                fakedShapedIngredients,
                createResultStack(recipe, potionStack)
        );
    }

    private static ItemStack createResultStack(IncenseCandleRecipe recipe, ItemStack potionStack) {
        List<MobEffect> effects = new ArrayList<>();
        AtomicInteger maxDuration = new AtomicInteger();
        AtomicInteger amplifier = new AtomicInteger();
        AtomicInteger potionEffectsFound = new AtomicInteger();

        PotionUtils.getMobEffects(potionStack).forEach(me -> {
            effects.add(me.getEffect());
            maxDuration.addAndGet(me.getEffect().isInstantenous() ? 200 : me.getDuration());
            amplifier.addAndGet(me.getAmplifier() + 1);
            potionEffectsFound.getAndIncrement();
        });

        if (effects.isEmpty()) {
            return ItemStack.EMPTY;
        }

        HashSet<MobEffect> setPicker = new HashSet<>(effects);
        MobEffect chosenEffect = setPicker.stream().toList().get(new Random().nextInt(setPicker.size()));
        if (chosenEffect == null) {
            return ItemStack.EMPTY;
        }

        IncenseCandleRecipe.balanceStats(chosenEffect, maxDuration, amplifier, potionEffectsFound);
        amplifier.set(Math.min(amplifier.get(), recipe.getMaxLevelCap()));

        return IncenseCandleRecipe.createTaggedIncenseCandle(
                chosenEffect,
                maxDuration,
                amplifier,
                potionStack.getItem() instanceof SplashPotionItem ? 1 : 0,
                potionStack.getItem() instanceof LingeringPotionItem ? 1 : 0,
                recipe.getResultItem().getCount());
    }
}
