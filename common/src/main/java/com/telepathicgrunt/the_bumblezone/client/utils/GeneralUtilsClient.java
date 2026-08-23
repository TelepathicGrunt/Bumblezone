package com.telepathicgrunt.the_bumblezone.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class GeneralUtilsClient {

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static ClientLevel getClientLevel() {
        return Minecraft.getInstance().level;
    }

    public static RegistryAccess getClientRegistryAccess() {
        return Minecraft.getInstance().getConnection().registryAccess();
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static List<MutableComponent> autoWrappedTooltip(int lengthText, String wrappingText) {
        List<MutableComponent> list = new ArrayList<>();
        recursiveWrap(lengthText, wrappingText, list);
        return list;
    }

    private static void recursiveWrap(int lengthText, String wrappingText, List<MutableComponent> list) {
        if (lengthText > 10) {
            String translatedWrapString = Language.getInstance().getOrDefault(wrappingText);
            if (translatedWrapString.length() > lengthText) {
                list.add(Component.literal(translatedWrapString.substring(0, lengthText)));
                recursiveWrap(lengthText, translatedWrapString.substring(lengthText), list);
            }
            else {
                list.add(Component.translatable(wrappingText));
            }
        }
        else {
            list.add(Component.translatable(wrappingText));
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public static void renderScrollingString(
            GuiGraphicsExtractor guiGraphics,
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
            guiGraphics.text(font, component, minX - (int)g, o, color, true);
            guiGraphics.disableScissor();
        }
        else {
            guiGraphics.text(font, component, minX, o, color);
        }
    }

    ///////////////////////////////////////

    public static boolean isAdvancedToolTipActive() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null && minecraft.options.advancedItemTooltips;
    }

    ///////////////////////////////////////

    private static final BiFunction<Identifier, Boolean, RenderType> CRYSTAL_RENDER_TYPE = Util.memoize((texture, affectsOutline) -> {
        var state = RenderSetup.builder(RenderPipelines.OPAQUE_PARTICLE)
                .withTexture("Sampler0", texture)
                .useLightmap()
                .useOverlay()
                .affectsCrumbling()
                .sortOnUpload()
                .setOutline(affectsOutline ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE)
                .createRenderSetup();

        return RenderType.create("bz$entity_crystal_translucent", state);
    });

    public static RenderType renderTypeCrystal(Identifier texture, boolean affectsOutline) {
        return CRYSTAL_RENDER_TYPE.apply(texture, affectsOutline);
    }
}
