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
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.events.client.BlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Map;

public class EssenceOverlay {
    private static final ResourceLocation TEXTURE_OVERLAY_1 = new ResourceLocation(Bumblezone.MODID, "textures/rainbow_overlay.png");

    public static boolean nearEssenceOverlay(Player player, GuiGraphics guiGraphics) {
        if (player.level().dimension().equals(Level.OVERWORLD)) {
            // Find distance to essence block
            BlockPos playerPos = player.blockPosition();
            Level level = player.level();
            ChunkPos currentChunkPos = new ChunkPos(playerPos);
            int distanceToEssenceBlock = Integer.MAX_VALUE;
            Block essenceBlock = null;
            int diameter = 2;
            for (int chunkX = 0; chunkX <= diameter; chunkX++) {
                for (int chunkZ = 0; chunkZ <= diameter; chunkZ++) {
                    LevelChunk levelChunk = level.getChunk(
                        currentChunkPos.x + chunkX - (chunkX == diameter ? -3 : 0),
                        currentChunkPos.z + chunkZ - (chunkZ == diameter ? -3 : 0)
                    );

                    Map<BlockPos, BlockEntity> blockEntities = levelChunk.getBlockEntities();
                    for (Map.Entry<BlockPos, BlockEntity> blockPosBlockEntityEntry : blockEntities.entrySet()) {
                        if (blockPosBlockEntityEntry.getValue() instanceof EssenceBlockEntity) {
                            int distance = playerPos.distManhattan(blockPosBlockEntityEntry.getKey());
                            if (distance < distanceToEssenceBlock) {
                                distanceToEssenceBlock = distance;
                                essenceBlock = blockPosBlockEntityEntry.getValue().getBlockState().getBlock();
                            }
                        }
                    }
                }
            }

            // Check if too far, if so exit early
            if (distanceToEssenceBlock > 16) {
                return false;
            }

            // Draw using distance as part of alpha
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            RenderSystem.setShaderTexture(0, TEXTURE_OVERLAY_1);
            RenderSystem.enableBlend();


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

            Color color = new Color(essenceBlock.defaultMapColor().col);
            float red = color.getRed() / 256f;
            float green = color.getGreen() / 256f;
            float blue = color.getBlue() / 256f;

            for(int r = 0; r < 6; ++r) {
                int altR = ((r % 2) * 2) - 1;
                float scaledSizeR = (r * rSizeScale);
                float scaledSpinR = altR * ((r + rSpinStartSpeed) * rSpinScale);

                guiGraphics.pose().pushPose();
                guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((scaledSpinR * ((Util.getMillis() * 10101) % 1000000000000000000L / 100000.0F))));
                Matrix4f matrix4f = guiGraphics.pose().last().pose();
                bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
                bufferBuilder.vertex(matrix4f, -scale - scaledSizeR, -scale - scaledSizeR, -0.5F).color(red, green, blue, alpha).uv(lerp2, lerp4).endVertex();
                bufferBuilder.vertex(matrix4f, scale + scaledSizeR, -scale - scaledSizeR, -0.5F).color(red, green, blue, alpha).uv(lerp1, lerp4).endVertex();
                bufferBuilder.vertex(matrix4f, scale + scaledSizeR, scale + scaledSizeR, -0.5F).color(red, green, blue, alpha).uv(lerp1, lerp3).endVertex();
                bufferBuilder.vertex(matrix4f, -scale - scaledSizeR, scale + scaledSizeR, -0.5F).color(red, green, blue, alpha).uv(lerp2, lerp3).endVertex();
                BufferUploader.drawWithShader(bufferBuilder.end());
                guiGraphics.pose().popPose();
            }

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.disableBlend();

            return false;
        }

        return false;
    }
}
