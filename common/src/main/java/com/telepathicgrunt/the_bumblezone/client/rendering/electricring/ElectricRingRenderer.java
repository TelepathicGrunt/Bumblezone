package com.telepathicgrunt.the_bumblezone.client.rendering.electricring;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ElectricRingEntity;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class ElectricRingRenderer<M extends EntityModel<ElectricRingEntity>>
        extends EntityRenderer<ElectricRingEntity>
        implements RenderLayerParent<ElectricRingEntity, M>
{
    private static final ResourceLocation SKIN_1 = new ResourceLocation(Bumblezone.MODID, "textures/entity/electric_ring/ring_1.png");
    protected ElectricRingModel<ElectricRingEntity> model;
    protected final List<RenderLayer<ElectricRingEntity, M>> layers = Lists.newArrayList();

    public ElectricRingRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ElectricRingModel<>(context.bakeLayer(ElectricRingModel.LAYER_LOCATION));
    }

    protected final boolean addLayer(RenderLayer<ElectricRingEntity, M> renderLayer) {
        return this.layers.add(renderLayer);
    }

    @Override
    public M getModel() {
        return (M) this.model;
    }

    @Override
    public void render(ElectricRingEntity ringEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        i = 255;

        poseStack.pushPose();
        float rotationLerp = Mth.lerp(g, ringEntity.xRotO, ringEntity.getXRot());
        float scale;
        if (ringEntity.disappearingTime >= 0) {
            scale = Math.min((ringEntity.disappearingTime - g) / ElectricRingEntity.DISAPPERING_TIMESPAN, 1.0f);
        }
        else {
            scale = Math.min((ringEntity.tickCount + g) / ElectricRingEntity.APPERING_TIMESPAN, 1.0f);
        }

        poseStack.scale(-scale, -scale, scale);
        poseStack.translate(0.0f, -1.0f - (1.5f - (scale * 1.5f)), 0.0f);
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0f - ringEntity.getYRot()));
        poseStack.mulPose(Axis.XN.rotationDegrees(180.0f - ringEntity.getXRot()));
        poseStack.mulPose(Axis.ZN.rotationDegrees((ringEntity.tickCount + g) * 5f % 360));
        ((EntityModel)this.model).prepareMobModel(ringEntity, 0, 0, g);
        ((EntityModel)this.model).setupAnim(ringEntity, 0, 0, 0, 0, rotationLerp);
        Minecraft minecraft = Minecraft.getInstance();
        boolean bodyVisible = this.isBodyVisible(ringEntity);
        boolean hidden = !bodyVisible && !ringEntity.isInvisibleTo(minecraft.player);
        boolean glowing = minecraft.shouldEntityAppearGlowing(ringEntity);
        RenderType renderType = this.getRenderType(ringEntity, bodyVisible, hidden, glowing);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            ((Model)this.model).renderToBuffer(poseStack, vertexConsumer, i, 0, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        if (!ringEntity.isSpectator()) {
            for (RenderLayer<ElectricRingEntity, M> renderLayer : this.layers) {
                renderLayer.render(poseStack, multiBufferSource, i, ringEntity, 0, 0, g, 0, 0, rotationLerp);
            }
        }
        poseStack.popPose();
        super.render(ringEntity, f, g, poseStack, multiBufferSource, i);
    }

    @Nullable
    protected RenderType getRenderType(ElectricRingEntity ringEntity, boolean bodyVisible, boolean hidden, boolean glowing) {
        ResourceLocation resourceLocation = this.getTextureLocation(ringEntity);
        if (bodyVisible) {
            return this.model.renderType(resourceLocation);
        }
        if (glowing) {
            return RenderType.outline(resourceLocation);
        }
        return null;
    }

    protected boolean isBodyVisible(ElectricRingEntity ringEntity) {
        return !ringEntity.isInvisible();
    }

    @Override
    public ResourceLocation getTextureLocation(ElectricRingEntity ringEntity) {
        return SKIN_1;
    }
}