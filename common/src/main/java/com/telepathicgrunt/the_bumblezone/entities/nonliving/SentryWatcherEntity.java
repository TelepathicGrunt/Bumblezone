package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

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

   public SentryWatcherEntity(Level worldIn) {
      super(BzEntities.SENTRY_WATCHER.get(), worldIn);
      this.setMaxUpStep(0.65f);
   }

   public SentryWatcherEntity(EntityType<? extends SentryWatcherEntity> type, Level worldIn) {
      super(type, worldIn);
      this.setMaxUpStep(0.65f);
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
      compound.putBoolean("activated", this.hasActivated());
      compound.putBoolean("shaking", this.hasShaking());
      compound.putInt("shakingTime", this.getShakingTime());
      compound.putString("targetFacing", this.getTargetFacing().name());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      this.setHasActivated(compound.getBoolean("activated"));
      this.setHasShaking(compound.getBoolean("shaking"));
      this.setShakingTime(compound.getInt("shakingTime"));
      this.setTargetFacing(Direction.byName(compound.getString("targetFacing")));

      this.setHasShaking(this.getShakingTime() > 0);
   }

   @Override
   public void remove(RemovalReason removalReason) {
      super.remove(removalReason);

      if (!this.level().isClientSide() && (removalReason == RemovalReason.KILLED || removalReason == RemovalReason.DISCARDED)) {
         this.level().explode(this, this.getX(), this.getY(), this.getZ(), 6, Level.ExplosionInteraction.MOB);
         this.level().explode(this, this.getX(), this.getY(), this.getZ(), 9, Level.ExplosionInteraction.MOB);
      }
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
      return 0.8F;
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
      else {
         if (this.level().isClientSide() && this.onGround() && (Math.abs(this.getDeltaMovement().x()) > 0.001d || Math.abs(this.getDeltaMovement().z()) > 0.001d)) {
            int particlesToSpawn = (int) (1 + Math.abs(this.getDeltaMovement().x() * 50) + Math.abs(this.getDeltaMovement().z() + 50));
            for (int i = 0; i < particlesToSpawn; i++) {
               this.level().addParticle(ParticleTypes.SMOKE,
                       this.position().x() + random.nextGaussian() * 0.3d + 0.3d,
                       this.position().y() + random.nextGaussian() * 0.1d + 0.2d,
                       this.position().z() + random.nextGaussian() * 0.3d + 0.3d,
                       random.nextGaussian() * 0.01d + 0.01d,
                       random.nextGaussian() * 0.01d + 0.01d,
                       random.nextGaussian() * 0.01d + 0.01d);
            }
         }
      }
   }

   protected void serverAiStep() {
      if (this.hasActivated()) {
         if (this.horizontalCollision && this.getDeltaMovement().length() < 0.07841f) {
            this.setHasActivated(false);
            this.setTargetFacing(this.getTargetFacing().getOpposite());

            if (this.level() instanceof ServerLevel serverLevel) {
               serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
                       this.position().x(),
                       this.position().y() + 0.2d,
                       this.position().z(),
                       40,
                       1,
                       1,
                       1,
                       0.1D);
               serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                       this.position().x(),
                       this.position().y() + 0.5d,
                       this.position().z(),
                       40,
                       1,
                       1,
                       1,
                       0.1D);
               serverLevel.sendParticles(ParticleTypes.CRIT,
                       this.position().x(),
                       this.position().y() + 1d,
                       this.position().z(),
                       40,
                       1,
                       1,
                       1,
                       0.1D);
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
         Vec3 eyePosition = this.getEyePosition();
         Vec3 finalPos = eyePosition.add(Vec3.atLowerCornerOf(this.getTargetFacing().getNormal().multiply(20)));
         AABB boundsForChecking = this.getBoundingBox().inflate(20);

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
         float newYDiff = Math.max(Math.min(targetY - this.getYRot(), 1), -1);
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
      this.level().getProfiler().push("push");

      this.pushEntities();
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
      entity.push(this);
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