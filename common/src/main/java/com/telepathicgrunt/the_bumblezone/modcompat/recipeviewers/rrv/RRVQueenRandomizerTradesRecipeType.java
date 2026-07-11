package com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.rrv;

import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewScreen;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RRVQueenRandomizerTradesRecipeType implements ReliableClientRecipeType {

    protected static final ReliableClientRecipeType INSTANCE = new RRVQueenRandomizerTradesRecipeType();
    public static final int RECIPE_WIDTH = 136;
    public static final int RECIPE_HEIGHT = 28;
    public static final Identifier ICON = Identifier.fromNamespaceAndPath("the_bumblezone", "textures/gui/bee_queen_randomize_trades.png");

    @Override
    public Component getDisplayName() {
        return Component.translatable("the_bumblezone.recipe_viewers.bee_queen_color_randomizing_trades");
    }

    @Override
    public int getDisplayWidth() {
        return RECIPE_WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return RECIPE_HEIGHT;
    }

    @Override
    public @Nullable Identifier getGuiTexture() {
        return Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/gui/queen_randomizer_trades_layout.png");
    }

    @Override
    public int getSlotCount() {
        return 2;
    }

    @Override
    public void placeSlots(RecipeViewMenu.SlotDefinition slotDefinition) {
        slotDefinition.addItemSlot(0, 6, 6);
        slotDefinition.addItemSlot(1, 69, 6);
    }

    @Override
    public Identifier getId() {
        return Identifier.fromNamespaceAndPath(Bumblezone.MODID,  "bee_queen_randomizer_trades");
    }

    @Override
    public ItemStack getIcon() {
        return BzItems.BEE_QUEEN_SPAWN_EGG.get().getDefaultInstance();
    }

    /// Takes priority over getIcon
    @Override
    public void renderIcon(RecipeViewScreen screen, int x, int y, GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blit(ICON, 0, 0, 16, 16, 0, 16, 0, 16);
    }

    @Override
    public List<ItemStack> getCraftReferences() {
        return List.of(BzItems.BEE_QUEEN_SPAWN_EGG.get().getDefaultInstance());
    }
}