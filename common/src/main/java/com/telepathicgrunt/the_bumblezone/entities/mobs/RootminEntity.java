package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeAngerAttackingGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeFaceRandomGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeFloatGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeHopGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeRevengeGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeTemptGoal;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RootminEntity extends PathfinderMob implements Enemy {

   public RootminEntity(Level worldIn) {
      super(BzEntities.ROOTMIN.get(), worldIn);
   }

   public RootminEntity(EntityType<? extends RootminEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   protected void registerGoals() {
   }

   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
      this.refreshDimensions();
      this.setYRot(this.yHeadRot);
      this.setYBodyRot(this.yHeadRot);
      if (this.isInWater() && this.random.nextInt(20) == 0) {
         this.doWaterSplashEffect();
      }
      super.onSyncedDataUpdated(key);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
   }

   public static AttributeSupplier.Builder getAttributeBuilder() {
	 return Mob.createMobAttributes()
             .add(Attributes.MAX_HEALTH, 10.0D)
             .add(Attributes.MOVEMENT_SPEED, 2.0D)
             .add(Attributes.ATTACK_DAMAGE, 3.0D);
   }

   @Override
   public boolean checkSpawnRules(LevelAccessor world, MobSpawnType spawnReason) {
      return true;
   }

   @Override
   public void remove(RemovalReason removalReason) {
      super.remove(removalReason);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);

      return super.mobInteract(player, hand);
   }

   @Override
   public void tick() {
      super.tick();
   }

   @Override
   public void aiStep() {
      super.aiStep();
   }

   @Override
   protected void customServerAiStep() {
   }

   @Override
   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return 0.75F * sizeIn.height;
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
   public boolean canBeLeashed(Player player) {
      return false;
   }

   @Override
   public float getSoundVolume() {
      return 0.4F * (float) (isBaby() ? 1 : 2);
   }

   @Override
   public int getMaxHeadXRot() {
      return 0;
   }

   // TODO: custom sounds for rootmin
   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return this.isBaby() ? BzSounds.HONEY_SLIME_HURT_SMALL.get() : BzSounds.HONEY_SLIME_HURT.get();
   }

   @Override
   protected SoundEvent getDeathSound() {
      return this.isBaby() ? BzSounds.HONEY_SLIME_DEATH_SMALL.get() : BzSounds.HONEY_SLIME_DEATH.get();
   }
}