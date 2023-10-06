package com.telepathicgrunt.the_bumblezone.client.utils;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class GeneralUtilsClient {

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static List<MutableComponent> autoWrappedTooltip(String lengthText, String wrappingText) {
        List<MutableComponent> list = new ArrayList<>();

        String translatedLengthString = Language.getInstance().getOrDefault(lengthText);

        if (translatedLengthString.length() > 10) {
            String translatedWrapString = Language.getInstance().getOrDefault(wrappingText);
            if (translatedWrapString.length() > translatedLengthString.length()) {
                list.add(Component.literal(translatedWrapString.substring(0, translatedLengthString.length())));
                list.add(Component.literal(translatedWrapString.substring(translatedLengthString.length())));
            }
            else {
                list.add(Component.translatable(wrappingText));
            }
        }
        else {
            list.add(Component.translatable(wrappingText));
        }

        return list;
    }

    /////////////////////////////////////////////////////////////////////////////////////////

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
