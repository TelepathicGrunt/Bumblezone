package com.telepathicgrunt.the_bumblezone.client.rendering.essence;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;

public class EssenceOverlay {
    private static final ResourceLocation TEXTURE_OVERLAY_1 = new ResourceLocation(Bumblezone.MODID, "textures/rainbow_overlay.png");

    // Currently unused but may utilize later
    public static boolean portalOverlay(Player player, PoseStack matrixStack) {

        if (player.level().getBlockState(BlockPos.containing(player.getEyePosition(1))).getBlock() == BzBlocks.ESSENCE_BLOCK_WHITE.get()) {

            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            RenderSystem.depthFunc(519);
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderTexture(0, TEXTURE_OVERLAY_1);


            float minU = -0f;
            float maxU = 1f;
            float midU = (minU + maxU) / 2.0F;

            float minV = -0f;
            float maxV = 1f;
            float midV = (minV + maxV) / 2.0F;

            float lerpAmount = 0;
            float lerp1 = Mth.lerp(lerpAmount, minU, midU);
            float lerp2 = Mth.lerp(lerpAmount, maxU, midU);
            float lerp3 = Mth.lerp(lerpAmount, minV, midV);
            float lerp4 = Mth.lerp(lerpAmount, maxV, midV);

            float scale = 0.68f;
            float rSizeScale = 0.1f;
            float rSpinScale = 0.03f;
            float rSpinStartSpeed = 1f;
            float alpha = 0.37f;

            for(int r = 0; r < 6; ++r) {
                int altR = ((r % 2) * 2) - 1;
                float scaledSizeR = (r * rSizeScale);
                float scaledSpinR = altR * ((r + rSpinStartSpeed) * rSpinScale);

                matrixStack.pushPose();
                matrixStack.mulPose(Axis.ZP.rotationDegrees((scaledSpinR * ((Util.getMillis() * 10101) % 1000000000000000000L / 100000.0F))));
                Matrix4f matrix4f = matrixStack.last().pose();
                bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
                bufferBuilder.vertex(matrix4f, -scale - scaledSizeR, -scale - scaledSizeR, -0.5F).color(1.0F, 1.0F, 1.0F, alpha).uv(lerp2, lerp4).endVertex();
                bufferBuilder.vertex(matrix4f, scale + scaledSizeR, -scale - scaledSizeR, -0.5F).color(1.0F, 1.0F, 1.0F, alpha).uv(lerp1, lerp4).endVertex();
                bufferBuilder.vertex(matrix4f, scale + scaledSizeR, scale + scaledSizeR, -0.5F).color(1.0F, 1.0F, 1.0F, alpha).uv(lerp1, lerp3).endVertex();
                bufferBuilder.vertex(matrix4f, -scale - scaledSizeR, scale + scaledSizeR, -0.5F).color(1.0F, 1.0F, 1.0F, alpha).uv(lerp2, lerp3).endVertex();
                BufferUploader.drawWithShader(bufferBuilder.end());
                matrixStack.popPose();
            }

            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.depthFunc(515);

            return true;
        }

        return false;
    }
}
