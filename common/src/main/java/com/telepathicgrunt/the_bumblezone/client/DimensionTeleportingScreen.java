package com.telepathicgrunt.the_bumblezone.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DimensionTeleportingScreen {
    private static final Component DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT = Component.translatable("system.the_bumblezone.entering_dimension");
    private static final ResourceLocation BZ_BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "textures/gui/dimension_teleporting_background.png");

    public static void renderScreenAndText(ReceivingLevelScreen screen, GuiGraphics guiGraphics) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, BZ_BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bufferbuilder.addVertex(0.0F, screen.height, 0.0F).setUv(0.0F, (float)screen.height / 32.0F).setColor(64, 64, 64, 255);
        bufferbuilder.addVertex(screen.width, screen.height, 0.0F).setUv((float)screen.width / 32.0F, (float)screen.height / 32.0F).setColor(64, 64, 64, 255);
        bufferbuilder.addVertex(screen.width, 0.0F, 0.0F).setUv((float)screen.width / 32.0F, 0.0f).setColor(64, 64, 64, 255);
        bufferbuilder.addVertex(0.0F, 0.0F, 0.0F).setUv(0.0F, 0.0f).setColor(64, 64, 64, 255);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT, screen.width / 2 + 1, screen.height / 2 - 9, 0);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT, screen.width / 2, screen.height / 2 - 10, 16774120);
    }
}
