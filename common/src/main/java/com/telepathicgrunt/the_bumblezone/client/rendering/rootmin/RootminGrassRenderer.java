package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class RootminGrassRenderer extends RenderLayer<RootminEntity, RootminModel> {
    private static final ResourceLocation GRASS = new ResourceLocation(Bumblezone.MODID, "textures/entity/rootmin_grass.png");
    private final RootminModel model;
    private final RootminRenderer renderLayerParent;

    public RootminGrassRenderer(RootminRenderer renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new RootminModel(entityModelSet.bakeLayer(RootminModel.LAYER_LOCATION));
        this.renderLayerParent = renderLayerParent;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, RootminEntity entity, float f, float g, float partialTicks, float j, float k, float l) {

        poseStack.pushPose();
        renderGrassBodyTop(entity, partialTicks, poseStack, multiBufferSource, packedLight);
        poseStack.popPose();
    }

    private void renderGrassBodyTop(RootminEntity rootminEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {

        float belowEyeHeight;
        this.model.attackTime = renderLayerParent.getAttackAnim(rootminEntity, partialTicks);
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
        float l = renderLayerParent.getBob(rootminEntity, partialTicks);
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
        boolean bl = renderLayerParent.isBodyVisible(rootminEntity);
        boolean bl2 = !bl && !rootminEntity.isInvisibleTo(minecraft.player);
        boolean bl3 = minecraft.shouldEntityAppearGlowing(rootminEntity);
        RenderType renderType = this.getRenderTypeGrass(bl, bl2, bl3);
        if (renderType != null) {
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
            int p = LivingEntityRenderer.getOverlayCoords(rootminEntity, renderLayerParent.getWhiteOverlayProgress(rootminEntity, partialTicks));
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

                ((Model) this.model).renderToBuffer(
                        stack,
                        vertexConsumer,
                        packedLight,
                        p,
                        GeneralUtils.getRed(biomeColor) / 255f,
                        GeneralUtils.getGreen(biomeColor) / 255f,
                        GeneralUtils.getBlue(biomeColor) / 255f,
                        bl2 ? 0.15f : 1.0f);
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

    protected float xOffset(float f) {
        return f * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return GRASS;
    }

    protected EntityModel<RootminEntity> model() {
        return this.model;
    }
}