package com.telepathicgrunt.the_bumblezone.client.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.client.blockentityrenderer.renderstates.EssenceBlockEntityRendererState;
import com.telepathicgrunt.the_bumblezone.client.shaders.EssenceBlockShader;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
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

		state.colorInt = blockEntity.getBlockState().getMapColor(blockEntity.getLevel(), blockEntity.getBlockPos()).col;
		state.facesToShow.clear();

		for(Direction direction : Direction.values()) {
			if (blockEntity.shouldDrawSide(direction)) {
				state.facesToShow.add(direction);
			}
		}
	}

	public void submit(EssenceBlockEntityRendererState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (!BzClientConfigs.disableEssenceBlockShaders) {
			submitCube(state, EssenceBlockShader.BUMBLEZONE_ESSENCE_RENDERTYPE, poseStack, submitNodeCollector);
		}
	}

	public int getViewDistance() {
		return 256;
	}

	protected static void submitCube(EssenceBlockEntityRendererState state, RenderType renderType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
		if (!state.facesToShow.isEmpty()) {
			submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
				for (Direction direction : state.facesToShow) {
					for (Vector3fc faceVertex : FACES.get(direction)) {
						float red = ARGB.red(state.colorInt) / 255f;
						float green = ARGB.green(state.colorInt) / 255f;
						float blue = ARGB.blue(state.colorInt) / 255f;

						buffer.addVertex(pose, faceVertex).setColor(red, green, blue, 1);
					}
				}
			});
		}
	}
}