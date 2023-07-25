package com.telepathicgrunt.the_bumblezone.client.rendering.essence;

import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.items.essence.KnowingEssence;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.regex.Pattern;

public class KnowingEssenceStructureMessage {
    private static final String SEPARATOR = "item.the_bumblezone.essence_knowing_separator";
    private static final String WITHIN_TEXT = "item.the_bumblezone.essence_knowing_within_text";
    private static final String WITHIN_TEXT_PLURAL = "item.the_bumblezone.essence_knowing_within_text_plural";
    private static final String FIRST_LETTER_REGEX = "\\b(.)(.*?)\\b";
    private static final Pattern FIREST_LETTER_PATTERN = Pattern.compile(FIRST_LETTER_REGEX);

    public static void inStructureMessage(Player player, GuiGraphics guiGraphics) {
        if (KnowingEssence.IsKnowingEssenceActive(player) && BzClientConfigs.knowingEssenceStructureNameClient) {
            ItemStack offHandItem = player.getOffhandItem();
            String structureResourceLocationStrings = KnowingEssence.GetAllStructure(offHandItem);
            if (structureResourceLocationStrings != null && !structureResourceLocationStrings.isEmpty()) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.options.advancedItemTooltips) {

                    String[] structEntries = structureResourceLocationStrings.split(" ");

                    Component line1 = null;
                    Component line2 = null;
                    Component line3 = null;

                    int currentEntryIndex = 0;
                    for (String structEntry : structEntries) {
                        int currentLine = currentEntryIndex % 3;
                        if (currentLine == 0) {
                            if (line1 != null) {
                                line1 = Component.translatable(SEPARATOR, line1, Component.literal(structEntry));
                            }
                            else {
                                line1 = Component.literal(structEntry);
                            }
                        }
                        else if (currentLine == 1) {
                            if (line2 != null) {
                                line2 = Component.translatable(SEPARATOR, line2, Component.literal(structEntry));
                            }
                            else {
                                line2 = Component.literal(structEntry);
                            }
                        }
                        else if  (currentLine == 2) {
                            if (line3 != null) {
                                line3 = Component.translatable(SEPARATOR, line3, Component.literal(structEntry));
                            }
                            else {
                                line3 = Component.literal(structEntry);
                            }
                        }

                        currentEntryIndex++;
                    }

                    renderScrollingString(minecraft, guiGraphics, line1, line2, line3);
                }
                else {
                    String[] structEntries = structureResourceLocationStrings.split(" ");

                    Component line1 = null;
                    Component line2 = null;
                    Component line3 = null;

                    int currentEntryIndex = 0;
                    for (String structEntry : structEntries) {
                        int currentLine = currentEntryIndex % 3;

                        String langKey = transformStructureToLangKey(structEntry);
                        Language language = Language.getInstance();
                        if (!language.has(langKey)) {
                            langKey = structEntry
                                    .split(":")[1]
                                    .replace("_", " ");

                            langKey = FIREST_LETTER_PATTERN
                                    .matcher(langKey)
                                    .replaceAll(matchResult -> matchResult.group(1).toUpperCase() + matchResult.group(2));
                        }

                        if (currentLine == 0) {
                            if (line1 != null) {
                                line1 = Component.translatable(SEPARATOR, line1, Component.translatable(langKey));
                            }
                            else {
                                line1 = Component.translatable(langKey);
                            }
                        }
                        else if (currentLine == 1) {
                            if (line2 != null) {
                                line2 = Component.translatable(SEPARATOR, line2, Component.translatable(langKey));
                            }
                            else {
                                line2 = Component.translatable(langKey);
                            }
                        }
                        else if  (currentLine == 2) {
                            if (line3 != null) {
                                line3 = Component.translatable(SEPARATOR, line3, Component.translatable(langKey));
                            }
                            else {
                                line3 = Component.translatable(langKey);
                            }
                        }

                        currentEntryIndex++;
                    }

                    renderScrollingString(minecraft, guiGraphics, line1, line2, line3);
                }
            }
        }
    }

    private static String transformStructureToLangKey(String resourceLocation) {
        return "structure." + resourceLocation.replace(":", ".");
    }

    public static void renderScrollingString(Minecraft minecraft, GuiGraphics guiGraphics, Component line1, Component line2, Component line3) {
        int linesToMake = 0;
        if (line1 != null) {
            linesToMake++;
        }
        if (line2 != null) {
            linesToMake++;
        }
        if (line3 != null) {
            linesToMake++;
        }

        guiGraphics.drawString(
                minecraft.font,
                Component.translatable(line2 == null ? WITHIN_TEXT : WITHIN_TEXT_PLURAL),
                4,
                guiGraphics.guiHeight() - 12 - (10 * linesToMake),
                -50,
                true
        );

        if (line1 != null) {
            renderScrollingString(minecraft, guiGraphics, line1, 0);
        }
        if (line2 != null) {
            renderScrollingString(minecraft, guiGraphics, line2, 20);
        }
        if (line3 != null) {
            renderScrollingString(minecraft, guiGraphics, line3, 40);
        }
    }

    public static void renderScrollingString(Minecraft minecraft, GuiGraphics guiGraphics, Component component, int yOffset) {
        renderScrollingString(
                guiGraphics,
                minecraft.font,
                component,
                4,
                guiGraphics.guiHeight() - 16 - yOffset,
                118,
                guiGraphics.guiHeight(),
                0xFFE090
        );
    }

    public static void renderScrollingString(
            GuiGraphics guiGraphics,
            Font font,
            Component component,
            int minX,
            int minY,
            int maxX,
            int maxY,
            int color)
    {
        int n = font.width(component);
        int o = (minY + maxY - font.lineHeight) / 2 + 1;
        int p = maxX - minX;
        if (n > p) {
            int q = n - p;
            double d = (double) Util.getMillis() / 1000.0;
            double e = Math.max((double)q * 0.5, 3.0);
            double f = Math.sin(1.5707963267948966 * Math.cos(Math.PI * 2 * d / e)) / 2.0 + 0.5;
            double g = Mth.lerp(f, 0.0, q);
            guiGraphics.enableScissor(minX, minY, maxX, maxY);
            guiGraphics.drawString(font, component, minX - (int)g, o, color, true);
            guiGraphics.disableScissor();
        }
        else {
            guiGraphics.drawString(font, component, minX, o, color);
        }
    }
}
