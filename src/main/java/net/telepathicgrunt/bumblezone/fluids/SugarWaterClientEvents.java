package net.telepathicgrunt.bumblezone.fluids;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class SugarWaterClientEvents
{
	private static final ResourceLocation TEXTURE_UNDERWATER = new ResourceLocation(Bumblezone.MODID + ":textures/misc/sugar_water_underwater.png");
	
	public static void sugarWaterOverlay(RenderBlockOverlayEvent event)
	{
		if (event.getPlayer().world.getBlockState(event.getBlockPos()).getBlock() == SugarWaterEvents.SUGAR_WATER_BLOCK)
		{
			Minecraft minecraftIn = Minecraft.getInstance();
			minecraftIn.getTextureManager().bindTexture(TEXTURE_UNDERWATER);
			BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
			float f = minecraftIn.player.getBrightness();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			float f7 = -minecraftIn.player.rotationYaw / 64.0F;
			float f8 = minecraftIn.player.rotationPitch / 64.0F;
			Matrix4f matrix4f = event.getMatrixStack().getLast().getMatrix();
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
			bufferbuilder.pos(matrix4f, -1.0F, -1.0F, -0.5F).color(f, f, f, 0.42F).tex(4.0F + f7, 4.0F + f8).endVertex();
			bufferbuilder.pos(matrix4f, 1.0F, -1.0F, -0.5F).color(f, f, f, 0.42F).tex(0.0F + f7, 4.0F + f8).endVertex();
			bufferbuilder.pos(matrix4f, 1.0F, 1.0F, -0.5F).color(f, f, f, 0.42F).tex(0.0F + f7, 0.0F + f8).endVertex();
			bufferbuilder.pos(matrix4f, -1.0F, 1.0F, -0.5F).color(f, f, f, 0.42F).tex(4.0F + f7, 0.0F + f8).endVertex();
			bufferbuilder.finishDrawing();
			WorldVertexBufferUploader.draw(bufferbuilder);
			RenderSystem.disableBlend();
			event.setCanceled(true);
		}
	}
}
