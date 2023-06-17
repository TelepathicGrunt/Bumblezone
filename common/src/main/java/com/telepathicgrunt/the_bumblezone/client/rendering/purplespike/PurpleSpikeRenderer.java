package com.telepathicgrunt.the_bumblezone.client.rendering.purplespike;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.electricring.ElectricRingModel;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.ElectricRingEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PurpleSpikeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PurpleSpikeRenderer<M extends EntityModel<PurpleSpikeEntity>>
        extends EntityRenderer<PurpleSpikeEntity>
        implements RenderLayerParent<PurpleSpikeEntity, M>
{
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/purple_spike.png");
    protected PurpleSpikeModel<PurpleSpikeEntity> model;
    protected final List<RenderLayer<PurpleSpikeEntity, M>> layers = Lists.newArrayList();

    public PurpleSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PurpleSpikeModel<>(context.bakeLayer(PurpleSpikeModel.LAYER_LOCATION));
    }

    protected final boolean addLayer(RenderLayer<PurpleSpikeEntity, M> renderLayer) {
        return this.layers.add(renderLayer);
    }

    @Override
    public M getModel() {
        return (M) this.model;
    }

    @Override
    public void render(PurpleSpikeEntity ringEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        float n;
        poseStack.pushPose();
        float m = Mth.lerp(g, ringEntity.xRotO, ringEntity.getXRot());

        poseStack.scale(1.0f, 1.0f, 1.0f);
        poseStack.translate(0.0f, 1.5f, 0.0f);
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
            for (RenderLayer<PurpleSpikeEntity, M> renderLayer : this.layers) {
                renderLayer.render(poseStack, multiBufferSource, i, ringEntity, o, n, g, 0, 0, m);
            }
        }
        poseStack.popPose();
        super.render(ringEntity, f, g, poseStack, multiBufferSource, i);
    }

    @Nullable
    protected RenderType getRenderType(PurpleSpikeEntity ringEntity, boolean bl, boolean bl2, boolean bl3) {
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

    protected boolean isBodyVisible(PurpleSpikeEntity ringEntity) {
        return !ringEntity.isInvisible();
    }

    @Override
    public ResourceLocation getTextureLocation(PurpleSpikeEntity ringEntity) {
        return SKIN;
    }
}