package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Map;

import static java.util.Map.entry;

public class ContainerCraftingRecipe implements CraftingRecipe {
    private final String group;
    private final CraftingBookCategory category;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    public static final Map<Item, Item> HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET = Map.ofEntries(
            entry(Items.POWDER_SNOW_BUCKET, Items.BUCKET),
            entry(Items.AXOLOTL_BUCKET, Items.BUCKET),
            entry(Items.COD_BUCKET, Items.BUCKET),
            entry(Items.PUFFERFISH_BUCKET, Items.BUCKET),
            entry(Items.SALMON_BUCKET, Items.BUCKET),
            entry(Items.TROPICAL_FISH_BUCKET, Items.BUCKET),
            entry(Items.SUSPICIOUS_STEW, Items.BOWL),
            entry(Items.MUSHROOM_STEW, Items.BOWL),
            entry(Items.RABBIT_STEW, Items.BOWL),
            entry(Items.BEETROOT_SOUP, Items.BOWL),
            entry(Items.POTION, Items.GLASS_BOTTLE),
            entry(Items.SPLASH_POTION, Items.GLASS_BOTTLE),
            entry(Items.LINGERING_POTION, Items.GLASS_BOTTLE),
            entry(Items.EXPERIENCE_BOTTLE, Items.GLASS_BOTTLE)
    );

    public ContainerCraftingRecipe(String groupIn, CraftingBookCategory craftingBookCategory, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        this.group = groupIn;
        this.category = craftingBookCategory;
        this.result = recipeOutputIn;
        this.ingredients = recipeItemsIn;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BzRecipes.CONTAINER_CRAFTING_RECIPE.get();
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
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= this.ingredients.size();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remainingInv = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        int containerOutput = PlatformHooks.hasCraftingRemainder(this.result) ? this.result.getCount() : 0;

        for(int i = 0; i < remainingInv.size(); ++i) {
            ItemStack craftingInput = inv.getItem(i);
            ItemStack craftingContainer = PlatformHooks.getCraftingRemainder(craftingInput);
            ItemStack recipeContainer = PlatformHooks.getCraftingRemainder(this.result);
            if (craftingContainer.isEmpty() && HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.containsKey(craftingInput.getItem())) {
                craftingContainer = HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.get(craftingInput.getItem()).getDefaultInstance();
            }
            if (recipeContainer.isEmpty() && HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.containsKey(this.result.getItem())) {
                recipeContainer = HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.get(this.result.getItem()).getDefaultInstance();
            }

            if (!craftingContainer.isEmpty()) {
                if(containerOutput > 0 &&
                    (this.result.getItem() == craftingContainer.getItem() ||
                    recipeContainer.getItem() == craftingInput.getItem() ||
                    recipeContainer.getItem() == craftingContainer.getItem()))
                {
                    containerOutput--;
                }
                else {
                    remainingInv.set(i, craftingContainer);
                }
            }
        }

        return remainingInv;
    }

    public static class Serializer implements RecipeSerializer<ContainerCraftingRecipe> {
        private static final Codec<ContainerCraftingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(shapelessRecipe -> shapelessRecipe.group),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(shapelessRecipe -> shapelessRecipe.category),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(shapelessRecipe -> shapelessRecipe.result),
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(list -> {
                    Ingredient[] ingredients = list.toArray(Ingredient[]::new);
                    if (ingredients.length == 0) {
                        return DataResult.error(() -> "No ingredients for shapeless recipe");
                    }
                    return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                }, DataResult::success).forGetter(shapelessRecipe -> shapelessRecipe.ingredients)
        ).apply(instance, ContainerCraftingRecipe::new));

        @Override
        public Codec<ContainerCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public ContainerCraftingRecipe fromNetwork(FriendlyByteBuf buffer) {
            String s = buffer.readUtf(32767);
            CraftingBookCategory craftingBookCategory = buffer.readEnum(CraftingBookCategory.class);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> defaultedList = NonNullList.withSize(i, Ingredient.EMPTY);

            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new ContainerCraftingRecipe(s, craftingBookCategory, itemstack, defaultedList);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ContainerCraftingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
        }
    }
}