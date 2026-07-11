package com.telepathicgrunt.the_bumblezone.client.rendering.purplespike;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PurpleSpikeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class PurpleSpikeRenderer extends EntityRenderer<PurpleSpikeEntity, PurpleSpikeRenderState>
{
    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/purple_spike.png");
    protected final PurpleSpikeModel model;

    public PurpleSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PurpleSpikeModel(context.bakeLayer(PurpleSpikeModel.LAYER_LOCATION));
    }

    @Override
    public PurpleSpikeRenderState createRenderState() {
        return new PurpleSpikeRenderState();
    }

    @Override
    public void extractRenderState(PurpleSpikeEntity entity, PurpleSpikeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.hasSpike = entity.hasSpike();
        state.hasSpikeCharge = entity.hasSpikeCharge();
        state.spikeChargeClientTimeTracker = entity.spikeChargeClientTimeTracker;
        state.isInvisibleToPlayer = state.isInvisible && entity.isInvisibleTo(Minecraft.getInstance().player);
    }

    @Override
    public void submit(PurpleSpikeRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();

        float offSet = 1.0f + Math.min(state.spikeChargeClientTimeTracker / 40f, 0.5f);

        poseStack.scale(1.0f, 1.0f, 1.0f);
        poseStack.translate(0.0f, offSet, 0.0f);
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0f - state.yRot));
        poseStack.mulPose(Axis.XN.rotationDegrees(180.0f - state.xRot));
        this.model.setupAnim(state);
        boolean isBodyVisible = !state.isInvisible;
        boolean forceTransparent = !isBodyVisible && !state.isInvisibleToPlayer;
        RenderType renderType = this.getRenderType(isBodyVisible, forceTransparent, state.appearsGlowing());
        if (renderType != null) {
            collector.submitModel(this.model, state, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, forceTransparent ? 654311423 : -1, null, state.outlineColor, null);
        }
        poseStack.popPose();
    }

    @Nullable
    private RenderType getRenderType(boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
        if (forceTransparent) {
            return RenderTypes.entityTranslucentCullItemTarget(SKIN);
        } else if (isBodyVisible) {
            return this.model.renderType(SKIN);
        } else {
            return appearGlowing ? RenderTypes.outline(SKIN) : null;
        }
    }
}