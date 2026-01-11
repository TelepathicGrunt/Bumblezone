package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.codec.net.BCodec;

import java.util.List;

public class ItemStackSmeltingRecipe extends AbstractCookingRecipe {
    protected final RecipeType<SmeltingRecipe> type;
    protected final CookingBookCategory category;
    protected final String group;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookingTime;

    public ItemStackSmeltingRecipe(String string, CookingBookCategory category, Ingredient ingredient, ItemStack itemStack, float f, int i) {
        super(string, category, ingredient, itemStack, f, i);
        this.type = RecipeType.SMELTING;
        this.category = category;
        this.group = string;
        this.ingredient = ingredient;
        this.result = itemStack;
        this.experience = f;
        this.cookingTime = i;
    }

    @Override
    public boolean matches(SingleRecipeInput craftingInput, Level level) {
        return this.ingredient.test(craftingInput.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput craftingInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public float experience() {
        return this.experience;
    }

    @Override
    public int cookingTime() {
        return this.cookingTime;
    }

    @Override
    public RecipeType<SmeltingRecipe> getType() {
        return this.type;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return switch (this.category()) {
            case BLOCKS -> RecipeBookCategories.FURNACE_BLOCKS;
            case FOOD -> RecipeBookCategories.FURNACE_FOOD;
            case MISC -> RecipeBookCategories.FURNACE_MISC;
        };
    }

    @Override
    public CookingBookCategory category() {
        return this.category;
    }

    @Override
    protected Item furnaceIcon() {
        return Items.FURNACE;
    }

    @Override
    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return BzRecipes.ITEMSTACK_SMELTING_RECIPE.get();
    }

    public static class ItemStackSmeltingRecipeSerializer implements RecipeSerializer<ItemStackSmeltingRecipe> {
        private final MapCodec<ItemStackSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(o -> o.group),
                CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter(o -> o.category),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(o -> o.ingredient),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(o -> o.result),
                Codec.FLOAT.fieldOf("experience").orElse(0.0f).forGetter(abstractCookingRecipe -> abstractCookingRecipe.experience),
                Codec.INT.fieldOf("cookingtime").orElse(200).forGetter(abstractCookingRecipe -> abstractCookingRecipe.cookingTime)
        ).apply(instance, ItemStackSmeltingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackSmeltingRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                r -> r.group,
                CookingBookCategory.STREAM_CODEC,
                r -> r.category,
                Ingredient.CONTENTS_STREAM_CODEC,
                r -> r.ingredient,
                ItemStack.STREAM_CODEC,
                r -> r.result,
                ByteBufCodecs.FLOAT,
                r -> r.experience,
                ByteBufCodecs.INT,
                r -> r.cookingTime,
                ItemStackSmeltingRecipe::new
        );

        @Override
        public MapCodec<ItemStackSmeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemStackSmeltingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

