package com.telepathicgrunt.the_bumblezone.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.CrystallineFlowerClickedEnchantmentButtonPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

public class BuzzingBriefcaseScreen extends AbstractContainerScreen<BuzzingBreifcaseMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Bumblezone.MODID, "textures/gui/buzzing_briefcase/background.png");
    private static final ResourceLocation BEE_SLOT_BACKGROUND = new ResourceLocation(Bumblezone.MODID, "textures/gui/buzzing_briefcase/bee_slots.png");
    private static final ResourceLocation BEE_ICON = new ResourceLocation(Bumblezone.MODID, "textures/gui/buzzing_briefcase/bee_icon.png");
    private static final ResourceLocation BEE_ICON_BABY = new ResourceLocation(Bumblezone.MODID, "textures/gui/buzzing_briefcase/bee_icon_baby.png");
    private static final float SCALE = 1.25f;
    private static final int MENU_HEIGHT = (int) (176 * SCALE);
    private static final int MENU_WIDTH = (int) (302 * SCALE);

    public BuzzingBriefcaseScreen(BuzzingBreifcaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 240;
        this.imageHeight = 126;
        this.titleLabelX = 75;
        this.titleLabelY = -38;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.enableDepthTest();
    }

    @Override
    protected void init() {
        this.leftPos = (getTrueWidth() - this.imageWidth) / 2;
        this.topPos = (getTrueHeight() - this.imageHeight) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialtick, int x, int y) {
        int startX = (getTrueWidth() - MENU_WIDTH) / 2;
        int startY = (getTrueHeight() - MENU_HEIGHT) / 2;
        RenderSystem.enableDepthTest();
        guiGraphics.blit(
                CONTAINER_BACKGROUND,
                startX,
                startY,
                0,
                0,
                MENU_WIDTH,
                MENU_HEIGHT * 2,
                MENU_WIDTH,
                MENU_HEIGHT * 2
        );
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j) {
        guiGraphics.drawString(this.font, this.title, 74, -38, 0xFFEFAF, true);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        sendButtonPressToMenu(-5);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
        return mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.imageWidth) || mouseY >= (double)(top + this.imageHeight + 32);
    }

    private void sendButtonPressToMenu(Integer sectionId) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
        this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, sectionId);
    }


    private int getTrueWidth() {
        return width;
    }

    private int getTrueHeight() {
        return height - 29;
    }

}