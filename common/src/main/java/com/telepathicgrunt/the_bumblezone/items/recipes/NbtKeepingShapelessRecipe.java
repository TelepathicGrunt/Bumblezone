package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class NbtKeepingShapelessRecipe implements CraftingRecipe {

    private final String group;
    private final CraftingBookCategory category;
    private final ItemStack result;
    private final List<Ingredient> ingredients;
    private final Item itemToKeepNbtOf;

    @Nullable
    private PlacementInfo placementInfo;

    public NbtKeepingShapelessRecipe(String group, CraftingBookCategory category, ItemStack result, List<Ingredient> ingredients, Item itemToKeepNbtOf) {
        this.group = group;
        this.category = category;
        this.result = result;
        this.ingredients = ingredients;
        this.itemToKeepNbtOf = itemToKeepNbtOf;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != this.ingredients.size()) {
            return false;
        }
        else {
            return input.size() == 1 && this.ingredients.size() == 1
                    ? this.ingredients.getFirst().test(input.getItem(0))
                    : input.stackedContents().canCraft(this, null);
        }
    }

    @Override
    public ItemStack assemble(CraftingInput craftingContainer, HolderLookup.Provider provider) {
        ItemStack resultItem = this.result.copy();
        for (ItemStack input : craftingContainer.items()) {
            if (input.is(this.itemToKeepNbtOf)) {
                resultItem = input.transmuteCopy(resultItem.getItem(), 1);
                break;
            }
        }

        return resultItem;
    }

    @Override
    public RecipeSerializer<NbtKeepingShapelessRecipe> getSerializer() {
        return BzRecipes.NBT_KEEPING_SHAPELESS_RECIPE.get();
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredients);
        }

        return this.placementInfo;
    }

    public static class Serializer implements RecipeSerializer<NbtKeepingShapelessRecipe> {
        private static final MapCodec<NbtKeepingShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(o -> o.group),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(o -> o.category),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(o -> o.result),
                Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(o -> o.ingredients),
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("keep_nbt_of").forGetter(o -> o.itemToKeepNbtOf)
        ).apply(instance, NbtKeepingShapelessRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, NbtKeepingShapelessRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                r -> r.group,
                CraftingBookCategory.STREAM_CODEC,
                r -> r.category,
                ItemStack.STREAM_CODEC,
                r -> r.result,
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                r -> r.ingredients,
                ByteBufCodecs.registry(Registries.ITEM),
                r -> r.itemToKeepNbtOf,
                NbtKeepingShapelessRecipe::new
        );

        @Override
        public MapCodec<NbtKeepingShapelessRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NbtKeepingShapelessRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}