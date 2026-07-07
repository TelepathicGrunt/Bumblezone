package com.telepathicgrunt.the_bumblezone.client.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.client.blockentityrenderer.renderstates.EssenceBlockEntityRendererState;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class EssenceBlockEntityRenderer implements BlockEntityRenderer<EssenceBlockEntity, EssenceBlockEntityRendererState> {

	private static final List<Direction> ALL_FACES = List.of(Direction.values());
	private static final Vector3fc FROM = new Vector3f(0.0F, 0.0F, 0.0F);
	private static final Vector3fc TO = new Vector3f(1.0F, 1.0F, 1.0F);
	private static final Map<Direction, List<Vector3fc>> FACES = Util.makeEnumMap(
			Direction.class,
			direction -> {
				FaceInfo faceInfo = FaceInfo.fromFacing(direction);
				return List.of(
						faceInfo.getVertexInfo(0).select(FROM, TO),
						faceInfo.getVertexInfo(1).select(FROM, TO),
						faceInfo.getVertexInfo(2).select(FROM, TO),
						faceInfo.getVertexInfo(3).select(FROM, TO)
				);
			}
	);

	public EssenceBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	public EssenceBlockEntityRendererState createRenderState() {
		return new EssenceBlockEntityRendererState();
	}

	public void extractRenderState(EssenceBlockEntity blockEntity, EssenceBlockEntityRendererState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.facesToShow.clear();

		for(Direction direction : Direction.values()) {
			if (blockEntity.shouldDrawSide(direction)) {
				state.facesToShow.add(direction);
			}
		}
	}

	public void submit(EssenceBlockEntityRendererState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		submitCube(state.facesToShow, RenderTypes.endGateway(), poseStack, submitNodeCollector);
	}

	public int getViewDistance() {
		return 256;
	}

	protected static void submitCube(Collection<Direction> facesToShow, net.minecraft.client.renderer.rendertype.RenderType renderType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
		if (!facesToShow.isEmpty()) {
			submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
				for (Direction direction : facesToShow) {
					for (Vector3fc faceVertex : FACES.get(direction)) {
						buffer.addVertex(pose, faceVertex);
					}
				}
			});
		}
	}

	public static void submitSpecial(RenderType renderType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
		submitCube(ALL_FACES, renderType, poseStack, submitNodeCollector);
	}

	public static void getExtents(Consumer<Vector3fc> output) {
		FACES.values().forEach(vertices -> vertices.forEach(output));
	}

//	private static final long RANDOM_SEED = 31100L;
//	private static final Random RANDOM = new Random(RANDOM_SEED);
//	public static final Identifier BASE_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/block/essence/base_background.png");
//	public static final Identifier BEE_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/block/essence/bee_icon_background.png");
//
//	public static final VertexFormat POSITION_COLOR_NORMAL = VertexFormat.builder()
//			.add("Position", VertexFormatElement.POSITION)
//			.add("Color", VertexFormatElement.COLOR)
//			.add("Normal", VertexFormatElement.NORMAL)
//			.build();
//
//	public static ShaderInstance SAFE_SHADER_INSTANCE = null;
//	public final RenderType.CompositeRenderType ESSENCE_RENDER_TYPE =
//			RenderType.create(
//					"bumblezone_essence_block",
//					POSITION_COLOR_NORMAL,
//					VertexFormat.Mode.QUADS,
//					256,
//					false,
//					false,
//					RenderType.CompositeState.builder()
//							.setShaderState(new RenderStateShard.ShaderStateShard(() -> SAFE_SHADER_INSTANCE))
//							.setTextureState(RenderStateShard.MultiTextureStateShard.builder()
//									.add(BASE_TEXTURE, false, false)
//									.add(BEE_TEXTURE, false, false)
//									.build())
//							.createCompositeState(false)
//			);
//
//	@Override
//	public void render(EssenceBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int brightness, int overlayType) {
//		if (BzClientConfigs.disableEssenceBlockShaders) {
//			return;
//		}
//		RANDOM.setSeed(RANDOM_SEED);
//		Matrix4f matrix4f = poseStack.last().pose();
//		this.renderSides(blockEntity, matrix4f, multiBufferSource.getBuffer(this.getType()));
//	}
//
//	private void renderSides(EssenceBlockEntity blockEntity, Matrix4f matrix4f, VertexConsumer vertexConsumer) {
//		int colorInt = blockEntity.getBlockState().getMapColor(blockEntity.getLevel(), blockEntity.getBlockPos()).col;
//
//		float red = FastColor.ARGB32.red(colorInt) / 255f;
//		float green = FastColor.ARGB32.green(colorInt) / 255f;
//		float blue = FastColor.ARGB32.blue(colorInt) / 255f;
//
//		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, red, green, blue, Direction.SOUTH);
//		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, red, green, blue, Direction.NORTH);
//		this.renderSide(blockEntity, matrix4f, vertexConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.EAST);
//		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, red, green, blue, Direction.WEST);
//		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, red, green, blue, Direction.DOWN);
//		this.renderSide(blockEntity, matrix4f, vertexConsumer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, red, green, blue, Direction.UP);
//	}
//
//	private void renderSide(EssenceBlockEntity blockEntity, Matrix4f model, VertexConsumer vertexConsumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, float red, float green, float blue, Direction direction) {
//		if (blockEntity.shouldDrawSide(direction)) {
//			Vec3i normal = direction.getUnitVec3i();
//			addPortalVertex(vertexConsumer, model, x1, y1, z1, red, green, blue, normal);
//			addPortalVertex(vertexConsumer, model, x2, y1, z2, red, green, blue, normal);
//			addPortalVertex(vertexConsumer, model, x2, y2, z3, red, green, blue, normal);
//			addPortalVertex(vertexConsumer, model, x1, y2, z4, red, green, blue, normal);
//		}
//	}
//
//	private static void addPortalVertex(VertexConsumer vertexConsumer, Matrix4f mat, float x, float y, float z, float red, float green, float blue, Vec3i normal) {
//		vertexConsumer
//				.addVertex(mat, x, y, z)
//				.setColor(red, green, blue, 1)
//				.setNormal(
//					normal.getX(),
//					normal.getY(),
//					normal.getZ()
//				);
//	}
//
//	protected RenderType getType() {
//		return ESSENCE_RENDER_TYPE;
//	}
}