package com.telepathicgrunt.the_bumblezone.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.utils.EnchantmentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private static final int TIER_X_OFFSET = 11;
    private static final int TIER_Y_OFFSET = 15;
    private static final float TIER_FLOWER_U_TEXTURE = 195.0F;
    private static final float TIER_FLOWER_V_TEXTURE = 197.0F;
    private static final float TIER_BODY_U_TEXTURE = 195.0F;
    private static final float TIER_BODY_V_TEXTURE = 207.0F;
    private static final float TIER_BLOCK_U_TEXTURE = 195.0F;
    private static final float TIER_BLOCK_V_TEXTURE = 217.0F;

    private static final int BUTTON_PRESSED_TIMER_VISUAL = 25;

    private float scrollOff;
    private boolean scrolling;
    private int startIndex;
    private int pressedXp1Timer = 0;
    private int pressedXp2Timer = 0;
    private int pressedXp3Timer = 0;
    private int pressedConsumeTimer = 0;

    private List<Boolean> cachedObstructions = new ArrayList<>();
    private int cachedObstructionsTimer = 0;
    private int prevXpTier = 0;
    private boolean prevBookSlotEmpty = true;

    public static List<EnchantmentSkeleton> enchantmentsAvailable = new ArrayList<>();

    public CrystallineFlowerScreen(CrystallineFlowerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        passEvents = false;
        inventoryLabelY = imageHeight + INVENTORY_LABEL_Y_OFFSET;
        titleLabelY += TITLE_LABEL_Y_OFFSET;
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        RenderSystem.enableDepthTest();

        ItemStack book = this.menu.bookSlot.getItem();
        if (book.isEmpty() != prevBookSlotEmpty || this.menu.xpTier.get() != prevXpTier) {
            populateAvailableEnchants();
            prevXpTier = this.menu.xpTier.get();
            prevBookSlotEmpty = book.isEmpty();
        }

        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        final int rowStartX = startX + ENCHANTMENT_AREA_X_OFFSET;
        final int rowStartY = startY + ENCHANTMENT_AREA_Y_OFFSET;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        renderScroller(poseStack, startX + ENCHANTMENT_SCROLLBAR_X_OFFSET, startY + ENCHANTMENT_SCROLLBAR_Y_OFFSET);

        handleEnchantmentAreaRow(mouseX, mouseY,
            (Integer sectionId) -> {
                if (sectionId >= enchantmentsAvailable.size()) {
                    return false;
                }

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);

                EnchantmentSkeleton enchantmentEntry = enchantmentsAvailable.get(sectionId);
                boolean isCurse = enchantmentEntry.isCurse;
                boolean isTreasure = enchantmentEntry.isTreasure;
                int row = sectionId - this.startIndex;
                if (sectionId == this.menu.selectedEnchantmentIndex.get()) {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, rowStartX - 2, rowStartY - 2 + row * ENCHANTMENT_SECTION_HEIGHT, getBlitOffset(), ENCHANTMENT_SELECTED_U_TEXTURE, ENCHANTMENT_SELECTED_V_TEXTURE, ENCHANTMENT_SECTION_WIDTH + 1, ENCHANTMENT_SECTION_HEIGHT, 256, 256);
                    drawEnchantmentText(
                            poseStack,
                            rowStartX,
                            rowStartY + row * ENCHANTMENT_SECTION_HEIGHT,
                            enchantmentEntry,
                            isCurse ? 0x990000 : isTreasure ? 0xFFF000 : 0xFFD000,
                            0xC0FF00
                    );
                }
                else {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, rowStartX - 2, rowStartY - 2 + row * ENCHANTMENT_SECTION_HEIGHT, getBlitOffset(), ENCHANTMENT_HIGHLIGHTED_U_TEXTURE, ENCHANTMENT_HIGHLIGHTED_V_TEXTURE, ENCHANTMENT_SECTION_WIDTH + 1, ENCHANTMENT_SECTION_HEIGHT, 256, 256);
                    drawEnchantmentText(
                            poseStack,
                            rowStartX,
                            rowStartY + row * 19,
                            enchantmentEntry,
                            isCurse ? 0x800000 : isTreasure ? 0xFFFF50 : 0x402020,
                            0x304000
                    );
                }
                return true;
            },
            (Integer sectionId) -> {
                if (sectionId >= enchantmentsAvailable.size()) {
                    return;
                }

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);

                EnchantmentSkeleton enchantmentEntry = enchantmentsAvailable.get(sectionId);
                boolean isCurse = enchantmentEntry.isCurse;
                boolean isTreasure = enchantmentEntry.isTreasure;
                int row = sectionId - this.startIndex;
                if (sectionId == this.menu.selectedEnchantmentIndex.get()) {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, rowStartX - 2, rowStartY - 2 + row * ENCHANTMENT_SECTION_HEIGHT, getBlitOffset(), ENCHANTMENT_SELECTED_U_TEXTURE, ENCHANTMENT_SELECTED_V_TEXTURE, ENCHANTMENT_SECTION_WIDTH + 1, ENCHANTMENT_SECTION_HEIGHT, 256, 256);
                    drawEnchantmentText(
                            poseStack,
                            rowStartX,
                            rowStartY + row * ENCHANTMENT_SECTION_HEIGHT,
                            enchantmentEntry,
                            isCurse ? 0x990000 : isTreasure ? 0xFFF000 : 0xFFD000,
                            0xC0FF00);
                }
                else {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, rowStartX - 2, rowStartY - 2 + row * ENCHANTMENT_SECTION_HEIGHT, getBlitOffset(), ENCHANTMENT_UNSELECTED_U_TEXTURE, ENCHANTMENT_UNSELECTED_V_TEXTURE, ENCHANTMENT_SECTION_WIDTH + 1, ENCHANTMENT_SECTION_HEIGHT, 256, 256);
                    drawEnchantmentText(
                            poseStack,
                            rowStartX,
                            rowStartY + row * ENCHANTMENT_SECTION_HEIGHT,
                            enchantmentEntry,
                            isCurse ? 0xFF2000 : isTreasure ? 0xFFF000 : 0xD0B0F0,
                            0x00DD40);
                }
            });


        if (this.menu.tooManyEnchantmentsOnInput.get() == 1) {
            MutableComponent mutableComponent = Component.translatable("the_bumblezone.container.crystalline_flower.too_many_enchants").withStyle(ChatFormatting.BOLD);
            Screen.drawCenteredString(poseStack, font, mutableComponent, rowStartX + 45, rowStartY - 36, 0xD03010);
        }
        else if (this.menu.selectedEnchantmentIndex.get() != -1) {
            EnchantmentSkeleton enchantment = enchantmentsAvailable.get(this.menu.selectedEnchantmentIndex.get());
            int tierCost = EnchantmentUtils.getEnchantmentTierCost(enchantment.level, enchantment.minCost, enchantment.isTreasure, enchantment.isCurse);
            MutableComponent mutableComponent = Component.translatable("the_bumblezone.container.crystalline_flower.tier_cost_arrow", tierCost).withStyle(ChatFormatting.BOLD);
            Screen.drawCenteredString(poseStack, font, mutableComponent, rowStartX + 45, rowStartY - 36, 0xD03010);
        }

        drawPushableButtons(poseStack, startX, startY, mouseX, mouseY);
        drawTierState(poseStack, startX, startY);
        renderXPBar(poseStack, startX, startY);
        renderTooltip(poseStack, mouseX, mouseY);
    }


    private void drawTierState(PoseStack poseStack, int startX, int startY) {
        int xOffset = startX + TIER_X_OFFSET;
        int yOffset = startY + TIER_Y_OFFSET;

        if (cachedObstructionsTimer <= 0) {
            cachedObstructions = CrystallineFlower.getObstructions(
                7,
                this.minecraft.player.getLevel(),
                new BlockPos(
                    this.menu.bottomBlockPosX.get(),
                    this.menu.bottomBlockPosY.get(),
                    this.menu.bottomBlockPosZ.get()));
            cachedObstructionsTimer = 100;
        }
        cachedObstructionsTimer--;

        for (int i = 0; i < 7; i++) {
            if (i >= this.menu.xpTier.get()) {
                if (i < cachedObstructions.size() && cachedObstructions.get(i)) {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, xOffset, yOffset + (72 - (i * 12)), getBlitOffset(), TIER_BLOCK_U_TEXTURE, TIER_BLOCK_V_TEXTURE, 10, 10, 256, 256);
                }
                continue;
            }

            float textureU = TIER_BODY_U_TEXTURE;
            float textureV = TIER_BODY_V_TEXTURE;
            if (i + 1 == this.menu.xpTier.get()) {
                textureU = TIER_FLOWER_U_TEXTURE;
                textureV = TIER_FLOWER_V_TEXTURE;
            }
            RenderSystem.enableDepthTest();
            blit(poseStack, xOffset, yOffset + (72 - (i * 12)), getBlitOffset(), textureU, textureV, 10, 10, 256, 256);
        }
    }

    private void drawPushableButtons(PoseStack poseStack, int startX, int startY, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);

        if (BzGeneralConfigs.crystallineFlowerConsumeExperienceUI.get()) {
            if (pressedXp1Timer > 0 ||
                    this.menu.xpTier.get() == 7 ||
                    isPathObstructed(1) ||
                    !canPlayerBuyTier(1)) {
                pressedXp1Timer--;
                RenderSystem.enableDepthTest();
                blit(poseStack, startX + XP_CONSUME_1_X_OFFSET, startY + XP_CONSUME_1_Y_OFFSET, getBlitOffset(), XP_CONSUME_1_U_OFFSET, XP_CONSUME_1_V_OFFSET + 18, 18, 18, 256, 256);
            }
            else {
                int xOffset = startX + XP_CONSUME_1_X_OFFSET;
                int yOffset = startY + XP_CONSUME_1_Y_OFFSET;
                if (mouseX - xOffset >= 0.0D && mouseX - xOffset < 18.0D && mouseY - yOffset >= 0.0D && mouseY - yOffset < 18.0D) {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_1_U_OFFSET, XP_CONSUME_1_V_OFFSET + 36, 18, 18, 256, 256);
                }
                else {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_1_U_OFFSET, XP_CONSUME_1_V_OFFSET, 18, 18, 256, 256);
                }
            }
        }

        if (BzGeneralConfigs.crystallineFlowerConsumeExperienceUI.get()) {
            if (pressedXp2Timer > 0 ||
                    this.menu.xpTier.get() == 7 ||
                    isPathObstructed(2) ||
                    !canPlayerBuyTier(2)) {
                pressedXp2Timer--;
                RenderSystem.enableDepthTest();
                blit(poseStack, startX + XP_CONSUME_2_X_OFFSET, startY + XP_CONSUME_2_Y_OFFSET, getBlitOffset(), XP_CONSUME_2_U_OFFSET, XP_CONSUME_2_V_OFFSET + 18, 18, 18, 256, 256);
            }
            else {
                int xOffset = startX + XP_CONSUME_2_X_OFFSET;
                int yOffset = startY + XP_CONSUME_2_Y_OFFSET;
                if (mouseX - xOffset >= 0.0D && mouseX - xOffset < 18.0D && mouseY - yOffset >= 0.0D && mouseY - yOffset < 18.0D) {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_2_U_OFFSET, XP_CONSUME_2_V_OFFSET + 36, 18, 18, 256, 256);
                }
                else {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_2_U_OFFSET, XP_CONSUME_2_V_OFFSET, 18, 18, 256, 256);
                }
            }
        }

        if (BzGeneralConfigs.crystallineFlowerConsumeExperienceUI.get()) {
            if (pressedXp3Timer > 0 ||
                    this.menu.xpTier.get() == 7 ||
                    isPathObstructed(3) ||
                    !canPlayerBuyTier(3))
            {
                pressedXp3Timer--;
                RenderSystem.enableDepthTest();
                blit(poseStack, startX + XP_CONSUME_3_X_OFFSET, startY + XP_CONSUME_3_Y_OFFSET, getBlitOffset(), XP_CONSUME_3_U_OFFSET, XP_CONSUME_3_V_OFFSET + 18, 18, 18, 256, 256);
            }
            else {
                int xOffset = startX + XP_CONSUME_3_X_OFFSET;
                int yOffset = startY + XP_CONSUME_3_Y_OFFSET;
                if (mouseX - xOffset >= 0.0D && mouseX - xOffset < 18.0D && mouseY - yOffset >= 0.0D && mouseY - yOffset < 18.0D) {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_3_U_OFFSET, XP_CONSUME_3_V_OFFSET + 36, 18, 18, 256, 256);
                }
                else {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, xOffset, yOffset, getBlitOffset(), XP_CONSUME_3_U_OFFSET, XP_CONSUME_3_V_OFFSET, 18, 18, 256, 256);
                }
            }
        }

        if (!BzGeneralConfigs.crystallineFlowerConsumeExperienceUI.get()) {
            int xOffset = startX + 26;
            int yOffset = startY + 14;
            RenderSystem.enableDepthTest();
            blit(poseStack, xOffset, yOffset, getBlitOffset(), 176, 0, 48, 58, 256, 256);
        }

        if (pressedConsumeTimer > 0) {
            pressedConsumeTimer--;
        }
        if (this.menu.consumeSlotFullyObstructed.get() != 1 && BzGeneralConfigs.crystallineFlowerConsumeItemUI.get()) {
            if (pressedConsumeTimer > 0) {
                RenderSystem.enableDepthTest();
                blit(poseStack, startX + CONSUME_CONFIRMATION_X_OFFSET, startY + CONSUME_CONFIRMATION_Y_OFFSET, getBlitOffset(), CONSUME_CONFIRMATION_U_OFFSET, CONSUME_CONFIRMATION_V_OFFSET + 18, 18, 18, 256, 256);
                blit(poseStack, startX + CONSUME_ARROW_X_OFFSET, startY + CONSUME_ARROW_Y_OFFSET, getBlitOffset(), CONSUME_ARROW_U_OFFSET, CONSUME_ARROW_V_OFFSET + 18, 15, 11, 256, 256);
            }
            else if (this.menu.consumeSlot.hasItem() && this.menu.xpTier.get() < 7) {
                int xOffset = startX + CONSUME_CONFIRMATION_X_OFFSET;
                int yOffset = startY + CONSUME_CONFIRMATION_Y_OFFSET;
                if (mouseX - xOffset >= 0.0D && mouseX - xOffset < 18.0D && mouseY - yOffset >= 0.0D && mouseY - yOffset < 18.0D) {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, startX + CONSUME_CONFIRMATION_X_OFFSET, startY + CONSUME_CONFIRMATION_Y_OFFSET, getBlitOffset(), CONSUME_CONFIRMATION_U_OFFSET, CONSUME_CONFIRMATION_V_OFFSET + 36, 18, 18, 256, 256);
                }
                else {
                    RenderSystem.enableDepthTest();
                    blit(poseStack, startX + CONSUME_CONFIRMATION_X_OFFSET, startY + CONSUME_CONFIRMATION_Y_OFFSET, getBlitOffset(), CONSUME_CONFIRMATION_U_OFFSET, CONSUME_CONFIRMATION_V_OFFSET, 18, 18, 256, 256);
                }
                RenderSystem.enableDepthTest();
                blit(poseStack, startX + CONSUME_ARROW_X_OFFSET, startY + CONSUME_ARROW_Y_OFFSET, getBlitOffset(), CONSUME_ARROW_U_OFFSET, CONSUME_ARROW_V_OFFSET, 15, 11, 256, 256);
            }
        }

        if (!BzGeneralConfigs.crystallineFlowerConsumeItemUI.get()) {
            int xOffset = startX + 26;
            int yOffset = startY + 78;
            RenderSystem.enableDepthTest();
            blit(poseStack, xOffset, yOffset, getBlitOffset(), 176, 59, 48, 19, 256, 256);
        }
    }

    private void drawEnchantmentText(PoseStack poseStack, int rowStartX, int currentRowStartY, EnchantmentSkeleton enchantmentEntry, int enchantmentNameColor, int enchantmentLevelColor) {
        String translatedEnchantmentName = getTruncatedString(
                enchantmentEntry.namespace,
                enchantmentEntry.path,
                88);

        MutableComponent mutableComponent = Component.literal(translatedEnchantmentName);
        MutableComponent mutableComponent2 = Component.translatable("the_bumblezone.container.crystalline_flower.level", enchantmentEntry.level);
        if (enchantmentEntry.isMaxLevel) {
            mutableComponent2.append(Component.translatable("the_bumblezone.container.crystalline_flower.level_star"));
        }

        font.draw(poseStack, mutableComponent, rowStartX, currentRowStartY, enchantmentNameColor);
        font.draw(poseStack, mutableComponent2, rowStartX + 5, currentRowStartY + 8, enchantmentLevelColor);
    }

    @NotNull
    private String getTruncatedString(String namespace, String path, int maxSize) {
        StringBuilder translatedEnchantmentName = new StringBuilder(Language.getInstance().getOrDefault("""
                enchantment.%s.%s""".formatted(namespace, path)));

        String originalNameOutput = translatedEnchantmentName.toString();
        if (originalNameOutput.contains("enchantment.")) {
            translatedEnchantmentName = new StringBuilder(Arrays.stream(path
                    .split("_"))
                    .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1).toLowerCase(Locale.ROOT))
                    .collect(Collectors.joining(" ")));
        }

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
        ItemStack book = this.menu.bookSlot.getItem();
        if (!book.isEmpty() && this.menu.xpTier.get() > 1 && this.menu.tooManyEnchantmentsOnInput.get() != 1) {
            ItemStack tempBook = book.copy();
            tempBook.setCount(1);
            CompoundTag compoundtag = book.getTag();
            if (compoundtag != null) {
                tempBook.setTag(compoundtag.copy());
            }
        }
        else {
            enchantmentsAvailable.clear();
        }
    }

    protected void renderBg(PoseStack poseStack, float partialtick, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int startX = (width - imageWidth) / 2;
        int startY = (height - imageHeight) / 2;
        RenderSystem.enableDepthTest();
        blit(poseStack, startX, startY, 0, 0, imageWidth, MENU_HEIGHT);
        blit(poseStack, startX, startY + MENU_HEIGHT, 0, 126, imageWidth, 71);
    }

    private void renderXPBar(PoseStack poseStack, int startX, int startY) {
        if (this.menu.xpTier.get() == 7) {
            RenderSystem.enableDepthTest();
            blit(poseStack, startX + XP_BAR_X_OFFSET, startY + XP_BAR_Y_OFFSET, getBlitOffset(), XP_BAR_U_TEXTURE, XP_BAR_V_TEXTURE - 5, 54, 5, 256, 256);
        }
        else {
            RenderSystem.enableDepthTest();
            blit(poseStack, startX + XP_BAR_X_OFFSET, startY + XP_BAR_Y_OFFSET, getBlitOffset(), XP_BAR_U_TEXTURE, XP_BAR_V_TEXTURE, 54, 5, 256, 256);
            if (this.menu.xpBarPercent.get() > 0) {
                RenderSystem.enableDepthTest();
                blit(poseStack, startX + XP_BAR_X_OFFSET, startY + XP_BAR_Y_OFFSET, getBlitOffset(), XP_BAR_U_TEXTURE, XP_BAR_V_TEXTURE + 5, (int) (54 * (this.menu.xpBarPercent.get() / 100f)), 5, 256, 256);
            }
        }
    }

    private void renderScroller(PoseStack poseStack, int posX, int posY) {
        int rowCount = enchantmentsAvailable.size() + 1 - 3;
        if (rowCount > 1) {
            if (startIndex > rowCount) {
                scrollOff = 1.0F;
            }
            startIndex = (int)((double)(this.scrollOff * (float)this.getOffscreenRows()) + 0.5D);
            int scrollPosition = (int) (scrollOff * 42);
            RenderSystem.enableDepthTest();
            blit(poseStack, posX, posY + scrollPosition, getBlitOffset(), ENCHANTMENT_SCROLLBAR_U_TEXTURE, ENCHANTMENT_SCROLLBAR_V_TEXTURE, 6, 17, 256, 256);
        }
        else {
            RenderSystem.enableDepthTest();
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

    protected void renderTooltip(PoseStack poseStack, int x, int y) {
        super.renderTooltip(poseStack, x, y);
        int startX = this.leftPos + ENCHANTMENT_AREA_X_OFFSET - 2;
        int startY = this.topPos + ENCHANTMENT_AREA_Y_OFFSET - 2;
        int selectableSections = this.startIndex + Math.min(enchantmentsAvailable.size(), 3);
        for(int currentSection = this.startIndex; currentSection < selectableSections; ++currentSection) {
            if (currentSection >= enchantmentsAvailable.size()) continue;

            int sectionOffset = currentSection - this.startIndex;
            double sectionMouseX = x - (double)(startX);
            double sectionMouseY = y - (double)(startY + sectionOffset * ENCHANTMENT_SECTION_HEIGHT);
            if (sectionMouseX >= 0.0D && sectionMouseX < ENCHANTMENT_SECTION_WIDTH && sectionMouseY >= 0.0D && sectionMouseY < ENCHANTMENT_SECTION_HEIGHT) {
                EnchantmentSkeleton enchantment = enchantmentsAvailable.get(currentSection);
                int tierCost = EnchantmentUtils.getEnchantmentTierCost(enchantment.level, enchantment.minCost, enchantment.isTreasure, enchantment.isCurse);

                String translatedEnchantmentName = Language.getInstance().getOrDefault("""
                    enchantment.%s.%s""".formatted(enchantment.namespace, enchantment.path));

                if (translatedEnchantmentName.contains("enchantment.")) {
                    translatedEnchantmentName = Arrays.stream(enchantment.path
                            .split("_"))
                            .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1).toLowerCase(Locale.ROOT))
                            .collect(Collectors.joining(" "));
                }

                MutableComponent mutableComponent = Component.literal(translatedEnchantmentName)
                        .withStyle(ChatFormatting.GOLD);

                MutableComponent mutableComponent2 = Component.translatable("the_bumblezone.container.crystalline_flower.level", enchantment.level)
                        .withStyle(ChatFormatting.GREEN);

                if (enchantment.isMaxLevel) {
                    mutableComponent2.append(Component.translatable("the_bumblezone.container.crystalline_flower.level_star"));
                }

                MutableComponent mutableComponent3 = Component.translatable("the_bumblezone.container.crystalline_flower.tier_cost", tierCost)
                        .withStyle(ChatFormatting.RED);

                MutableComponent mutableComponent4 = Component.literal(enchantment.namespace)
                        .withStyle(ChatFormatting.DARK_GRAY);

                this.renderTooltip(
                        poseStack,
                        List.of(mutableComponent, mutableComponent2, mutableComponent3, mutableComponent4),
                        Optional.empty(),
                        x,
                        y,
                        this.font);
                return;
            }
        }
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

        if (this.menu.xpTier.get() != 7)
        {
            if (BzGeneralConfigs.crystallineFlowerConsumeExperienceUI.get() &&
                canPlayerBuyTier(1) &&
                !isPathObstructed(1) &&
                mouseX >= this.leftPos + XP_CONSUME_1_X_OFFSET &&
                mouseX < this.leftPos + XP_CONSUME_1_X_OFFSET + 18 &&
                mouseY >= this.topPos + XP_CONSUME_1_Y_OFFSET &&
                mouseY < this.topPos + XP_CONSUME_1_Y_OFFSET + 18)
            {
                pressedXp1Timer = BUTTON_PRESSED_TIMER_VISUAL;
                sendButtonPressToMenu(-2);
            }
            else if (BzGeneralConfigs.crystallineFlowerConsumeExperienceUI.get() &&
                    canPlayerBuyTier(2) &&
                    !isPathObstructed(2) &&
                    mouseX >= this.leftPos + XP_CONSUME_2_X_OFFSET &&
                    mouseX < this.leftPos + XP_CONSUME_2_X_OFFSET + 18 &&
                    mouseY >= this.topPos + XP_CONSUME_2_Y_OFFSET &&
                    mouseY < this.topPos + XP_CONSUME_2_Y_OFFSET + 18)
            {
                pressedXp2Timer = BUTTON_PRESSED_TIMER_VISUAL;
                sendButtonPressToMenu(-3);
            }
            else if (BzGeneralConfigs.crystallineFlowerConsumeExperienceUI.get() &&
                    canPlayerBuyTier(3) &&
                    !isPathObstructed(3) &&
                    mouseX >= this.leftPos + XP_CONSUME_3_X_OFFSET &&
                    mouseX < this.leftPos + XP_CONSUME_3_X_OFFSET + 18 &&
                    mouseY >= this.topPos + XP_CONSUME_3_Y_OFFSET &&
                    mouseY < this.topPos + XP_CONSUME_3_Y_OFFSET + 18)
            {
                pressedXp3Timer = BUTTON_PRESSED_TIMER_VISUAL;
                sendButtonPressToMenu(-4);
            }
            else if (BzGeneralConfigs.crystallineFlowerConsumeItemUI.get() &&
                    this.menu.consumeSlotFullyObstructed.get() != 1 &&
                    mouseX >= this.leftPos + CONSUME_CONFIRMATION_X_OFFSET &&
                    mouseX < this.leftPos + CONSUME_CONFIRMATION_X_OFFSET + 18 &&
                    mouseY >= this.topPos + CONSUME_CONFIRMATION_Y_OFFSET &&
                    mouseY < this.topPos + CONSUME_CONFIRMATION_Y_OFFSET + 18)
            {
                pressedConsumeTimer = BUTTON_PRESSED_TIMER_VISUAL;
                sendButtonPressToMenu(-5);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private Boolean canPlayerBuyTier(int xpTiersToCheck) {
        return xpTiersToCheck <= this.menu.playerHasXPForTier.get();
    }

    private Boolean isPathObstructed(int xpTiersToCheck) {
        for (int i = 0; i < xpTiersToCheck; i++) {
            if (this.menu.xpTier.get() + i < cachedObstructions.size() &&
                cachedObstructions.get(this.menu.xpTier.get() + i))
            {
                return true;
            }
        }

        return false;
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

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
        return mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.imageWidth) || mouseY >= (double)(top + this.imageHeight + 32);
    }
}