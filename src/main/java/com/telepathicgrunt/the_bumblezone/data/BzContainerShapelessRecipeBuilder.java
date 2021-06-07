package com.telepathicgrunt.the_bumblezone.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class BzContainerShapelessRecipeBuilder {
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private String group;

    public BzContainerShapelessRecipeBuilder(IItemProvider itemProvider, int count) {
        this.result = itemProvider.asItem();
        this.count = count;
    }

    public static BzContainerShapelessRecipeBuilder shapeless(IItemProvider itemProvider) {
        return new BzContainerShapelessRecipeBuilder(itemProvider, 1);
    }

    public static BzContainerShapelessRecipeBuilder shapeless(IItemProvider itemProvider, int count) {
        return new BzContainerShapelessRecipeBuilder(itemProvider, count);
    }

    public BzContainerShapelessRecipeBuilder requires(ITag<Item> tag) {
        return this.requires(Ingredient.of(tag));
    }

    public BzContainerShapelessRecipeBuilder requires(IItemProvider itemProvider) {
        return this.requires(itemProvider, 1);
    }

    public BzContainerShapelessRecipeBuilder requires(IItemProvider itemProvider, int count) {
        for(int i = 0; i < count; ++i) {
            this.requires(Ingredient.of(itemProvider));
        }

        return this;
    }

    public BzContainerShapelessRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public BzContainerShapelessRecipeBuilder requires(Ingredient ingredient, int count) {
        for(int i = 0; i < count; ++i) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    public BzContainerShapelessRecipeBuilder unlockedBy(String critera, ICriterionInstance criterionInstance) {
        this.advancement.addCriterion(critera, criterionInstance);
        return this;
    }

    public BzContainerShapelessRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    public void save(Consumer<IFinishedRecipe> recipeConsumer) {
        this.save(recipeConsumer, Registry.ITEM.getKey(this.result));
    }

    public void save(Consumer<IFinishedRecipe> recipeConsumer, String identifier) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        if ((new ResourceLocation(identifier)).equals(resourcelocation)) {
            throw new IllegalStateException("Shapeless Recipe " + identifier + " should remove its 'save' argument");
        } else {
            this.save(recipeConsumer, new ResourceLocation(identifier));
        }
    }

    public void save(Consumer<IFinishedRecipe> recipeConsumer, ResourceLocation resourceLocation) {
        this.ensureValid(resourceLocation);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation)).rewards(AdvancementRewards.Builder.recipe(resourceLocation)).requirements(IRequirementsStrategy.OR);
        recipeConsumer.accept(new BzContainerShapelessRecipeBuilder.Result(resourceLocation, this.result, this.count, this.group == null ? "" : this.group, this.ingredients, this.advancement, new ResourceLocation(resourceLocation.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + resourceLocation.getPath())));
    }

    private void ensureValid(ResourceLocation resourceLocation) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation);
        }
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation p_i48268_1_, Item p_i48268_2_, int p_i48268_3_, String p_i48268_4_, List<Ingredient> p_i48268_5_, Advancement.Builder p_i48268_6_, ResourceLocation p_i48268_7_) {
            this.id = p_i48268_1_;
            this.result = p_i48268_2_;
            this.count = p_i48268_3_;
            this.group = p_i48268_4_;
            this.ingredients = p_i48268_5_;
            this.advancement = p_i48268_6_;
            this.advancementId = p_i48268_7_;
        }

        public void serializeRecipeData(JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }

            jsonObject.add("ingredients", jsonarray);
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonobject.addProperty("count", this.count);
            }

            jsonObject.add("result", jsonobject);
        }

        public IRecipeSerializer<?> getType() {
            return BzItems.CONTAINER_CRAFTING_RECIPE.get();
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
