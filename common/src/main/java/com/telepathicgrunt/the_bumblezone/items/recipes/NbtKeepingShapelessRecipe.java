package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class NbtKeepingShapelessRecipe implements CraftingRecipe {

    private final String group;
    private final CraftingBookCategory category;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final Item itemToKeepNbtOf;

    public NbtKeepingShapelessRecipe(String string, CraftingBookCategory craftingBookCategory, ItemStack itemStack, NonNullList<Ingredient> nonNullList, Item itemToKeepNbtOf) {
        this.group = string;
        this.category = craftingBookCategory;
        this.result = itemStack;
        this.ingredients = nonNullList;
        this.itemToKeepNbtOf = itemToKeepNbtOf;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        StackedContents stackedContents = new StackedContents();
        int i = 0;
        for (int j = 0; j < craftingContainer.getContainerSize(); ++j) {
            ItemStack itemStack = craftingContainer.getItem(j);
            if (itemStack.isEmpty()) continue;
            ++i;
            stackedContents.accountStack(itemStack, 1);
        }
        return i == this.ingredients.size() && stackedContents.canCraft(this, null);
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= this.ingredients.size();
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack resultItem = this.result.copy();
        for (ItemStack input : craftingContainer.getItems()) {
            if (input.is(this.itemToKeepNbtOf)) {
                if (input.hasTag()) {
                    resultItem.setTag(input.getTag());
                    break;
                }
            }
        }

        return resultItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.NBT_KEEPING_SHAPELESS_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<NbtKeepingShapelessRecipe> {
        private static final MapCodec<NbtKeepingShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(shapelessRecipe -> shapelessRecipe.group),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(shapelessRecipe -> shapelessRecipe.category),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(shapelessRecipe -> shapelessRecipe.result),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(list -> {
                    Ingredient[] ingredients = list.toArray(Ingredient[]::new);
                    if (ingredients.length == 0) {
                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                    }
                    return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                }, DataResult::success).forGetter(shapelessRecipe -> shapelessRecipe.ingredients),
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("keep_nbt_of").forGetter(shapelessRecipe -> shapelessRecipe.itemToKeepNbtOf)
        ).apply(instance, NbtKeepingShapelessRecipe::new));

        @Override
        public Codec<NbtKeepingShapelessRecipe> codec() {
            return CODEC;
        }

        @Override
        public NbtKeepingShapelessRecipe fromNetwork(FriendlyByteBuf friendlyByteBuf) {
            String string = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            int ingredientCount = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> ingredientNonNullList = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            ingredientNonNullList.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));
            ItemStack itemStack = friendlyByteBuf.readItem();
            Item item = friendlyByteBuf.readById(BuiltInRegistries.ITEM);
            return new NbtKeepingShapelessRecipe(string, craftingBookCategory, itemStack, ingredientNonNullList, item);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, NbtKeepingShapelessRecipe shapelessRecipe) {
            friendlyByteBuf.writeUtf(shapelessRecipe.getGroup());
            friendlyByteBuf.writeEnum(shapelessRecipe.category());
            friendlyByteBuf.writeVarInt(shapelessRecipe.getIngredients().size());
            for (Ingredient ingredient : shapelessRecipe.getIngredients()) {
                ingredient.toNetwork(friendlyByteBuf);
            }
            friendlyByteBuf.writeItem(shapelessRecipe.getResultItem(RegistryAccess.EMPTY));
            friendlyByteBuf.writeId(BuiltInRegistries.ITEM, shapelessRecipe.itemToKeepNbtOf);
        }
    }
}