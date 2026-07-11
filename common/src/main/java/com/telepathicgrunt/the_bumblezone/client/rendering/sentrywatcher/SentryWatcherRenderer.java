package com.telepathicgrunt.the_bumblezone.client.rendering.sentrywatcher;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.SentryWatcherEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class SentryWatcherRenderer extends EntityRenderer<SentryWatcherEntity, SentryWatcherRenderState> {
    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/sentry_watcher.png");
    private static final Identifier EYES = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/sentry_watcher_eyes.png");
    protected final SentryWatcherModel model;

    public SentryWatcherRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SentryWatcherModel(context.bakeLayer(SentryWatcherModel.LAYER_LOCATION));
    }

    @Override
    public void submit(SentryWatcherRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0f, state.ySize + 0.05f, 0.0f);

        float shakeEffect = 0;
        if (state.shaking) {
            shakeEffect += (float)(Math.cos(Mth.floor(state.ageInTicks) * 3.25D) * Mth.PI * 0.4F);
        }

        poseStack.mulPose(Axis.YN.rotationDegrees(state.yRot + shakeEffect));
        poseStack.mulPose(Axis.XN.rotationDegrees(180.0f - state.xRot));
        this.model.setupAnim(state);
        boolean isBodyVisible = !state.isInvisible;
        boolean forceTransparent = !isBodyVisible && !state.isInvisibleToPlayer;
        RenderType renderType = this.getRenderType(isBodyVisible, forceTransparent, state.appearsGlowing());
        if (renderType != null) {
            collector.submitModel(this.model, state, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);

            if (state.activated) {
                collector.submitModel(this.model, state, poseStack, RenderTypes.eyes(EYES), LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
            }
        }
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
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

    @Override
    public SentryWatcherRenderState createRenderState() {
        return new SentryWatcherRenderState();
    }

    @Override
    public void extractRenderState(SentryWatcherEntity entity, SentryWatcherRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.ySize = entity.getBoundingBox().getYsize();
        state.shaking = entity.hasShaking();
        state.activated = entity.hasActivated();
        state.isInvisibleToPlayer = state.isInvisible && entity.isInvisibleTo(Minecraft.getInstance().player);
    }
}