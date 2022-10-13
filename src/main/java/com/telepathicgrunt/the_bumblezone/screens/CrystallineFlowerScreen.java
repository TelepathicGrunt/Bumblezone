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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class CrystallineFlowerScreen extends AbstractContainerScreen<CrystallineFlowerMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Bumblezone.MODID, "textures/gui/container/crystallized_flower.png");

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
    private static final float ENCHANTMENT_SCROLLBAR_U_TEXTURE = 162.0F;
    private static final float ENCHANTMENT_SCROLLBAR_V_TEXTURE = 233.0F;

    private static final float ENCHANTMENT_SELECTED_U_TEXTURE = 0F;
    private static final float ENCHANTMENT_SELECTED_V_TEXTURE = 197.0F;
    private static final float ENCHANTMENT_UNSELECTED_U_TEXTURE = 0F;
    private static final float ENCHANTMENT_UNSELECTED_V_TEXTURE = 216.0F;
    private static final float ENCHANTMENT_HIGHLIGHTED_U_TEXTURE = 0F;
    private static final float ENCHANTMENT_HIGHLIGHTED_V_TEXTURE = 235.0F;

    private static final int XP_BAR_X_OFFSET = 11;
    private static final int XP_BAR_Y_OFFSET = 99;
    private static final float XP_BAR_U_TEXTURE = 108.0F;
    private static final float XP_BAR_V_TEXTURE = 238.0F;

    private float scrollOff;
    private boolean scrolling;
    private int startIndex;

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

        renderTooltip(poseStack, mouseX, mouseY);
    }

    private void drawEnchantmentText(PoseStack poseStack, int rowStartX, int currentRowStartY, Map.Entry<ResourceKey<Enchantment>, EnchantmentInstance> enchantmentEntry, int enchantmentNameColor, int enchantmentLevelColor) {
        MutableComponent mutableComponent = Component.translatable("""
                enchantment.%s.%s""".formatted(
                    enchantmentEntry.getKey().location().getNamespace(),
                    enchantmentEntry.getKey().location().getPath()));
        MutableComponent mutableComponent2 = Component.translatable("the_bumblezone.container.crystalline_flower.level", enchantmentEntry.getValue().level);
        font.draw(poseStack, mutableComponent, rowStartX, currentRowStartY, enchantmentNameColor);
        font.draw(poseStack, mutableComponent2, rowStartX + 5, currentRowStartY + 8, enchantmentLevelColor);
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
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, sectionId);
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
        if (mouseX >= (double)startX && mouseX < (double)(startX + 6) && mouseY >= (double)startY && mouseY < (double)(startY + ENCHANTMENT_SCROLLBAR_Y_RANGE)) {
            this.scrolling = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
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