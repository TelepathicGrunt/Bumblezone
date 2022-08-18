package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class JEIIncenseCandleRecipe {
    public static ShapedRecipe getFakeShapedRecipe(IncenseCandleRecipe recipe, Potion potion, ItemStack potionItem) {
        List<Ingredient> fakedShapedIngredientsMutable = new ArrayList<>();
        fakedShapedIngredientsMutable.addAll(recipe.getShapedRecipeItems());
        fakedShapedIngredientsMutable.addAll(recipe.getShapelessRecipeItems());
        fakedShapedIngredientsMutable.add(Ingredient.of(potionItem));

        NonNullList<Ingredient> fakedShapedIngredients = NonNullList.create();
        fakedShapedIngredients.addAll(fakedShapedIngredientsMutable);

        return new ShapedRecipe(
                recipe.getId(),
                recipe.getGroup(),
                recipe.getWidth() + 1,
                recipe.getHeight(),
                fakedShapedIngredients,
                createResultStack(recipe.getResultItem().getCount(), potion, potionItem)
        );
    }

    private static ItemStack createResultStack(int count, Potion potion, ItemStack potionItem) {
        List<MobEffect> effects = new ArrayList<>();
        AtomicInteger maxDuration = new AtomicInteger();
        AtomicInteger amplifier = new AtomicInteger();
        AtomicInteger potionEffectsFound = new AtomicInteger();

        ItemStack potionStack = PotionUtils.setCustomEffects(potionItem, potion.getEffects());
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
                0,
                0,
                count);
    }
}
