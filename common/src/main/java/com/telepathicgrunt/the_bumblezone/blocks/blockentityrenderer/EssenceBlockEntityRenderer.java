package com.telepathicgrunt.the_bumblezone.blocks.blockentityrenderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferVertexConsumer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.Random;


public class EssenceBlockEntityRenderer implements BlockEntityRenderer<EssenceBlockEntity> {

	public EssenceBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	private static final long RANDOM_SEED = 31100L;
	private static final Random RANDOM = new Random(RANDOM_SEED);
	public static final ResourceLocation BASE_TEXTURE = new ResourceLocation(Bumblezone.MODID, "textures/block/essence/base_background.png");
	public static final ResourceLocation BEE_TEXTURE = new ResourceLocation(Bumblezone.MODID, "textures/block/essence/bee_icon_background.png");

	public static final VertexFormatElement ELEMENT_UV3D = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 3);
	public static final VertexFormat POSITION_COLOR_TEX3D = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder().put("Position", DefaultVertexFormat.ELEMENT_POSITION).put("Color", DefaultVertexFormat.ELEMENT_COLOR).put("UV3D", ELEMENT_UV3D).build());
	RenderType.CompositeRenderType ESSENCE_RENDER_TYPE =
			RenderType.create(
					"bumblezone_essence_block",
					DefaultVertexFormat.POSITION_COLOR,
					VertexFormat.Mode.QUADS,
					256,
					false,
					false,
					RenderType.CompositeState.builder()
							.setShaderState(new RenderStateShard.ShaderStateShard(() -> {
								try {
									return new ShaderInstance(Minecraft.getInstance().getResourceManager(), "rendertype_bumblezone_essence", POSITION_COLOR_TEX3D);
								}
								catch (IOException e) {
									e.printStackTrace();
								}
								return null;
							}))
							.setTextureState(RenderStateShard.MultiTextureStateShard.builder()
									.add(BASE_TEXTURE, false, false)
									.add(BEE_TEXTURE, false, false)
									.build())
							.createCompositeState(false)
			);

	@Override
	public void render(EssenceBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int brightness, int overlayType) {
		RANDOM.setSeed(RANDOM_SEED);
		Matrix4f matrix4f = poseStack.last().pose();
		this.renderSides(blockEntity, matrix4f, multiBufferSource.getBuffer(this.getType()));
	}

	private void renderSides(EssenceBlockEntity entity, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
		float red = 1.0F;
		float green = 1.0F;
		float blue = 1.0F;

		this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, red, green, blue, Direction.SOUTH);
		this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, red, green, blue, Direction.NORTH);
		this.renderSide(entity, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.EAST);
		this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.WEST);
		this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, red, green, blue, Direction.DOWN);
		this.renderSide(entity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, red, green, blue, Direction.UP);
	}

	private void renderSide(EssenceBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, float red, float green, float blue, Direction direction) {
		if (entity.shouldDrawSide(direction) && vertices instanceof BufferVertexConsumer bufferVertexConsumer) {
			BlockPos blockPos = entity.getBlockPos();

			addPortalVertex(bufferVertexConsumer, model, x1, y1, z1, red, green, blue, blockPos.getX() + x1, blockPos.getZ() + z1, blockPos.getY() + y2);
			addPortalVertex(bufferVertexConsumer, model, x2, y1, z2, red, green, blue, blockPos.getX() + x2, blockPos.getZ() + z1, blockPos.getY() + y1);
			addPortalVertex(bufferVertexConsumer, model, x2, y2, z3, red, green, blue, blockPos.getX() + x2, blockPos.getZ() + z2, blockPos.getY() + y1);
			addPortalVertex(bufferVertexConsumer, model, x1, y2, z4, red, green, blue, blockPos.getX() + x1, blockPos.getZ() + z2, blockPos.getY() + y2);
		}
	}

	private static void addPortalVertex(BufferVertexConsumer buffer, Matrix4f mat, float x, float y, float z, float red, float green, float blue, float s, float t, float p) {
		buffer.vertex(mat, x, y, z).color(red, green, blue, 1);
		buffer.putFloat(0, s);
		buffer.putFloat(4, t);
		buffer.putFloat(8, p);
		buffer.endVertex();
	}

	protected RenderType getType() {
		return ESSENCE_RENDER_TYPE;
	}
}