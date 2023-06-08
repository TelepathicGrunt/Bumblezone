package com.telepathicgrunt.the_bumblezone.blocks.blockentityrenderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.InfinityBarrierBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.Random;


public class InfinityBarrierBlockEntityRenderer implements BlockEntityRenderer<InfinityBarrierBlockEntity> {

	public InfinityBarrierBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	private static final long RANDOM_SEED = 31100L;
	private static final Random RANDOM = new Random(RANDOM_SEED);
	public static final ResourceLocation BASE_TEXTURE = new ResourceLocation(Bumblezone.MODID, "textures/block/infinity_barrier/blank_background.png");
	public static final ResourceLocation LAYER_TEXTURE = new ResourceLocation(Bumblezone.MODID, "textures/block/infinity_barrier/barrier_background.png");

	public static final VertexFormat POSITION_COLOR_NORMAL = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
			.put("Position", DefaultVertexFormat.ELEMENT_POSITION)
			.put("Color", DefaultVertexFormat.ELEMENT_COLOR)
			.put("Normal", DefaultVertexFormat.ELEMENT_NORMAL).build());

	RenderType.CompositeRenderType BARRIER_RENDER_TYPE =
			RenderType.create(
					"bumblezone_infinity_barrier",
					POSITION_COLOR_NORMAL,
					VertexFormat.Mode.QUADS,
					256,
					false,
					false,
					RenderType.CompositeState.builder()
							.setShaderState(new RenderStateShard.ShaderStateShard(() -> {
								try {
									return new ShaderInstance(Minecraft.getInstance().getResourceManager(), "rendertype_bumblezone_infinity_barrier", POSITION_COLOR_NORMAL);
								}
								catch (IOException e) {
									e.printStackTrace();
								}
								return null;
							}))
							.setTextureState(RenderStateShard.MultiTextureStateShard.builder()
									.add(BASE_TEXTURE, false, false)
									.add(LAYER_TEXTURE, false, false)
									.build())
							.createCompositeState(false)
			);

	@Override
	public void render(InfinityBarrierBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int brightness, int overlayType) {
		RANDOM.setSeed(RANDOM_SEED);
		Matrix4f matrix4f = poseStack.last().pose();
		this.renderSides(blockEntity, matrix4f, multiBufferSource.getBuffer(this.getType()));
	}

	private void renderSides(InfinityBarrierBlockEntity blockEntity, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
		int colorInt = 7467520;

		float red = FastColor.ARGB32.red(colorInt) / 255f;
		float green = FastColor.ARGB32.green(colorInt) / 255f;
		float blue = FastColor.ARGB32.blue(colorInt) / 255f;

		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, red, green, blue, Direction.SOUTH);
		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, red, green, blue, Direction.NORTH);
		this.renderSide(blockEntity, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.EAST);
		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.WEST);
		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, red, green, blue, Direction.DOWN);
		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, red, green, blue, Direction.UP);
	}

	private void renderSide(InfinityBarrierBlockEntity blockEntity, Matrix4f model, VertexConsumer vertexConsumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, float red, float green, float blue, Direction direction) {
		if (blockEntity.shouldDrawSide(direction)) {
			Vec3i normal = direction.getNormal();
			addVertex(vertexConsumer, model, x1, y1, z1, red, green, blue, normal);
			addVertex(vertexConsumer, model, x2, y1, z2, red, green, blue, normal);
			addVertex(vertexConsumer, model, x2, y2, z3, red, green, blue, normal);
			addVertex(vertexConsumer, model, x1, y2, z4, red, green, blue, normal);
		}
	}

	private static void addVertex(VertexConsumer vertexConsumer, Matrix4f mat, float x, float y, float z, float red, float green, float blue, Vec3i normal) {
		vertexConsumer
				.vertex(mat, x, y, z)
				.color(red, green, blue, 1)
				.normal(
					normal.getX(),
					normal.getY(),
					normal.getZ()
				)
				.endVertex();
	}

	protected RenderType getType() {
		return BARRIER_RENDER_TYPE;
	}
}