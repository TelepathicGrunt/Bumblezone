package com.telepathicgrunt.the_bumblezone.client.rendering.beestinger;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.BeeStingerEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class BeeStingerRenderer extends EntityRenderer<BeeStingerEntity, ArrowRenderState> {
    public static final Identifier BEE_STINGER_LOCATION = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/bee_stinger.png");
    private final BeeStingerModel model;

    public BeeStingerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BeeStingerModel(context.bakeLayer(BeeStingerModel.LAYER_LOCATION));
    }

    @Override
    public void submit(ArrowRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));
        collector.submitModel(this.model, state, poseStack, BEE_STINGER_LOCATION, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    public void extractRenderState(BeeStingerEntity entity, ArrowRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.shake = entity.shakeTime - partialTicks;
    }
}