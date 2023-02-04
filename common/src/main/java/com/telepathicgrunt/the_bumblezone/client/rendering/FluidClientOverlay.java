package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class FluidClientOverlay {
    private static final ResourceLocation HONEY_TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/honey_fluid_underwater.png");
    private static final ResourceLocation ROYAL_JELLY_TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/royal_jelly_fluid_underwater.png");

    public static boolean renderHoneyOverlay(Player clientPlayerEntity, PoseStack matrixStack) {
        BlockState state = clientPlayerEntity.level.getBlockState(new BlockPos(clientPlayerEntity.getEyePosition(1)));
        if (state.is(BzFluids.HONEY_FLUID_BLOCK.get()) || state.is(BzFluids.ROYAL_JELLY_FLUID_BLOCK.get())) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, state.is(BzFluids.HONEY_FLUID_BLOCK.get()) ? HONEY_TEXTURE_UNDERWATER : ROYAL_JELLY_TEXTURE_UNDERWATER);
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
            float brightness = (float) Math.max(
                    Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(clientPlayerEntity), 2D),
                    clientPlayerEntity.level.dimensionType().ambientLight()
            );
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
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
            RenderSystem.disableBlend();
            return true;
        }

        return false;
    }

    public static float getDimensionBrightnessAtEyes(Entity entity) {
        float lightLevelAtEyes = entity.level.getRawBrightness(new BlockPos(entity.getEyePosition(1)), 0);
        return lightLevelAtEyes / 15f;
    }
}
