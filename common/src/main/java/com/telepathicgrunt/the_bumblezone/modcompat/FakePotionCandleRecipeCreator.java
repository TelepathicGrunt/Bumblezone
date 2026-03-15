package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.items.recipes.PotionCandleRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class FakePotionCandleRecipeCreator {

    public static List<CraftingRecipe> constructFakeRecipes(PotionCandleRecipe potionCandleRecipe, boolean oneRecipeOnly) {
        List<CraftingRecipe> extraRecipes = new ArrayList<>();
        Set<MobEffect> effects = new HashSet<>();
        List<Holder<Potion>> potions = new ArrayList<>();
        for (Identifier potionKey : BuiltInRegistries.POTION.keySet()) {
            Optional<Holder.Reference<Potion>> potion = BuiltInRegistries.POTION.get(potionKey);
            if (oneRecipeOnly && !potions.isEmpty()) {
                break;
            }

            if (potion.isEmpty() || potion.get().value().getEffects().stream().allMatch(e ->
                effects.contains(e.getEffect().value()) || BuiltInRegistries.MOB_EFFECT.getOrThrow(BuiltInRegistries.MOB_EFFECT.getResourceKey(e.getEffect().value()).orElseThrow()).is(BzTags.DISALLOWED_POTION_CANDLE_EFFECTS)))
            {
                continue;
            }

            potion.get().value().getEffects().forEach(e -> effects.add(e.getEffect().value()));
            potions.add(potion.get());
        }
        potions.sort(Comparator.comparingInt(a -> a.value().getEffects().size()));
        for (Holder<Potion> potion : potions) {
            if (potion.value().getEffects().stream().allMatch(e -> e.getEffect().is(BzTags.DISALLOWED_POTION_CANDLE_EFFECTS))) {
                continue;
            }

            addRecipeIfValid(extraRecipes, FakePotionCandleRecipeCreator.getFakeShapedRecipe(potionCandleRecipe, potion, Items.POTION.getDefaultInstance()));
            addRecipeIfValid(extraRecipes, FakePotionCandleRecipeCreator.getFakeShapedRecipe(potionCandleRecipe, potion, Items.SPLASH_POTION.getDefaultInstance()));
            addRecipeIfValid(extraRecipes, FakePotionCandleRecipeCreator.getFakeShapedRecipe(potionCandleRecipe, potion, Items.LINGERING_POTION.getDefaultInstance()));
        }
        return extraRecipes;
    }

    private static void addRecipeIfValid(List<CraftingRecipe> extraRecipes, ShapedRecipe recipe) {
        extraRecipes.add(recipe);
    }

    private static ShapedRecipe getFakeShapedRecipe(PotionCandleRecipe recipe, Holder<Potion> potion, ItemStack potionItem) {
        ItemStack potionStack = PotionContents.createItemStack(potionItem.getItem(), potion);

        List<Optional<Ingredient>> fakedShapedIngredientsMutable = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            fakedShapedIngredientsMutable.add(Optional.empty());
        }

        int currentShapedIndex = 0;
        int shapedRecipeSize = recipe.getShapedRecipeItems().size();
        for (int x = 0; x < recipe.getWidth(); x++) {
            for (int z = 0; z < recipe.getHeight(); z++) {
                if (currentShapedIndex >= shapedRecipeSize) {
                    continue;
                }

                Optional<Ingredient> ingredient = recipe.getShapedRecipeItems().get(currentShapedIndex);
                fakedShapedIngredientsMutable.set(x + (z * 3), ingredient);
                currentShapedIndex++;
            }
        }

        int currentShapelessIndex = 0;
        int shapelessRecipeSize = recipe.getShapelessRecipeItems().size();
        for (int i = 0; i < 9; i++) {
            Optional<Ingredient> ingredient = fakedShapedIngredientsMutable.get(i);
            if (ingredient.isEmpty()) {
                if (currentShapelessIndex >= shapelessRecipeSize) {
                    fakedShapedIngredientsMutable.set(i, Optional.of(Ingredient.of(potionStack.getItem())));
                    break;
                }

                fakedShapedIngredientsMutable.set(i, Optional.of(recipe.getShapelessRecipeItems().get(currentShapelessIndex)));
                currentShapelessIndex++;
            }
        }

        NonNullList<Optional<Ingredient>> fakedShapedIngredients = NonNullList.create();
        fakedShapedIngredients.addAll(fakedShapedIngredientsMutable);

        return new ShapedRecipe(
                new Recipe.CommonInfo(false),
                new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, "the_bumblezone:potion_candle"),
                new ShapedRecipePattern(3, 3, fakedShapedIngredients, Optional.empty()),
                createResultStack(recipe, potionStack)
        );
    }

    private static ItemStackTemplate createResultStack(PotionCandleRecipe recipe, ItemStack potionStack) {
        List<MobEffect> effects = new ArrayList<>();
        AtomicInteger maxDuration = new AtomicInteger();
        AtomicInteger effectLevel = new AtomicInteger();
        AtomicInteger potionEffectsFound = new AtomicInteger();

        potionStack.get(DataComponents.POTION_CONTENTS).getAllEffects().forEach(me -> {
            effects.add(me.getEffect().value());
            maxDuration.addAndGet(me.getEffect().value().isInstantenous() ? 200 : me.getDuration());
            effectLevel.addAndGet(me.getAmplifier() + 1);
            potionEffectsFound.getAndIncrement();
        });

        if (effects.isEmpty()) {
            return null;
        }

        HashSet<MobEffect> setPicker = new HashSet<>(effects);
        MobEffect chosenEffect = setPicker.stream().toList().get(new Random().nextInt(setPicker.size()));
        if (chosenEffect == null) {
            return null;
        }

        PotionCandleRecipe.balanceMainStats(chosenEffect, maxDuration, effectLevel, potionEffectsFound);
        effectLevel.set(Math.min(effectLevel.get(), recipe.getMaxLevelCap()));

        return PotionCandleRecipe.createTaggedPotionCandle(
                chosenEffect,
                maxDuration,
                effectLevel,
                potionStack.getItem() instanceof SplashPotionItem ? 1 : 0,
                potionStack.getItem() instanceof LingeringPotionItem ? 1 : 0,
                recipe.assemble(CraftingInput.EMPTY));
    }
}
