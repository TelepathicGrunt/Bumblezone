package com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalEntity;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalState;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Predicate;

public class CosmicCrystalRenderer extends LivingEntityRenderer<CosmicCrystalEntity, CosmicCrystalRenderState, CosmicCrystalModel> {
    private static final Identifier SKIN = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/cosmic_crystal.png");
    private static final Identifier LASER_LOCATION = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/entity/cosmic_crystal_laser.png");

    public CosmicCrystalRenderer(EntityRendererProvider.Context context) {
        super(context, new CosmicCrystalModel(context.bakeLayer(CosmicCrystalModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new CosmicCrystalShieldRenderer(this));
    }

    @Override
    public CosmicCrystalRenderState createRenderState() {
        return new CosmicCrystalRenderState();
    }

    @Override
    public void extractRenderState(CosmicCrystalEntity entity, CosmicCrystalRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.crystalState = entity.getCosmicCrystalState();
        state.firing = entity.isLaserFiring();
        state.shielded = entity.getShield();
        state.currentStateTimeTick = entity.currentStateTimeTick;
        state.laserStartDelay = entity.getLaserStartDelay();
        state.laserFireStartTime = entity.getLaserFireStartTime();

        state.health = entity.getHealth();
        state.onFire = entity.isOnFire();
        state.freezing = entity.getTicksFrozen() > 0;
        state.currentHealthState = Math.min(1, (Math.min(1, entity.getHealth() / entity.getMaxHealth()) * 0.45f) + 0.6f);
        state.activeEffects = new HashSet<>(entity.getActiveEffectsMap().keySet());

        //laser shenanigans
        if(state.firing) {
            var laserPositions = entity.getLaserPositions(partialTick);
            Vec3 vectToTarget = laserPositions.getSecond().subtract(laserPositions.getFirst());

            state.laser.uniqueValue = entity.getUUID().getLeastSignificantBits() % 1000000;
            state.laser.lookAngle = entity.getViewVector(partialTick);
            state.laser.laserLength = (float) vectToTarget.length() - 0.01f;
            state.laser.vecToTarget = vectToTarget.normalize();
            state.laser.positions = laserPositions;
        }
    }

    @Override
    public void submit(CosmicCrystalRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
//        this.renderHealth(state, Component.literal("Health: " + state.health), poseStack, collector, camera);
        this.renderLaser(state, poseStack, collector, camera);
    }

    @Override
    protected int getModelTint(CosmicCrystalRenderState state) {
        float red = state.currentHealthState;
        float green = state.currentHealthState;
        float blue = state.currentHealthState;

        if (state.freezing) {
            red *= 0.75f;
            green *= 0.75f;
        }
        if (state.onFire) {
            green *= 0.75f;
            blue *= 0.75f;
        }
        for (Holder<MobEffect> mobEffect : state.activeEffects) {
            if (mobEffect.is(MobEffects.POISON)) {
                red *= 0.75f;
                blue *= 0.75f;
            }
            else if (mobEffect.is(MobEffects.WITHER)) {
                red *= 0.5f;
                green *= 0.5f;
                blue *= 0.5f;
            }
            else if (!mobEffect.value().isInstantenous() && !mobEffect.value().isBeneficial()) {
                String namespace = mobEffect.unwrapKey().get().identifier().getNamespace();
                if (!namespace.equals("minecraft") && !namespace.equals(Bumblezone.MODID)) {
                    red = (red + (GeneralUtils.getRed(mobEffect.value().getColor()) / 255f)) / 2f;
                    green = (green + (GeneralUtils.getGreen(mobEffect.value().getColor()) / 255f)) / 2f;
                    blue = (blue + (GeneralUtils.getBlue(mobEffect.value().getColor()) / 255f)) / 2f;
                }
            }
        }
        return ARGB.colorFromFloat(1.0F, red, green, blue);
    }

    protected void renderHealth(CosmicCrystalRenderState state, Component component, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.distanceToCameraSq > 100.0) {
            return;
        }
        float f = state.boundingBoxHeight + 1F;
        poseStack.pushPose();
        poseStack.translate(0.0f, f, 0.0f);
        poseStack.mulPose(camera.orientation);
        poseStack.scale(-0.025f, -0.025f, 0.025f);
        float g = Minecraft.getInstance().options.getBackgroundOpacity(0.25f);
        int k = (int)(g * 255.0f) << 24;
        Font font = this.getFont();
        float h = -font.width(component) / 2F;
        collector.submitText(poseStack, h, 0, component.getVisualOrderText(), false, Font.DisplayMode.NORMAL, state.lightCoords, 0x20FFFFFF, k, state.outlineColor);
        collector.submitText(poseStack, h, 0, component.getVisualOrderText(), false, Font.DisplayMode.NORMAL, state.lightCoords, -1, 0, state.outlineColor);
        poseStack.popPose();
    }

    @Override
    protected void setupRotations(CosmicCrystalRenderState state, PoseStack poseStack, float yRot, float scale) {
        float xRot = state.xRot;
        if (this.isShaking(state)) {
            xRot += (float)(Math.cos(Mth.floor(state.ageInTicks) * 3.25) * Mth.PI * 0.4F);
        }

        poseStack.translate(0, 1, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90 - xRot));
        poseStack.translate(0, -1, 0);
    }


