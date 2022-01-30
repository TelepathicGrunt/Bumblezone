package com.telepathicgrunt.the_bumblezone.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ThrownStingerSpearEntity;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class StingerSpearRenderer extends EntityRenderer<ThrownStingerSpearEntity> {
    public static final ResourceLocation STINGER_SPEAR_LOCATION = new ResourceLocation(Bumblezone.MODID, "textures/entity/stinger_spear.png");
    private final StingerSpearModel model;

    public StingerSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new StingerSpearModel(context.bakeLayer(StingerSpearModel.LAYER_LOCATION));
    }

    @Override
    public void render(ThrownStingerSpearEntity thrownStingerSpearEntity, float float1, float float2, PoseStack poseStack, MultiBufferSource multiBufferSource, int int1) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(float2, thrownStingerSpearEntity.yRotO, thrownStingerSpearEntity.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(float2, thrownStingerSpearEntity.xRotO, thrownStingerSpearEntity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, this.model.renderType(this.getTextureLocation(thrownStingerSpearEntity)), false, thrownStingerSpearEntity.isFoil());
        this.model.renderToBuffer(poseStack, vertexconsumer, int1, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(thrownStingerSpearEntity, float1, float2, poseStack, multiBufferSource, int1);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownStingerSpearEntity thrownStingerSpearEntity) {
        return STINGER_SPEAR_LOCATION;
    }
}