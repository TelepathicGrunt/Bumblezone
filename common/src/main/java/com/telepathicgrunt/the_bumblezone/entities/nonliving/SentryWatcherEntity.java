package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SentryWatcherEntity extends Entity implements Enemy {
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

   public SentryWatcherEntity(Level worldIn) {
      super(BzEntities.SENTRY_WATCHER.get(), worldIn);
      this.setMaxUpStep(0.65f);
   }

   public SentryWatcherEntity(EntityType<? extends SentryWatcherEntity> type, Level worldIn) {
      super(type, worldIn);
      this.setMaxUpStep(0.65f);
   }

   @Override
   protected void defineSynchedData() {
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
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
   }

   @Override
   public void remove(RemovalReason removalReason) {
      super.remove(removalReason);
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

         float f;
         double e;
         if (this.isInWater()) {
            e = this.getY();
            f = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
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
            f = this.onGround() ? p * 0.91F : 0.91F;
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

            this.setDeltaMovement(vec37.x * (double)f, q * 0.9800000190734863, vec37.z * (double)f);
         }
      }
   }

   protected void serverAiStep() {}

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
      else if (!this.isEffectiveAi()) {
         this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
      }

      Vec3 vec3 = this.getDeltaMovement();
      double h = vec3.x;
      double i = vec3.y;
      double j = vec3.z;
      if (Math.abs(vec3.x) < 0.003) {
         h = 0.0;
      }

      if (Math.abs(vec3.y) < 0.003) {
         i = 0.0;
      }

      if (Math.abs(vec3.z) < 0.003) {
         j = 0.0;
      }

      this.setDeltaMovement(h, i, j);
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
      this.xxa *= 0.98F;
      this.zza *= 0.98F;

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

      if (!this.level().isClientSide && this.tickCount % 40 == 0 && this.isFullyFrozen() && this.canFreeze()) {
         this.hurt(this.damageSources().freeze(), 1.0F);
      }

      this.level().getProfiler().pop();
      this.level().getProfiler().push("push");

      this.pushEntities();
      this.level().getProfiler().pop();
   }

   protected void pushEntities() {
      if (this.level().isClientSide()) {
         this.level().getEntities(EntityTypeTest.forClass(Player.class), this.getBoundingBox(), EntitySelector.pushableBy(this)).forEach(this::doPush);
      } else {
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

               if (j > i - 1) {
                  this.hurt(this.damageSources().cramming(), 6.0F);
               }
            }

            for(j = 0; j < list.size(); ++j) {
               Entity entity = (Entity)list.get(j);
               this.doPush(entity);
            }
         }

      }
   }

   protected void doPush(Entity entity) {
      entity.push(this);
   }

   public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 vec3, float f) {
      this.moveRelative(this.getFrictionInfluencedSpeed(f), vec3);
      this.move(MoverType.SELF, this.getDeltaMovement());
      Vec3 vec32 = this.getDeltaMovement();
      if (this.horizontalCollision && (this.getFeetBlockState().is(Blocks.POWDER_SNOW) && PowderSnowBlock.canEntityWalkOnPowderSnow(this))) {
         vec32 = new Vec3(vec32.x, 0.2, vec32.z);
      }

      return vec32;
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


   private float getFrictionInfluencedSpeed(float f) {
      return this.onGround() ? this.getSpeed() * (0.21600002F / (f * f * f)) : this.getFlyingSpeed();
   }

   protected float getFlyingSpeed() {
      return this.getControllingPassenger() instanceof Player ? this.getSpeed() * 0.1F : 0.02F;
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