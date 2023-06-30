package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.mixin.entities.EntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzDamageSources;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class SentryWatcherEntity extends Entity implements Enemy {
   private static final EntityDataAccessor<Boolean> DATA_ID_ACTIVATED = SynchedEntityData.defineId(SentryWatcherEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> DATA_ID_SHAKING = SynchedEntityData.defineId(SentryWatcherEntity.class, EntityDataSerializers.BOOLEAN);

   public float xxa;
   public float yya;
   public float zza;
   protected int lerpSteps;
   protected double lerpX;
   protected double lerpY;
   protected double lerpZ;
   protected double lerpYRot;
   protected double lerpXRot;
   private float speed;
   private int shakingTime = 0;
   private Direction targetFacing;
   private boolean explosionPrimed = false;
   private boolean prevShaking;

   public SentryWatcherEntity(Level worldIn) {
      super(BzEntities.SENTRY_WATCHER.get(), worldIn);
      this.setMaxUpStep(0.8f);
   }

   public SentryWatcherEntity(EntityType<? extends SentryWatcherEntity> type, Level worldIn) {
      super(type, worldIn);
      this.setMaxUpStep(0.8f);
   }

   public int getShakingTime() {
      return shakingTime;
   }

   public void setShakingTime(int shakingTime) {
      this.shakingTime = shakingTime;
   }

   public Direction getTargetFacing() {
      if (targetFacing == null) {
         targetFacing = this.getDirection();
      }

      return targetFacing;
   }

   public void setTargetFacing(Direction targetFacing) {
      this.targetFacing = targetFacing;
   }

   @Override
   protected void defineSynchedData() {
      this.entityData.define(DATA_ID_ACTIVATED, false);
      this.entityData.define(DATA_ID_SHAKING, false);
   }

   public boolean hasActivated() {
      return this.entityData.get(DATA_ID_ACTIVATED);
   }

   protected void setHasActivated(boolean activated) {
      this.entityData.set(DATA_ID_ACTIVATED, activated);
   }

   public boolean hasShaking() {
      return this.entityData.get(DATA_ID_SHAKING);
   }

   protected void setHasShaking(boolean isShaking) {
      this.entityData.set(DATA_ID_SHAKING, isShaking);
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
      this.refreshDimensions();
      if (this.isInWater() && this.random.nextInt(20) == 0) {
         this.doWaterSplashEffect();
      }
      super.onSyncedDataUpdated(key);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      compound.putBoolean("explosionPrimed", explosionPrimed);
      compound.putBoolean("activated", this.hasActivated());
      compound.putBoolean("shaking", this.hasShaking());
      compound.putInt("shakingTime", this.getShakingTime());

      String targetFacingName = this.getTargetFacing().getName();
      compound.putString("targetFacing", targetFacingName);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      this.explosionPrimed = compound.getBoolean("explosionPrimed");
      this.setHasActivated(compound.getBoolean("activated"));
      this.setHasShaking(compound.getBoolean("shaking"));
      this.setShakingTime(compound.getInt("shakingTime"));
      this.setHasShaking(this.getShakingTime() > 0);

      String targetFacingName = compound.getString("targetFacing");
      Direction targetDirection = Direction.byName(targetFacingName);
      this.setTargetFacing(targetDirection);
   }

   @Override
   public void remove(RemovalReason removalReason) {
      super.remove(removalReason);

      if (this.explosionPrimed && !this.level().isClientSide() && removalReason == RemovalReason.KILLED) {
         largeExplosion();
      }
   }

   private void largeExplosion() {
      this.level().explode(this, this.getX(), this.getY(), this.getZ(), 6, Level.ExplosionInteraction.MOB);
      this.level().explode(this, this.getX(), this.getY(), this.getZ(), 9, Level.ExplosionInteraction.MOB);
   }

   @Override
   public void refreshDimensions() {
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      super.refreshDimensions();
      this.absMoveTo(x, y, z);
   }

   @Override
   public Iterable<ItemStack> getArmorSlots() {
      return new ArrayList<>();
   }

   @Override
   public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {}

   @Override
   public boolean causeFallDamage(float f, float g, DamageSource arg) {
      if (f > 1.5) {
         this.playSound(SoundEvents.GENERIC_BIG_FALL, 1.0f, 0.5f);
         this.playBlockFallSound();
         return true;
      }
      return false;
   }

   protected void playBlockFallSound() {
      if (!this.isSilent()) {
         int i = Mth.floor(this.getX());
         int j = Mth.floor(this.getY() - (double)0.2f);
         int k = Mth.floor(this.getZ());
         BlockPos pos = new BlockPos(i, j, k);
         BlockState blockstate = this.level().getBlockState(pos);
         if (!blockstate.isAir()) {
            SoundType soundtype = blockstate.getSoundType();
            this.playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5f, soundtype.getPitch() * 0.75f);
         }
      }
   }

   @Override
   public boolean isPushable() {
      return false;
   }

   protected float getWaterSlowDown() {
      return 0.95F;
   }

   @Override
   public boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> tagKey, double d) {
      if (this.touchingUnloadedChunk()) {
         return false;
      }
      else {
         AABB aABB = this.getBoundingBox().deflate(0.001);
         int i = Mth.floor(aABB.minX);
         int j = Mth.ceil(aABB.maxX);
         int k = Mth.floor(aABB.minY);
         int l = Mth.ceil(aABB.maxY);
         int m = Mth.floor(aABB.minZ);
         int n = Mth.ceil(aABB.maxZ);
         double e = 0.0;
         boolean bl2 = false;
         BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

         for(int p = i; p < j; ++p) {
            for(int q = k; q < l; ++q) {
               for(int r = m; r < n; ++r) {
                  mutableBlockPos.set(p, q, r);
                  FluidState fluidState = this.level().getFluidState(mutableBlockPos);
                  if (fluidState.is(tagKey)) {
                     double f = (float)q + fluidState.getHeight(this.level(), mutableBlockPos);
                     if (f >= aABB.minY) {
                        bl2 = true;
                        e = Math.max(f - aABB.minY, e);
                     }
                  }
               }
            }
         }

         this.fluidHeight.put(tagKey, e);
         return bl2;
      }
   }

   @Override
   public void tick() {
      super.tick();

      if (!this.isRemoved()) {
         this.aiStep();
      }

      this.level().getProfiler().push("rangeChecks");

      while(this.getYRot() - this.yRotO < -180.0F) {
         this.yRotO -= 360.0F;
      }

      while(this.getYRot() - this.yRotO >= 180.0F) {
         this.yRotO += 360.0F;
      }

      while(this.getXRot() - this.xRotO < -180.0F) {
         this.xRotO -= 360.0F;
      }

      while(this.getXRot() - this.xRotO >= 180.0F) {
         this.xRotO += 360.0F;
      }

      this.level().getProfiler().pop();
      //this.animStep += h;
   }

   public void travel(Vec3 vec3) {
      if (this.isControlledByLocalInstance()) {
         double d = 0.08;
         boolean bl = this.getDeltaMovement().y <= 0.0;

         double e;
         if (this.isInWater()) {
            e = this.getY();
            float f = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
            float g = 0.02F;
            float h = 0;
            if (!this.onGround()) {
               h *= 0.5F;
            }

            if (h > 0.0F) {
               f += (0.54600006F - f) * h / 3.0F;
               g += (this.getSpeed() - g) * h / 3.0F;
            }

            this.moveRelative(g, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vec3 vec32 = this.getDeltaMovement();

            this.setDeltaMovement(vec32.multiply(f, 0.800000011920929, f));
            Vec3 vec33 = this.getFluidFallingAdjustedMovement(d, bl, this.getDeltaMovement());
            this.setDeltaMovement(vec33);
            if (this.horizontalCollision && this.isFree(vec33.x, vec33.y + 0.6000000238418579 - this.getY() + e, vec33.z)) {
               this.setDeltaMovement(vec33.x, 0.30000001192092896, vec33.z);
            }
         }
         else if (this.isInLava()) {
            e = this.getY();
            this.moveRelative(0.02F, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vec3 vec34;
            if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
               this.setDeltaMovement(this.getDeltaMovement().multiply(0.5, 0.800000011920929, 0.5));
               vec34 = this.getFluidFallingAdjustedMovement(d, bl, this.getDeltaMovement());
               this.setDeltaMovement(vec34);
            }
            else {
               this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            }

            if (!this.isNoGravity()) {
               this.setDeltaMovement(this.getDeltaMovement().add(0.0, -d / 4.0, 0.0));
            }

            vec34 = this.getDeltaMovement();
            if (this.horizontalCollision && this.isFree(vec34.x, vec34.y + 0.6000000238418579 - this.getY() + e, vec34.z)) {
               this.setDeltaMovement(vec34.x, 0.30000001192092896, vec34.z);
            }
         }
         else {
            BlockPos blockPos = this.getBlockPosBelowThatAffectsMyMovement();
            float p = this.level().getBlockState(blockPos).getBlock().getFriction();
            Vec3 vec37 = this.handleRelativeFrictionAndCalculateMovement(vec3, p);
            double q = vec37.y;
            if (this.level().isClientSide && !this.level().hasChunkAt(blockPos)) {
               if (this.getY() > (double)this.level().getMinBuildHeight()) {
                  q = -0.1;
               }
               else {
                  q = 0.0;
               }
            }
            else if (!this.isNoGravity()) {
               q -= d;
            }

            this.setDeltaMovement(vec37.x, q * 0.9800000190734863, vec37.z);
         }
      }
      else if (this.level().isClientSide()) {
         if (this.hasActivated() && this.onGround() && (Math.abs(this.getDeltaMovement().x()) > 0.001d || Math.abs(this.getDeltaMovement().z()) > 0.001d)) {
            int particlesToSpawn = (int) (1 + Math.abs(this.getDeltaMovement().x() * 50) + Math.abs(this.getDeltaMovement().z() + 50));
            for (int i = 0; i < particlesToSpawn; i++) {
               this.level().addParticle(ParticleTypes.SMOKE,
                       this.position().x() + random.nextGaussian() * 0.6d,
                       this.position().y() + random.nextGaussian() * 0.1d + 0.2d,
                       this.position().z() + random.nextGaussian() * 0.6d,
                       random.nextGaussian() * 0.01d + 0.01d,
                       random.nextGaussian() * 0.01d + 0.01d,
                       random.nextGaussian() * 0.01d + 0.01d);
            }
         }

         if (this.hasShaking() && !prevShaking) {
            this.level().playLocalSound(
                    this.blockPosition(),
                    BzSounds.SENTRY_WATCHER_ACTIVATING.get(),
                    SoundSource.NEUTRAL,
                    2.0F,
                    1.0f,
                    false);
         }
         else if (this.hasActivated() && this.prevShaking && !this.hasShaking()) {
            this.level().playLocalSound(
                    this.blockPosition(),
                    BzSounds.SENTRY_WATCHER_MOVING.get(),
                    SoundSource.NEUTRAL,
                    2.0F,
                    1.0f,
                    false);
         }

         this.prevShaking = this.hasShaking();
      }
   }

   protected void serverAiStep() {
      if (this.hasActivated()) {
         if (this.horizontalCollision && this.getDeltaMovement().length() < 0.07841f) {
            this.setHasActivated(false);
            this.setTargetFacing(this.getTargetFacing().getOpposite());

            if (this.level() instanceof ServerLevel serverLevel) {
               serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
                       this.getX(),
                       this.getY() + 0.2d,
                       this.getZ(),
                       40,
                       1,
                       1,
                       1,
                       0.1D);
               serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                       this.getX(),
                       this.getY() + 0.5d,
                       this.getZ(),
                       40,
                       1,
                       1,
                       1,
                       0.1D);
               serverLevel.sendParticles(ParticleTypes.CRIT,
                       this.getX(),
                       this.getY() + 1,
                       this.getZ(),
                       40,
                       1,
                       1,
                       1,
                       0.1D);

               serverLevel.playSound(
                       this,
                       this.blockPosition(),
                       BzSounds.SENTRY_WATCHER_CRASH.get(),
                       SoundSource.NEUTRAL,
                       2.0F,
                       1.0f);
            }
         }
         else if (this.getShakingTime() > 0) {
            //play shake animation
            this.setShakingTime(this.getShakingTime() - 1);

            if (this.getShakingTime() <= 0) {
               this.setHasShaking(false);
            }
         }
         else {
            Vec3 currentVelocity = this.getDeltaMovement();
            double newX = currentVelocity.x();
            double newY = currentVelocity.y();
            double newZ = currentVelocity.z();

            Direction currentDirection = this.getTargetFacing();
            if (currentDirection.getStepX() != 0) {
               newX += (currentDirection.getStepX() / 200f);
               newX *= 1.05;
            }
            else if (currentDirection.getStepZ() != 0) {
               newZ += (currentDirection.getStepZ() / 200f);
               newZ *= 1.05;
            }

            this.setDeltaMovement(newX, newY, newZ);
         }
      }
      else if (this.tickCount % 10 == 0 && this.getYRot() == this.getTargetFacing().toYRot()) {
         int sightRange = 36;
         Vec3 eyePosition = this.getEyePosition();
         Vec3 finalPos = eyePosition.add(Vec3.atLowerCornerOf(this.getTargetFacing().getNormal().multiply(sightRange)));
         AABB boundsForChecking = this.getBoundingBox().inflate(sightRange);

         EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                 this.level(),
                 this,
                 eyePosition,
                 finalPos,
                 boundsForChecking,
                 (entity) -> entity instanceof LivingEntity && !BeeAggression.isBeelikeEntity(entity)
         );

         if (entityHitResult != null) {
            this.setHasActivated(true);
            this.setShakingTime(40);
            this.setHasShaking(true);
         }
         else {
            finalPos = this.position().add(0, 0.1d, 0).add(Vec3.atLowerCornerOf(this.getTargetFacing().getNormal().multiply(sightRange)));
            boundsForChecking = this.getBoundingBox().inflate(sightRange);

            EntityHitResult entityHitResult2 = ProjectileUtil.getEntityHitResult(
                    this.level(),
                    this,
                    eyePosition,
                    finalPos,
                    boundsForChecking,
                    (entity) -> {
                       if (entity.getType().is(BzTags.SENTRY_WATCHER_FORCED_NEVER_ACTIVATES_WHEN_SEEN) || entity.isSpectator()) {
                          return false;
                       }
                       else if (entity.getType().is(BzTags.SENTRY_WATCHER_ACTIVATES_WHEN_SEEN)) {
                          return true;
                       }
                       else if (!(entity instanceof LivingEntity) || BeeAggression.isBeelikeEntity(entity)) {
                          return false;
                       }

                       return !(entity instanceof Player player) || !player.isCreative();
                    }
            );

            if (entityHitResult2 != null) {
               this.setHasActivated(true);
               this.setShakingTime(40);
               this.setHasShaking(true);
            }
         }
      }

      if (this.explosionPrimed && this.tickCount % 20 == 0 && this.level() instanceof ServerLevel serverLevel) {
         StructureStart structureStart = serverLevel.structureManager().getStructureWithPieceAt(this.blockPosition(), BzTags.SEMPITERNAL_SANCTUMS);
         if (structureStart == null || !structureStart.isValid()) {
            this.kill();
         }
      }
   }

   public void aiStep() {
      if (this.isControlledByLocalInstance()) {
         this.lerpSteps = 0;
         this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
      }

      if (this.lerpSteps > 0) {
         double d = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
         double e = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
         double f = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
         double g = Mth.wrapDegrees(this.lerpYRot - (double)this.getYRot());
         this.setYRot(this.getYRot() + (float)g / (float)this.lerpSteps);
         this.setXRot(this.getXRot() + (float)(this.lerpXRot - (double)this.getXRot()) / (float)this.lerpSteps);
         --this.lerpSteps;
         this.setPos(d, e, f);
         this.setRot(this.getYRot(), this.getXRot());
      }

      if (this.isEffectiveAi() && !this.hasActivated() && this.getYRot() != this.getTargetFacing().toYRot()) {
         float targetY = this.getTargetFacing().toYRot();
         float currentY = this.getYRot();
         float diff = targetY - currentY;
         float diff2 = targetY - (currentY + 360);
         float diffToUse = diff;
         if (Math.abs(diff) > Math.abs(diff2)) {
            diffToUse = diff2;
         }
         float newYDiff = Math.max(Math.min(diffToUse, 1), -1);
         this.setYRot(this.getYRot() + newYDiff);
      }

      Vec3 vec3 = this.getDeltaMovement();
      double newX = vec3.x;
      double newY = vec3.y;
      double newZ = vec3.z;
      if (Math.abs(vec3.x) < 0.003) {
         newX = 0.0;
      }

      if (Math.abs(vec3.y) < 0.003) {
         newY = 0.0;
      }

      if (Math.abs(vec3.z) < 0.003) {
         newZ = 0.0;
      }

      this.setDeltaMovement(newX, newY, newZ);
      this.level().getProfiler().push("ai");
      if (this.isImmobile()) {
         this.xxa = 0.0F;
         this.zza = 0.0F;
      }
      else if (this.isEffectiveAi()) {
         this.level().getProfiler().push("newAi");
         this.serverAiStep();
         this.level().getProfiler().pop();
      }

      this.level().getProfiler().pop();

      this.level().getProfiler().push("travel");
      this.xxa *= 1.02F;
      this.zza *= 1.02F;

      Vec3 vec32 = new Vec3(this.xxa, this.yya, this.zza);
      this.travel(vec32);

      this.level().getProfiler().pop();
      this.level().getProfiler().push("freezing");
      if (!this.level().isClientSide) {
         int m = this.getTicksFrozen();
         if (this.isInPowderSnow && this.canFreeze()) {
            this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), m + 1));
         } else {
            this.setTicksFrozen(Math.max(0, m - 2));
         }
      }

      this.level().getProfiler().pop();
   }

   protected void pushEntities() {
      if (this.level().isClientSide()) {
         this.level().getEntities(EntityTypeTest.forClass(Player.class), this.getBoundingBox(), EntitySelector.pushableBy(this)).forEach(this::doPush);
      }
      else {
         List<Entity> list = this.level().getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this));
         if (!list.isEmpty()) {
            int i = this.level().getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
            int j;
            if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
               j = 0;

               for (Entity entity : list) {
                  if (!entity.isPassenger()) {
                     ++j;
                  }
               }
            }

            for(j = 0; j < list.size(); ++j) {
               Entity entity = list.get(j);
               this.doPush(entity);
            }
         }
      }
   }

   protected void doPush(Entity entity) {
      if (entity instanceof LivingEntity) {
         Vec3 currentVelocity = this.getDeltaMovement();
         Vec3 victimVelocity = entity.getDeltaMovement();
         Vec3 diffVelocity = currentVelocity.subtract(victimVelocity);
         double speedDiff = this.getTargetFacing().getStepX() != 0 ? Math.abs(diffVelocity.x()) : Math.abs(diffVelocity.z());
         if (speedDiff > 0.3d) {
            speedDiff -= 0.2d;

            double pushEffect = 0.6d;
            float damageMultiplier = 15;
            if (entity instanceof ServerPlayer serverPlayer && EssenceOfTheBees.hasEssence(serverPlayer)) {
               damageMultiplier = 10;
               pushEffect = 0.45d;
            }

            entity.hurt(this.level().damageSources().source(BzDamageSources.SENTRY_WATCHER_CRUSHING_TYPE), (float) (speedDiff * damageMultiplier));
            entity.push(this.getDeltaMovement().x() * pushEffect, 0, this.getDeltaMovement().z() * pushEffect);
         }
         else {
            entity.push(this);
         }
      }
      else {
         entity.push(this);
      }
   }

   public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 vec3, float f) {
      this.moveRelative(this.getFrictionInfluencedSpeed(), vec3);
      this.move(MoverType.SELF, this.getDeltaMovement());
      Vec3 deltaMovement = this.getDeltaMovement();
      if (this.horizontalCollision && (this.getFeetBlockState().is(Blocks.POWDER_SNOW) && PowderSnowBlock.canEntityWalkOnPowderSnow(this))) {
         deltaMovement = new Vec3(deltaMovement.x, 0.2, deltaMovement.z);
      }

      return deltaMovement;
   }

   public Vec3 getFluidFallingAdjustedMovement(double d, boolean bl, Vec3 vec3) {
      if (!this.isNoGravity() && !this.isSprinting()) {
         double e;
         if (bl && Math.abs(vec3.y - 0.005) >= 0.003 && Math.abs(vec3.y - d / 16.0) < 0.003) {
            e = -0.003;
         }
         else {
            e = vec3.y - d / 16.0;
         }

         return new Vec3(vec3.x, e, vec3.z);
      }
      else {
         return vec3;
      }
   }

   @Override
   public void move(MoverType moverType, Vec3 vec3) {
      if (this.noPhysics) {
         this.setPos(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
      }
      else {
         this.wasOnFire = this.isOnFire();
         if (moverType == MoverType.PISTON) {
            vec3 = this.limitPistonMovement(vec3);
            if (vec3.equals(Vec3.ZERO)) {
               return;
            }
         }

         this.level().getProfiler().push("move");
         if (this.stuckSpeedMultiplier.lengthSqr() > 1.0E-7) {
            vec3 = vec3.multiply(this.stuckSpeedMultiplier);
            this.stuckSpeedMultiplier = Vec3.ZERO;
            this.setDeltaMovement(Vec3.ZERO);
         }

         vec3 = this.maybeBackOffFromEdge(vec3, moverType);
         Vec3 collision = this.collide(vec3);
         double d = collision.lengthSqr();
         if (d > 1.0E-7) {
            if (this.fallDistance != 0.0F && d >= 1.0) {
               BlockHitResult blockHitResult = this.level().clip(new ClipContext(this.position(), this.position().add(collision), ClipContext.Block.FALLDAMAGE_RESETTING, net.minecraft.world.level.ClipContext.Fluid.WATER, this));
               if (blockHitResult.getType() != HitResult.Type.MISS) {
                  this.resetFallDistance();
               }
            }

            this.setPos(this.getX() + collision.x, this.getY() + collision.y, this.getZ() + collision.z);
         }

         this.level().getProfiler().pop();
         this.level().getProfiler().push("rest");
         boolean loseXSpeed = !Mth.equal(vec3.x, collision.x);
         boolean loseZSpeed = !Mth.equal(vec3.z, collision.z);
         this.horizontalCollision = loseXSpeed || loseZSpeed;
         this.verticalCollision = vec3.y != collision.y;
         this.verticalCollisionBelow = this.verticalCollision && vec3.y < 0.0;

         Vec3 deltaMovement = this.getDeltaMovement();
         if (this.horizontalCollision && (Math.abs(deltaMovement.x()) + Math.abs(deltaMovement.z()) > 0.01)) {
            destroyBlocksInWay();
         }

         if (this.horizontalCollision) {
            this.minorHorizontalCollision = this.isHorizontalCollisionMinor(collision);
         }
         else {
            this.minorHorizontalCollision = false;
         }

         this.pushEntities();

         this.setOnGroundWithKnownMovement(this.verticalCollisionBelow, collision);
         BlockPos blockPos = this.getOnPosLegacy();
         BlockState blockState = this.level().getBlockState(blockPos);
         this.checkFallDamage(collision.y, this.onGround(), blockState, blockPos);
         if (this.isRemoved()) {
            this.level().getProfiler().pop();
         }
         else {
            if (this.horizontalCollision) {
               Vec3 vec33 = this.getDeltaMovement();
               this.setDeltaMovement(loseXSpeed ? 0.0 : vec33.x, vec33.y, loseZSpeed ? 0.0 : vec33.z);
            }

            net.minecraft.world.level.block.Block block = blockState.getBlock();
            if (vec3.y != collision.y) {
               block.updateEntityAfterFallOn(this.level(), this);
            }

            if (this.onGround()) {
               block.stepOn(this.level(), blockPos, blockState, this);
            }

            MovementEmission movementEmission = this.getMovementEmission();
            if (movementEmission.emitsAnything() && !this.isPassenger()) {
               double e = collision.x;
               double f = collision.y;
               double g = collision.z;
               this.flyDist += (float)(collision.length() * 0.6);
               BlockPos blockPos2 = this.getOnPos();
               BlockState blockState2 = this.level().getBlockState(blockPos2);

               this.walkDist += (float)collision.horizontalDistance() * 0.6F;
               this.moveDist += (float)Math.sqrt(e * e + f * f + g * g) * 0.6F;
               if (this.moveDist > ((EntityAccessor) this).getNextStep() && !blockState2.isAir()) {
                  boolean bl4 = blockPos2.equals(blockPos);
                  boolean bl5 = ((EntityAccessor)this).callVibrationAndSoundEffectsFromBlock(blockPos, blockState, movementEmission.emitsSounds(), bl4, vec3);
                  if (!bl4) {
                     bl5 |= ((EntityAccessor)this).callVibrationAndSoundEffectsFromBlock(blockPos2, blockState2, false, movementEmission.emitsEvents(), vec3);
                  }

                  if (bl5) {
                     ((EntityAccessor) this).setNextStep(this.nextStep());
                  }
                  else if (this.isInWater()) {
                     ((EntityAccessor) this).setNextStep(this.nextStep());
                     if (movementEmission.emitsSounds()) {
                        this.waterSwimSound();
                     }

                     if (movementEmission.emitsEvents()) {
                        this.gameEvent(GameEvent.SWIM);
                     }
                  }
               }
               else if (blockState2.isAir()) {
                  this.processFlappingMovement();
               }
            }

            this.tryCheckInsideBlocks();
            float h = this.getBlockSpeedFactor();
            this.setDeltaMovement(this.getDeltaMovement().multiply(h, 1.0, h));
            if (this.level().getBlockStatesIfLoaded(this.getBoundingBox().deflate(1.0E-6)).noneMatch((blockStatex) -> blockStatex.is(BlockTags.FIRE) || blockStatex.is(Blocks.LAVA))) {
               if (this.getRemainingFireTicks() <= 0) {
                  this.setRemainingFireTicks(-this.getFireImmuneTicks());
               }

               if (this.wasOnFire && (this.isInPowderSnow || this.isInWaterRainOrBubble())) {
                  this.playEntityOnFireExtinguishedSound();
               }
            }

            if (this.isOnFire() && (this.isInPowderSnow || this.isInWaterRainOrBubble())) {
               this.setRemainingFireTicks(-this.getFireImmuneTicks());
            }

            this.level().getProfiler().pop();
         }
      }
   }

   private void destroyBlocksInWay() {
      Direction facing = this.getTargetFacing();
      AABB aabb = this.getBoundingBox();
      BlockPos min = null;
      BlockPos max = null;
      double xStep = (facing.getStepX() / 3d);
      double zStep = (facing.getStepZ() / 3d);
      switch (facing) {
         case NORTH -> {
            min = new BlockPos((int) Math.floor(aabb.minX + xStep + 0.0001d), (int)Math.floor(aabb.minY), (int)Math.floor(aabb.minZ + zStep + 0.0001d));
            max = new BlockPos((int) Math.floor(aabb.maxX + xStep - 0.0001f), (int)Math.floor(aabb.minY), (int)Math.floor(aabb.minZ + zStep + 0.0001d));
         }
         case SOUTH -> {
            min = new BlockPos((int) Math.floor(aabb.minX + xStep + 0.0001d), (int)Math.floor(aabb.minY), (int)Math.floor(aabb.maxZ + zStep - 0.0001d));
            max = new BlockPos((int) Math.floor(aabb.maxX + xStep - 0.0001d), (int)Math.floor(aabb.minY), (int)Math.floor(aabb.maxZ + zStep - 0.0001d));
         }
         case WEST -> {
            min = new BlockPos((int) Math.floor(aabb.minX + xStep + 0.0001d), (int)Math.floor(aabb.minY), (int)Math.floor(aabb.minZ + zStep + 0.0001d));
            max = new BlockPos((int) Math.floor(aabb.minX + xStep + 0.0001d), (int)Math.floor(aabb.minY), (int)Math.floor(aabb.maxZ + zStep - 0.0001d));
         }
         case EAST -> {
            min = new BlockPos((int) Math.floor(aabb.maxX + xStep - 0.0001d), (int)Math.floor(aabb.minY), (int)Math.floor(aabb.minZ + zStep + 0.0001d));
            max = new BlockPos((int) Math.floor(aabb.maxX + xStep - 0.0001d), (int)Math.floor(aabb.minY), (int)Math.floor(aabb.maxZ + zStep - 0.0001d));
         }
      }

      if (min != null) {
         boolean canDemolish = true;
         double totalhardness = 0;
         int alwaysDestroyCounter = 0;
         List<BlockPos> demolishPos = new ArrayList<>();
         for (BlockPos pos : BlockPos.betweenClosed(min, max)) {

            BlockState state = this.level().getBlockState(pos);
            if (!state.getCollisionShape(this.level(), pos).isEmpty()) {
               if (state.is(BzTags.SENTRY_WATCHER_FORCED_NEVER_DESTROY)) {
                  canDemolish = false;
                  break;
               }
               else {
                  demolishPos.add(pos.immutable());
                  totalhardness += state.getBlock().getExplosionResistance();
                  if (state.is(BzTags.SENTRY_WATCHER_ALWAYS_DESTROY)) {
                     alwaysDestroyCounter++;
                  }
               }
            }

            BlockPos abovePos = pos.above();
            BlockState aboveState = this.level().getBlockState(abovePos);
            if (!aboveState.getCollisionShape(this.level(), abovePos).isEmpty()) {
               if (aboveState.is(BzTags.SENTRY_WATCHER_FORCED_NEVER_DESTROY)) {
                  canDemolish = false;
                  break;
               }
               else {
                  demolishPos.add(abovePos);
                  totalhardness += aboveState.getBlock().getExplosionResistance();
                  if (aboveState.is(BzTags.SENTRY_WATCHER_ALWAYS_DESTROY)) {
                     alwaysDestroyCounter++;
                  }
               }
            }
         }

         if (canDemolish &&
                 (alwaysDestroyCounter == demolishPos.size() ||
                 (demolishPos.size() <= 1 && totalhardness < 10) ||
                 totalhardness < 5.95f))
         {
            for (BlockPos pos : demolishPos) {
               this.level().destroyBlock(pos, true);
            }

            this.horizontalCollision = false;
         }
      }
   }

   private Vec3 collide(Vec3 vec3) {
      AABB aABB = this.getBoundingBox();
      List<VoxelShape> list = this.level().getEntityCollisions(this, aABB.expandTowards(vec3));
      Vec3 vec32 = vec3.lengthSqr() == 0.0 ? vec3 : collideBoundingBox(this, vec3, aABB, this.level(), list);
      boolean bl = vec3.x != vec32.x;
      boolean bl2 = vec3.y != vec32.y;
      boolean bl3 = vec3.z != vec32.z;
      boolean bl4 = this.onGround() || bl2 && vec3.y < 0.0;
      if (this.maxUpStep() > 0.0F && bl4 && (bl || bl3)) {
         Vec3 vec33 = collideBoundingBox(this, new Vec3(vec3.x, this.maxUpStep(), vec3.z), aABB, this.level(), list);
         Vec3 vec34 = collideBoundingBox(this, new Vec3(0.0, this.maxUpStep(), 0.0), aABB.expandTowards(vec3.x, 0.0, vec3.z), this.level(), list);
         if (vec34.y < (double)this.maxUpStep()) {
            Vec3 vec35 = collideBoundingBox(this, new Vec3(vec3.x, 0.0, vec3.z), aABB.move(vec34), this.level(), list).add(vec34);
            if (vec35.horizontalDistanceSqr() > vec33.horizontalDistanceSqr()) {
               vec33 = vec35;
            }
         }

         if (vec33.horizontalDistanceSqr() > vec32.horizontalDistanceSqr()) {
            return vec33.add(collideBoundingBox(this, new Vec3(0.0, -vec33.y + vec3.y, 0.0), aABB.move(vec33), this.level(), list));
         }
      }

      return vec32;
   }

   private float getFrictionInfluencedSpeed() {
      return this.getSpeed();
   }

   public float getSpeed() {
      return this.speed;
   }

   public void setSpeed(float f) {
      this.speed = f;
   }

   public void lerpTo(double d, double e, double f, float g, float h, int i, boolean bl) {
      this.lerpX = d;
      this.lerpY = e;
      this.lerpZ = f;
      this.lerpYRot = g;
      this.lerpXRot = h;
      this.lerpSteps = i;
   }

   @Override
   public boolean canChangeDimensions() {
      return false;
   }

   @Override
   public float getVisualRotationYInDegrees() {
      return this.getYRot();
   }

   @Override
   public float maxUpStep() {
      return super.maxUpStep();
   }

   protected boolean isImmobile() {
      return false;
   }
}