package com.telepathicgrunt.the_bumblezone.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.BuzzingBriefcase;
import com.telepathicgrunt.the_bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BuzzingBriefcaseScreen extends AbstractContainerScreen<BuzzingBriefcaseMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Bumblezone.MODID, "textures/gui/buzzing_briefcase/background.png");
    private static final ResourceLocation BEE_SLOT_BACKGROUND = new ResourceLocation(Bumblezone.MODID, "textures/gui/buzzing_briefcase/bee_slots.png");
    private static final ResourceLocation GENERAL_ICONS = new ResourceLocation(Bumblezone.MODID, "textures/gui/buzzing_briefcase/general_icons.png");
    private static final ResourceLocation BEE_ICON = new ResourceLocation(Bumblezone.MODID, "textures/gui/buzzing_briefcase/bee_icon.png");
    private static final float SCALE = 1.25f;
    private static final int MENU_HEIGHT = (int) (174 * SCALE);
    private static final int MENU_WIDTH = (int) (306 * SCALE);
    private static final int MAX_ROW_LENGTH = 7;

    private record BeeState(Bee beeEntity){}
    private final List<BeeState> BEE_INVENTORY = new ArrayList<>();
    private final Inventory inventory;
    private boolean syncedItem = false;

    public BuzzingBriefcaseScreen(BuzzingBriefcaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 240;
        this.imageHeight = 126;
        this.titleLabelX = 75;
        this.titleLabelY = -38;
        this.inventory = playerInventory;

    }

    @Override
    protected void init() {
        this.leftPos = (getTrueWidth() - this.imageWidth) / 2;
        this.topPos = (getTrueHeight() - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!BEE_INVENTORY.isEmpty() || !menu.getItems().get(0).isEmpty()) {
            BEE_INVENTORY.clear();

            List<Entity> beesStored = BuzzingBriefcase.getBeesStored(inventory.player.level(), menu.getItems().get(0), false);
            for (Entity entity : beesStored) {
                if (entity instanceof Bee bee) {
                    BEE_INVENTORY.add(new BeeState(bee));
                }
            }
        }

        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.enableDepthTest();

        drawBeeSlots(guiGraphics, leftPos, topPos, mouseX, mouseY);
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

    protected void renderButtonTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int rowIndex = 0;
        int columnIndex = 0;
        for (BeeState beeState : BEE_INVENTORY) {
            int xOffset = getXOffset(rowIndex);
            int yOffset = getYOffset(columnIndex);
            int mainX = leftPos + xOffset;
            int mainY = topPos + yOffset;

            rowIndex++;
            if (rowIndex == MAX_ROW_LENGTH) {
                rowIndex = 0;
                columnIndex++;
            }

            if (mouseX - mainX >= 0.0D &&
                    mouseX - mainX < 22.0D &&
                    mouseY - mainY >= 0.0D &&
                    mouseY - mainY < 22.0D)
            {
                guiGraphics.renderTooltip(
                        this.font,
                        List.of(
                            Component.translatable("item.the_bumblezone.buzzing_briefcase_bee_name", beeState.beeEntity().getName().getString()),
                            Component.translatable("item.the_bumblezone.buzzing_briefcase_bee_type", BuiltInRegistries.ENTITY_TYPE.getKey(beeState.beeEntity().getType()))
                        ),
                        Optional.empty(),
                        mouseX,
                        mouseY);
            }
            else if (mouseX - (mainX + 22) >= 0.0D &&
                    mouseX - (mainX + 22) < 11.0D &&
                    mouseY - mainY >= 0.0D &&
                    mouseY - mainY < 11.0D)
            {
                guiGraphics.renderTooltip(
                    this.font,
                    List.of(
                        Component.translatable("item.the_bumblezone.buzzing_briefcase_release", beeState.beeEntity().getName().getString())
                    ),
                    Optional.empty(),
                    mouseX,
                    mouseY);
            }
            else if (mouseX - mainX >= 0.0D &&
                    mouseX - mainX < 11.0D &&
                    mouseY - (mainY + 22) >= 0.0D &&
                    mouseY - (mainY + 22) < 11.0D)
            {
                if (beeState.beeEntity().getHealth() < beeState.beeEntity().getMaxHealth()) {
                    boolean hasHoneyBottleItem = inventory.contains(Items.HONEY_BOTTLE.getDefaultInstance());
                    if (hasHoneyBottleItem) {
                        guiGraphics.renderTooltip(
                            this.font,
                            List.of(
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_health_1"),
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_health_2")
                            ),
                            Optional.empty(),
                            mouseX,
                            mouseY);
                    }
                    else {
                        guiGraphics.renderTooltip(
                            this.font,
                            List.of(
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_health_missing_item")
                            ),
                            Optional.empty(),
                            mouseX,
                            mouseY);
                    }
                }
            }
            else if (mouseX - (mainX + 11) >= 0.0D &&
                    mouseX - (mainX + 11) < 11.0D &&
                    mouseY - (mainY + 22) >= 0.0D &&
                    mouseY - (mainY + 22) < 11.0D)
            {
                if (beeState.beeEntity().hasStung()) {
                    boolean hasBeeStingerItem = inventory.contains(BzItems.BEE_STINGER.get().getDefaultInstance());
                    if (hasBeeStingerItem) {
                        guiGraphics.renderTooltip(
                            this.font,
                            List.of(
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_stinger_1"),
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_stinger_2")
                            ),
                            Optional.empty(),
                            mouseX,
                            mouseY);
                    }
                    else {
                        guiGraphics.renderTooltip(
                            this.font,
                            List.of(
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_stinger_missing_item")
                            ),
                            Optional.empty(),
                            mouseX,
                            mouseY);
                    }
                }
            }
            else if (mouseX - mainX >= 0.0D &&
                    mouseX - mainX < 11.0D &&
                    mouseY - (mainY + 33) >= 0.0D &&
                    mouseY - (mainY + 33) < 11.0D)
            {
                if (beeState.beeEntity().isBaby()) {
                    boolean hasHoneyBottleItem = inventory.contains(Items.HONEY_BOTTLE.getDefaultInstance());
                    if (hasHoneyBottleItem) {
                        guiGraphics.renderTooltip(
                            this.font,
                            List.of(
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_grow_up_1"),
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_grow_up_2")
                            ),
                            Optional.empty(),
                            mouseX,
                            mouseY);
                    }
                    else {
                        guiGraphics.renderTooltip(
                            this.font,
                            List.of(
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_grow_up_missing_item")
                            ),
                            Optional.empty(),
                            mouseX,
                            mouseY);
                    }
                }
            }
            else if (mouseX - (mainX + 11) >= 0.0D &&
                    mouseX - (mainX + 11) < 11.0D &&
                    mouseY - (mainY + 33) >= 0.0D &&
                    mouseY - (mainY + 33) < 11.0D)
            {
                if (!beeState.beeEntity().hasNectar()) {
                    boolean hasPollenPuffItem = inventory.contains(BzItems.POLLEN_PUFF.get().getDefaultInstance());
                    if (hasPollenPuffItem) {
                        guiGraphics.renderTooltip(
                            this.font,
                            List.of(
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_pollen_1"),
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_pollen_2")
                            ),
                            Optional.empty(),
                            mouseX,
                            mouseY);
                    }
                    else {
                        guiGraphics.renderTooltip(
                            this.font,
                            List.of(
                                Component.translatable("item.the_bumblezone.buzzing_briefcase_pollen_missing_item")
                            ),
                            Optional.empty(),
                            mouseX,
                            mouseY);
                    }
                }
            }
        }
    }

    private void drawBeeSlots(GuiGraphics guiGraphics, int startX, int startY, int mouseX, int mouseY) {
        boolean hasStingerItem = false;
        boolean hasHoneyBottleItem = false;
        boolean hasPollenPuffItem = false;
        if (!BEE_INVENTORY.isEmpty()) {
            hasStingerItem = inventory.contains(BzItems.BEE_STINGER.get().getDefaultInstance());
            hasHoneyBottleItem = inventory.contains(Items.HONEY_BOTTLE.getDefaultInstance());
            hasPollenPuffItem = inventory.contains(BzItems.POLLEN_PUFF.get().getDefaultInstance());
        }

        int rowIndex = 0;
        int columnIndex = 0;
        for (int beeIndex = 0; beeIndex < BuzzingBriefcase.MAX_NUMBER_OF_BEES; beeIndex++) {
            int xOffset = getXOffset(rowIndex);
            int yOffset = getYOffset(columnIndex);
            int mainX = startX + xOffset;
            int mainY = startY + yOffset;

            rowIndex++;
            if (rowIndex == MAX_ROW_LENGTH) {
                rowIndex = 0;
                columnIndex++;
            }

            BeeState beeState = null;
            if (beeIndex < BEE_INVENTORY.size()) {
                beeState = BEE_INVENTORY.get(beeIndex);
            }

            //mainSlot
            RenderSystem.enableDepthTest();
            if (beeState == null) {
                guiGraphics.blit(BEE_SLOT_BACKGROUND, mainX, mainY, 22, 0, 22, 22, 64, 64);
                guiGraphics.blit(BEE_SLOT_BACKGROUND, mainX, mainY + 22, 22, 22, 11, 11, 64, 64);
                guiGraphics.blit(BEE_SLOT_BACKGROUND, mainX + 11, mainY + 22, 22, 22, 11, 11, 64, 64);
                guiGraphics.blit(BEE_SLOT_BACKGROUND, mainX, mainY + 33, 22, 22, 11, 11, 64, 64);
                guiGraphics.blit(BEE_SLOT_BACKGROUND, mainX + 11, mainY + 33, 22, 22, 11, 11, 64, 64);
                continue;
            }

            drawBeeSlot(guiGraphics, mainX, mainY, beeState);
            renderHealthButton(guiGraphics, mouseX, mouseY, hasHoneyBottleItem, mainX, mainY, beeState);
            renderStingerButton(guiGraphics, mouseX, mouseY, hasStingerItem, mainX, mainY, beeState);
            renderGrowUpButton(guiGraphics, mouseX, mouseY, hasHoneyBottleItem, mainX, mainY, beeState);
            renderPollenButton(guiGraphics, mouseX, mouseY, hasPollenPuffItem, mainX, mainY, beeState);
            renderReleaseButton(guiGraphics, mouseX, mouseY, mainX, mainY);
            renderButtonTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    private static void drawBeeSlot(GuiGraphics guiGraphics, int mainX, int mainY, BeeState beeState) {
        guiGraphics.blit(BEE_SLOT_BACKGROUND, mainX, mainY, 0, 0, 22, 22, 64, 64);

        float healthPercentage = beeState.beeEntity().getHealth() / beeState.beeEntity().getMaxHealth();
        int barColor = Mth.hsvToRgb(healthPercentage / 3.0f, 1.0f, 1.0f);
        int barWidth = (int) (Math.max(1, 16 * healthPercentage));
        guiGraphics.fill(RenderType.guiOverlay(), mainX + 3, mainY + 19, mainX + 3 + barWidth, mainY + 18, barColor | 0xFF000000);

        int beeIconXOffset = 0;
        int beeIconYOffset = 0;
        if (beeState.beeEntity().isBaby()) {
            beeIconYOffset += 16;
        }
        if (beeState.beeEntity().hasStung()) {
            beeIconXOffset += 16;
        }
        if (beeState.beeEntity().hasNectar()) {
            beeIconXOffset += 32;
        }

        guiGraphics.blit(BEE_ICON, mainX + 3, mainY + 3, beeIconXOffset, beeIconYOffset, 16, 16, 64, 64);
    }

    private static void renderReleaseButton(GuiGraphics guiGraphics, int mouseX, int mouseY, int mainX, int mainY) {
        //hover release button
        if (mouseX - (mainX + 22) >= 0.0D &&
            mouseX - (mainX + 22) < 11.0D &&
            mouseY - mainY >= 0.0D &&
            mouseY - mainY < 11.0D)
        {
            guiGraphics.blit(BEE_SLOT_BACKGROUND, mainX + 22, mainY, 33, 33, 11, 11, 64, 64);
        }
        //release button
        else {
            guiGraphics.blit(BEE_SLOT_BACKGROUND, mainX + 22, mainY, 0, 33, 11, 11, 64, 64);
        }
    }

    private static void renderHealthButton(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hasHoneyBottle, int mainX, int mainY, BeeState beeState) {
        //has health button
        if (beeState.beeEntity().getHealth() == beeState.beeEntity().getMaxHealth()) {
            guiGraphics.blit(GENERAL_ICONS, mainX, mainY + 22, 11, 11, 11, 11, 64, 64);
        }
        //no inventory stinger button
        else if (!hasHoneyBottle) {
            guiGraphics.blit(GENERAL_ICONS, mainX, mainY + 22, 22, 11, 11, 11, 64, 64);
        }
        //hover/click health button
        else if (mouseX - mainX >= 0.0D &&
                mouseX - mainX < 11.0D &&
                mouseY - (mainY + 22) >= 0.0D &&
                mouseY - (mainY + 22) < 11.0D)
        {
            guiGraphics.blit(GENERAL_ICONS, mainX, mainY + 22, 33, 11, 11, 11, 64, 64);
        }
        //needs health button
        else {
            guiGraphics.blit(GENERAL_ICONS, mainX, mainY + 22, 0, 11, 11, 11, 64, 64);
        }
    }

    private static void renderStingerButton(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hasStingerItem, int mainX, int mainY, BeeState beeState) {
        //has stinger button
        if (!beeState.beeEntity().hasStung()) {
            guiGraphics.blit(GENERAL_ICONS, mainX + 11, mainY + 22, 11, 0, 11, 11, 64, 64);
        }
        //no inventory stinger button
        else if (!hasStingerItem) {
            guiGraphics.blit(GENERAL_ICONS, mainX + 11, mainY + 22, 22, 0, 11, 11, 64, 64);
        }
        //hover stinger button
        else if (mouseX - (mainX + 11) >= 0.0D &&
                mouseX - (mainX + 11) < 11.0D &&
                mouseY - (mainY + 22) >= 0.0D &&
                mouseY - (mainY + 22) < 11.0D)
        {
            guiGraphics.blit(GENERAL_ICONS, mainX + 11, mainY + 22, 33, 0, 11, 11, 64, 64);
        }
        //normal stinger button
        else {
            guiGraphics.blit(GENERAL_ICONS, mainX + 11, mainY + 22, 0, 0, 11, 11, 64, 64);
        }
    }

    private static void renderGrowUpButton(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hasHoneyBottle, int mainX, int mainY, BeeState beeState) {
        //cannot grow up button
        if (!beeState.beeEntity().isBaby()) {
            guiGraphics.blit(GENERAL_ICONS, mainX, mainY + 33, 11, 33, 11, 11, 64, 64);
        }
        //no inventory honey bottle button
        else if (!hasHoneyBottle) {
            guiGraphics.blit(GENERAL_ICONS, mainX, mainY + 33, 22, 33, 11, 11, 64, 64);
        }
        //hover grow up button
        else if (mouseX - mainX >= 0.0D &&
                mouseX - mainX < 11.0D &&
                mouseY - (mainY + 33) >= 0.0D &&
                mouseY - (mainY + 33) < 11.0D)
        {
            guiGraphics.blit(GENERAL_ICONS, mainX, mainY + 33, 33, 33, 11, 11, 64, 64);
        }
        //normal grow up button
        else {
            guiGraphics.blit(GENERAL_ICONS, mainX, mainY + 33, 0, 33, 11, 11, 64, 64);
        }
    }

    private static void renderPollenButton(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hasPollenPuff, int mainX, int mainY, BeeState beeState) {
        //cannot pollinate button
        if (beeState.beeEntity().hasNectar()) {
            guiGraphics.blit(GENERAL_ICONS, mainX + 11, mainY + 33, 11, 22, 11, 11, 64, 64);
        }
        //no inventory pollen puff button
        else if (!hasPollenPuff) {
            guiGraphics.blit(GENERAL_ICONS, mainX + 11, mainY + 33, 22, 22, 11, 11, 64, 64);
        }
        //hover pollinate button
        else if (mouseX - (mainX + 11) >= 0.0D &&
                mouseX - (mainX + 11) < 11.0D &&
                mouseY - (mainY + 33) >= 0.0D &&
                mouseY - (mainY + 33) < 11.0D)
        {
            guiGraphics.blit(GENERAL_ICONS, mainX + 11, mainY + 33, 33, 22, 11, 11, 64, 64);
        }
        //normal pollinate button
        else {
            guiGraphics.blit(GENERAL_ICONS, mainX + 11, mainY + 33, 0, 22, 11, 11, 64, 64);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int rowIndex = 0;
        int columnIndex = 0;
        for (int beeIndex = 0; beeIndex < BuzzingBriefcase.MAX_NUMBER_OF_BEES; beeIndex++) {
            if (beeIndex >= BEE_INVENTORY.size()) {
                break;
            }
            Bee bee = BEE_INVENTORY.get(beeIndex).beeEntity();

            int xOffset = getXOffset(rowIndex);
            int yOffset = getYOffset(columnIndex);
            int mainX = leftPos + xOffset;
            int mainY = topPos + yOffset;

            rowIndex++;
            if (rowIndex == MAX_ROW_LENGTH) {
                rowIndex = 0;
                columnIndex++;
            }

            if (mouseX - (mainX + 22) >= 0.0D &&
                mouseX - (mainX + 22) < 11.0D &&
                mouseY - mainY >= 0.0D &&
                mouseY - mainY < 11.0D)
            {
                sendButtonPressToMenu((beeIndex * BuzzingBriefcaseMenu.NUMBER_OF_BUTTONS) + BuzzingBriefcaseMenu.RELEASE_ID);
            }
            else if (mouseX - mainX >= 0.0D &&
                    mouseX - mainX < 11.0D &&
                    mouseY - (mainY + 22) >= 0.0D &&
                    mouseY - (mainY + 22) < 11.0D)
            {
                if (bee.getHealth() < bee.getMaxHealth()) {
                    sendButtonPressToMenu((beeIndex * BuzzingBriefcaseMenu.NUMBER_OF_BUTTONS) + BuzzingBriefcaseMenu.HEALTH_ID);

                    int honeyBottleSlotIndex = inventory.findSlotMatchingItem(Items.HONEY_BOTTLE.getDefaultInstance());
                    ItemStack playerHoneyBottleStack = inventory.getItem(honeyBottleSlotIndex);
                    if (!playerHoneyBottleStack.isEmpty()) {
                        if (!menu.player.getAbilities().instabuild) {
                            playerHoneyBottleStack.shrink(1);
                            if (playerHoneyBottleStack.isEmpty()) {
                                inventory.add(honeyBottleSlotIndex, Items.GLASS_BOTTLE.getDefaultInstance());
                            }
                            else {
                                inventory.add(Items.GLASS_BOTTLE.getDefaultInstance());
                            }
                        }
                    }
                }
            }
            else if (mouseX - (mainX + 11) >= 0.0D &&
                    mouseX - (mainX + 11) < 11.0D &&
                    mouseY - (mainY + 22) >= 0.0D &&
                    mouseY - (mainY + 22) < 11.0D)
            {
                if (bee.hasStung()) {
                    sendButtonPressToMenu((beeIndex * BuzzingBriefcaseMenu.NUMBER_OF_BUTTONS) + BuzzingBriefcaseMenu.STINGER_ID);

                    int stingerSlotIndex = inventory.findSlotMatchingItem(BzItems.BEE_STINGER.get().getDefaultInstance());
                    ItemStack playerStingerStack = inventory.getItem(stingerSlotIndex);
                    if (!playerStingerStack.isEmpty()) {
                        ((BeeEntityInvoker) bee).callSetHasStung(false);

                        if (!menu.player.getAbilities().instabuild) {
                            playerStingerStack.shrink(1);
                        }
                    }
                }
            }
            else if (mouseX - mainX >= 0.0D &&
                    mouseX - mainX < 11.0D &&
                    mouseY - (mainY + 33) >= 0.0D &&
                    mouseY - (mainY + 33) < 11.0D)
            {
                if (bee.isBaby()) {
                    sendButtonPressToMenu((beeIndex * BuzzingBriefcaseMenu.NUMBER_OF_BUTTONS) + BuzzingBriefcaseMenu.GROW_UP_ID);

                    int honeyBottleSlotIndex = inventory.findSlotMatchingItem(Items.HONEY_BOTTLE.getDefaultInstance());
                    ItemStack playerHoneyBottleStack = inventory.getItem(honeyBottleSlotIndex);
                    if (!playerHoneyBottleStack.isEmpty()) {
                        if (!menu.player.getAbilities().instabuild) {
                            playerHoneyBottleStack.shrink(1);
                            if (playerHoneyBottleStack.isEmpty()) {
                                inventory.add(honeyBottleSlotIndex, Items.GLASS_BOTTLE.getDefaultInstance());
                            } else {
                                inventory.add(Items.GLASS_BOTTLE.getDefaultInstance());
                            }
                        }
                    }
                }
            }
            else if (mouseX - (mainX + 11) >= 0.0D &&
                    mouseX - (mainX + 11) < 11.0D &&
                    mouseY - (mainY + 33) >= 0.0D &&
                    mouseY - (mainY + 33) < 11.0D)
            {
                if (!bee.hasNectar()) {
                    sendButtonPressToMenu((beeIndex * BuzzingBriefcaseMenu.NUMBER_OF_BUTTONS) + BuzzingBriefcaseMenu.POLLEN_ID);

                    int pollenSlotIndex = inventory.findSlotMatchingItem(BzItems.POLLEN_PUFF.get().getDefaultInstance());
                    ItemStack playerPollenStack = inventory.getItem(pollenSlotIndex);
                    if (!playerPollenStack.isEmpty()) {
                        if (!menu.player.getAbilities().instabuild) {
                            playerPollenStack.shrink(1);
                        }
                    }
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
        return false;
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

    private static int getXOffset(int rowIndex) {
        return -11 + (rowIndex * 38);
    }

    private static int getYOffset(int columnIndex) {
        return 46 + (columnIndex * 54);
    }
}