    public void renderLaser(CosmicCrystalRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.firing) {
            float colorSpeed = -5;
            long uniqueValue = state.laser.uniqueValue;
            float radianColor = ((state.ageInTicks * colorSpeed + uniqueValue) % 360) * Mth.DEG_TO_RAD;
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

            float eyeY = state.eyeHeight;
            if (state.crystalState == CosmicCrystalState.SWEEP_LASER || state.crystalState == CosmicCrystalState.TRACKING_LASER) {
                eyeY = 1;
            }
            poseStack.pushPose();

            float n = (float)Math.acos(state.laser.vecToTarget.y);
            float o = (float)Mth.atan2(state.laser.vecToTarget.z, state.laser.vecToTarget.x);
            poseStack.translate(state.laser.lookAngle.x(), eyeY + state.laser.lookAngle.y(), state.laser.lookAngle.z());
            poseStack.mulPose(Axis.YP.rotation(Mth.HALF_PI - o));
            poseStack.mulPose(Axis.XP.rotation(n));
            float q = state.ageInTicks * 0.05f * -1.5f;
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

            float x1 = Mth.cos(q + Mth.PI) * v;
            float z1 = Mth.sin(q + Mth.PI) * v;
            float x2 = Mth.cos(q + 0.0f) * v;
            float z2 = Mth.sin(q + 0.0f) * v;
            float x3 = Mth.cos(q + Mth.HALF_PI) * v;
            float z3 = Mth.sin(q + Mth.HALF_PI) * v;
            float x4 = Mth.cos(q + 4.712389f) * v;
            float z4 = Mth.sin(q + 4.712389f) * v;
            float y1 = state.laser.laserLength;
            float y2 = 0.0f;
            float ux1 = 0.5f;
            float ux2 = 0.0f;

            float uv2 = (state.ageInTicks * -0.2f % 1.0f) - 1.0f;
            float uv1 = state.laser.laserLength * 2.5f + uv2;

            int light = LightCoordsUtil.FULL_BRIGHT;

            collector.submitCustomGeometry(poseStack, GeneralUtilsClient.renderTypeCrystal(LASER_LOCATION, false), (pose, vertexConsumer) -> {
                vertex(vertexConsumer, pose, x1, y1, z1, red2, green2, blue2, light, ux1, uv1);
                vertex(vertexConsumer, pose, x1, y2, z1, red, green, blue, light, ux1, uv2);
                vertex(vertexConsumer, pose, x2, y2, z2, red, green, blue, light, ux2, uv2);
                vertex(vertexConsumer, pose, x2, y1, z2, red2, green2, blue2, light, ux2, uv1);
                vertex(vertexConsumer, pose, x3, y1, z3, red2, green2, blue2, light, ux1, uv1);
                vertex(vertexConsumer, pose, x3, y2, z3, red, green, blue, light, ux1, uv2);
                vertex(vertexConsumer, pose, x4, y2, z4, red, green, blue, light, ux2, uv2);
                vertex(vertexConsumer, pose, x4, y1, z4, red2, green2, blue2, light, ux2, uv1);
                float as = 0.0f;
                if (Mth.floor(state.ageInTicks) % 4 < 2) {
                    as = 0.5f;
                }
                vertex(vertexConsumer, pose, x7, y1, z5, red2, green2, blue2, light, 0.5f, as + 0.5f);
                vertex(vertexConsumer, pose, z9, y1, z6, red2, green2, blue2, light, 1.0f, as + 0.5f);
                vertex(vertexConsumer, pose, x5, y1, z7, red2, green2, blue2, light, 1.0f, as);
                vertex(vertexConsumer, pose, x6, y1, z8, red2, green2, blue2, light, 0.5f, as);
            });
            poseStack.popPose();
        }
    }

    // FIXME this probably needs to be called during camera setup event
    private static final double LASER_SCREENSHAKE_RADIUS = 10.0D;
    public static void laserScreenShake(Level level, Camera camera, float partialTicks, CameraOrientation orientation) {
        if(Minecraft.getInstance().isPaused()) {
            return;
        }

        var cameraEntity = Objects.requireNonNullElse(camera.entity(), Minecraft.getInstance().player);
        var lasers = level.getEntities(EntityTypeTest.forClass(CosmicCrystalEntity.class), cameraEntity.getBoundingBox().inflate(LASER_SCREENSHAKE_RADIUS), ((Predicate<CosmicCrystalEntity>) CosmicCrystalEntity::isLaserFiring).and(EntitySelector.ENTITY_STILL_ALIVE.and(EntitySelector.NO_SPECTATORS)));

        double closestLaserDistance = Double.MAX_VALUE;
        CosmicCrystalEntity closestLaser = null;
        for (CosmicCrystalEntity laser : lasers) {
            var positions = laser.getLaserPositions(partialTicks);

            // project camera entity position onto laser segment to find closest point
            var laserDirection = positions.getSecond().subtract(positions.getFirst());
            var distStart = positions.getFirst().subtract(cameraEntity.position());
            var distEnd = positions.getSecond().subtract(cameraEntity.position());

            var dot = distStart.dot(distEnd);
            var t = Mth.clamp(dot / laserDirection.lengthSqr(), 0.0D, 1.0D);

            var hit = positions.getFirst().add(laserDirection.scale(t));
            var dist = cameraEntity.distanceToSqr(hit);

            if(closestLaser == null || dist < closestLaserDistance) {
                closestLaser = laser;
                closestLaserDistance = dist;
            }
        }

        if (closestLaserDistance <= LASER_SCREENSHAKE_RADIUS * LASER_SCREENSHAKE_RADIUS) {
            double percentageToCenter = 1.0D - (Math.sqrt(closestLaserDistance) / LASER_SCREENSHAKE_RADIUS);

            double spinSlowdown = 0.15d + (0.3d * (1 - percentageToCenter * percentageToCenter));
            float intensity = (float) (0.175d * percentageToCenter * percentageToCenter * percentageToCenter);
            double currentMillisecond = System.currentTimeMillis() % (360 * spinSlowdown);
            double angle = (currentMillisecond / spinSlowdown);
            orientation.bz$setYaw(orientation.bz$getYaw() + (Mth.sin(angle) * intensity * 30)); // FIXME those values need adjusting
        }
    }

    public interface CameraOrientation {
        float bz$getYaw();
        void bz$setYaw(float yaw);

        float bz$getPitch();
        void bz$setPitch(float pitch);
    }

    private static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, float x, float y, float z, int red, int green, int blue, int light, float ux, float uz) {
        vertexConsumer
                .addVertex(pose, x, y, z)
                .setColor(ARGB.color(red, green, blue))
                .setUv(ux, uz)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }

    @Override
    protected boolean affectedByCulling(CosmicCrystalEntity entity) {
        return false;
    }

    @Override
    public Identifier getTextureLocation(CosmicCrystalRenderState state) {
        return SKIN;
    }

    @Override
    protected boolean shouldShowName(CosmicCrystalEntity entity, double distanceToCameraSq) {
        return false;
    }
}
