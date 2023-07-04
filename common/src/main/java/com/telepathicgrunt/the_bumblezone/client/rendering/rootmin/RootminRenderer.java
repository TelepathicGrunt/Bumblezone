package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;

import java.awt.*;

public class RootminRenderer extends MobRenderer<RootminEntity, RootminModel> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/rootmin.png");
    private static final ResourceLocation GRASS = new ResourceLocation(Bumblezone.MODID, "textures/entity/rootmin_grass.png");

    public RootminRenderer(EntityRendererProvider.Context context) {
        super(context, new RootminModel(context.bakeLayer(RootminModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new FlowerBlockLayer(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public void render(RootminEntity rootminEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        super.render(rootminEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        stack.popPose();

        stack.pushPose();
        renderGrassBodyTop(rootminEntity, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    private void renderGrassBodyTop(RootminEntity rootminEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        float belowEyeHeight;
        this.model.attackTime = this.getAttackAnim(rootminEntity, partialTicks);
        this.model.riding = rootminEntity.isPassenger();
        this.model.young = rootminEntity.isBaby();
        float h = Mth.rotLerp(partialTicks, rootminEntity.yBodyRotO, rootminEntity.yBodyRot);
        float j = Mth.rotLerp(partialTicks, rootminEntity.yHeadRotO, rootminEntity.yHeadRot);
        float k = j - h;
        if (rootminEntity.isPassenger() && rootminEntity.getVehicle() instanceof LivingEntity rootminEntity2) {
            h = Mth.rotLerp(partialTicks, rootminEntity2.yBodyRotO, rootminEntity2.yBodyRot);
            k = j - h;
            float l = Mth.wrapDegrees(k);
            if (l < -85.0f) {
                l = -85.0f;
            }
            if (l >= 85.0f) {
                l = 85.0f;
            }
            h = j - l;
            if (l * l > 2500.0f) {
                h += l * 0.2f;
            }
            k = j - h;
        }
        float m = Mth.lerp(partialTicks, rootminEntity.xRotO, rootminEntity.getXRot());
        if (LivingEntityRenderer.isEntityUpsideDown(rootminEntity)) {
            m *= -1.0f;
            k *= -1.0f;
        }
        float l = this.getBob(rootminEntity, partialTicks);
        this.setupRotations(rootminEntity, stack, l, h, partialTicks);
        stack.scale(-1.0f, -1.0f, 1.0f);
        this.scale(rootminEntity, stack, partialTicks);
        stack.translate(0.0f, -1.501f, 0.0f);
        belowEyeHeight = 0.0f;
        float o = 0.0f;
        if (!rootminEntity.isPassenger() && rootminEntity.isAlive()) {
            belowEyeHeight = rootminEntity.walkAnimation.speed(partialTicks);
            o = rootminEntity.walkAnimation.position(partialTicks);
            if (rootminEntity.isBaby()) {
                o *= 3.0f;
            }
            if (belowEyeHeight > 1.0f) {
                belowEyeHeight = 1.0f;
            }
        }
        this.model.prepareMobModel(rootminEntity, o, belowEyeHeight, partialTicks);
        this.model.setupAnim(rootminEntity, o, belowEyeHeight, l, k, m);
        Minecraft minecraft = Minecraft.getInstance();
        boolean bl = this.isBodyVisible(rootminEntity);
        boolean bl2 = !bl && !rootminEntity.isInvisibleTo(minecraft.player);
        boolean bl3 = minecraft.shouldEntityAppearGlowing(rootminEntity);
        RenderType renderType = this.getRenderTypeGrass(bl, bl2, bl3);
        if (renderType != null) {
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
            int p = LivingEntityRenderer.getOverlayCoords(rootminEntity, this.getWhiteOverlayProgress(rootminEntity, partialTicks));
            if (rootminEntity.hasCustomName() && "jeb_".equals(rootminEntity.getName().getString())) {
                int speed = 25;
                int offset = rootminEntity.tickCount / speed + rootminEntity.getId();
                int dyeColors = DyeColor.values().length;
                int firstDye = offset % dyeColors;
                int secondDye = (offset + 1) % dyeColors;
                float theColorThingy = ((float)(rootminEntity.tickCount % speed) + partialTicks) / 25.0f;
                float[] fs = Sheep.getColorArray(DyeColor.byId(firstDye));
                float[] gs = Sheep.getColorArray(DyeColor.byId(secondDye));
                float red = fs[0] * (1.0f - theColorThingy) + gs[0] * theColorThingy;
                float green = fs[1] * (1.0f - theColorThingy) + gs[1] * theColorThingy;
                float blue = fs[2] * (1.0f - theColorThingy) + gs[2] * theColorThingy;
                ((Model) this.model).renderToBuffer(stack, vertexConsumer, packedLight, p, red, green, blue, bl2 ? 0.15f : 1.0f);
            }
            else {
                int biomeColor = rootminEntity.level().getBlockTint(rootminEntity.blockPosition(), BiomeColors.GRASS_COLOR_RESOLVER);
                Color color = new Color(biomeColor);
                ((Model) this.model).renderToBuffer(stack, vertexConsumer, packedLight, p, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, bl2 ? 0.15f : 1.0f);
            }
        }
    }

    protected RenderType getRenderTypeGrass(boolean bl, boolean bl2, boolean bl3) {
        if (bl2) {
            return RenderType.itemEntityTranslucentCull(GRASS);
        }
        if (bl) {
            return this.model.renderType(GRASS);
        }
        if (bl3) {
            return RenderType.outline(GRASS);
        }
        return null;
    }

    @Override
    public ResourceLocation getTextureLocation(RootminEntity rootminEntity) {
        return SKIN;
    }


    public static class FlowerBlockLayer extends RenderLayer<RootminEntity, RootminModel> {
        private final BlockRenderDispatcher blockRenderer;

        public FlowerBlockLayer(RenderLayerParent<RootminEntity, RootminModel> renderLayerParent, BlockRenderDispatcher blockRenderDispatcher) {
            super(renderLayerParent);
            this.blockRenderer = blockRenderDispatcher;
        }

        @Override
        public void render(PoseStack stack, MultiBufferSource multiBufferSource, int i, RootminEntity rootminEntity, float f, float g, float h, float j, float k, float l) {
            BlockState blockState = rootminEntity.getFlowerBlock();
            if (blockState == null) {
                return;
            }

            ModelPart rootModel = this.getParentModel().root();
            ModelPart bodyModel = rootModel.getChild("body");
            poseStack.pushPose();
            rootModel.translateAndRotate(poseStack);
            bodyModel.translateAndRotate(poseStack);
            poseStack.translate(-0.5f, -15/16f, -0.5f);
            poseStack.scale(1,-1,1);
            this.blockRenderer.renderSingleBlock(blockState, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}