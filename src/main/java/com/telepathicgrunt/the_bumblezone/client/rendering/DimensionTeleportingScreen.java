package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public class DimensionTeleportingScreen {
    private static final Component DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT = Component.translatable("system.the_bumblezone.entering_dimension");
    private static final ResourceLocation BZ_BACKGROUND_LOCATION = new ResourceLocation(Bumblezone.MODID, "textures/gui/dimension_teleporting_background.png");

    public static void renderScreenAndText(ReceivingLevelScreen screen, PoseStack poseStack) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, BZ_BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(0.0D, screen.height, 0.0D).uv(0.0F, (float)screen.height / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(screen.width, screen.height, 0.0D).uv((float)screen.width / 32.0F, (float)screen.height / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(screen.width, 0.0D, 0.0D).uv((float)screen.width / 32.0F, 0.0f).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, 0.0D).uv(0.0F, 0.0f).color(64, 64, 64, 255).endVertex();
        tesselator.end();
        MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(screen, new PoseStack()));

        GuiComponent.drawCenteredString(poseStack, screen.getMinecraft().font, DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT, screen.width / 2 - 1, screen.height / 2 - 9, 0);
        GuiComponent.drawCenteredString(poseStack, screen.getMinecraft().font, DOWNLOADING_BUMBLEZONE_TERRAIN_TEXT, screen.width / 2, screen.height / 2 - 10, 16773085);
    }
}
