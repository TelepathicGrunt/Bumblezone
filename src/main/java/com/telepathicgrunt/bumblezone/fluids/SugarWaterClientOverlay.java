package com.telepathicgrunt.bumblezone.fluids;

import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.biome.Biome;

public class SugarWaterClientOverlay {
    private static final Identifier TEXTURE_UNDERWATER = new Identifier(Bumblezone.MODID, "textures/misc/sugar_water_underwater.png");

    public static boolean sugarWaterOverlay(PlayerEntity player, BlockPos pos, MatrixStack matrixStack) {

        if (player instanceof ClientPlayerEntity clientPlayerEntity && player.world.getBlockState(new BlockPos(player.getCameraPosVec(1))).getBlock() == BzBlocks.SUGAR_WATER_BLOCK) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, TEXTURE_UNDERWATER);
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            float brightnessAtEyes = clientPlayerEntity.getBrightnessAtEyes();
            float textureAlpha = 0.42F;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(brightnessAtEyes, brightnessAtEyes, brightnessAtEyes, textureAlpha);
            float modifiedYaw = -clientPlayerEntity.getYaw() / 64.0F;
            float modifiedPitch = clientPlayerEntity.getPitch() / 64.0F;
            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(brightnessAtEyes, brightnessAtEyes, brightnessAtEyes, textureAlpha).texture(4.0F + modifiedYaw, 4.0F + modifiedPitch).next();
            bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(brightnessAtEyes, brightnessAtEyes, brightnessAtEyes, textureAlpha).texture(0.0F + modifiedYaw, 4.0F + modifiedPitch).next();
            bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(brightnessAtEyes, brightnessAtEyes, brightnessAtEyes, textureAlpha).texture(0.0F + modifiedYaw, 0.0F + modifiedPitch).next();
            bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(brightnessAtEyes, brightnessAtEyes, brightnessAtEyes, textureAlpha).texture(4.0F + modifiedYaw, 0.0F + modifiedPitch).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.disableBlend();
            return true;
        }

        return false;
    }
}
