package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.DirtPelletEntity;
import com.telepathicgrunt.the_bumblezone.items.BeeArmor;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RootminEntity extends PathfinderMob implements Enemy {

   private static final EntityDataAccessor<Optional<BlockState>> FLOWER_BLOCK_STATE = SynchedEntityData.defineId(RootminEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);

   public final AnimationState idleAnimationState = new AnimationState();
   public final AnimationState angryAnimationState = new AnimationState();
   public final AnimationState curiousAnimationState = new AnimationState();
   public final AnimationState curseAnimationState = new AnimationState();
   public final AnimationState embarassedAnimationState = new AnimationState();
   public final AnimationState shockAnimationState = new AnimationState();
   public final AnimationState shootAnimationState = new AnimationState();
   public final AnimationState runAnimationState = new AnimationState();
   public final AnimationState walkAnimationState = new AnimationState();
   public final AnimationState blockToEntityAnimationState = new AnimationState();
   public final AnimationState entityToBlockAnimationState = new AnimationState();

   public static final EntityDataSerializer<RootminPose> ROOTMIN_POSE_SERIALIZER = EntityDataSerializer.simpleEnum(RootminPose.class);
   private static final EntityDataAccessor<RootminPose> ROOTMIN_POSE = SynchedEntityData.defineId(RootminEntity.class, ROOTMIN_POSE_SERIALIZER);

   private boolean checkedDefaultFlowerTag = false;
   private boolean isHidden = false;
   private int delayTillIdle = -1;
   public int animationTimeBetweenHiding = 0;

   public RootminEntity(Level worldIn) {
      super(BzEntities.ROOTMIN.get(), worldIn);
      getFlowerBlock();
      setAnimationState(this.getRootminPose(), RootminPose.NONE, this.idleAnimationState);
   }

   public RootminEntity(EntityType<? extends RootminEntity> type, Level worldIn) {
      super(type, worldIn);
      getFlowerBlock();
      setAnimationState(this.getRootminPose(), RootminPose.NONE, this.idleAnimationState);
   }

   public void setFlowerBlock(@Nullable BlockState blockState) {
      this.entityData.set(FLOWER_BLOCK_STATE, Optional.ofNullable(blockState));
   }

   @Nullable
   public BlockState getFlowerBlock() {
      BlockState state = this.entityData.get(FLOWER_BLOCK_STATE).orElse(null);
      state = getFlowerOrSetIfMissing(state);
      return state;
   }

   @Nullable
   private BlockState getFlowerOrSetIfMissing(BlockState state) {
      if (state == null && !this.level().isClientSide() && !this.checkedDefaultFlowerTag) {
         List<Block> blockList = BuiltInRegistries.BLOCK
                 .getTag(BzTags.ROOTMIN_DEFAULT_FLOWERS)
                 .map(holders -> holders
                         .stream()
                         .map(Holder::value)
                         .toList()
                 ).orElseGet(ArrayList::new);

         state = blockList.isEmpty() ?
                 null :
                 blockList.get(this.level().getRandom().nextInt(blockList.size())).defaultBlockState();

         if (state != null && state.isAir()) {
            state = null;
         }

         setFlowerBlock(state);
         this.checkedDefaultFlowerTag = true;
      }
      return state;
   }

   public void setRootminPose(RootminPose rootminPose) {
      this.entityData.set(ROOTMIN_POSE, rootminPose);
   }

   public RootminPose getRootminPose() {
      return this.entityData.get(ROOTMIN_POSE);
   }

   public void runAngry() {
      this.delayTillIdle = 40;
      setRootminPose(RootminPose.ANGRY);
   }

   public void runCurious() {
      this.delayTillIdle = 28;
      setRootminPose(RootminPose.CURIOUS);
   }

   public void runCurse() {
      this.delayTillIdle = 80;
      setRootminPose(RootminPose.CURSE);
   }

   public void runEmbarrassed() {
      this.delayTillIdle = 60;
      setRootminPose(RootminPose.EMBARRASSED);
   }

   public void runShock() {
      this.delayTillIdle = 10;
      setRootminPose(RootminPose.SHOCK);
   }

   public void runShoot(@Nullable LivingEntity target) {
      this.shootDirt(target);
      this.delayTillIdle = 8;
      setRootminPose(RootminPose.SHOOT);
   }

   public void exposeFromBlock() {
      this.isHidden = false;
      this.delayTillIdle = 20;
      this.animationTimeBetweenHiding = 20;
      setRootminPose(RootminPose.BLOCK_TO_ENTITY);
   }

   public void hideAsBlock() {
      this.isHidden = true;
      this.animationTimeBetweenHiding = 20;
      setRootminPose(RootminPose.ENTITY_TO_BLOCK);
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
      this.entityData.define(FLOWER_BLOCK_STATE, Optional.empty());
      this.entityData.define(ROOTMIN_POSE, RootminPose.NONE);
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
      if (ROOTMIN_POSE.equals(entityDataAccessor)) {
         RootminPose pose = this.getRootminPose();
         setAnimationState(pose, RootminPose.NONE, this.idleAnimationState, this.tickCount - 27);
         setAnimationState(pose, RootminPose.ANGRY, this.angryAnimationState);
         setAnimationState(pose, RootminPose.CURIOUS, this.curiousAnimationState);
         setAnimationState(pose, RootminPose.CURSE, this.curseAnimationState);
         setAnimationState(pose, RootminPose.EMBARRASSED, this.embarassedAnimationState);
         setAnimationState(pose, RootminPose.SHOCK, this.shockAnimationState);
         setAnimationState(pose, RootminPose.SHOOT, this.shootAnimationState);
         setAnimationState(pose, RootminPose.RUN, this.runAnimationState);
         setAnimationState(pose, RootminPose.WALK, this.walkAnimationState);
         setAnimationState(pose, RootminPose.BLOCK_TO_ENTITY, this.blockToEntityAnimationState);
         setAnimationState(pose, RootminPose.ENTITY_TO_BLOCK, this.entityToBlockAnimationState, this.tickCount <= 20 ? -100000 : this.tickCount);

         if (this.getRootminPose() == RootminPose.BLOCK_TO_ENTITY || this.getRootminPose() == RootminPose.ENTITY_TO_BLOCK) {
            this.animationTimeBetweenHiding = 20;
         }
      }

      this.refreshDimensions();
      this.setYRot(this.yHeadRot);
      this.setYBodyRot(this.yHeadRot);
      if (this.isInWater() && this.random.nextInt(20) == 0) {
         this.doWaterSplashEffect();
      }

      super.onSyncedDataUpdated(entityDataAccessor);
   }

   private void setAnimationState(RootminPose pose, RootminPose poseToCheckFor, AnimationState animationState) {
      if (pose == poseToCheckFor) {
         animationState.start(this.tickCount);
      }
      else {
         animationState.stop();
      }
   }

   private void setAnimationState(RootminPose pose, RootminPose poseToCheckFor, AnimationState animationState, int tickCount) {
      if (pose == poseToCheckFor) {
         animationState.start(tickCount);
      }
      else {
         animationState.stop();
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      BlockState blockState = this.getFlowerBlock();
      if (blockState != null) {
         compound.put("flowerBlock", NbtUtils.writeBlockState(blockState));
      }
      compound.putBoolean("hidden", this.isHidden);
      compound.putInt("delayTillIdle", this.delayTillIdle);
      compound.putString("animationState", this.getRootminPose().name());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      BlockState blockState = null;
      if (compound.contains("flowerBlock", 10) &&
           (blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK),
           compound.getCompound("flowerBlock"))).isAir())
      {
         blockState = null;
      }
      this.setFlowerBlock(blockState);
      this.isHidden = compound.getBoolean("hidden");
      this.delayTillIdle = compound.getInt("delayTillIdle");
      if (this.isHidden) {
         this.setRootminPose(RootminPose.ENTITY_TO_BLOCK);
      }
      else {
         if (compound.contains("animationState")) {
            this.setRootminPose(RootminPose.valueOf(compound.getString("animationState")));
         }
      }
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
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResult.PASS;
      }

      ItemStack itemstack = player.getItemInHand(hand);
      boolean instantBuild = player.getAbilities().instabuild;

      if (itemstack.getItem() instanceof BlockItem blockItem && (instantBuild || BeeArmor.getBeeThemedGearCount(player) > 0)) {
         BlockState blockState = blockItem.getBlock().defaultBlockState();

         if (blockState.is(BzTags.ROOTMIN_ALLOWED_FLOWER) && !blockState.is(BzTags.ROOTMIN_FORCED_DISALLOWED_FLOWER)) {
            if (!this.level().isClientSide()) {
               if (!instantBuild && this.getFlowerBlock() != null) {
                  ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                  itemStack.enchant(Enchantments.SILK_TOUCH, 1);
                  LootParams.Builder builder = new LootParams.Builder((ServerLevel)this.level()).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.TOOL, itemStack).withOptionalParameter(LootContextParams.THIS_ENTITY, this);
                  List<ItemStack> flowerDrops = this.getFlowerBlock().getDrops(builder);
                  for (ItemStack flowerDrop : flowerDrops) {
                     this.spawnAtLocation(flowerDrop, 1.0f);
                  }
               }

               this.setFlowerBlock(blockState);
               player.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
               if (!instantBuild) {
                  itemstack.shrink(1);
               }
            }
            return InteractionResult.SUCCESS;
         }
      }

      return super.mobInteract(player, hand);
   }

   public void shootDirt(@Nullable LivingEntity livingEntity) {
      if (!this.level().isClientSide()) {
         DirtPelletEntity pelletEntity = new DirtPelletEntity(this.level(), this);
         pelletEntity.setPos(pelletEntity.position().add(this.getLookAngle().x(), 0, this.getLookAngle().z()));

         if (livingEntity != null) {
            double x = livingEntity.getX() - this.getX();
            double y = livingEntity.getY(0.3333333333333333) - pelletEntity.getY();
            double z = livingEntity.getZ() - this.getZ();
            double archOffset = Math.sqrt(x * x + z * z);
            pelletEntity.shoot(x, y + archOffset * (double) 0.2f, z, 1.5f, 1);
         }
         else {
            double defaultSpeed = 5;
            double x = this.getLookAngle().x() * defaultSpeed;
            double y = 0.3333333333333333;
            double z = this.getLookAngle().z() * defaultSpeed;
            double archOffset = Math.sqrt(x * x + z * z);
            pelletEntity.shoot(x, y + archOffset * (double) 0.2f, z, 1.5f, 1);
         }

         // #TODO: custom shoot sound
         this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
         this.level().addFreshEntity(pelletEntity);
      }
   }

   @Override
   public void tick() {
      super.tick();

      if (this.hurtTime > 0) {
         this.isHidden = false;
      }

      if (!this.level().isClientSide()) {
         double horizontalSpeed = this.getDeltaMovement().horizontalDistance();
         if (horizontalSpeed > 0.2d || this.hurtTime > 0) {
            setRootminPose(RootminPose.RUN);
         }
         else if (horizontalSpeed > 0.01d) {
            setRootminPose(RootminPose.WALK);
         }

         if (this.delayTillIdle >= 0) {
            if (delayTillIdle == 0) {
               setRootminPose(RootminPose.NONE);
            }
            this.delayTillIdle--;
         }
         else {
            if (!isHidden && this.getRootminPose() != RootminPose.NONE && this.isAlive() && horizontalSpeed <= 0.01d) {
               setRootminPose(RootminPose.NONE);
            }
         }
      }

      if (animationTimeBetweenHiding > 0) {
         animationTimeBetweenHiding--;
         if (this.getRootminPose() == RootminPose.BLOCK_TO_ENTITY || this.getRootminPose() == RootminPose.ENTITY_TO_BLOCK) {
            this.refreshDimensions();
         }
      }
   }

   @Override
   public void aiStep() {
      super.aiStep();
   }

   @Override
   protected void customServerAiStep() {
   }

   @Override
   protected void dropAllDeathLoot(DamageSource damageSource) {
      BlockState flower = this.getFlowerBlock();
      Entity sourceEntity = damageSource.getEntity() == null ? this : damageSource.getEntity();
      if (flower != null) {
         ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
         itemStack.enchant(Enchantments.SILK_TOUCH, 1);
         LootParams.Builder builder = new LootParams.Builder((ServerLevel)this.level())
                 .withParameter(LootContextParams.ORIGIN, this.position())
                 .withParameter(LootContextParams.TOOL, itemStack)
                 .withOptionalParameter(LootContextParams.THIS_ENTITY, sourceEntity);
         List<ItemStack> flowerDrops = flower.getDrops(builder);
         for (ItemStack flowerDrop : flowerDrops) {
            this.spawnAtLocation(flowerDrop, 0.5f);
         }
      }
      super.dropAllDeathLoot(damageSource);
   }

   @Override
   public boolean canBeCollidedWith() {
      return this.getRootminPose() == RootminPose.ENTITY_TO_BLOCK && this.isAlive();
   }

   @Override
   protected AABB makeBoundingBox() {
      if (this.getRootminPose() == RootminPose.BLOCK_TO_ENTITY || this.getRootminPose() == RootminPose.ENTITY_TO_BLOCK) {
         AABB currentAABB = this.getBoundingBox();
         float target = this.getRootminPose() == RootminPose.BLOCK_TO_ENTITY ? 1.56f : 1f;
         float from = this.getRootminPose() == RootminPose.BLOCK_TO_ENTITY ? 1f : 1.56f;
         float percentage = (20f - this.animationTimeBetweenHiding) / 20f;
         currentAABB = currentAABB.setMaxY(Mth.lerp(percentage, from + currentAABB.minY, target + currentAABB.minY));
         return currentAABB;
      }
      return super.makeBoundingBox();
   }

   @Override
   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return (float) (this.getBoundingBox().getYsize() - 0.575f);
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