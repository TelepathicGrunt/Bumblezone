package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
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
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
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
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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
                ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(abstractCookingRecipe -> abstractCookingRecipe.group),
                CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter(abstractCookingRecipe -> abstractCookingRecipe.category),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(abstractCookingRecipe -> abstractCookingRecipe.ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(abstractCookingRecipe -> abstractCookingRecipe.result),
                Codec.FLOAT.fieldOf("experience").orElse(0.0f).forGetter(abstractCookingRecipe -> abstractCookingRecipe.experience),
                Codec.INT.fieldOf("cookingtime").orElse(200).forGetter(abstractCookingRecipe -> abstractCookingRecipe.cookingTime)
        ).apply(instance, ItemStackSmeltingRecipe::new));

        @Override
        public Codec<ItemStackSmeltingRecipe> codec() {
            return this.codec;
        }

        @Override
        public ItemStackSmeltingRecipe fromNetwork(FriendlyByteBuf friendlyByteBuf) {
            String string = friendlyByteBuf.readUtf();
            CookingBookCategory cookingBookCategory = friendlyByteBuf.readEnum(CookingBookCategory.class);
            Ingredient ingredient = Ingredient.fromNetwork(friendlyByteBuf);
            ItemStack itemStack = friendlyByteBuf.readItem();
            float f = friendlyByteBuf.readFloat();
            int i = friendlyByteBuf.readVarInt();
            return new ItemStackSmeltingRecipe(string, cookingBookCategory, ingredient, itemStack, f, i);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ItemStackSmeltingRecipe itemStackSmeltingRecipe) {
            friendlyByteBuf.writeUtf(itemStackSmeltingRecipe.group);
            friendlyByteBuf.writeEnum(itemStackSmeltingRecipe.category());
            itemStackSmeltingRecipe.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeItem(itemStackSmeltingRecipe.result);
            friendlyByteBuf.writeFloat(itemStackSmeltingRecipe.experience);
            friendlyByteBuf.writeVarInt(itemStackSmeltingRecipe.cookingTime);
        }
    }
}

