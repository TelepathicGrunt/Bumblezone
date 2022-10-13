package com.telepathicgrunt.the_bumblezone.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrystallineFlowerScreen extends AbstractContainerScreen<CrystallineFlowerMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Bumblezone.MODID, "textures/gui/container/crystallized_flower.png");
    private static final Pattern SPLIT_WITH_COMBINING_CHARS = Pattern.compile("(\\p{M}+|\\P{M}\\p{M}*)"); // {M} is any kind of 'mark' http://stackoverflow.com/questions/29110887/detect-any-combining-character-in-java/29111105

    private static final int MENU_HEIGHT = 126;

    private static final int INVENTORY_LABEL_Y_OFFSET = -60;
    private static final int TITLE_LABEL_Y_OFFSET = -3;

    private static final int ENCHANTMENT_AREA_X_OFFSET = 76;
    private static final int ENCHANTMENT_AREA_Y_OFFSET = 52;
    private static final int ENCHANTMENT_SECTION_WIDTH = 88;
    private static final int ENCHANTMENT_SECTION_HEIGHT = 19;

    private static final int ENCHANTMENT_SCROLLBAR_X_OFFSET = 164;
    private static final int ENCHANTMENT_SCROLLBAR_Y_OFFSET = 50;
    private static final int ENCHANTMENT_SCROLLBAR_Y_RANGE = 50;
    private static final float ENCHANTMENT_SCROLLBAR_U_TEXTURE = 230.0F;
    private static final float ENCHANTMENT_SCROLLBAR_V_TEXTURE = 182.0F;

    private static final float ENCHANTMENT_SELECTED_U_TEXTURE = 0F;
    private static final float ENCHANTMENT_SELECTED_V_TEXTURE = 197.0F;
    private static final float ENCHANTMENT_UNSELECTED_U_TEXTURE = 0F;
    private static final float ENCHANTMENT_UNSELECTED_V_TEXTURE = 216.0F;
    private static final float ENCHANTMENT_HIGHLIGHTED_U_TEXTURE = 0F;
    private static final float ENCHANTMENT_HIGHLIGHTED_V_TEXTURE = 235.0F;

    private static final int XP_BAR_X_OFFSET = 11;
    private static final int XP_BAR_Y_OFFSET = 99;
    private static final float XP_BAR_U_TEXTURE = 176.0F;
    private static final float XP_BAR_V_TEXTURE = 187.0F;

    private static final int XP_CONSUME_1_X_OFFSET = 46;
    private static final int XP_CONSUME_1_Y_OFFSET = 14;
    private static final int XP_CONSUME_2_X_OFFSET = 46;
    private static final int XP_CONSUME_2_Y_OFFSET = 34;
    private static final int XP_CONSUME_3_X_OFFSET = 46;
    private static final int XP_CONSUME_3_Y_OFFSET = 54;

    private static final float XP_CONSUME_1_U_OFFSET = 108.0F;
    private static final float XP_CONSUME_1_V_OFFSET = 197.0F;
    private static final float XP_CONSUME_2_U_OFFSET = 126.0F;
    private static final float XP_CONSUME_2_V_OFFSET = 197.0F;
    private static final float XP_CONSUME_3_U_OFFSET = 144.0F;
    private static final float XP_CONSUME_3_V_OFFSET = 197.0F;

    private static final int CONSUME_CONFIRMATION_X_OFFSET = 25;
    private static final int CONSUME_CONFIRMATION_Y_OFFSET = 62;
    private static final float CONSUME_CONFIRMATION_U_OFFSET = 162.0F;
    private static final float CONSUME_CONFIRMATION_V_OFFSET = 197.0F;
    private static final int CONSUME_ARROW_X_OFFSET = 26;
    private static final int CONSUME_ARROW_Y_OFFSET = 82;
    private static final float CONSUME_ARROW_U_OFFSET = 180.0F;
    private static final float CONSUME_ARROW_V_OFFSET = 197.0F;

    private static final int BUTTON_PRESSED_TIMER_VISUAL = 25;

    private float scrollOff;
    private boolean scrolling;
    private int startIndex;
    private int pressedXp1Timer = 0;
    private int pressedXp2Timer = 0;
    private int pressedXp3Timer = 0;
    private int pressedConsumeTimer = 0;

    private final List<Map.Entry<ResourceKey<Enchantment>, EnchantmentInstance>> enchantmentsAvailable = new ArrayList<>();

    public CrystallineFlowerScreen(CrystallineFlowerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        passEvents = false;
        inventoryLabelY = imageHeight + INVENTORY_LABEL_Y_OFFSET;
        titleLabelY += TITLE_LABEL_Y_OFFSET;
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        populateAvailableEnchants();

        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        final int rowStartX = startX + ENCHANTMENT_AREA_X_OFFSET;
        final int rowStartY = startY + ENCHANTMENT_AREA_Y_OFFSET;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        renderScroller(poseStack, startX + ENCHANTMENT_SCROLLBAR_X_OFFSET, startY + ENCHANTMENT_SCROLLBAR_Y_OFFSET);
        renderXPBar(poseStack, startX, startY);

        handleEnchantmentAreaRow(mouseX, mouseY,
            (Integer sectionId) -> {
                if (sectionId >= enchantmentsAvailable.size()) {
                    return false;
                }

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);

                int row = sectionId - this.startIndex;
                blit(poseStack, rowStartX - 2, rowStartY - 2 + row * ENCHANTMENT_SECTION_HEIGHT, getBlitOffset(), ENCHANTMENT_HIGHLIGHTED_U_TEXTURE, ENCHANTMENT_HIGHLIGHTED_V_TEXTURE, ENCHANTMENT_SECTION_WIDTH + 1, ENCHANTMENT_SECTION_HEIGHT, 256, 256);
                drawEnchantmentText(poseStack, rowStartX, rowStartY + row * 19, enchantmentsAvailable.get(sectionId), 0x402020, 0x304000);
                return true;
            },
            (Integer sectionId) -> {
                if (sectionId >= enchantmentsAvailable.size()) {
                    return;
                }

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);

                int row = sectionId - this.startIndex;
                if (sectionId == this.menu.selectedEnchantmentIndex.get()) {
                    blit(poseStack, rowStartX - 2, rowStartY - 2 + row * ENCHANTMENT_SECTION_HEIGHT, getBlitOffset(), ENCHANTMENT_SELECTED_U_TEXTURE, ENCHANTMENT_SELECTED_V_TEXTURE, ENCHANTMENT_SECTION_WIDTH + 1, ENCHANTMENT_SECTION_HEIGHT, 256, 256);
                    drawEnchantmentText(poseStack, rowStartX, rowStartY + row * ENCHANTMENT_SECTION_HEIGHT, enchantmentsAvailable.get(sectionId), 0xFFF000, 0xC0FF00);
                }
                else {
                    blit(poseStack, rowStartX - 2, rowStartY - 2 + row * ENCHANTMENT_SECTION_HEIGHT, getBlitOffset(), ENCHANTMENT_UNSELECTED_U_TEXTURE, ENCHANTMENT_UNSELECTED_V_TEXTURE, ENCHANTMENT_SECTION_WIDTH + 1, ENCHANTMENT_SECTION_HEIGHT, 256, 256);
                    drawEnchantmentText(poseStack, rowStartX, rowStartY + row * ENCHANTMENT_SECTION_HEIGHT, enchantmentsAvailable.get(sectionId), 0xD0B0F0, 0x00DD40);
                }
            });

        drawPushableButtons(poseStack, startX, startY, mouseX, mouseY);

        renderTooltip(poseStack, mouseX, mouseY);
    }

    private void drawPushableButtons(PoseStack poseStack, int startX, int startY, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);

        if (pressedXp1Timer > 0) {
            pressedXp1Timer--;
            blit(poseStack, startX + XP_CONSUME_1_X_OFFSET, startY + XP_CONSUME_1_Y_OFFSET, getBlitOffset(), XP_CONSUME_1_U_OFFSET, XP_CONSUME_1_V_OFFSET + 18, 18, 18, 256, 256);
        }
        else {
            int xOffset = startX + XP_CONSUME_1_X_OFFSET;
            int yOffset = startY + XP_CONSUME_1_Y_OFFSET;
            if (mouseX - xOffset >= 0.0D && mouseX - xOffset < 18.0D && mouseY - yOffset >= 0.0D && mouseY - yOffset < 18.0D) {
                blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_1_U_OFFSET, XP_CONSUME_1_V_OFFSET + 36, 18, 18, 256, 256);
            }
            else {
                blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_1_U_OFFSET, XP_CONSUME_1_V_OFFSET, 18, 18, 256, 256);
            }
        }

        if (pressedXp2Timer > 0) {
            pressedXp2Timer--;
            blit(poseStack, startX + XP_CONSUME_2_X_OFFSET, startY + XP_CONSUME_2_Y_OFFSET, getBlitOffset(), XP_CONSUME_2_U_OFFSET, XP_CONSUME_2_V_OFFSET + 18, 18, 18, 256, 256);
        }
        else {
            int xOffset = startX + XP_CONSUME_2_X_OFFSET;
            int yOffset = startY + XP_CONSUME_2_Y_OFFSET;
            if (mouseX - xOffset >= 0.0D && mouseX - xOffset < 18.0D && mouseY - yOffset >= 0.0D && mouseY - yOffset < 18.0D) {
                blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_2_U_OFFSET, XP_CONSUME_2_V_OFFSET + 36, 18, 18, 256, 256);
            }
            else {
                blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_2_U_OFFSET, XP_CONSUME_2_V_OFFSET, 18, 18, 256, 256);
            }
        }

        if (pressedXp3Timer > 0) {
            pressedXp3Timer--;
            blit(poseStack, startX + XP_CONSUME_3_X_OFFSET, startY + XP_CONSUME_3_Y_OFFSET, getBlitOffset(), XP_CONSUME_3_U_OFFSET, XP_CONSUME_3_V_OFFSET + 18, 18, 18, 256, 256);
        }
        else {
            int xOffset = startX + XP_CONSUME_3_X_OFFSET;
            int yOffset = startY + XP_CONSUME_3_Y_OFFSET;
            if (mouseX - xOffset >= 0.0D && mouseX - xOffset < 18.0D && mouseY - yOffset >= 0.0D && mouseY - yOffset < 18.0D) {
                blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_3_U_OFFSET, XP_CONSUME_3_V_OFFSET + 36, 18, 18, 256, 256);
            }
            else {
                blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_3_U_OFFSET, XP_CONSUME_3_V_OFFSET, 18, 18, 256, 256);
            }
        }

        if (pressedConsumeTimer > 0) {
            pressedConsumeTimer--;
            blit(poseStack, startX + CONSUME_CONFIRMATION_X_OFFSET, startY + CONSUME_CONFIRMATION_Y_OFFSET, getBlitOffset(), CONSUME_CONFIRMATION_U_OFFSET, CONSUME_CONFIRMATION_V_OFFSET + 18, 18, 18, 256, 256);
            blit(poseStack, startX + CONSUME_ARROW_X_OFFSET, startY + CONSUME_ARROW_Y_OFFSET, getBlitOffset(), CONSUME_ARROW_U_OFFSET, CONSUME_ARROW_V_OFFSET + 18, 16, 16, 256, 256);
        }
        else if (this.menu.consumeSlot.hasItem()) {
            int xOffset = startX + CONSUME_CONFIRMATION_X_OFFSET;
            int yOffset = startY + CONSUME_CONFIRMATION_Y_OFFSET;
            if (mouseX - xOffset >= 0.0D && mouseX - xOffset < 18.0D && mouseY - yOffset >= 0.0D && mouseY - yOffset < 18.0D) {
                blit(poseStack, startX + CONSUME_CONFIRMATION_X_OFFSET, startY + CONSUME_CONFIRMATION_Y_OFFSET, getBlitOffset(), CONSUME_CONFIRMATION_U_OFFSET, CONSUME_CONFIRMATION_V_OFFSET + 36, 18, 18, 256, 256);
            }
            else {
                blit(poseStack, startX + CONSUME_CONFIRMATION_X_OFFSET, startY + CONSUME_CONFIRMATION_Y_OFFSET, getBlitOffset(), CONSUME_CONFIRMATION_U_OFFSET, CONSUME_CONFIRMATION_V_OFFSET, 18, 18, 256, 256);
            }
            blit(poseStack, startX + CONSUME_ARROW_X_OFFSET, startY + CONSUME_ARROW_Y_OFFSET, getBlitOffset(), CONSUME_ARROW_U_OFFSET, CONSUME_ARROW_V_OFFSET, 16, 16, 256, 256);
        }
    }

    private void drawEnchantmentText(PoseStack poseStack, int rowStartX, int currentRowStartY, Map.Entry<ResourceKey<Enchantment>, EnchantmentInstance> enchantmentEntry, int enchantmentNameColor, int enchantmentLevelColor) {
        String translatedEnchantmentName = getTruncatedString("""
                enchantment.%s.%s""".formatted(
                    enchantmentEntry.getKey().location().getNamespace(),
                    enchantmentEntry.getKey().location().getPath()),
                88);

        MutableComponent mutableComponent = Component.literal(translatedEnchantmentName);
        MutableComponent mutableComponent2 = Component.translatable("the_bumblezone.container.crystalline_flower.level", enchantmentEntry.getValue().level);

        font.draw(poseStack, mutableComponent, rowStartX, currentRowStartY, enchantmentNameColor);
        font.draw(poseStack, mutableComponent2, rowStartX + 5, currentRowStartY + 8, enchantmentLevelColor);
    }

    @NotNull
    private String getTruncatedString(String stringToTruncate, int maxSize) {
        StringBuilder translatedEnchantmentName = new StringBuilder(Language.getInstance().getOrDefault(stringToTruncate));
        boolean hasTruncated = false;
        while (font.width(translatedEnchantmentName.toString()) > maxSize) {
            int nameLength = translatedEnchantmentName.length();
            if (hasTruncated) {
                translatedEnchantmentName.delete(nameLength - 3, nameLength);
            }
            nameLength = translatedEnchantmentName.length();

            Matcher matcher = SPLIT_WITH_COMBINING_CHARS.matcher(translatedEnchantmentName);
            if (matcher.find()) {
                List<MatchResult> matchResults = matcher.results().toList();
                MatchResult match = matchResults.get(matchResults.size() - 1);
                String lastCharacter = match.group();
                if (translatedEnchantmentName.toString().endsWith(lastCharacter)) {
                    translatedEnchantmentName.delete(nameLength - lastCharacter.length(), nameLength);
                }
            }
            else {
                break;
            }

            translatedEnchantmentName.append("...");
            hasTruncated = true;
        }
        return translatedEnchantmentName.toString();
    }

    private void populateAvailableEnchants() {
        enchantmentsAvailable.clear();
        ItemStack book = this.menu.bookSlot.getItem();
        if (!book.isEmpty()) {
            ItemStack tempBook = book.copy();
            tempBook.setCount(1);
            CompoundTag compoundtag = book.getTag();
            if (compoundtag != null) {
                tempBook.setTag(compoundtag.copy());
            }

            List<EnchantmentInstance> availableEnchantments = GeneralUtils.allAllowedEnchantsWithoutMaxLimit(100, tempBook, false);
            availableEnchantments.forEach(e -> enchantmentsAvailable.add(Map.entry(Registry.ENCHANTMENT.getResourceKey(e.enchantment).get(), e)));
            enchantmentsAvailable.sort(
                    Map.Entry.<ResourceKey<Enchantment>, EnchantmentInstance>comparingByKey()
                    .thenComparingInt(a -> a.getValue().level));
        }
    }

    protected void renderBg(PoseStack poseStack, float partialtick, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int startX = (width - imageWidth) / 2;
        int startY = (height - imageHeight) / 2;
        blit(poseStack, startX, startY, 0, 0, imageWidth, MENU_HEIGHT);
        blit(poseStack, startX, startY + MENU_HEIGHT, 0, 126, imageWidth, 72);
    }

    private void renderXPBar(PoseStack poseStack, int startX, int startY) {
        blit(poseStack, startX + XP_BAR_X_OFFSET, startY + XP_BAR_Y_OFFSET, getBlitOffset(), XP_BAR_U_TEXTURE, XP_BAR_V_TEXTURE, 54, 5, 256, 256);
    }

    private void renderScroller(PoseStack poseStack, int posX, int posY) {
        int rowCount = enchantmentsAvailable.size() + 1 - 3;
        if (rowCount > 1) {
            int scrollPosition = (int) (scrollOff * 42);
            blit(poseStack, posX, posY + scrollPosition, getBlitOffset(), ENCHANTMENT_SCROLLBAR_U_TEXTURE, ENCHANTMENT_SCROLLBAR_V_TEXTURE, 6, 17, 256, 256);
        }
        else {
            blit(poseStack, posX, posY, getBlitOffset(), ENCHANTMENT_SCROLLBAR_U_TEXTURE + 6.0F, ENCHANTMENT_SCROLLBAR_V_TEXTURE, 6, 17, 256, 256);
        }
    }

    private boolean canScroll(int numOffers) {
        return numOffers > 3;
    }

    private boolean handleEnchantmentAreaRow(double mouseX, double mouseY, Function<Integer, Boolean> targetedSectionTask, Consumer<Integer> untargetedSectionTask) {
        int startX = this.leftPos + ENCHANTMENT_AREA_X_OFFSET - 2;
        int startY = this.topPos + ENCHANTMENT_AREA_Y_OFFSET - 2;
        int selectableSections = this.startIndex + Math.min(enchantmentsAvailable.size(), 3);
        boolean targetedSectionTaskSuccess = false;
        for(int currentSection = this.startIndex; currentSection < selectableSections; ++currentSection) {
            int sectionOffset = currentSection - this.startIndex;
            double sectionMouseX = mouseX - (double)(startX);
            double sectionMouseY = mouseY - (double)(startY + sectionOffset * ENCHANTMENT_SECTION_HEIGHT);
            if (sectionMouseX >= 0.0D && sectionMouseX < ENCHANTMENT_SECTION_WIDTH && sectionMouseY >= 0.0D && sectionMouseY < ENCHANTMENT_SECTION_HEIGHT) {
                targetedSectionTaskSuccess = targetedSectionTask.apply(currentSection);
            }
            else {
                untargetedSectionTask.accept(currentSection);
            }
        }
        return targetedSectionTaskSuccess;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;

        if (handleEnchantmentAreaRow(mouseX, mouseY, (Integer sectionId) -> {
            if(this.menu.clickMenuButton(this.minecraft.player, sectionId)) {
                sendButtonPressToMenu(sectionId);
                return true;
            }
            return false;
        }, (i) -> {})) {
            return true;
        }

        int startY;
        int startX;

        startX = this.leftPos + ENCHANTMENT_SCROLLBAR_X_OFFSET;
        startY = this.topPos + ENCHANTMENT_SCROLLBAR_Y_OFFSET;
        if (mouseX >= startX &&
            mouseX < startX + 6 &&
            mouseY >= startY &&
            mouseY < startY + ENCHANTMENT_SCROLLBAR_Y_RANGE)
        {
            this.scrolling = true;
        }

        if (mouseX >= this.leftPos + XP_CONSUME_1_X_OFFSET &&
            mouseX < this.leftPos + XP_CONSUME_1_X_OFFSET + 18 &&
            mouseY >= this.topPos + XP_CONSUME_1_Y_OFFSET &&
            mouseY < this.topPos + XP_CONSUME_1_Y_OFFSET + 18)
        {
            pressedXp1Timer = BUTTON_PRESSED_TIMER_VISUAL;
            sendButtonPressToMenu(-2);
        }
        else if (mouseX >= this.leftPos + XP_CONSUME_2_X_OFFSET &&
                mouseX < this.leftPos + XP_CONSUME_2_X_OFFSET + 18 &&
                mouseY >= this.topPos + XP_CONSUME_2_Y_OFFSET &&
                mouseY < this.topPos + XP_CONSUME_2_Y_OFFSET + 18)
        {
            pressedXp2Timer = BUTTON_PRESSED_TIMER_VISUAL;
            sendButtonPressToMenu(-3);
        }
        else if (mouseX >= this.leftPos + XP_CONSUME_3_X_OFFSET &&
                mouseX < this.leftPos + XP_CONSUME_3_X_OFFSET + 18 &&
                mouseY >= this.topPos + XP_CONSUME_3_Y_OFFSET &&
                mouseY < this.topPos + XP_CONSUME_3_Y_OFFSET + 18)
        {
            pressedXp3Timer = BUTTON_PRESSED_TIMER_VISUAL;
            sendButtonPressToMenu(-4);
        }
        else if (mouseX >= this.leftPos + CONSUME_CONFIRMATION_X_OFFSET &&
                mouseX < this.leftPos + CONSUME_CONFIRMATION_X_OFFSET + 18 &&
                mouseY >= this.topPos + CONSUME_CONFIRMATION_Y_OFFSET &&
                mouseY < this.topPos + CONSUME_CONFIRMATION_Y_OFFSET + 18)
        {
            pressedConsumeTimer = BUTTON_PRESSED_TIMER_VISUAL;
            sendButtonPressToMenu(-5);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void sendButtonPressToMenu(Integer sectionId) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
        this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, sectionId);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && canScroll(enchantmentsAvailable.size())) {
            int topY = this.topPos + ENCHANTMENT_SCROLLBAR_Y_OFFSET;
            int bottomY = topY + ENCHANTMENT_SCROLLBAR_Y_RANGE;
            this.scrollOff = ((float)mouseY - (float)topY - 7.5F) / ((float)(bottomY - topY) - 15.0F);
            this.scrollOff = Mth.clamp(this.scrollOff, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOff * (float)this.getOffscreenRows()) + 0.5D);
            return true;
        }
        else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (canScroll(enchantmentsAvailable.size())) {
            int offscreenRows = this.getOffscreenRows();
            float percentage = (float)delta / (float)offscreenRows;
            this.scrollOff = Mth.clamp(this.scrollOff - percentage, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOff * (float)offscreenRows) + 0.5D);
        }

        return true;
    }

    protected int getOffscreenRows() {
        return Math.max(enchantmentsAvailable.size() - 3, 0);
    }
}