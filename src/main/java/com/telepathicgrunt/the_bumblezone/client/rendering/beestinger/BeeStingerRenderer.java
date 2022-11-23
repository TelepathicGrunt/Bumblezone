package com.telepathicgrunt.the_bumblezone.client.rendering.beestinger;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.BeeStingerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class BeeStingerRenderer extends EntityRenderer<BeeStingerEntity> {
    public static final ResourceLocation BEE_STINGER_LOCATION = new ResourceLocation(Bumblezone.MODID, "textures/entity/bee_stinger.png");
    private final BeeStingerModel model;

    public BeeStingerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BeeStingerModel(context.bakeLayer(BeeStingerModel.LAYER_LOCATION));
    }

    @Override
    public void render(BeeStingerEntity beeStingerEntity, float float1, float float2, PoseStack poseStack, MultiBufferSource multiBufferSource, int int1) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(float2, beeStingerEntity.yRotO, beeStingerEntity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(float2, beeStingerEntity.xRotO, beeStingerEntity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, this.model.renderType(this.getTextureLocation(beeStingerEntity)), false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, int1, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(beeStingerEntity, float1, float2, poseStack, multiBufferSource, int1);
    }

    @Override
    public ResourceLocation getTextureLocation(BeeStingerEntity beeStingerEntity) {
        return BEE_STINGER_LOCATION;
    }
}