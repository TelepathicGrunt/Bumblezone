package com.telepathicgrunt.the_bumblezone.client.rendering.pileofpollen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;


// CLIENT-SIDED
public class PileOfPollenRenderer {

    private static final ResourceLocation TEXTURE_POLLEN = new ResourceLocation(Bumblezone.MODID + ":textures/block/pile_of_pollen.png");

    public static boolean pileOfPollenOverlay(Player playerEntity, PoseStack matrixStack, BlockState blockState)
    {
        if (blockState != null && blockState.is(BzBlocks.PILE_OF_POLLEN))
        {
            boolean isInPollen = false;
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    for(int y = -1; y <= 1; y++) {
                        // Squared to make it positive always for the addition
                        // Skips all non corner spots
                        if((x*x) + (y*y) + (z*z) <= 2) continue;

                        double eyePosX = playerEntity.getX() + x * playerEntity.getBbWidth() * 0.155F;
                        double eyePosY = playerEntity.getEyeY() + y * 0.12F;
                        double eyePosZ = playerEntity.getZ() + z * playerEntity.getBbWidth() * 0.155F;
                        Vec3 eyePosition = new Vec3(eyePosX, eyePosY, eyePosZ);
                        BlockPos eyeBlockPos = new BlockPos(eyePosition);
                        BlockState eyeBlock = playerEntity.level.getBlockState(eyeBlockPos);
                        VoxelShape blockBounds = eyeBlock.getShape(playerEntity.level, eyeBlockPos);
                        if (!blockBounds.isEmpty() && blockBounds.bounds().contains(eyePosition.subtract(Vec3.atLowerCornerOf(eyeBlockPos)))) {
                            isInPollen = true;
                            x = 2;
                            z = 2;
                            break;
                        }
                    }
                }
            }

            if(!isInPollen) {
                return true;
            }

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, TEXTURE_POLLEN);
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float opacity = 1f;
            float brightness = 0.3f;
            float redStrength = 1f;
            float greenStrength = 0.9f;
            float blueStrength = 0.8f;
            RenderSystem.setShaderColor(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity);

            float pitch = -playerEntity.getYRot() / 64.0F;
            float yaw = playerEntity.getXRot() / 64.0F;
            float yawPlus4 = 4.0F + yaw;
            float pitchPlus4 = 4.0F + pitch;

            float movementScaling = 0.85f;
            Vector3f playerPosition = new Vector3f(playerEntity.position().multiply(movementScaling, movementScaling, movementScaling));
            float smallXZOffset = playerPosition.x() * playerPosition.z() * 0;
            float smallYOffset = playerPosition.y() * 0.33f;

            Matrix4f matrix4f = matrixStack.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).uv(pitchPlus4 - smallXZOffset, yawPlus4 - playerPosition.y()).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).uv(pitch - smallXZOffset, yawPlus4 - playerPosition.y()).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).uv(pitch - smallXZOffset, yaw - playerPosition.y()).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).uv(pitchPlus4 - smallXZOffset, yaw - playerPosition.y()).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
            RenderSystem.setShaderColor(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).uv(pitchPlus4 - playerPosition.x(), yawPlus4 - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).uv(pitch - playerPosition.x(), yawPlus4 - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).uv(pitch - playerPosition.x(), yaw - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).uv(pitchPlus4 - playerPosition.x(), yaw - smallYOffset).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
            RenderSystem.setShaderColor(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).uv(pitchPlus4 - playerPosition.z(), yawPlus4 - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).uv(pitch - playerPosition.z(), yawPlus4 - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).uv(pitch - playerPosition.z(), yaw - smallYOffset).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).uv(pitchPlus4 - playerPosition.z(), yaw - smallYOffset).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
            RenderSystem.disableBlend();
            return true;
        }
        return false;
    }
}
