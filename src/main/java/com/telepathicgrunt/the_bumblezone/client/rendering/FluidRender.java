package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;


// CLIENT-SIDED
public class FluidRender {

    private static final ResourceLocation TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/sugar_water_underwater.png");
    private static final ResourceLocation HONEY_TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/honey_fluid_underwater.png");

    public static void sugarWaterOverlay(RenderBlockOverlayEvent event)
    {
        BlockState state = event.getPlayer().level.getBlockState(new BlockPos(event.getPlayer().getEyePosition(0)));
        if (state.is(BzFluids.SUGAR_WATER_BLOCK.get()))
        {
            Minecraft minecraftIn = Minecraft.getInstance();
            minecraftIn.getTextureManager().bind(TEXTURE_UNDERWATER);
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
            float f = minecraft.player.getBrightness();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            float f7 = -minecraft.player.yRot / 64.0F;
            float f8 = minecraft.player.xRot / 64.0F;
            Matrix4f matrix4f = matrixStack.last().pose();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
            bufferbuilder.vertex(matrix4f, -1.0F, -1.0F, -0.5F).color(f, f, f, 0.95F).uv(4.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, -1.0F, -0.5F).color(f, f, f, 0.95F).uv(0.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, 1.0F, 1.0F, -0.5F).color(f, f, f, 0.95F).uv(0.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.vertex(matrix4f, -1.0F, 1.0F, -0.5F).color(f, f, f, 0.95F).uv(4.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.end();
            WorldVertexBufferUploader.end(bufferbuilder);
            RenderSystem.disableBlend();
        }
    }

    public static void renderHoneyFog(EntityViewRenderEvent.RenderFogEvent event)
    {
        FluidState fluidstate = event.getInfo().getFluidInCamera();
        if(fluidstate.is(BzFluidTags.BZ_HONEY_FLUID)) {
            RenderSystem.fogStart(0.35f);
            RenderSystem.fogEnd(4);
            RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
            RenderSystem.setupNvFogDistance();
        }
    }
}
