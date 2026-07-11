package com.telepathicgrunt.the_bumblezone.client.rendering.stingerspear;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

public class StingerSpearRenderer extends EntityRenderer<ThrownStingerSpearEntity, ThrownTridentRenderState> {
    public static final Identifier STINGER_SPEAR_LOCATION = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/stinger_spear.png");
    private final StingerSpearModel model;

    public StingerSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StingerSpearModel(context.bakeLayer(StingerSpearModel.LAYER_LOCATION));
    }

    @Override
    public void submit(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));
        submitNodeCollector.order(0).submitModel(this.model, Unit.INSTANCE, poseStack, STINGER_SPEAR_LOCATION, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        if (state.isFoil) {
            submitNodeCollector.order(1).submitModel(this.model, Unit.INSTANCE, poseStack, ItemFeatureRenderer.getFoilRenderType(this.model.renderType(STINGER_SPEAR_LOCATION), false), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        }

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    @Override
    public void extractRenderState(ThrownStingerSpearEntity entity, ThrownTridentRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.isFoil = entity.isFoil();
    }
}