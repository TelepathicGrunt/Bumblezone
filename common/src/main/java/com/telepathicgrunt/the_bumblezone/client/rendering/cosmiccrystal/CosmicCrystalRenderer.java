package com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalEntity;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.HashSet;

public class CosmicCrystalRenderer extends LivingEntityRenderer<CosmicCrystalEntity, CosmicCrystalModel<CosmicCrystalEntity>> {
    private static final ResourceLocation SKIN = new ResourceLocation(Bumblezone.MODID, "textures/entity/cosmic_crystal.png");
    private static final ResourceLocation LASER_LOCATION = new ResourceLocation(Bumblezone.MODID, "textures/entity/cosmic_crystal_laser.png");

    public CosmicCrystalRenderer(EntityRendererProvider.Context context) {
        super(context, new CosmicCrystalModel<>(context.bakeLayer(CosmicCrystalModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public void render(CosmicCrystalEntity cosmicCrystalEntity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        renderLiving(cosmicCrystalEntity, entityYaw, partialTicks, stack, buffer, LightTexture.FULL_BRIGHT);

//        renderHealth(
//                cosmicCrystalEntity,
//                Component.literal("Health: " + cosmicCrystalEntity.getHealth()),
//                stack,
//                buffer,
//                LightTexture.FULL_BRIGHT);

        renderLaser(
                cosmicCrystalEntity,
                entityYaw,
                partialTicks,
                stack,
                buffer,
                LightTexture.FULL_BRIGHT);
    }

    public void renderLiving(CosmicCrystalEntity cosmicCrystalEntity, float f, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        poseStack.pushPose();
        this.model.attackTime = this.getAttackAnim(cosmicCrystalEntity, partialTick);
        this.model.riding = cosmicCrystalEntity.isPassenger();
        this.model.young = cosmicCrystalEntity.isBaby();
        float lerpedXRot = Mth.rotLerp(partialTick, cosmicCrystalEntity.xRotO, cosmicCrystalEntity.getXRot());
        float lerpedYRot = Mth.rotLerp(partialTick, cosmicCrystalEntity.yRotO, cosmicCrystalEntity.getYRot());
        float rotAxisDiff = lerpedYRot - lerpedXRot;

        float xRot = Mth.lerp(partialTick, cosmicCrystalEntity.xRotO, cosmicCrystalEntity.getXRot());
        this.setupRotations(cosmicCrystalEntity, poseStack, lerpedXRot, lerpedYRot, partialTick);
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        this.scale(cosmicCrystalEntity, poseStack, partialTick);
        poseStack.translate(0.0f, -1.501f, 0.0f);

        float n = 0.0f;
        float o = 0.0f;
        if (!cosmicCrystalEntity.isPassenger() && cosmicCrystalEntity.isAlive()) {
            n = cosmicCrystalEntity.walkAnimation.speed(partialTick);
            o = cosmicCrystalEntity.walkAnimation.position(partialTick);
            if (n > 1.0f) {
                n = 1.0f;
            }
        }

        float tickTimeWithPartial = this.getBob(cosmicCrystalEntity, partialTick);

        this.model.prepareMobModel(cosmicCrystalEntity, o, n, partialTick);
        this.model.setupAnim(cosmicCrystalEntity, o, n, tickTimeWithPartial, rotAxisDiff, xRot);

        Minecraft minecraft = Minecraft.getInstance();
        boolean bl = this.isBodyVisible(cosmicCrystalEntity);
        boolean bl2 = !bl && !cosmicCrystalEntity.isInvisibleTo(minecraft.player);
        boolean bl3 = minecraft.shouldEntityAppearGlowing(cosmicCrystalEntity);

        RenderType renderType = this.getRenderType(cosmicCrystalEntity, bl, bl2, bl3);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(cosmicCrystalEntity, this.getWhiteOverlayProgress(cosmicCrystalEntity, partialTick));

            float currentHealthState = Math.min(1, (Math.min(1, cosmicCrystalEntity.getHealth() / cosmicCrystalEntity.getMaxHealth()) * 0.45f) + 0.6f);

            float red = currentHealthState;
            float green = currentHealthState;
            float blue = currentHealthState;

            if (cosmicCrystalEntity.getTicksFrozen() > 0) {
                red *= 0.75f;
                green *= 0.75f;
            }
            if (cosmicCrystalEntity.isOnFire()) {
                green *= 0.75f;
                blue *= 0.75f;
            }
            for(MobEffect mobEffect : new HashSet<>(cosmicCrystalEntity.getActiveEffectsMap().keySet())) {
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
                        red = (red + (GeneralUtils.getRed(mobEffect.getColor()) / 255f)) / 2f;
                        green = (green + (GeneralUtils.getGreen(mobEffect.getColor()) / 255f)) / 2f;
                        blue = (blue + (GeneralUtils.getBlue(mobEffect.getColor()) / 255f)) / 2f;
                    }
                }
            }

            ((Model)this.model).renderToBuffer(poseStack, vertexConsumer, packedLight, overlayCoords, red, green, blue, bl2 ? 0.15f : 1.0f);
        }

        if (!cosmicCrystalEntity.isSpectator()) {
            for (RenderLayer<CosmicCrystalEntity, ?> renderLayer : this.layers) {
                renderLayer.render(poseStack, multiBufferSource, packedLight, cosmicCrystalEntity, o, n, partialTick, tickTimeWithPartial, rotAxisDiff, xRot);
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

    protected void setupRotations(CosmicCrystalEntity cosmicCrystalEntity, PoseStack poseStack, float lerpedXRot, float lerpedYRot, float partialTick) {
        if (this.isShaking(cosmicCrystalEntity)) {
            lerpedXRot += (float)(Math.cos((double) cosmicCrystalEntity.tickCount * 3.25) * Math.PI * 0.4000000059604645);
        }

        poseStack.translate(0, 1, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - lerpedYRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90 - lerpedXRot));
        poseStack.translate(0, -1, 0);
    }


    public void renderLaser(CosmicCrystalEntity cosmicCrystalEntity, float f, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (cosmicCrystalEntity.isLaserFiring()) {
            float totalTickTime = cosmicCrystalEntity.tickCount + partialTick;

            float colorSpeed = -5;
            long uniqueValue = cosmicCrystalEntity.getUUID().getLeastSignificantBits() % 1000000;
            float radianColor = ((totalTickTime * colorSpeed + uniqueValue) % 360) * Mth.DEG_TO_RAD;
            int baseBrightness = 200;
            int colorStrength = 55;

            float redSin = Mth.sin(radianColor);
            float greenSin = Mth.sin(radianColor + 30);
            float blueSin = Mth.sin(radianColor + 60);
            int red = baseBrightness + (int) (redSin * colorStrength);
            int green = baseBrightness + (int) (greenSin * colorStrength);
            int blue = baseBrightness + (int) (blueSin * colorStrength);

            float redSin2 = Mth.cos(radianColor);
            float greenSin2 = Mth.cos(radianColor + 30);
            float blueSin2 = Mth.cos(radianColor + 60);
            int red2 = baseBrightness + (int) (redSin2 * colorStrength);
            int green2 = baseBrightness + (int) (greenSin2 * colorStrength);
            int blue2 = baseBrightness + (int) (blueSin2 * colorStrength);

            float eyeY = cosmicCrystalEntity.getEyeHeight();
            poseStack.pushPose();

            Vec3 startPos = cosmicCrystalEntity.getEyePosition();
            Vec3 prevLookAngle = cosmicCrystalEntity.prevLookAngle;
            Vec3 lookAngle = cosmicCrystalEntity.getLookAngle();


            Vec3 lerpedLook = new Vec3(
                Mth.lerp(partialTick, prevLookAngle.x(), lookAngle.x()),
                Mth.lerp(partialTick, prevLookAngle.y(), lookAngle.y()),
                Mth.lerp(partialTick, prevLookAngle.z(), lookAngle.z())
            );
            Vec3 endPos = lerpedLook.scale(50).add(startPos);

            HitResult hitResult = cosmicCrystalEntity.level()
                    .clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, cosmicCrystalEntity));

            if (hitResult.getType() != HitResult.Type.MISS) {
                endPos = hitResult.getLocation().subtract(lookAngle);
            }

            Vec3 vectToTarget = endPos.subtract(startPos);

            float laserLength = (float) vectToTarget.length() - 0.01f;
            vectToTarget = vectToTarget.normalize();
            float n = (float)Math.acos(vectToTarget.y);
            float o = (float)Math.atan2(vectToTarget.z, vectToTarget.x);
            poseStack.translate(lookAngle.x(), eyeY + lookAngle.y(), lookAngle.z());
            poseStack.mulPose(Axis.YP.rotationDegrees((1.5707964f - o) * 57.295776f));
            poseStack.mulPose(Axis.XP.rotationDegrees(n * 57.295776f));
            float q = totalTickTime * 0.05f * -1.5f;
            float v = 0.2f;
            float w2 = 0.5f;
            float z5 = Mth.sin(q + 2.3561945f) * w2;
            float x7 = Mth.cos(q + 2.3561945f) * w2;
            float z9 = Mth.cos(q + 0.7853982f) * w2;
            float z6 = Mth.sin(q + 0.7853982f) * w2;
            float x6 = Mth.cos(q + 3.926991f) * w2;
            float z8 = Mth.sin(q + 3.926991f) * w2;
            float x5 = Mth.cos(q + 5.4977875f) * w2;
            float z7 = Mth.sin(q + 5.4977875f) * w2;

            float x1 = Mth.cos(q + (float)Math.PI) * v;
            float z1 = Mth.sin(q + (float)Math.PI) * v;
            float x2 = Mth.cos(q + 0.0f) * v;
            float z2 = Mth.sin(q + 0.0f) * v;
            float x3 = Mth.cos(q + 1.5707964f) * v;
            float z3 = Mth.sin(q + 1.5707964f) * v;
            float x4 = Mth.cos(q + 4.712389f) * v;
            float z4 = Mth.sin(q + 4.712389f) * v;
            float y1 = laserLength;
            float y2 = 0.0f;
            float ux1 = 0.4999f;
            float ux2 = 0.0f;

            float uv2 = -1.0f + (totalTickTime * -0.2f % 1.0f);
            float uv1 = laserLength * 2.5f + uv2;
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(BumblezoneClient.ENTITY_CUTOUT_EMISSIVE_RENDER_TYPE.apply(LASER_LOCATION));
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix4f = pose.pose();
            Matrix3f matrix3f = pose.normal();
            vertex(vertexConsumer, matrix4f, matrix3f, x1, y1, z1, red2, green2, blue2, ux1, uv1);
            vertex(vertexConsumer, matrix4f, matrix3f, x1, y2, z1, red, green, blue, ux1, uv2);
            vertex(vertexConsumer, matrix4f, matrix3f, x2, y2, z2, red, green, blue, ux2, uv2);
            vertex(vertexConsumer, matrix4f, matrix3f, x2, y1, z2, red2, green2, blue2, ux2, uv1);
            vertex(vertexConsumer, matrix4f, matrix3f, x3, y1, z3, red2, green2, blue2, ux1, uv1);
            vertex(vertexConsumer, matrix4f, matrix3f, x3, y2, z3, red, green, blue, ux1, uv2);
            vertex(vertexConsumer, matrix4f, matrix3f, x4, y2, z4, red, green, blue, ux2, uv2);
            vertex(vertexConsumer, matrix4f, matrix3f, x4, y1, z4, red2, green2, blue2, ux2, uv1);
            float as = 0.0f;
            if (cosmicCrystalEntity.tickCount % 4 < 2) {
                as = 0.5f;
            }
            vertex(vertexConsumer, matrix4f, matrix3f, x7, y1, z5, red2, green2, blue2, 0.5f, as + 0.5f);
            vertex(vertexConsumer, matrix4f, matrix3f, z9, y1, z6, red2, green2, blue2, 1.0f, as + 0.5f);
            vertex(vertexConsumer, matrix4f, matrix3f, x5, y1, z7, red2, green2, blue2, 1.0f, as);
            vertex(vertexConsumer, matrix4f, matrix3f, x6, y1, z8, red2, green2, blue2, 0.5f, as);
            poseStack.popPose();

            laserScreenShake(cosmicCrystalEntity, endPos);
        }
    }

    private static void laserScreenShake(CosmicCrystalEntity cosmicCrystalEntity, Vec3 endPos) {
        Entity camera = Minecraft.getInstance().getCameraEntity();
        if (camera != null) {
            double distance1 = cosmicCrystalEntity.position().distanceTo(camera.position());
            double distance2 = endPos.distanceTo(camera.position());
            double minDistance = Math.min(distance1, distance2);
            double threshold = 10;

            if (minDistance <= threshold) {
                double percentageToCenter = 1 - (minDistance / threshold);

                double spinSlowdown = 0.15d + (0.3d * (1 - percentageToCenter * percentageToCenter));
                float intensity = (float) (0.175d * percentageToCenter * percentageToCenter * percentageToCenter);
                double currentMillisecond = System.currentTimeMillis() % (360 * spinSlowdown);
                double degrees = (currentMillisecond / spinSlowdown);
                float angle = (float) (degrees * Mth.DEG_TO_RAD);
                camera.setYRot(camera.getYRot() + (Mth.sin(angle) * intensity));
            }
        }
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, int red, int green, int blue, float ux, float uz) {
        vertexConsumer
                .vertex(matrix4f, x, y, z)
                .color(red, green, blue, 255)
                .uv(ux, uz)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(0xF000F0)
                .normal(matrix3f, 0.0f, 1.0f, 0.0f)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(CosmicCrystalEntity cosmicCrystalEntity) {
        return SKIN;
    }

    @Override
    protected boolean shouldShowName(CosmicCrystalEntity entity) {
        return false;
    }
}