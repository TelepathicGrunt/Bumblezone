package com.telepathicgrunt.the_bumblezone.client.rendering.honeyslime;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class HoneySlimeRendering extends MobRenderer<HoneySlimeEntity, HoneySlimeRenderState, SlimeModel> {
    protected static final Identifier HONEY_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/honey_slime.png");
    protected static final Identifier HONEYLESS_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/honey_slime_naked.png");

    public HoneySlimeRendering(EntityRendererProvider.Context context) {
        super(context, new SlimeModel(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
        this.addLayer(new HoneySlimeOuterLayer(this, context.getModelSet()));
    }

    @Override
    public void submit(HoneySlimeRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        this.shadowRadius = 0.25F * (state.isBaby ? 1f : 2f);
        poseStack.translate(0, -0.0001f, 0);
        super.submit(state, poseStack, collector, camera);
    }

    // unused. Dont ask how the scaling even works automatically
    @Override
    protected void scale(HoneySlimeRenderState state, PoseStack stack) {
        stack.scale(0.999F, 0.999F, 0.999F);
        stack.translate(0.0D, 0.001D, 0.0D);
        float mainScale = state.isBaby ? 1f : 2f;
        float currentSquish = state.squish / (mainScale * 0.5F + 1.0F);
        float scaledSquish = 1.0F / (currentSquish + 1.0F);
        stack.scale(scaledSquish * mainScale, 1.0F / scaledSquish * mainScale, scaledSquish * mainScale);
    }

    @Override
    public Identifier getTextureLocation(HoneySlimeRenderState state) {
        return state.inHoney ? HONEY_TEXTURE : HONEYLESS_TEXTURE;
    }

    @Override
    public HoneySlimeRenderState createRenderState() {
        return new HoneySlimeRenderState();
    }

    @Override
    public void extractRenderState(HoneySlimeEntity entity, HoneySlimeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.squish = Mth.lerp(partialTicks, entity.prevSquishFactor, entity.squishFactor);
        state.inHoney = entity.isInHoney();
    }

    //[VanillaCopy] of SlimeOuterLayer. Necessary as mojang tightened the restrictions and hardcoded the textures. Thanks for that
    public static class HoneySlimeOuterLayer extends RenderLayer<HoneySlimeRenderState, SlimeModel> {
        private final SlimeModel model;

        public HoneySlimeOuterLayer(RenderLayerParent<HoneySlimeRenderState, SlimeModel> renderer, EntityModelSet modelSet) {
            super(renderer);
            this.model = new SlimeModel(modelSet.bakeLayer(ModelLayers.SLIME_OUTER));
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, HoneySlimeRenderState state, float yRot, float xRot) {
            boolean appearsGlowingWithInvisibility = state.appearsGlowing() && state.isInvisible;
            if (!state.isInvisible || appearsGlowingWithInvisibility) {
                Identifier texture = state.inHoney ? HONEY_TEXTURE : HONEYLESS_TEXTURE;
                int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
                if (appearsGlowingWithInvisibility) {
                    collector.order(1).submitModel(this.model, state, poseStack, RenderTypes.outline(texture), lightCoords, overlayCoords, state.outlineColor, null);
                } else {
                    collector.order(1).submitModel(this.model, state, poseStack, RenderTypes.entityTranslucent(texture), lightCoords, overlayCoords, state.outlineColor, null);
                }
            }
        }
    }
}

