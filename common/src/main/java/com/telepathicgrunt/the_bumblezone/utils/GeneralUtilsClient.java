package com.telepathicgrunt.the_bumblezone.utils;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GeneralUtilsClient {

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static boolean isSimilarInColor(Color color1, Color color2, int threshold) {
        return (Math.abs(color1.getRed() - color2.getRed()) +
                Math.abs(color1.getGreen() - color2.getGreen()) +
                Math.abs(color1.getBlue() - color2.getBlue())) < threshold;
    }

    public static boolean isSimilarInVisualColor(Color color1, Color color2, int hueThreshold, int valueThreshold) {
        double[] hue1 = ColorToHsv(color1);
        double[] hue2 = ColorToHsv(color2);

        double hueDiff = hue1[0] - hue2[0];
        if (hueDiff > 180) {
            hueDiff -= 360;
        }
        else if (hueDiff < -180) {
            hueDiff += 360;
        }
        double hueDistance = Math.sqrt(hueDiff * hueDiff);

        double valueDiff = Math.abs(hue1[2] - hue2[2]);

        return hueDistance < hueThreshold && valueDiff < valueThreshold;
    }

    // Source: http://www.java2s.com/example/csharp/system.drawing/calculate-the-difference-in-hue-between-two-s.html
    public static double[] ColorToHsv(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        double h = 0, s, v;
        double min = Math.min(Math.min(r, g), b);
        v = Math.max(Math.max(r, g), b);
        double delta = v - min;

        if (v == 0.0) {
            s = 0;
        }
        else {
            s = delta / v;
        }

        if (s == 0) {
            h = 0.0;
        }
        else {
            if (r == v) {
                h = (g - b) / delta;
            }
            else if (g == v) {
                h = 2 + (b - r) / delta;
            }
            else if (b == v) {
                h = 4 + (r - g) / delta;
            }

            h *= 60;
            if (h < 0.0) {
                h = h + 360;
            }
        }

        var hsv = new double[3];
        hsv[0] = h; // 0 to 360
        hsv[1] = s * 360; // 0 to 360
        hsv[2] = v; // 0 to 360
        return hsv;
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
