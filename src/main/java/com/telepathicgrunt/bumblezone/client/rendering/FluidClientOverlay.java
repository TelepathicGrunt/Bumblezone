package com.telepathicgrunt.bumblezone.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzFluids;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FluidClientOverlay {
    private static final Identifier TEXTURE_UNDERWATER = new Identifier(Bumblezone.MODID, "textures/misc/sugar_water_underwater.png");
    private static final Identifier HONEY_TEXTURE_UNDERWATER = new Identifier(Bumblezone.MODID + ":textures/misc/honey_fluid_underwater.png");

    public static boolean sugarWaterFluidOverlay(PlayerEntity player, MatrixStack matrixStack) {
        if(!(player instanceof ClientPlayerEntity clientPlayerEntity)) return false;
        BlockState state = player.world.getBlockState(new BlockPos(player.getCameraPosVec(0)));
        if (state.isOf(BzFluids.SUGAR_WATER_BLOCK)) {
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
            bufferBuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(4.0F + modifiedYaw, 4.0F + modifiedPitch).next();
            bufferBuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F + modifiedYaw, 4.0F + modifiedPitch).next();
            bufferBuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(0.0F + modifiedYaw, 0.0F + modifiedPitch).next();
            bufferBuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(4.0F + modifiedYaw, 0.0F + modifiedPitch).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.disableBlend();
            return true;
        }

        return false;
    }

    public static boolean renderHoneyOverlay(PlayerEntity player, MatrixStack matrixStack) {
        if(!player.isSubmergedIn(BzFluidTags.BZ_HONEY_FLUID) || !(player instanceof ClientPlayerEntity clientPlayerEntity))
            return false;

        BlockState state = clientPlayerEntity.world.getBlockState(new BlockPos(clientPlayerEntity.getCameraPosVec(1)));
        if (state.isOf(BzFluids.HONEY_FLUID_BLOCK)) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, HONEY_TEXTURE_UNDERWATER);
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
            float f = (float) Math.pow(clientPlayerEntity.getBrightnessAtEyes(), 2D);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(f, f, f, 0.95F);
            float modifiedYaw = -clientPlayerEntity.getYaw() / (64.0F * 8F);
            float modifiedPitch = clientPlayerEntity.getPitch() / (64.0F * 8F);
            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).texture(1.0F + modifiedYaw, 1.0F + modifiedPitch).next();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).texture(0.0F + modifiedYaw, 2.0F + modifiedPitch).next();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).texture(1.0F + modifiedYaw, 1.0F + modifiedPitch).next();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).texture(2.0F + modifiedYaw, 0.0F + modifiedPitch).next();
            bufferbuilder.end();
            BufferRenderer.draw(bufferbuilder);
            RenderSystem.disableBlend();
            return true;
        }

        return false;
    }

    public static void renderHoneyFog(Camera camera) {
        FluidState fluidstate = getNearbyHoneyFluid(camera);
        if(fluidstate.isIn(BzFluidTags.BZ_HONEY_FLUID)) {
            RenderSystem.setShaderFogStart(0.35f);
            RenderSystem.setShaderFogEnd(4);
        }
    }

    public static FluidState getNearbyHoneyFluid(Camera camera){
        Entity entity = camera.getFocusedEntity();
        World world = entity.world;
        FluidState fluidstate = world.getFluidState(camera.getBlockPos());

        Vec3d currentPos = camera.getPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        double offsetDistanceCheck = 0.075D;

        for(Direction direction : Direction.values()) {
            double x = currentPos.getX() + direction.getOffsetX() * offsetDistanceCheck;
            double y = currentPos.getY() + direction.getOffsetY() * offsetDistanceCheck;
            double z = currentPos.getZ() + direction.getOffsetZ() * offsetDistanceCheck;
            mutable.set(x, y, z);
            if(!mutable.equals(camera.getBlockPos())) {
                FluidState neighboringFluidstate = world.getFluidState(mutable);
                if(neighboringFluidstate.isIn(BzFluidTags.BZ_HONEY_FLUID)) {
                    fluidstate = neighboringFluidstate;
                }
            }
        }

        return fluidstate;
    }
}
