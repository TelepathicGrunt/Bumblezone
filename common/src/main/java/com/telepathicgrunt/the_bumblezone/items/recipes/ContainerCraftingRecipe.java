package com.telepathicgrunt.the_bumblezone.items.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class ContainerCraftingRecipe implements CraftingRecipe {
    protected final Recipe.CommonInfo commonInfo;
    protected final CraftingRecipe.CraftingBookInfo bookInfo;
    private final ItemStack result;
    private final List<Ingredient> ingredients;

    @Nullable
    private PlacementInfo placementInfo;

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

    public ContainerCraftingRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStack result, List<Ingredient> ingredients) {
        this.commonInfo = commonInfo;
        this.bookInfo = bookInfo;
        this.result = result;
        this.ingredients = ingredients;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(this.ingredients);
        }

        return this.placementInfo;
    }

    @Override
    public final String group() {
        return this.bookInfo.group();
    }

    @Override
    public final CraftingBookCategory category() {
        return this.bookInfo.category();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != this.ingredients.size()) {
            return false;
        } else {
            return input.size() == 1 && this.ingredients.size() == 1
                    ? this.ingredients.getFirst().test(input.getItem(0))
                    : input.stackedContents().canCraft(this, null);
        }
    }

    @Override
    public ItemStack assemble(CraftingInput recipeInput) {
        return this.result.copy();
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
        NonNullList<ItemStack> remainingInv = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);
        int containerOutput = PlatformService.INSTANCE.hasCraftingRemainder(this.result) ? this.result.getCount() : 0;

        for(int i = 0; i < remainingInv.size(); ++i) {
            ItemStack itemStack = craftingInput.getItem(i);
            ItemStack craftingContainer = PlatformService.INSTANCE.getCraftingRemainder(itemStack);
            ItemStack recipeContainer = PlatformService.INSTANCE.getCraftingRemainder(this.result);
            if (craftingContainer.isEmpty() && HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.containsKey(itemStack.getItem())) {
                craftingContainer = HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.get(itemStack.getItem()).getDefaultInstance();
            }
            if (recipeContainer.isEmpty() && HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.containsKey(this.result.getItem())) {
                recipeContainer = HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.get(this.result.getItem()).getDefaultInstance();
            }

            if (!craftingContainer.isEmpty()) {
                if(containerOutput > 0 &&
                    (this.result.getItem() == craftingContainer.getItem() ||
                    recipeContainer.getItem() == itemStack.getItem() ||
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

    private static final MapCodec<ContainerCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
            ItemStack.CODEC.fieldOf("result").forGetter(o -> o.result),
            Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(o -> o.ingredients)
    ).apply(instance, ContainerCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ContainerCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
            o -> o.bookInfo,
            ItemStack.STREAM_CODEC,
            r -> r.result,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            r -> r.ingredients,
            ContainerCraftingRecipe::new
    );

    public static final RecipeSerializer<ContainerCraftingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public RecipeSerializer<ContainerCraftingRecipe> getSerializer() {
        return BzRecipes.CONTAINER_CRAFTING_RECIPE.get();
    }
}