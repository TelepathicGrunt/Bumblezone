package com.telepathicgrunt.the_bumblezone.client.rendering.honeycrystalshard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import org.joml.Vector3f;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.HoneyCrystalShardEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HoneyCrystalShardRenderer extends EntityRenderer<HoneyCrystalShardEntity> {
    public static final ResourceLocation HONEY_CRYSTAL_SHARD_LOCATION = new ResourceLocation(Bumblezone.MODID, "textures/entity/honey_crystal_shard.png");
    private final HoneyCrystalShardModel model;

    public HoneyCrystalShardRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new HoneyCrystalShardModel(context.bakeLayer(HoneyCrystalShardModel.LAYER_LOCATION));
    }

    @Override
    public void render(HoneyCrystalShardEntity honeyCrystalShardEntity, float float1, float float2, PoseStack poseStack, MultiBufferSource multiBufferSource, int int1) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(float2, honeyCrystalShardEntity.yRotO, honeyCrystalShardEntity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(float2, honeyCrystalShardEntity.xRotO, honeyCrystalShardEntity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, this.model.renderType(this.getTextureLocation(honeyCrystalShardEntity)), false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, int1, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(honeyCrystalShardEntity, float1, float2, poseStack, multiBufferSource, int1);
    }

    @Override
    public ResourceLocation getTextureLocation(HoneyCrystalShardEntity honeyCrystalShardEntity) {
        return HONEY_CRYSTAL_SHARD_LOCATION;
    }
}