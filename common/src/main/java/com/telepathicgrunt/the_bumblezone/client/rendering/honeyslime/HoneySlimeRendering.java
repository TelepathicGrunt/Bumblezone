package com.telepathicgrunt.the_bumblezone.client.rendering.honeyslime;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HoneySlimeRendering extends MobRenderer<HoneySlimeEntity, SlimeModel<HoneySlimeEntity>> {
    private static final ResourceLocation HONEY_TEXTURE = new ResourceLocation(Bumblezone.MODID, "textures/entity/honey_slime.png");
    private static final ResourceLocation HONEYLESS_TEXTURE = new ResourceLocation(Bumblezone.MODID, "textures/entity/honey_slime_naked.png");

    public HoneySlimeRendering(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
        this.addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
    }

    @Override
    public void render(HoneySlimeEntity honeySlimeEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25F * (honeySlimeEntity.isBaby() ? 1f : 2f);
        super.render(honeySlimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    // unused. Dont ask how the scaling even works automatically
    @Override
    protected void scale(HoneySlimeEntity honeySlimeEntity, PoseStack matrixStack, float f) {
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0D, 0.001D, 0.0D);
        float mainScale = honeySlimeEntity.isBaby() ? 1f : 2f;
        float currentSquish = Mth.lerp(f, honeySlimeEntity.prevSquishFactor, honeySlimeEntity.squishFactor) / (mainScale * 0.5F + 1.0F);
        float scaledSquish = 1.0F / (currentSquish + 1.0F);
        matrixStack.scale(scaledSquish * mainScale, 1.0F / scaledSquish * mainScale, scaledSquish * mainScale);
    }

    @Override
    public ResourceLocation getTextureLocation(HoneySlimeEntity honeySlimeEntity) {
        return honeySlimeEntity.isInHoney() ? HONEY_TEXTURE : HONEYLESS_TEXTURE;
    }
}

