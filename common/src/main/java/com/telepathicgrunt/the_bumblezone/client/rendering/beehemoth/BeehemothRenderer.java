package com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BeehemothRenderer extends MobRenderer<BeehemothEntity, BeehemothRenderState, BeehemothModel> {
    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/beehemoth.png");

    public BeehemothRenderer(EntityRendererProvider.Context context) {
        super(context, new BeehemothModel(context.bakeLayer(BeehemothModel.LAYER_LOCATION)), 0.4F);
    }

    @Override
    public void submit(BeehemothRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        stack.pushPose();
        super.submit(state, stack, collector, camera);
        stack.popPose();

        if (state.shouldRenderFriendshipProgress ) {
            renderFriendshipProgress(
                state,
                Component.translatable("entity.the_bumblezone.beehemoth_friendship_progress", state.friendship),
                stack,
                collector,
                camera);
        }
    }

    @Override
    protected void scale(BeehemothRenderState state, PoseStack poseStack) {
        float scale = 1.6f;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0, 0.15, 0);
    }

    protected void renderFriendshipProgress(BeehemothRenderState state, Component component, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.distanceToCameraSq > 100.0) {
            return;
        }

        float f = state.boundingBoxHeight + 0.75F;
        float h = -this.getFont().width(component) / 2F;

        float shadowXYOffsets = 0.011f;
        float shadowZOffsets = -0.0001f;
        float textSize = 0.025f;

        // Shadow
        RenderTextShadow(state, component, poseStack, f, shadowXYOffsets, shadowXYOffsets, shadowZOffsets, textSize, collector, h, camera);
        RenderTextShadow(state, component, poseStack, f, shadowXYOffsets, -shadowXYOffsets, shadowZOffsets, textSize, collector, h, camera);
        RenderTextShadow(state, component, poseStack, f, -shadowXYOffsets, shadowXYOffsets, shadowZOffsets, textSize, collector, h, camera);
        RenderTextShadow(state, component, poseStack, f, -shadowXYOffsets, -shadowXYOffsets, shadowZOffsets, textSize, collector, h, camera);

        // Actual text
        poseStack.pushPose();
        poseStack.translate(0.0f, f, 0.0f);
        poseStack.mulPose(camera.orientation);
        poseStack.scale(textSize, -textSize, textSize);
        collector.submitText(poseStack, h, 0, component.getVisualOrderText(), false, Font.DisplayMode.NORMAL, state.lightCoords, -1, 0, 0);
        poseStack.popPose();
    }

    private void RenderTextShadow(
            BeehemothRenderState state,
            Component component,
            PoseStack poseStack,
            float f,
            float shadowXOffsets,
            float shadowYOffsets,
            float shadowZOffsets,
            float textSize,
            SubmitNodeCollector collector,
            float h,
            CameraRenderState camera)
    {
        poseStack.pushPose();
        poseStack.translate(0.0f, f, 0.0f);
        poseStack.mulPose(camera.orientation);
        poseStack.translate(shadowXOffsets, shadowYOffsets, shadowZOffsets);
        poseStack.scale(textSize, -textSize, textSize);
        collector.submitText(poseStack, h, 0, component.getVisualOrderText(), false, Font.DisplayMode.NORMAL, state.lightCoords, 0, 0, 0);
        poseStack.popPose();
    }

    @Override
    public BeehemothRenderState createRenderState() {
        return new BeehemothRenderState();
    }

    @Override
    public void extractRenderState(BeehemothEntity entity, BeehemothRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.shouldRenderFriendshipProgress = entity == this.entityRenderDispatcher.crosshairPickEntity && !entity.isMaxFriendship() && entity.isTame() && entity.isOwnedBy(GeneralUtilsClient.getClientPlayer());
        state.friendship = entity.getFriendship();
        state.saddled = entity.isSaddled();
        state.queen = entity.isQueen();
        state.onGround = entity.onGround() || entity.isPassenger();
        state.sitting = entity.isInSittingPose();
        state.xzSpeed = Math.abs(entity.getDeltaMovement().x()) + Math.abs(entity.getDeltaMovement().z());
        state.kneeOffsets = new float[]{
            entity.offset1,
            entity.offset2,
            entity.offset3,
            entity.offset4,
            entity.offset5,
            entity.offset6
        };
    }

    @Override
    public Identifier getTextureLocation(BeehemothRenderState bee) {
        return SKIN;
    }
}