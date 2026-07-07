package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei;

// TODO: Re-enable when REI updates
//public class QueenEggIconRenderer extends DisplayRenderer {
//
//	private final Identifier texture;
//
//	public QueenEggIconRenderer(Identifier texture) {
//		this.texture = texture;
//	}
//
//	@Override
//	public void render(GuiGraphicsExtractor graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
//		RenderSystem.setShaderTexture(0, this.texture);
//		graphics.pose().pushPose();
//		Matrix4f matrix = graphics.pose().last().pose();
//		RenderSystem.setShader(GameRenderer::getPositionTexShader);
//		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//		bufferBuilder.addVertex(matrix, bounds.getCenterX() - 8, bounds.getCenterY() - 8, 0).setUv(0, 0);
//		bufferBuilder.addVertex(matrix, bounds.getCenterX() - 8, bounds.getCenterY() + 8, 0).setUv(0, 1);
//		bufferBuilder.addVertex(matrix, bounds.getCenterX() + 8, bounds.getCenterY() + 8, 0).setUv(1, 1);
//		bufferBuilder.addVertex(matrix, bounds.getCenterX() + 8, bounds.getCenterY() - 8, 0).setUv(1, 0);
//		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
//		graphics.pose().popPose();
//	}
//
//	@Override
//	public int getHeight() {
//		return 16;
//	}
//}
