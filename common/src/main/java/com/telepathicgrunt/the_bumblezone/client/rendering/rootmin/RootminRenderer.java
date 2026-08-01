package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class RootminRenderer extends MobRenderer<RootminEntity, RootminRenderState, RootminModel> {
    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/rootmin.png");

    private final BlockModelResolver resolver;

    public RootminRenderer(EntityRendererProvider.Context context) {
        super(context, new RootminModel(context.bakeLayer(RootminModel.LAYER_LOCATION)), 0.7F);
        this.resolver = context.getBlockModelResolver();
        this.addLayer(new FlowerBlockLayer(this));
        this.addLayer(new RootminGrassRenderer(this));
        this.addLayer(new RootminShieldRenderer(this));
    }

    @Override
    public RootminRenderState createRenderState() {
        return new RootminRenderState();
    }

    @Override
    public void extractRenderState(RootminEntity entity, RootminRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.angryAnimationState.copyFrom(entity.angryAnimationState);
        state.curiousAnimationState.copyFrom(entity.curiousAnimationState);
        state.curseAnimationState.copyFrom(entity.curseAnimationState);
        state.embarassedAnimationState.copyFrom(entity.embarassedAnimationState);
        state.shockAnimationState.copyFrom(entity.shockAnimationState);
        state.shootAnimationState.copyFrom(entity.shootAnimationState);
        state.runAnimationState.copyFrom(entity.runAnimationState);
        state.walkAnimationState.copyFrom(entity.walkAnimationState);
        state.blockToEntityAnimationState.copyFrom(entity.blockToEntityAnimationState);
        state.entityToBlockAnimationState.copyFrom(entity.entityToBlockAnimationState);

        state.pose = entity.getRootminPose();
        state.hideTransitionPercentage = (20f - Math.max(entity.animationTimeBetweenHiding - partialTicks, 0)) / 20f;
        state.isJebRootmin = checkMagicName(entity, "jeb_");
        state.grassColor = BiomeColors.getAverageGrassColor(Minecraft.getInstance().level, entity.blockPosition());
        state.shielded = entity.getRootminShield();

        if (entity.getFlowerBlock() != null) {
            this.resolver.update(state.flower, entity.getFlowerBlock(), BlockDisplayContext.create());
            state.flowerLightCoords = this.getLightCoordsAbove(entity, partialTicks, 1);
        } else {
            state.flower.clear();
            state.flowerLightCoords = this.getPackedLightCoords(entity, partialTicks);
        }

        if (entity.getFlowerBlock().getBlock() instanceof DoublePlantBlock) {
            this.resolver.update(state.tallFlowerHalf, entity.getFlowerBlock().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), BlockDisplayContext.create());
            state.doubleFlowerLightCoords = this.getLightCoordsAbove(entity, partialTicks, 2);
        } else {
            state.tallFlowerHalf.clear();
            state.doubleFlowerLightCoords = this.getPackedLightCoords(entity, partialTicks);
        }
    }

    public final int getLightCoordsAbove(RootminEntity entity, float partialTickTime, int above) {
        BlockPos blockPos = BlockPos.containing(entity.getLightProbePosition(partialTickTime)).above(above);
        return LightCoordsUtil.pack(this.getBlockLightLevel(entity, blockPos), this.getSkyLightLevel(entity, blockPos));
    }

    @Override
    protected float getShadowRadius(RootminRenderState state) {
        float target = state.pose == RootminState.ENTITY_TO_BLOCK ? 0f : 0.7f;
        float from = state.pose != RootminState.ENTITY_TO_BLOCK ? 0f : 0.7f;
        return Mth.lerp(state.hideTransitionPercentage, from, target);
    }

    @Override
    protected float getShadowStrength(RootminRenderState state) {
        float target = state.pose == RootminState.ENTITY_TO_BLOCK ? 0f : 1.0f;
        float from = state.pose != RootminState.ENTITY_TO_BLOCK ? 0f : 1.0f;
        return Mth.lerp(state.hideTransitionPercentage, from, target);
    }

    @Override
    public Identifier getTextureLocation(RootminRenderState state) {
        return SKIN;
    }


    public static class FlowerBlockLayer extends RenderLayer<RootminRenderState, RootminModel> {

        public FlowerBlockLayer(RenderLayerParent<RootminRenderState, RootminModel> renderLayerParent) {
            super(renderLayerParent);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, RootminRenderState state, float yRot, float xRot) {
            if (!state.flower.isEmpty()) {
                ModelPart rootModel = this.getParentModel().root().getChild("root");
                ModelPart bodyModel = rootModel.getChild("body");
                poseStack.pushPose();
                rootModel.translateAndRotate(poseStack);
                bodyModel.translateAndRotate(poseStack);
                poseStack.translate(-0.5f, -15 / 16f, 0.5f);
                poseStack.scale(1, -1, -1);
                state.flower.submit(poseStack, collector, state.flowerLightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
                if (!state.tallFlowerHalf.isEmpty()) {
                    poseStack.translate(0f, 1f, 0f);
                    state.tallFlowerHalf.submit(poseStack, collector, state.doubleFlowerLightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
                }
                poseStack.popPose();
            }
        }
    }
}