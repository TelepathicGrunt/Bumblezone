package com.telepathicgrunt.the_bumblezone.client.items;

import com.telepathicgrunt.the_bumblezone.items.HoneyCompass;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HoneyCompassItemProperty {

    public static ClampedItemPropertyFunction getClampedItemPropertyFunction() {
        return new ClampedItemPropertyFunction() {
            private final CompassWobble wobble = new CompassWobble();
            private final CompassWobble wobbleRandom = new CompassWobble();

            @Override
            public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                Entity entity = livingEntity != null ? livingEntity : itemStack.getEntityRepresentation();
                if (entity == null) {
                    return 0.0F;
                }
                else {
                    if (clientLevel == null && entity.level() instanceof ClientLevel) {
                        clientLevel = (ClientLevel)entity.level();
                    }

                    BlockPos blockPos = this.getStructurePosition(clientLevel, itemStack.getOrCreateTag());
                    long gameTime = clientLevel.getGameTime();
                    if (blockPos != null && !(entity.position().distanceToSqr((double)blockPos.getX() + 0.5, entity.position().y(), (double)blockPos.getZ() + 0.5) < 1.0E-5F)) {
                        boolean isLocalPlayer = livingEntity instanceof Player && ((Player)livingEntity).isLocalPlayer();
                        double currentFacingAngle = 0.0;
                        if (isLocalPlayer) {
                            currentFacingAngle = livingEntity.getYRot();
                        }
                        else if (entity instanceof ItemFrame) {
                            currentFacingAngle = this.getFrameRotation((ItemFrame)entity);
                        }
                        else if (entity instanceof ItemEntity) {
                            currentFacingAngle = 180.0F - ((ItemEntity)entity).getSpin(0.5F) / (float) (Math.PI * 2) * 360.0F;
                        }
                        else if (livingEntity != null) {
                            currentFacingAngle = livingEntity.yBodyRot;
                        }

                        currentFacingAngle = Mth.positiveModulo(currentFacingAngle / 360.0, 1.0);
                        double angleToTarget = this.getAngleTo(Vec3.atCenterOf(blockPos), entity) / (float) (Math.PI * 2);
                        double angleToRender;
                        if (isLocalPlayer) {
                            if (this.wobble.shouldUpdate(gameTime)) {
                                this.wobble.update(gameTime, 0.5 - (currentFacingAngle - 0.25));
                            }

                            angleToRender = angleToTarget + this.wobble.rotation;
                        }
                        else {
                            angleToRender = 0.5 - (currentFacingAngle - 0.25 - angleToTarget);
                        }

                        return Mth.positiveModulo((float)angleToRender, 1.0F);
                    }
                    else {
                        if (this.wobbleRandom.shouldUpdate(gameTime)) {
                            this.wobbleRandom.update(gameTime, Math.random());
                        }

                        double d = this.wobbleRandom.rotation + (double)((float)this.hash(i) / 2.14748365E9F);
                        return Mth.positiveModulo((float)d, 1.0F);
                    }
                }
            }

            private int hash(int i) {
                return i * 1327217883;
            }

            @Nullable
            private BlockPos getStructurePosition(Level level, CompoundTag compoundTag) {
                boolean structurePos = compoundTag.contains(HoneyCompass.TAG_TARGET_POS);
                boolean dimension = compoundTag.contains(HoneyCompass.TAG_TARGET_DIMENSION);
                if (structurePos && dimension) {
                    Optional<ResourceKey<Level>> optional = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag.get(HoneyCompass.TAG_TARGET_DIMENSION)).result();
                    if (optional.isPresent() && level.dimension() == optional.get()) {
                        return NbtUtils.readBlockPos(compoundTag.getCompound(HoneyCompass.TAG_TARGET_POS));
                    }
                }

                return null;
            }

            private double getFrameRotation(ItemFrame itemFrame) {
                Direction direction = itemFrame.getDirection();
                int itemFrameFacingAngle = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
                return Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + itemFrame.getRotation() * 45 + itemFrameFacingAngle);
            }

            private double getAngleTo(Vec3 vec3, Entity entity) {
                return Math.atan2(vec3.z() - entity.getZ(), vec3.x() - entity.getX());
            }
        };
    }

    static class CompassWobble {
        double rotation;
        private double deltaRotation;
        private long lastUpdateTick;

        boolean shouldUpdate(long currentTick) {
            return this.lastUpdateTick != currentTick;
        }

        void update(long lastTick, double currentRotation) {
            this.lastUpdateTick = lastTick;
            double rotDiff = currentRotation - this.rotation;
            rotDiff = Mth.positiveModulo(rotDiff + 0.5, 1.0) - 0.5;
            this.deltaRotation += rotDiff * 0.1;
            this.deltaRotation *= 0.8;
            this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0);
        }
    }
}
