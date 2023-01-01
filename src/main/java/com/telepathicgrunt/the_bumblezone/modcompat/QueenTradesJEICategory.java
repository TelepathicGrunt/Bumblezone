package com.telepathicgrunt.the_bumblezone.modcompat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueenTradesJEICategory implements IRecipeCategory<QueenTradesInfo> {

    public static final Map<Item, List<QueenTradesInfo.RewardInfo>> TRADE_CACHE = new HashMap<>();

    public static final RecipeType<QueenTradesInfo> TYPE = RecipeType.create(Bumblezone.MODID, "effects", QueenTradesInfo.class);

    public static final int RECIPE_WIDTH = 160;
    public static final int RECIPE_HEIGHT = 125;
    private static final int LINE_SPACING = 2;

    private static final int Y_OFFSET = 12;

    private final IDrawable background;
    private final IDrawable slotBackground;
    private final IDrawable effectBackground;
    private final IDrawable icon;
    //private final JeiInternalPlugin jeiPlugin;
    private final Component localizedName;

    public QueenTradesJEICategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(RECIPE_WIDTH, RECIPE_HEIGHT);
        this.effectBackground = guiHelper.createDrawable(ContainerScreen.INVENTORY_LOCATION, 141, 166, 24, 24);

        this.slotBackground = guiHelper.getSlotDrawable();
        //this.jeiPlugin = jeiPlugin;
        this.icon = guiHelper.createDrawableItemStack(BzItems.BEE_QUEEN_SPAWN_EGG.getDefaultInstance());
        this.localizedName = Component.translatable("the_bumblezone.category.bee_queen_trade_info");
    }


    @Override
    public RecipeType<QueenTradesInfo> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void draw(QueenTradesInfo recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        int xPos = 0;
        int yPos = effectBackground.getHeight() + 4 + Y_OFFSET;

        Font font = Minecraft.getInstance().font;

        MutableComponent name = (MutableComponent) recipe.wantItem().getDisplayName();

        float x = RECIPE_WIDTH / 2f - font.width(name) / 2f;
        font.drawShadow(matrixStack, Language.getInstance().getVisualOrder(name), x, 0, 0xFF000000);

        for (int slotId = 0; slotId < 14; slotId++) {
            this.slotBackground.draw(matrixStack, (int) (RECIPE_WIDTH / 2f + (19f * ((slotId % 7) - 7 / 2f))), RECIPE_HEIGHT - 19 * (1 + slotId / 7));
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, QueenTradesInfo recipe, IFocusGroup focuses) {
        //adds to both output and input
        IRecipeSlotBuilder mainSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, (RECIPE_WIDTH - 18) / 2, Y_OFFSET + 3);

        mainSlot.setBackground(effectBackground, -3, -3);

        List<List<ItemStack>> slotContents = Arrays.asList(NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create(), NonNullList.create());
        List<QueenTradesInfo.RewardInfo> compatible = TRADE_CACHE.get(recipe.wantItem().getItem());

        for (int slotId = 0; slotId < compatible.size(); slotId++) {
            slotContents.get(slotId % slotContents.size()).add(compatible.get(slotId).itemStack());
        }

        for (int slotId = 0; slotId < slotContents.size(); slotId++) {
            int x = 1 + (int) (RECIPE_WIDTH / 2f + (19f * ((slotId % 7) - 7 / 2f)));
            int y = 1 + RECIPE_HEIGHT - 19 * (2 - (slotId / 7));
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                    .addItemStacks(slotContents.get(slotId));
        }
    }
}