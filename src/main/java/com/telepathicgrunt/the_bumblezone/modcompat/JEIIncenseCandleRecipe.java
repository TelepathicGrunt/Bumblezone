package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class JEIIncenseCandleRecipe {
    public static ShapedRecipe getFakeShapedRecipe(IncenseCandleRecipe recipe, Potion potion, ItemStack potionItem, int currentRecipe) {
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
                createResultStack(recipe.getResultItem().getCount(), potionStack)
        );
    }

    private static ItemStack createResultStack(int count, ItemStack potionStack) {
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

        return IncenseCandleRecipe.createTaggedIncenseCandle(
                chosenEffect,
                maxDuration,
                amplifier,
                potionStack.getItem() instanceof SplashPotionItem ? 1 : 0,
                potionStack.getItem() instanceof LingeringPotionItem ? 1 : 0,
                count);
    }
}
