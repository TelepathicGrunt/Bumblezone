package com.telepathicgrunt.bumblezone.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.shape.VoxelShape;


// CLIENT-SIDED
public class PileOfPollenRenderer {

    private static final Identifier TEXTURE_POLLEN = new Identifier(Bumblezone.MODID + ":textures/block/pile_of_pollen.png");

    public static boolean pileOfPollenOverlay(PlayerEntity playerEntity, MatrixStack matrixStack, BlockState blockState)
    {
        if (blockState != null && blockState.isOf(BzBlocks.PILE_OF_POLLEN))
        {
            boolean isInPollen = false;
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    for(int y = -1; y <= 1; y++) {
                        // Squared to make it positive always for the addition
                        // Skips all non corner spots
                        if((x*x) + (y*y) + (z*z) <= 2) continue;

                        double eyePosX = playerEntity.getX() + x * playerEntity.getWidth() * 0.155F;
                        double eyePosY = playerEntity.getEyeY() + y * 0.12F;
                        double eyePosZ = playerEntity.getZ() + z * playerEntity.getWidth() * 0.155F;
                        Vec3d eyePosition = new Vec3d(eyePosX, eyePosY, eyePosZ);
                        BlockPos eyeBlockPos = new BlockPos(eyePosition);
                        BlockState eyeBlock = playerEntity.world.getBlockState(eyeBlockPos);
                        VoxelShape blockBounds = eyeBlock.getOutlineShape(playerEntity.world, eyeBlockPos);
                        if (!blockBounds.isEmpty() && blockBounds.getBoundingBox().contains(eyePosition.subtract(Vec3d.of(eyeBlockPos)))) {
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
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float opacity = 1f;
            float brightness = 0.3f;
            float redStrength = 1f;
            float greenStrength = 0.9f;
            float blueStrength = 0.8f;
            RenderSystem.setShaderColor(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity);

            float pitch = -playerEntity.getYaw() / 64.0F;
            float yaw = playerEntity.getPitch() / 64.0F;
            float yawPlus4 = 4.0F + yaw;
            float pitchPlus4 = 4.0F + pitch;

            float movementScaling = 0.85f;
            Vec3f playerPosition = new Vec3f(playerEntity.getPos().multiply(movementScaling, movementScaling, movementScaling));
            float smallXZOffset = playerPosition.getX() * playerPosition.getZ() * 0;
            float smallYOffset = playerPosition.getY() * 0.33f;

            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(pitchPlus4 - smallXZOffset, yawPlus4 - playerPosition.getY()).next();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(pitch - smallXZOffset, yawPlus4 - playerPosition.getY()).next();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(pitch - smallXZOffset, yaw - playerPosition.getY()).next();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(pitchPlus4 - smallXZOffset, yaw - playerPosition.getY()).next();
            bufferbuilder.end();
            BufferRenderer.draw(bufferbuilder);
            RenderSystem.setShaderColor(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f);
            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(pitchPlus4 - playerPosition.getX(), yawPlus4 - smallYOffset).next();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(pitch - playerPosition.getX(), yawPlus4 - smallYOffset).next();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(pitch - playerPosition.getX(), yaw - smallYOffset).next();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(pitchPlus4 - playerPosition.getX(), yaw - smallYOffset).next();
            bufferbuilder.end();
            BufferRenderer.draw(bufferbuilder);
            RenderSystem.setShaderColor(brightness * redStrength, brightness * greenStrength, brightness * blueStrength, opacity * 0.33f);
            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(pitchPlus4 - playerPosition.getZ(), yawPlus4 - smallYOffset).next();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(pitch - playerPosition.getZ(), yawPlus4 - smallYOffset).next();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(pitch - playerPosition.getZ(), yaw - smallYOffset).next();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(pitchPlus4 - playerPosition.getZ(), yaw - smallYOffset).next();
            bufferbuilder.end();
            BufferRenderer.draw(bufferbuilder);
            RenderSystem.disableBlend();
            return true;
        }
        return false;
    }
}
