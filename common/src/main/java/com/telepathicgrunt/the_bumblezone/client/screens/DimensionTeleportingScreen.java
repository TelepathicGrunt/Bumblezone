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
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BZ_BACKGROUND_LOCATION,
                0,
                0,
                0,
                0,
                screen.width,
                screen.height,
                screen.width,
                screen.height
        );
        guiGraphics.centeredText(Minecraft.getInstance().font, DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT, screen.width / 2 + 1, screen.height / 2 - 9, 0);
        guiGraphics.centeredText(Minecraft.getInstance().font, DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT, screen.width / 2, screen.height / 2 - 10, 16774120);
    }
}
