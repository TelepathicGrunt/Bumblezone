package com.telepathicgrunt.the_bumblezone.client.rendering.honeycrystalshard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.HoneyCrystalShardEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class HoneyCrystalShardRenderer extends EntityRenderer<HoneyCrystalShardEntity, ArrowRenderState> {
    public static final Identifier HONEY_CRYSTAL_SHARD_LOCATION = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/honey_crystal_shard.png");
    private final HoneyCrystalShardModel model;

    public HoneyCrystalShardRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new HoneyCrystalShardModel(context.bakeLayer(HoneyCrystalShardModel.LAYER_LOCATION));
    }

    @Override
    public void submit(ArrowRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));
        collector.submitModel(this.model, state, poseStack, HONEY_CRYSTAL_SHARD_LOCATION, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    public void extractRenderState(HoneyCrystalShardEntity entity, ArrowRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.shake = entity.shakeTime - partialTicks;
    }
}