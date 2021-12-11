package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;

public class FluidClientOverlay {
    private static final ResourceLocation TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID, "textures/misc/sugar_water_underwater.png");
    private static final ResourceLocation HONEY_TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/honey_fluid_underwater.png");

    public static void sugarWaterFluidOverlay(RenderBlockOverlayEvent event) {
        Player player = event.getPlayer();
        PoseStack matrixStack = event.getPoseStack();
        if(!(player instanceof LocalPlayer clientPlayerEntity)) {
            return;
        }
        BlockState state = player.level.getBlockState(new BlockPos(player.getEyePosition(0)));
        if (state.is(BzFluids.SUGAR_WATER_BLOCK.get())) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, TEXTURE_UNDERWATER);
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            float brightnessAtEyes = clientPlayerEntity.getBrightness();
            float textureAlpha = 0.42F;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(brightnessAtEyes, brightnessAtEyes, brightnessAtEyes, textureAlpha);
            float modifiedYaw = -clientPlayerEntity.getYRot() / 64.0F;
            float modifiedPitch = clientPlayerEntity.getXRot() / 64.0F;
            Matrix4f matrix4f = matrixStack.last().pose();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).uv(4.0F + modifiedYaw, 4.0F + modifiedPitch).endVertex();
            bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).uv(0.0F + modifiedYaw, 4.0F + modifiedPitch).endVertex();
            bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).uv(0.0F + modifiedYaw, 0.0F + modifiedPitch).endVertex();
            bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).uv(4.0F + modifiedYaw, 0.0F + modifiedPitch).endVertex();
            bufferBuilder.end();
            BufferUploader.end(bufferBuilder);
            RenderSystem.disableBlend();
        }
    }

    public static boolean renderHoneyOverlay(LocalPlayer clientPlayerEntity, PoseStack matrixStack) {
        if(!clientPlayerEntity.isEyeInFluid(BzFluidTags.BZ_HONEY_FLUID)) {
            return false;
        }

        BlockState state = clientPlayerEntity.level.getBlockState(new BlockPos(clientPlayerEntity.getEyePosition(1)));
        if (state.is(BzFluids.HONEY_FLUID_BLOCK.get())) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, HONEY_TEXTURE_UNDERWATER);
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
            float brightness = (float) Math.max(
                    Math.pow(FluidClientOverlay.getDimensionBrightnessAtEyes(clientPlayerEntity), 2D),
                    clientPlayerEntity.level.dimensionType().brightness(0)
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
            bufferBuilder.end();
            BufferUploader.end(bufferBuilder);
            RenderSystem.disableBlend();
            return true;
        }

        return false;
    }

    public static void renderHoneyFog(EntityViewRenderEvent.RenderFogEvent event) {
        FluidState fluidstate = getNearbyHoneyFluid(event.getCamera());
        if(fluidstate.is(BzFluidTags.BZ_HONEY_FLUID)) {
            RenderSystem.setShaderFogStart(0.35f);
            RenderSystem.setShaderFogEnd(4);
        }
    }

    public static float getDimensionBrightnessAtEyes(Entity entity) {
        float lightLevelAtEyes = entity.level.getRawBrightness(new BlockPos(entity.getEyePosition(1)), 0);
        return lightLevelAtEyes / 15f;
    }

    public static FluidState getNearbyHoneyFluid(Camera camera) {
        Entity entity = camera.getEntity();
        Level world = entity.level;
        FluidState fluidstate = world.getFluidState(camera.getBlockPosition());

        Vec3 currentPos = camera.getPosition();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        double offsetDistanceCheck = 0.075D;

        for(Direction direction : Direction.values()) {
            double x = currentPos.x() + direction.getStepX() * offsetDistanceCheck;
            double y = currentPos.y() + direction.getStepY() * offsetDistanceCheck;
            double z = currentPos.z() + direction.getStepZ() * offsetDistanceCheck;
            mutable.set(x, y, z);
            if(!mutable.equals(camera.getBlockPosition())) {
                FluidState neighboringFluidstate = world.getFluidState(mutable);
                if(neighboringFluidstate.is(BzFluidTags.BZ_HONEY_FLUID)) {
                    fluidstate = neighboringFluidstate;
                }
            }
        }

        return fluidstate;
    }
}
