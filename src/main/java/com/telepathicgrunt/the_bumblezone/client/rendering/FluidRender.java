package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;


// CLIENT-SIDED
public class FluidRender {

    private static final ResourceLocation SUGAR_WATER_TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/sugar_water_underwater.png");
    private static final ResourceLocation HONEY_TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/honey_fluid_underwater.png");

    public static void sugarWaterOverlay(RenderBlockOverlayEvent event)
    {
        BlockState state = event.getPlayer().level.getBlockState(new BlockPos(event.getPlayer().getEyePosition(0)));
        if (state.is(BzFluids.SUGAR_WATER_BLOCK.get()))
        {
            Minecraft minecraftIn = Minecraft.getInstance();
            minecraftIn.getTextureManager().bind(SUGAR_WATER_TEXTURE_UNDERWATER);
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
            float f = event.getPlayer().getBrightness();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float f7 = -event.getPlayer().yRot / 64.0F;
            float f8 = event.getPlayer().xRot / 64.0F;
            Matrix4f matrix4f = event.getMatrixStack().last().pose();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(f, f, f, 0.42F).uv(4.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(f, f, f, 0.42F).uv(0.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(f, f, f, 0.42F).uv(0.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(f, f, f, 0.42F).uv(4.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.end();
            WorldVertexBufferUploader.end(bufferbuilder);
            RenderSystem.disableBlend();
            event.setCanceled(true);
        }
    }

    public static void renderHoneyOverlay(Minecraft minecraft, MatrixStack matrixStack) {
        BlockState state = minecraft.player.level.getBlockState(new BlockPos(minecraft.player.getEyePosition(1)));
        if (state.is(BzFluids.HONEY_FLUID_BLOCK.get())) {
            minecraft.getTextureManager().bind(HONEY_TEXTURE_UNDERWATER);
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
            // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
            float brightness = (float) Math.max(
                    Math.pow(FluidRender.getDimensionBrightnessAtEyes(minecraft.player), 2D),
                    minecraft.player.level.dimensionType().brightness(0)
            );
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float f7 = -minecraft.player.yRot / (64.0F * 8F);
            float f8 = minecraft.player.xRot / (64.0F * 8F);
            Matrix4f matrix4f = matrixStack.last().pose();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(brightness, brightness, brightness, 0.95F).uv(1.0F + f7, 1.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(brightness, brightness, brightness, 0.95F).uv(0.0F + f7, 2.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(brightness, brightness, brightness, 0.95F).uv(1.0F + f7, 1.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(brightness, brightness, brightness, 0.95F).uv(2.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.end();
            WorldVertexBufferUploader.end(bufferbuilder);
            RenderSystem.disableBlend();
        }
    }

    public static void renderHoneyFog(EntityViewRenderEvent.RenderFogEvent event)
    {
        FluidState fluidstate = getNearbyHoneyFluid(event.getInfo());
        if(fluidstate.is(BzFluidTags.BZ_HONEY_FLUID)) {
            RenderSystem.fogStart(0.35f);
            RenderSystem.fogEnd(4);
            RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
            RenderSystem.setupNvFogDistance();
        }
    }

    public static float getDimensionBrightnessAtEyes(Entity entity){
        float lightLevelAtEyes = entity.level.getRawBrightness(new BlockPos(entity.getEyePosition(1)), 0);
        return lightLevelAtEyes / 15f;
    }

    public static FluidState getNearbyHoneyFluid(ActiveRenderInfo activeRenderInfo){
        Entity entity = activeRenderInfo.getEntity();
        World world = entity.level;
        FluidState fluidstate = world.getFluidState(activeRenderInfo.getBlockPosition());

        Vector3d currentPos = activeRenderInfo.getPosition();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        double offsetDistanceCheck = 0.075D;

        for(Direction direction : Direction.values()) {
            double x = currentPos.x() + direction.getStepX() * offsetDistanceCheck;
            double y = currentPos.y() + direction.getStepY() * offsetDistanceCheck;
            double z = currentPos.z() + direction.getStepZ() * offsetDistanceCheck;
            mutable.set(x, y, z);
            if(!mutable.equals(activeRenderInfo.getBlockPosition())) {
                FluidState neighboringFluidstate = world.getFluidState(mutable);
                if(neighboringFluidstate.is(BzFluidTags.BZ_HONEY_FLUID)) {
                    fluidstate = neighboringFluidstate;
                }
            }
        }

        return fluidstate;
    }
}
