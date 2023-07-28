package com.telepathicgrunt.the_bumblezone.client.rendering.boundlesscrystal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.living.BoundlessCrystalEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

import java.awt.*;

public class BoundlessCrystalRenderer extends LivingEntityRenderer<BoundlessCrystalEntity, BoundlessCrystalModel<BoundlessCrystalEntity>> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/boundless_crystal.png");

    public BoundlessCrystalRenderer(EntityRendererProvider.Context context) {
        super(context, new BoundlessCrystalModel<>(context.bakeLayer(BoundlessCrystalModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public void render(BoundlessCrystalEntity boundlessCrystalEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        renderLiving(boundlessCrystalEntity, entityYaw, partialTicks, stack, buffer, packedLight);
        renderHealth(
                boundlessCrystalEntity,
                Component.literal("Health: " + boundlessCrystalEntity.getHealth()),
                stack,
                buffer,
                LightTexture.FULL_BRIGHT);
    }

    public void renderLiving(BoundlessCrystalEntity boundlessCrystalEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        poseStack.pushPose();
        packedLight = LightTexture.FULL_BRIGHT;
        this.model.attackTime = this.getAttackAnim(boundlessCrystalEntity, g);
        this.model.riding = boundlessCrystalEntity.isPassenger();
        this.model.young = boundlessCrystalEntity.isBaby();
        float h = Mth.rotLerp(g, boundlessCrystalEntity.yBodyRotO, boundlessCrystalEntity.yBodyRot);
        float j = Mth.rotLerp(g, boundlessCrystalEntity.yHeadRotO, boundlessCrystalEntity.yHeadRot);
        float k = j - h;

        float l = this.getBob(boundlessCrystalEntity, g);
        float m = Mth.lerp(g, boundlessCrystalEntity.xRotO, boundlessCrystalEntity.getXRot());
        this.setupRotations(boundlessCrystalEntity, poseStack, l, h, g);
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        this.scale(boundlessCrystalEntity, poseStack, g);
        poseStack.translate(0.0f, -1.501f, 0.0f);
        float n = 0.0f;
        float o = 0.0f;
        if (!boundlessCrystalEntity.isPassenger() && boundlessCrystalEntity.isAlive()) {
            n = boundlessCrystalEntity.walkAnimation.speed(g);
            o = boundlessCrystalEntity.walkAnimation.position(g);
            if (n > 1.0f) {
                n = 1.0f;
            }
        }

        this.model.prepareMobModel(boundlessCrystalEntity, o, n, g);
        ((EntityModel)this.model).setupAnim(boundlessCrystalEntity, o, n, l, k, m);
        Minecraft minecraft = Minecraft.getInstance();
        boolean bl = this.isBodyVisible(boundlessCrystalEntity);
        boolean bl2 = !bl && !boundlessCrystalEntity.isInvisibleTo(minecraft.player);
        boolean bl3 = minecraft.shouldEntityAppearGlowing(boundlessCrystalEntity);

        RenderType renderType = this.getRenderType(boundlessCrystalEntity, bl, bl2, bl3);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(boundlessCrystalEntity, this.getWhiteOverlayProgress(boundlessCrystalEntity, g));

            float currentHealthState = Math.min(1, (Math.min(1, boundlessCrystalEntity.getHealth() / boundlessCrystalEntity.getMaxHealth()) * 0.35f) + 0.7f);

            float red = currentHealthState;
            float green = currentHealthState;
            float blue = currentHealthState;

            if (boundlessCrystalEntity.getTicksFrozen() > 0) {
                red *= 0.75f;
                green *= 0.75f;
            }
            if (boundlessCrystalEntity.isOnFire()) {
                green *= 0.75f;
                blue *= 0.75f;
            }
            for(MobEffect mobEffect : boundlessCrystalEntity.getActiveEffectsMap().keySet()) {
                if (mobEffect == MobEffects.POISON) {
                    red *= 0.75f;
                    blue *= 0.75f;
                }
                else if (mobEffect == MobEffects.WITHER) {
                    red *= 0.5f;
                    green *= 0.5f;
                    blue *= 0.5f;
                }
                else if (!mobEffect.isInstantenous() && !mobEffect.isBeneficial()) {
                    String namespace = BuiltInRegistries.MOB_EFFECT.getKey(mobEffect).getNamespace();
                    if (!namespace.equals("minecraft") && !namespace.equals(Bumblezone.MODID)) {
                        Color color = new Color(mobEffect.getColor());
                        red = (red + (color.getRed() / 255f)) / 2f;
                        green = (green + (color.getGreen() / 255f)) / 2f;
                        blue = (blue + (color.getBlue() / 255f)) / 2f;
                    }
                }
            }

            ((Model)this.model).renderToBuffer(poseStack, vertexConsumer, packedLight, overlayCoords, red, green, blue, bl2 ? 0.15f : 1.0f);
        }

        if (!boundlessCrystalEntity.isSpectator()) {
            for (RenderLayer<BoundlessCrystalEntity, ?> renderLayer : this.layers) {
                renderLayer.render(poseStack, multiBufferSource, packedLight, boundlessCrystalEntity, o, n, g, l, k, m);
            }
        }

        poseStack.popPose();
    }

    protected void renderHealth(Entity entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (this.entityRenderDispatcher.distanceToSqr(entity) > 100.0) {
            return;
        }
        float f = entity.getNameTagOffsetY() + 0.5F;
        poseStack.pushPose();
        poseStack.translate(0.0f, f, 0.0f);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(-0.025f, -0.025f, 0.025f);
        Matrix4f matrix4f = poseStack.last().pose();
        float g = Minecraft.getInstance().options.getBackgroundOpacity(0.25f);
        int k = (int)(g * 255.0f) << 24;
        Font font = this.getFont();
        float h = -font.width(component) / 2F;
        font.drawInBatch(component, h, 0, 0x20FFFFFF, false, matrix4f, multiBufferSource, Font.DisplayMode.NORMAL, k, packedLight);
        font.drawInBatch(component, h, 0, -1, false, matrix4f, multiBufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BoundlessCrystalEntity boundlessCrystalEntity) {
        return SKIN;
    }

    @Override
    protected boolean shouldShowName(BoundlessCrystalEntity entity) {
        return false;
    }
}