package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class FluidClientOverlay {
    private static final ResourceLocation HONEY_TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/honey_fluid_underwater.png");
    private static final ResourceLocation ROYAL_JELLY_TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/royal_jelly_fluid_underwater.png");

    public static boolean renderHoneyOverlay(Player clientPlayerEntity, PoseStack matrixStack) {
        BlockState state = clientPlayerEntity.level().getBlockState(BlockPos.containing(clientPlayerEntity.getEyePosition(1)));
        if (state.is(BzFluids.HONEY_FLUID_BLOCK.get()) || state.is(BzFluids.ROYAL_JELLY_FLUID_BLOCK.get())) {
            if (state.hasProperty(HoneyFluidBlock.BOTTOM_LEVEL)) {
                double yOffset = clientPlayerEntity.getEyePosition(1).y() - ((int)clientPlayerEntity.getEyePosition(1).y());
                if (state.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8D > yOffset + 0.1) {
                    return false;
                }
            }

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, state.is(BzFluids.HONEY_FLUID_BLOCK.get()) ? HONEY_TEXTURE_UNDERWATER : ROYAL_JELLY_TEXTURE_UNDERWATER);
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
            float brightness = (float) Math.max(
                    Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(clientPlayerEntity), 2D),
                    clientPlayerEntity.level().dimensionType().ambientLight()
            );
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float[] olderShaderColor = RenderSystem.getShaderColor().clone();
            RenderSystem.setShaderColor(brightness, brightness, brightness, 0.95F);
            float modifiedYaw = -clientPlayerEntity.getYRot() / (64.0F * 8F);
            float modifiedPitch = clientPlayerEntity.getXRot() / (64.0F * 8F);
            Matrix4f matrix4f = matrixStack.last().pose();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).uv(1.0F + modifiedYaw, 1.0F + modifiedPitch).endVertex();
            bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).uv(0.0F + modifiedYaw, 2.0F + modifiedPitch).endVertex();
            bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).uv(1.0F + modifiedYaw, 1.0F + modifiedPitch).endVertex();
            bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).uv(2.0F + modifiedYaw, 0.0F + modifiedPitch).endVertex();
            BufferUploader.drawWithShader(bufferBuilder.end());
            RenderSystem.setShaderColor(olderShaderColor[0], olderShaderColor[1], olderShaderColor[2], olderShaderColor[3]);
            RenderSystem.disableBlend();
            return true;
        }

        return false;
    }

    public static float getDimensionBrightnessAtEyes(Entity entity) {
        Level level = entity.level();
        float lightLevelAtEyes;

        if (level.dimensionType().hasSkyLight()) {
            lightLevelAtEyes = level.getRawBrightness(BlockPos.containing(entity.getEyePosition(1)), level.getSkyDarken());
            level.updateSkyBrightness();
            if (level.isNight()) {
                float moonBrightness = level.getMoonBrightness();
                lightLevelAtEyes *= moonBrightness;
            }
        }
        else {
            lightLevelAtEyes = level.getRawBrightness(BlockPos.containing(entity.getEyePosition(1)), 0);
        }

        return lightLevelAtEyes / 15f;
    }
}
