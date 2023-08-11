package com.telepathicgrunt.the_bumblezone.client.rendering.essence;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.items.essence.RadianceEssence;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtilsClient;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class RadianceEssenceArmorMessage {
    private static final String DURABILITY_TEXT = "item.the_bumblezone.essence_radiance_durability_text";
    private static final String DURABILITY_LOW_TEXT = "item.the_bumblezone.essence_radiance_durability_low_text";
    private static final String ADVANCED_TEXT = "item.the_bumblezone.essence_radiance_advanced_text";
    private static final String HELMET_TEXT = "item.the_bumblezone.essence_radiance_helmet_text";
    private static final String CHESTPLATE_TEXT = "item.the_bumblezone.essence_radiance_chestplate_text";
    private static final String LEGGINGS_TEXT = "item.the_bumblezone.essence_radiance_leggings_text";
    private static final String BOOTS_TEXT = "item.the_bumblezone.essence_radiance_boots_text";

    public static void armorDurabilityMessage(Player player, GuiGraphics guiGraphics) {
        if (RadianceEssence.IsRadianceEssenceActive(player) && BzClientConfigs.radianceEssenceArmorDurability) {
            Minecraft minecraft = Minecraft.getInstance();

            int i = 0;
            for (ItemStack armorStack : player.getArmorSlots()) {
                boolean isLowDurability = false;
                MutableComponent bodyText;
                if (armorStack.isEmpty()) {
                    bodyText = Component.empty();
                }
                else {
                    int maxDamage = armorStack.getMaxDamage();
                    int currentHealth = armorStack.getMaxDamage() - armorStack.getDamageValue();
                    if (currentHealth < maxDamage * 0.25) {
                        bodyText = Component.translatable(DURABILITY_LOW_TEXT, currentHealth, maxDamage).withStyle(ChatFormatting.RED);
                        isLowDurability = true;
                    }
                    else {
                        bodyText = Component.translatable(DURABILITY_TEXT, currentHealth, maxDamage);
                    }
                }

                if (i == 3) {
                    MutableComponent line = setupComponent(minecraft, HELMET_TEXT, bodyText, isLowDurability);
                    renderScrollingString(minecraft, guiGraphics, armorStack, line, 60, 30);
                }
                else if (i == 2) {
                    MutableComponent line = setupComponent(minecraft, CHESTPLATE_TEXT, bodyText, isLowDurability);
                    renderScrollingString(minecraft, guiGraphics, armorStack, line, 40, 20);
                }
                else if (i == 1) {
                    MutableComponent line = setupComponent(minecraft, LEGGINGS_TEXT, bodyText, isLowDurability);
                    renderScrollingString(minecraft, guiGraphics, armorStack, line, 20, 10);
                }
                else if (i == 0) {
                    MutableComponent line = setupComponent(minecraft, BOOTS_TEXT, bodyText, isLowDurability);
                    renderScrollingString(minecraft, guiGraphics, armorStack, line, 0, 0);
                }

                i++;
            }
        }
    }

    private static MutableComponent setupComponent(Minecraft minecraft, String helmetText, MutableComponent bodyText, boolean isLowDurability) {
        MutableComponent line = Component.translatable(
                helmetText,
                bodyText,
                minecraft.options.advancedItemTooltips ? Component.translatable(ADVANCED_TEXT) : Component.empty());

        if (isLowDurability) {
            line = line.withStyle(ChatFormatting.RED);
        }
        return line;
    }

    public static void renderScrollingString(Minecraft minecraft, GuiGraphics guiGraphics, ItemStack armorItem, Component component, int yOffset, int yOffset2) {
        if (component == null) {
            return;
        }

        int startOfHotbar = (guiGraphics.guiWidth() - 250) / 2;
        GeneralUtilsClient.renderScrollingString(
                guiGraphics,
                minecraft.font,
                component,
                BzClientConfigs.radianceEssenceArmorDurabilityXCoord,
                guiGraphics.guiHeight() - BzClientConfigs.radianceEssenceArmorDurabilityYCoord - yOffset,
                BzClientConfigs.radianceEssenceArmorDurabilityXCoord + startOfHotbar,
                guiGraphics.guiHeight(),
                0xFFE090
        );

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();

        pose.translate(
                BzClientConfigs.radianceEssenceArmorDurabilityXCoord - 2,
                guiGraphics.guiHeight() - (BzClientConfigs.radianceEssenceArmorDurabilityYCoord - 2) - yOffset2,
                0);

        pose.scale(0.7f, 0.7f, 1);
        guiGraphics.renderItem(armorItem, 0, 0);
        pose.popPose();
    }
}
