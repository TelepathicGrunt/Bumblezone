package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.AbstractRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class QueenEggIconRenderer extends AbstractRenderer {

	private final ResourceLocation texture;

	public QueenEggIconRenderer(ResourceLocation texture) {
		this.texture = texture;
	}

	@Override
	public void render(PoseStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderTexture(0, this.texture);
		matrices.pushPose();
		Matrix4f matrix = matrices.last().pose();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix, bounds.getCenterX() - 8, bounds.getCenterY() - 8, 0).uv(0, 0).endVertex();
		bufferBuilder.vertex(matrix, bounds.getCenterX() - 8, bounds.getCenterY() + 8, 0).uv(0, 1).endVertex();
		bufferBuilder.vertex(matrix, bounds.getCenterX() + 8, bounds.getCenterY() + 8, 0).uv(1, 1).endVertex();
		bufferBuilder.vertex(matrix, bounds.getCenterX() + 8, bounds.getCenterY() - 8, 0).uv(1, 0).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		matrices.popPose();
	}
}
