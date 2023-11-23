package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

public class NbtKeepingShapelessRecipe implements CraftingRecipe {

    private final String group;
    private final CraftingBookCategory category;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final Item itemToKeepNbtOf;

    public NbtKeepingShapelessRecipe(String string, CraftingBookCategory craftingBookCategory, ItemStack itemStack, NonNullList<Ingredient> nonNullList, Item itemToKeepNbtOf) {
        this.itemToKeepNbtOf = itemToKeepNbtOf;
        this.group = string;
        this.category = craftingBookCategory;
        this.result = itemStack;
        this.ingredients = nonNullList;
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

    public static class Serializer implements RecipeSerializer<NbtKeepingShapelessRecipe>, BzRecipeSerializer<NbtKeepingShapelessRecipe> {
        private static final Codec<NbtKeepingShapelessRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(shapelessRecipe -> shapelessRecipe.group),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(shapelessRecipe -> shapelessRecipe.category),
                CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(shapelessRecipe -> shapelessRecipe.result),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(list -> {
                    Ingredient[] ingredients = list.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
                    if (ingredients.length == 0) {
                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                    }
                    if (ingredients.length > 9) {
                        return DataResult.error(() -> "Too many ingredients for shapeless recipe");
                    }
                    return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                }, DataResult::success).forGetter(shapelessRecipe -> shapelessRecipe.ingredients),
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("keep_nbt_of").forGetter(shapelessRecipe -> shapelessRecipe.itemToKeepNbtOf)
        ).apply(instance, NbtKeepingShapelessRecipe::new));

        @Override
        public Codec<NbtKeepingShapelessRecipe> codec() {
            return CODEC;
        }

        public JsonObject toJson(NbtKeepingShapelessRecipe recipe) {
            JsonObject json = new JsonObject();
            json.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(BzRecipes.NBT_KEEPING_SHAPELESS_RECIPE.get()).toString());
            json.addProperty("group", recipe.getGroup());

            JsonArray ingredients = new JsonArray();
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredients.add(ingredient.toJson(true));
            }
            json.add("ingredients", ingredients);

            JsonObject result = new JsonObject();
            result.addProperty("item", BuiltInRegistries.ITEM.getKey(recipe.getResultItem(RegistryAccess.EMPTY).getItem()).toString());
            result.addProperty("count", recipe.getResultItem(RegistryAccess.EMPTY).getCount());
            json.add("result", result);
            JsonObject itemNbtToKeep = new JsonObject();
            itemNbtToKeep.addProperty("item", BuiltInRegistries.ITEM.getKey(recipe.itemToKeepNbtOf).toString());
            json.add("keep_nbt_of", itemNbtToKeep);
            return json;
        }

        @Override
        public NbtKeepingShapelessRecipe fromNetwork(FriendlyByteBuf friendlyByteBuf) {
            String string = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            int i = friendlyByteBuf.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
            nonNullList.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));
            ItemStack itemStack = friendlyByteBuf.readItem();
            Item item = friendlyByteBuf.readById(BuiltInRegistries.ITEM);
            return new NbtKeepingShapelessRecipe(string, craftingBookCategory, itemStack, nonNullList, item);
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