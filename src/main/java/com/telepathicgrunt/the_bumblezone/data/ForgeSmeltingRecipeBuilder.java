package com.telepathicgrunt.the_bumblezone.data;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ForgeSmeltingRecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final int resultCount;
    private String group;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final CookingRecipeSerializer<?> serializer;

    private ForgeSmeltingRecipeBuilder(Ingredient ingredient, IItemProvider resultItem, int resultCount, float experience, int cookTime) {
        this.result = resultItem.asItem();
        this.ingredient = ingredient;
        this.experience = experience;
        this.cookingTime = cookTime;
        this.resultCount = resultCount;
        this.serializer = IRecipeSerializer.SMELTING_RECIPE;
    }

    public static ForgeSmeltingRecipeBuilder smelting(Ingredient ingredient, IItemProvider resultItem, int resultCount, float experience, int cookTime) {
        return new ForgeSmeltingRecipeBuilder(ingredient, resultItem, resultCount, experience, cookTime);
    }

    public ForgeSmeltingRecipeBuilder unlockedBy(String p_218628_1_, ICriterionInstance p_218628_2_) {
        this.advancement.addCriterion(p_218628_1_, p_218628_2_);
        return this;
    }

    public void save(Consumer<IFinishedRecipe> consumer) {
        this.save(consumer, Registry.ITEM.getKey(this.result));
    }

    public void save(Consumer<IFinishedRecipe> consumer, String identifier) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        ResourceLocation resourcelocation1 = new ResourceLocation(identifier);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        } else {
            this.save(consumer, resourcelocation1);
        }
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation resourceLocation) {
        this.ensureValid(resourceLocation);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation)).rewards(AdvancementRewards.Builder.recipe(resourceLocation)).requirements(IRequirementsStrategy.OR);
        consumer.accept(new ForgeSmeltingRecipeBuilder.Result(resourceLocation, this.group == null ? "" : this.group, this.ingredient, this.result, this.resultCount, this.experience, this.cookingTime, this.advancement, new ResourceLocation(resourceLocation.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + resourceLocation.getPath()), this.serializer));
    }

    private void ensureValid(ResourceLocation resourceLocation) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation);
        }
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final Item result;
        private final float experience;
        private final int cookingTime;
        private final int resultCount;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<? extends AbstractCookingRecipe> serializer;

        public Result(ResourceLocation id, String group, Ingredient ingredient, Item result, int resultCount, float experience, int cookTime, Advancement.Builder advancement, ResourceLocation advancementId, IRecipeSerializer<? extends AbstractCookingRecipe> recipeSerializer) {
            this.id = id;
            this.group = group;
            this.ingredient = ingredient;
            this.result = result;
            this.experience = experience;
            this.cookingTime = cookTime;
            this.resultCount = resultCount;
            this.advancement = advancement;
            this.advancementId = advancementId;
            this.serializer = recipeSerializer;
        }

        public void serializeRecipeData(JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            jsonObject.add("ingredient", this.ingredient.toJson());
            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            resultObject.addProperty("count", this.resultCount);
            jsonObject.add("result", resultObject);
            jsonObject.addProperty("experience", this.experience);
            jsonObject.addProperty("cookingtime", this.cookingTime);
        }

        public IRecipeSerializer<?> getType() {
            return this.serializer;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}