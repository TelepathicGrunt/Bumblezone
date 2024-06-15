package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ItemStackSmeltingRecipe extends SmeltingRecipe {
    protected final RecipeType<?> type;
    protected final CookingBookCategory category;
    protected final String group;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookingTime;

    public ItemStackSmeltingRecipe(String string, CookingBookCategory cookingBookCategory, Ingredient ingredient, ItemStack itemStack, float f, int i) {
        super(string, cookingBookCategory, ingredient, itemStack, f, i);
        this.type = RecipeType.SMELTING;
        this.category = cookingBookCategory;
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
    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        nonNullList.add(this.ingredient);
        return nonNullList;
    }

    public float getExperience() {
        return this.experience;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    public CookingBookCategory category() {
        return this.category;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.FURNACE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.ITEMSTACK_SMELTING_RECIPE.get();
    }

    public static class ItemStackSmeltingRecipeSerializer implements RecipeSerializer<ItemStackSmeltingRecipe> {
        private final MapCodec<ItemStackSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.fieldOf("group").forGetter(abstractCookingRecipe -> abstractCookingRecipe.group),
                CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter(abstractCookingRecipe -> abstractCookingRecipe.category),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(abstractCookingRecipe -> abstractCookingRecipe.ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(abstractCookingRecipe -> abstractCookingRecipe.result),
                Codec.FLOAT.fieldOf("experience").orElse(0.0f).forGetter(abstractCookingRecipe -> abstractCookingRecipe.experience),
                Codec.INT.fieldOf("cookingtime").orElse(200).forGetter(abstractCookingRecipe -> abstractCookingRecipe.cookingTime)
        ).apply(instance, ItemStackSmeltingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackSmeltingRecipe> STREAM_CODEC = StreamCodec.of(
                ItemStackSmeltingRecipe.ItemStackSmeltingRecipeSerializer::toNetwork, ItemStackSmeltingRecipe.ItemStackSmeltingRecipeSerializer::fromNetwork
        );

        @Override
        public MapCodec<ItemStackSmeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemStackSmeltingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static ItemStackSmeltingRecipe fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
            String string = friendlyByteBuf.readUtf();
            CookingBookCategory cookingBookCategory = friendlyByteBuf.readEnum(CookingBookCategory.class);
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(friendlyByteBuf);
            ItemStack itemStack = ItemStack.STREAM_CODEC.decode(friendlyByteBuf);
            float f = friendlyByteBuf.readFloat();
            int i = friendlyByteBuf.readVarInt();
            return new ItemStackSmeltingRecipe(string, cookingBookCategory, ingredient, itemStack, f, i);
        }

        public static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ItemStackSmeltingRecipe itemStackSmeltingRecipe) {
            friendlyByteBuf.writeUtf(itemStackSmeltingRecipe.group);
            friendlyByteBuf.writeEnum(itemStackSmeltingRecipe.category());
            Ingredient.CONTENTS_STREAM_CODEC.encode(friendlyByteBuf, itemStackSmeltingRecipe.ingredient);
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, itemStackSmeltingRecipe.result);
            friendlyByteBuf.writeFloat(itemStackSmeltingRecipe.experience);
            friendlyByteBuf.writeVarInt(itemStackSmeltingRecipe.cookingTime);
        }
    }
}

