package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2d;

import java.util.Optional;
import java.util.UUID;

public class DirtPelletEntity extends ThrowableItemProjectile {
    private boolean eventBased = false;
    private Entity homingTarget = null;
    private static final EntityDataAccessor<Optional<UUID>> DATA_HOMING_TARGET_UUID = SynchedEntityData.defineId(DirtPelletEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> DATA_IS_HOMING = SynchedEntityData.defineId(DirtPelletEntity.class, EntityDataSerializers.BOOLEAN);

    public DirtPelletEntity(EntityType<? extends DirtPelletEntity> entityType, Level world) {
        super(entityType, world);
    }

    public DirtPelletEntity(Level world, LivingEntity livingEntity) {
        super(BzEntities.DIRT_PELLET_ENTITY.get(), livingEntity, world);
    }

    public DirtPelletEntity(Level world, double x, double y, double z) {
        super(BzEntities.DIRT_PELLET_ENTITY.get(), x, y, z, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_HOMING_TARGET_UUID, Optional.empty());
        this.entityData.define(DATA_IS_HOMING, false);
        super.defineSynchedData();
    }

    public boolean isEventBased() {
        return eventBased;
    }

    public void setEventBased(boolean eventBased) {
        this.eventBased = eventBased;
    }

    public boolean isHoming() {
        return this.entityData.get(DATA_IS_HOMING);
    }

    public void setHoming(boolean homing) {
        this.entityData.set(DATA_IS_HOMING, homing);
    }

    public UUID getHomingTargetUUID() {
        return this.entityData.get(DATA_HOMING_TARGET_UUID).orElse(null);
    }

    public void setHomingTargetUUID(UUID homingTargetUUID) {
        this.entityData.set(DATA_HOMING_TARGET_UUID, Optional.of(homingTargetUUID));
    }

    @Override
    protected Item getDefaultItem() {
        return BzItems.DIRT_PELLET.get();
    }

    private ParticleOptions getParticle() {
        ItemStack itemStack = this.getItemRaw();
        return itemStack.isEmpty() ?
                new ItemParticleOption(ParticleTypes.ITEM, this.getDefaultItem().getDefaultInstance()) :
                new ItemParticleOption(ParticleTypes.ITEM, itemStack);
    }

    protected ParticleOptions getTrailParticle() {
        return getParticle();
    }

    @Override
    public void tick() {
        if (this.isHoming() && this.homingTarget == null && this.getHomingTargetUUID() != null) {
            // Hit method will set it to rootmin.
            // This is for initial creation which is always targeting player.
            Player player = this.level().getPlayerByUUID(this.getHomingTargetUUID());
            if (player != null) {
                this.homingTarget = player;
            }
        }

        if ((this.tickCount + 2) % 5 == 0) {
            Vec3 futurePos = this.position().add(this.getDeltaMovement());
            this.level().addParticle(
                    this.getTrailParticle(),
                    futurePos.x(),
                    futurePos.y() + 0.1,
                    futurePos.z(),
                    0.0,
                    0.0,
                    0.0);
        }

        super.tick();

        if (this.homingTarget != null && this.getOwner() != null) {
            Vec3 deltaMovement = this.getDeltaMovement();

            Vec3 shooterProjectileDiff = this.getOwner().position().subtract(this.position());
            Vec3 shooterVictimDiff = this.getOwner().position().subtract(this.homingTarget.position());
            shooterProjectileDiff = shooterProjectileDiff.subtract(0, shooterProjectileDiff.y(), 0);
            shooterVictimDiff = shooterVictimDiff.subtract(0, shooterVictimDiff.y(), 0);

            double numeratorDotProduct = (shooterProjectileDiff.x() * shooterVictimDiff.x()) + (shooterProjectileDiff.z() * shooterVictimDiff.z());
            double demoninatorDotProduct = (shooterVictimDiff.x() * shooterVictimDiff.x()) + (shooterVictimDiff.z() * shooterVictimDiff.z());
            Vec3 projectionPoint = shooterVictimDiff.scale(numeratorDotProduct / demoninatorDotProduct);
            Vec3 vectorToProjectionPoint = shooterProjectileDiff.subtract(projectionPoint);

            double magicSideGravityConstant = 0.022D;
            Vec3 sideGravityVector = vectorToProjectionPoint.scale(magicSideGravityConstant);

            this.setDeltaMovement(deltaMovement.add(sideGravityVector));
        }
    }

    @Override
    public void handleEntityEvent(byte flag) {
        if (flag == 3) {
            ParticleOptions particleOptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(
                        particleOptions,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        this.random.nextGaussian() * 0.1,
                        this.random.nextGaussian() * 0.1,
                        this.random.nextGaussian() * 0.1);
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity instanceof DirtPelletEntity dirtPelletEntity2) {
            if (dirtPelletEntity2.getOwner() == this.getOwner()) {
                return false;
            }
            else if (this.position().closerThan(dirtPelletEntity2.position(), 0.5d)) {
                return super.canHitEntity(entity);
            }
            else {
                return false;
            }
        }
        return super.canHitEntity(entity);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        EntityType<?> type = entity.getType();
        ResourceLocation resourceLocation = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        int damage = 1;

        if (entity instanceof DirtPelletEntity hitDirtPelletEntity) {
            if (!this.level().isClientSide) {
                hitDirtPelletEntity.level().broadcastEntityEvent(this, (byte)3);
                hitDirtPelletEntity.level().playSound(null, this.blockPosition(), BzSounds.DIRT_PELLET_HIT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                hitDirtPelletEntity.discard();
            }
        }

        if (!type.is(BzTags.DIRT_PELLET_FORCE_NO_EXTRA_DAMAGE)) {
            if (type.is(BzTags.DIRT_PELLET_EXTRA_DAMAGE)) {
                damage = 3;
            }
            else if (!resourceLocation.getNamespace().equals("minecraft") && !resourceLocation.getNamespace().equals(Bumblezone.MODID)) {
                if (entity instanceof FlyingMob || (entity instanceof Mob mob && mob.getMoveControl() instanceof FlyingMoveControl)) {
                    damage = 3;
                }
            }
        }

        if (eventBased || (entity instanceof RootminEntity rootminEntity && rootminEntity.getEssenceController() != null)) {
            if (entity instanceof RootminEntity) {
                damage = 1;
            }
            else if (entity instanceof ServerPlayer serverPlayer) {
                float maxHeart = Math.max(serverPlayer.getHealth(), serverPlayer.getMaxHealth());
                if (!EssenceOfTheBees.hasEssence(serverPlayer)) {
                    damage = (int) (maxHeart / 2);
                }
                else {
                    damage = (int) (maxHeart / 4);
                }
            }
            else {
                damage = 5;
            }
        }

        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)damage);
        if (entity instanceof LivingEntity livingEntity && !livingEntity.isSpectator()) {
            if (livingEntity instanceof Player player && player.isCreative()) {
                return;
            }

            Vector2d direction = new Vector2d(this.getDeltaMovement().x(), this.getDeltaMovement().z()).normalize();
            double yRotHitRadian = Mth.atan2(direction.x(), direction.y());
            double knockbackStrength = 1D;

            if (livingEntity instanceof RootminEntity) {
                knockbackStrength = this.isEventBased() ? 0 : 0.1D;
            }

            livingEntity.knockback(
                knockbackStrength,
                -Mth.sin((float) yRotHitRadian),
                -Mth.cos((float) yRotHitRadian)
            );
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.level().playSound(null, this.blockPosition(), BzSounds.DIRT_PELLET_HIT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            this.discard();
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        double speed = this.getDeltaMovement().length();
        return (float)(speed / 1.5d);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        }

        this.markHurt();
        Entity entity = damageSource.getEntity();
        if (entity != null) {
            if (!this.level().isClientSide) {
                Vec3 vec3 = entity.getLookAngle();
                this.setDeltaMovement(vec3);

                if (this.isHoming() && this.getOwner() != null) {
                    this.setHomingTargetUUID(this.getOwner().getUUID());
                    this.homingTarget = this.getOwner();
                }
                else {
                    this.setHoming(false);
                }

                this.setOwner(entity);
            }
            return true;
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("isEvent", this.isEventBased());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setEventBased(compoundTag.getBoolean("isEvent"));
    }
}