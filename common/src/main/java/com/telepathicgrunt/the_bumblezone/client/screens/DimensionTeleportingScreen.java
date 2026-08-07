package com.telepathicgrunt.the_bumblezone.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class DimensionTeleportingScreen {
    private static final Component DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT = Component.translatable("system.the_bumblezone.entering_dimension");
    private static final Identifier BZ_BACKGROUND_LOCATION = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/gui/screens/dimension_teleporting_background.png");

    public static void renderScreenAndText(LevelLoadingScreen screen, GuiGraphicsExtractor guiGraphics) {
        int textureSize = 32;
        int xIterations = (int) Math.ceil(screen.width / (float)textureSize);
        int yIterations = (int) Math.ceil(screen.height / (float)textureSize);
        for (int x = -1; x <= xIterations; x++) {
            for (int y = -1; y <= yIterations; y++) {
                guiGraphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        BZ_BACKGROUND_LOCATION,
                        textureSize * x,
                        textureSize * y,
                        0.0F,
                        0.0F,
                        textureSize,
                        textureSize,
                        textureSize,
                        textureSize);
            }
        }

        int xCenter = screen.width / 2;
        int yCenter = screen.height / 2;
        guiGraphics.centeredText(Minecraft.getInstance().font, DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT, xCenter, yCenter - 9, 0);
        guiGraphics.centeredText(Minecraft.getInstance().font, DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT, xCenter, yCenter - 10, -3096);
    }
}
