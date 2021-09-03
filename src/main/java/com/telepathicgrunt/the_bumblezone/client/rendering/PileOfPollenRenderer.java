package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;


// CLIENT-SIDED
public class PileOfPollenRenderer {

    private static final ResourceLocation TEXTURE_POLLEN = new ResourceLocation(Bumblezone.MODID + ":textures/block/pile_of_pollen.png");

    public static void pileOfPollenOverlay(RenderBlockOverlayEvent event)
    {
        BlockPos blockPos = event.getBlockPos();
        PlayerEntity playerEntity = event.getPlayer();
        BlockState state = playerEntity.level.getBlockState(blockPos);
        if (state.is(BzBlocks.PILE_OF_POLLEN.get()))
        {
            boolean isInPollen = false;
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    for(int y = -1; y <= 1; y++) {
                        // Squared to make it positive always for the addition
                        // Skips all non corner spots
                        if((x*x) + (y*y) + (z*z) <= 2) continue;

                        double eyePosX = playerEntity.getX() + x * playerEntity.getBbWidth() * 0.155F;
                        double eyePosY = playerEntity.getEyeY() + y * 0.1F;
                        double eyePosZ = playerEntity.getZ() + z * playerEntity.getBbWidth() * 0.155F;
                        Vector3d eyePosition = new Vector3d(eyePosX, eyePosY, eyePosZ);
                        BlockPos eyeBlockPos = new BlockPos(eyePosition);
                        BlockState eyeBlock = playerEntity.level.getBlockState(eyeBlockPos);
                        VoxelShape blockBounds = eyeBlock.getShape(playerEntity.level, eyeBlockPos);
                        if (!blockBounds.isEmpty() && blockBounds.bounds().contains(eyePosition.subtract(Vector3d.atLowerCornerOf(eyeBlockPos)))) {
                            isInPollen = true;
                            x = 2;
                            z = 2;
                            break;
                        }
                    }
                }
            }

            if(!isInPollen) {
                event.setCanceled(true);
                return;
            }
            
            Minecraft minecraftIn = Minecraft.getInstance();
            minecraftIn.getTextureManager().bind(TEXTURE_POLLEN);
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float opacity = 1f;
            float brightness = 0.3f;
            float redStrength = 1f;
            float greenStrength = 0.9f;
            float blueStrength = 0.8f;

            float pitch = -playerEntity.yRot / 64.0F;
            float yaw = playerEntity.xRot / 64.0F;
            float yawPlus4 = 4.0F + yaw;
            float pitchPlus4 = 4.0F + pitch;

            float movementScaling = 0.85f;
            Vector3f playerPosition = new Vector3f(playerEntity.position().multiply(movementScaling, movementScaling, movementScaling));
            float smallXZOffset = playerPosition.x() * playerPosition.z() * 0;
            float smallYOffset = playerPosition.y() * 0.33f;

            Matrix4f matrix4f = event.getMatrixStack().last().pose();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity).uv(pitchPlus4 - smallXZOffset, yawPlus4 - playerPosition.y()).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity).uv(pitch - smallXZOffset, yawPlus4 - playerPosition.y()).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity).uv(pitch - smallXZOffset, yaw - playerPosition.y()).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity).uv(pitchPlus4 - smallXZOffset, yaw - playerPosition.y()).endVertex();
            bufferbuilder.end();
            WorldVertexBufferUploader.end(bufferbuilder);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f).uv(pitchPlus4 - playerPosition.x(), yawPlus4 - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f).uv(pitch - playerPosition.x(), yawPlus4 - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f).uv(pitch - playerPosition.x(), yaw - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f).uv(pitchPlus4 - playerPosition.x(), yaw - smallYOffset).endVertex();
            bufferbuilder.end();
            WorldVertexBufferUploader.end(bufferbuilder);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f).uv(pitchPlus4 - playerPosition.z(), yawPlus4 - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f).uv(pitch - playerPosition.z(), yawPlus4 - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f).uv(pitch - playerPosition.z(), yaw - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f).uv(pitchPlus4 - playerPosition.z(), yaw - smallYOffset).endVertex();
            bufferbuilder.end();
            WorldVertexBufferUploader.end(bufferbuilder);
            RenderSystem.disableBlend();
            event.setCanceled(true);
        }
    }
}
