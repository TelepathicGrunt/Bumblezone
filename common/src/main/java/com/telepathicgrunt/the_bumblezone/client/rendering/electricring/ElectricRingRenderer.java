package com.telepathicgrunt.the_bumblezone.client.rendering.electricring;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth.BeehemothModel;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ElectricRingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;

public class ElectricRingRenderer<M extends EntityModel<ElectricRingEntity>>
        extends EntityRenderer<ElectricRingEntity>
        implements RenderLayerParent<ElectricRingEntity, M>
{
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/electric_ring.png");
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
        float n;
        poseStack.pushPose();
        float m = Mth.lerp(g, ringEntity.xRotO, ringEntity.getXRot());

        float scale = Math.min((ringEntity.tickCount + g) / 20f, 1.0f);
        poseStack.scale(-scale, -scale, scale);
        poseStack.translate(0.0f, -1.0f - (1.5f - (scale * 1.5f)), 0.0f);
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0f - ringEntity.getYRot()));
        poseStack.mulPose(Axis.XN.rotationDegrees(180.0f - ringEntity.getXRot()));
        n = 0.0f;
        float o = 0.0f;
        ((EntityModel)this.model).prepareMobModel(ringEntity, o, n, g);
        ((EntityModel)this.model).setupAnim(ringEntity, o, n, 0, 0, m);
        Minecraft minecraft = Minecraft.getInstance();
        boolean bl = this.isBodyVisible(ringEntity);
        boolean bl2 = !bl && !ringEntity.isInvisibleTo(minecraft.player);
        boolean bl3 = minecraft.shouldEntityAppearGlowing(ringEntity);
        RenderType renderType = this.getRenderType(ringEntity, bl, bl2, bl3);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            ((Model)this.model).renderToBuffer(poseStack, vertexConsumer, i, 0, 1.0f, 1.0f, 1.0f, bl2 ? 0.15f : 1.0f);
        }
        if (!ringEntity.isSpectator()) {
            for (RenderLayer<ElectricRingEntity, M> renderLayer : this.layers) {
                renderLayer.render(poseStack, multiBufferSource, i, ringEntity, o, n, g, 0, 0, m);
            }
        }
        poseStack.popPose();
        super.render(ringEntity, f, g, poseStack, multiBufferSource, i);
    }

    @Nullable
    protected RenderType getRenderType(ElectricRingEntity ringEntity, boolean bl, boolean bl2, boolean bl3) {
        ResourceLocation resourceLocation = this.getTextureLocation(ringEntity);
        if (bl2) {
            return RenderType.itemEntityTranslucentCull(resourceLocation);
        }
        if (bl) {
            return this.model.renderType(resourceLocation);
        }
        if (bl3) {
            return RenderType.outline(resourceLocation);
        }
        return null;
    }

    protected boolean isBodyVisible(ElectricRingEntity ringEntity) {
        return !ringEntity.isInvisible();
    }

    @Override
    public ResourceLocation getTextureLocation(ElectricRingEntity ringEntity) {
        return SKIN;
    }
}