package com.telepathicgrunt.the_bumblezone.client.rendering.electricring;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ElectricRingEntity;
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

public class ElectricRingRenderer extends EntityRenderer<ElectricRingEntity, ElectricRingRenderState> {
    static final Identifier SKIN_1 = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/electric_ring/electric_ring_1.png");
    static final Identifier SKIN_2 = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/electric_ring/electric_ring_2.png");
    static final Identifier SKIN_3 = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/electric_ring/electric_ring_3.png");
    protected final ElectricRingModel model;

    public ElectricRingRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ElectricRingModel(context.bakeLayer(ElectricRingModel.LAYER_LOCATION));
    }

    @Override
    public ElectricRingRenderState createRenderState() {
        return new ElectricRingRenderState();
    }

    @Override
    public void extractRenderState(ElectricRingEntity entity, ElectricRingRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.isInvisibleToPlayer = state.isInvisible && entity.isInvisibleTo(Minecraft.getInstance().player);
        state.disappearing = entity.disappearingTime >= 0;
        state.disappearingTime = entity.disappearingTime - partialTicks;
    }

    @Override
    public void submit(ElectricRingRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        float angleExtra = -180f;
        int interval = 3;
        int ringState = Mth.floor(state.ageInTicks) / interval;

        float spinSpeed = 15f;
        float angle = state.ageInTicks * spinSpeed;

        angle += (ringState * angleExtra);

        poseStack.pushPose();
        float scale;
        if (state.disappearing) {
            scale = Math.min(state.disappearingTime / ElectricRingEntity.DISAPPERING_TIMESPAN, 1.0f);
        } else {
            scale = Math.min(state.ageInTicks / ElectricRingEntity.APPEARING_TIMESPAN, 1.0f);
        }

        poseStack.scale(-scale, -scale, scale);
        poseStack.translate(0.0f, -(state.eyeHeight) - (1.5f - (scale * 1.5f)), 0.0f);
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0f - state.yRot));
        poseStack.mulPose(Axis.XN.rotationDegrees(180.0f - state.xRot));
        poseStack.mulPose(Axis.ZN.rotationDegrees(angle % 360));
        this.model.setupAnim(state);
        boolean isBodyVisible = !state.isInvisible;
        boolean forceTransparent = !isBodyVisible && !state.isInvisibleToPlayer;
        RenderType renderType = this.getRenderType(state, isBodyVisible, forceTransparent, state.appearsGlowing());
        if (renderType != null) {
            collector.submitModel(this.model, state, poseStack, renderType, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        }
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    @Nullable
    private RenderType getRenderType(ElectricRingRenderState state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
        Identifier texture = this.getTextureLocation(state);
        if (forceTransparent) {
            return RenderTypes.entityTranslucentCullItemTarget(texture);
        } else if (isBodyVisible) {
            return this.model.renderType(texture);
        } else {
            return appearGlowing ? RenderTypes.outline(texture) : null;
        }
    }

    private Identifier getTextureLocation(ElectricRingRenderState ringEntity) {
        int interval = 3;
        int state = Mth.floor(ringEntity.ageInTicks) % (interval * 3);

        if (state < interval) {
            return SKIN_1;
        }
        else if (state < interval * 2) {
            return SKIN_2;
        }
        else {
            return SKIN_3;
        }
    }
}