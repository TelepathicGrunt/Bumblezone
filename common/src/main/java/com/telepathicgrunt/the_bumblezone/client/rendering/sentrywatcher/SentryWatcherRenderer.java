package com.telepathicgrunt.the_bumblezone.client.rendering.sentrywatcher;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.SentryWatcherEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SentryWatcherRenderer<M extends EntityModel<SentryWatcherEntity>>
        extends EntityRenderer<SentryWatcherEntity>
        implements RenderLayerParent<SentryWatcherEntity, M>
{
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/sentry_watcher.png");
    protected SentryWatcherModel model;
    protected final List<RenderLayer<SentryWatcherEntity, M>> layers = Lists.newArrayList();

    public SentryWatcherRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SentryWatcherModel(context.bakeLayer(SentryWatcherModel.LAYER_LOCATION));
    }

    protected final boolean addLayer(RenderLayer<SentryWatcherEntity, M> renderLayer) {
        return this.layers.add(renderLayer);
    }

    @Override
    public M getModel() {
        return (M) this.model;
    }

    @Override
    public void render(SentryWatcherEntity sentryWatcherEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        float rotationLerp = Mth.lerp(g, sentryWatcherEntity.xRotO, sentryWatcherEntity.getXRot());
        poseStack.scale(1, 1, 1);
        poseStack.translate(0.0f, sentryWatcherEntity.getBoundingBox().getYsize() + 0.05f, 0.0f);
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0f - sentryWatcherEntity.getYRot()));
        poseStack.mulPose(Axis.XN.rotationDegrees(180.0f - sentryWatcherEntity.getXRot()));
        ((EntityModel)this.model).prepareMobModel(sentryWatcherEntity, 0, 0, g);
        ((EntityModel)this.model).setupAnim(sentryWatcherEntity, 0, 0, 0, 0, rotationLerp);
        Minecraft minecraft = Minecraft.getInstance();
        boolean glowing = minecraft.shouldEntityAppearGlowing(sentryWatcherEntity);
        RenderType renderType = this.getRenderType(sentryWatcherEntity, true, false, glowing);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            int overlayTint = OverlayTexture.pack(OverlayTexture.u(f), OverlayTexture.v(false));
            ((Model)this.model).renderToBuffer(poseStack, vertexConsumer, i, overlayTint, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        if (!sentryWatcherEntity.isSpectator()) {
            for (RenderLayer<SentryWatcherEntity, M> renderLayer : this.layers) {
                renderLayer.render(poseStack, multiBufferSource, i, sentryWatcherEntity, 0, 0, g, 0, 0, rotationLerp);
            }
        }
        poseStack.popPose();
        super.render(sentryWatcherEntity, f, g, poseStack, multiBufferSource, i);
    }

    @Nullable
    protected RenderType getRenderType(SentryWatcherEntity sentryWatcherEntity, boolean bodyVisible, boolean hidden, boolean glowing) {
        ResourceLocation resourceLocation = this.getTextureLocation(sentryWatcherEntity);
        if (bodyVisible) {
            return this.model.renderType(resourceLocation);
        }
        if (glowing) {
            return RenderType.outline(resourceLocation);
        }
        return null;
    }

    @Override
    public ResourceLocation getTextureLocation(SentryWatcherEntity sentryWatcherEntity) {
        return SKIN;
    }
}