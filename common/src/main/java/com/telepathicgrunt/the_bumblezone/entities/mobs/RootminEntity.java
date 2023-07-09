package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.DirtPelletEntity;
import com.telepathicgrunt.the_bumblezone.items.BeeArmor;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class RootminEntity extends PathfinderMob implements Enemy {

   private static final EntityDataAccessor<Optional<BlockState>> FLOWER_BLOCK_STATE = SynchedEntityData.defineId(RootminEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
   public static final EntityDataSerializer<RootminPose> ROOTMIN_POSE_SERIALIZER = EntityDataSerializer.simpleEnum(RootminPose.class);
   private static final EntityDataAccessor<RootminPose> ROOTMIN_POSE = SynchedEntityData.defineId(RootminEntity.class, ROOTMIN_POSE_SERIALIZER);

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

   private boolean checkedDefaultFlowerTag = false;
   private boolean isHidden = false;
   private boolean disableAttackGoals = false;
   private RootminEntity rootminToLookAt = null;
   private LivingEntity attackerMemory = null;
   private int delayTillIdle = -1;
   private boolean takePotShot = false;
   private int exposedTimer = 0;
   public int animationTimeBetweenHiding = 0;

   private static final Set<RootminPose> POSES_THAT_CANT_BE_MOTION_INTERRUPTED = Set.of(
           RootminPose.ANGRY,
           RootminPose.CURIOUS,
           RootminPose.CURSE,
           RootminPose.EMBARRASSED,
           RootminPose.SHOOT,
           RootminPose.SHOCK,
           RootminPose.BLOCK_TO_ENTITY,
           RootminPose.ENTITY_TO_BLOCK
   );

   private static final Set<RootminPose> POSES_THAT_CAN_BE_FEAR_INTERRUPTED = Set.of(
           RootminPose.ANGRY,
           RootminPose.CURIOUS,
           RootminPose.CURSE,
           RootminPose.EMBARRASSED,
           RootminPose.SHOCK
   );

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
      this.delayTillIdle = -1;
      this.animationTimeBetweenHiding = 20;
      setRootminPose(RootminPose.ENTITY_TO_BLOCK);
      this.getNavigation().stop();
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(3, new EmbarrassedCurseGoal(this));
      this.goalSelector.addGoal(4, new HiddenGoal(this));
      this.goalSelector.addGoal(5, new AvoidEntityGoal(this, BzTags.ROOTMIN_PANIC_AVOID, 16.0f, 1.5, 2.5));
      this.goalSelector.addGoal(6, new HideGoal(this));
      this.goalSelector.addGoal(14, new RangedAttackGoal(this, 1.25, 20, 15, 30.0f));
      this.targetSelector.addGoal(15, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(16, new NearestAttackableTargetGoal(this, true));
   }

   public static AttributeSupplier.Builder getAttributeBuilder() {
      return Mob.createMobAttributes()
              .add(Attributes.MAX_HEALTH, 10.0D)
              .add(Attributes.MOVEMENT_SPEED, 0.18D)
              .add(Attributes.ATTACK_DAMAGE, 3.0D)
              .add(Attributes.FOLLOW_RANGE, 30.0D);
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
                      compound.getCompound("flowerBlock"))).isAir()) {
         blockState = null;
      }

      if (blockState == null) {
         this.getFlowerBlock();
      }
      else {
         this.setFlowerBlock(blockState);
      }

      this.isHidden = compound.getBoolean("hidden");
      this.delayTillIdle = compound.getInt("delayTillIdle");
      if (this.isHidden) {
         this.setRootminPose(RootminPose.ENTITY_TO_BLOCK);
      } else {
         if (compound.contains("animationState")) {
            this.setRootminPose(RootminPose.valueOf(compound.getString("animationState")));
         }
      }
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
         if (pose== RootminPose.BLOCK_TO_ENTITY || pose == RootminPose.ENTITY_TO_BLOCK) {
            this.animationTimeBetweenHiding = 20;
         }

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
         setAnimationState(pose, RootminPose.ENTITY_TO_BLOCK, this.entityToBlockAnimationState, this.tickCount <= 2 ? -100000 : this.tickCount);

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
                  LootParams.Builder builder = new LootParams.Builder((ServerLevel) this.level()).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.TOOL, itemStack).withOptionalParameter(LootContextParams.THIS_ENTITY, this);
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

      if (itemstack.isEmpty() && !this.level().isClientSide()) {
         runEmbarrassed();
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
         if (!this.level().isClientSide()) {
            this.isHidden = false;
            if (this.getRootminPose() != RootminPose.SHOOT && this.getRootminPose() != RootminPose.CURSE) {
               runShock();
            }
         }
      }

      if (!this.level().isClientSide()) {
         double horizontalSpeed = this.getDeltaMovement().horizontalDistance();
         if (!POSES_THAT_CANT_BE_MOTION_INTERRUPTED.contains(getRootminPose())) {
            if (horizontalSpeed > 0.2d || this.hurtTime > 0) {
               setRootminPose(RootminPose.RUN);
            }
            else if (horizontalSpeed > 0.01d) {
               setRootminPose(RootminPose.WALK);
            }
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
      if (this.exposedTimer > 0) {
         this.exposedTimer--;
      }
   }

   @Override
   protected void dropAllDeathLoot(DamageSource damageSource) {
      BlockState flower = this.getFlowerBlock();
      Entity sourceEntity = damageSource.getEntity() == null ? this : damageSource.getEntity();
      if (flower != null) {
         ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
         itemStack.enchant(Enchantments.SILK_TOUCH, 1);
         LootParams.Builder builder = new LootParams.Builder((ServerLevel) this.level())
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
      return this.getRootminPose() == RootminPose.ENTITY_TO_BLOCK && !this.isDeadOrDying();
   }

   @Override
   protected AABB makeBoundingBox() {
      if (this.getRootminPose() == RootminPose.BLOCK_TO_ENTITY || this.getRootminPose() == RootminPose.ENTITY_TO_BLOCK) {
         AABB currentAABB = super.makeBoundingBox();
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

   private static boolean isFacingMob(RootminEntity rootminEntity, LivingEntity target) {
      Vec3 targetView = target.getLookAngle().normalize();
      Vec3 currentDirection = rootminEntity.position().subtract(target.position()).normalize();

      double dotProduct =
              (currentDirection.x() * targetView.x()) +
              (currentDirection.y() * targetView.y()) +
              (currentDirection.z() * targetView.z());

      return dotProduct >= 0;
   }


   ///////////////////////////////////////////////////////
   //GOALS

   private static class EmbarrassedCurseGoal extends Goal {
      protected final RootminEntity mob;
      protected int timer = 0;

      public EmbarrassedCurseGoal(RootminEntity pathfinderMob) {
         this.mob = pathfinderMob;
         this.setFlags(EnumSet.of(Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         boolean isCursing = this.mob.getRootminPose() == RootminPose.CURSE;
         boolean isEmbarrassed = this.mob.getRootminPose() == RootminPose.EMBARRASSED;
         return this.mob.rootminToLookAt != null && (isCursing || isEmbarrassed);
      }

      @Override
      public boolean canContinueToUse() {
         Bumblezone.LOGGER.warn("Rootmin event: {}, {}, {}, {}", this.mob, this.timer, this.mob.getRootminPose().name(), this.mob.delayTillIdle);

         return this.timer > 0 &&
                 this.mob.rootminToLookAt != null &&
                 !this.mob.isDeadOrDying() &&
                 (this.mob.getRootminPose() == RootminPose.CURSE || this.mob.getRootminPose() == RootminPose.EMBARRASSED);
      }

      @Override
      public void start() {
         this.timer = 40;
      }

      @Override
      public void stop() {
         this.timer = 0;
         this.mob.disableAttackGoals = false;
         this.mob.rootminToLookAt = null;
         this.mob.exposedTimer = 0;
         this.mob.setRootminPose(RootminPose.NONE);
      }

      @Override
      public void tick() {
         this.mob.getNavigation().stop();
         if (this.mob.rootminToLookAt == null) {
            this.timer = 0;
         }
         else {
            this.mob.lookAt(this.mob.rootminToLookAt, 30, 30);
            this.timer--;
         }
      }
   }

   private static class HideGoal extends Goal {
      protected final RootminEntity mob;
      @Nullable
      protected Path path;
      protected final PathNavigation pathNav;
      @Nullable
      protected Vec3 destination;

      public HideGoal(RootminEntity pathfinderMob) {
         this.mob = pathfinderMob;
         this.setFlags(EnumSet.of(Flag.MOVE));
         this.pathNav = pathfinderMob.getNavigation();
      }

      @Override
      public boolean canUse() {
         if (this.mob.exposedTimer != 0 || this.mob.rootminToLookAt != null) {
            return false;
         }

         if (this.mob.getTarget() != null && !(this.mob.getTarget() instanceof Player)) {
            return false;
         }

         if (this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 1200 &&
              this.mob.attackerMemory != null &&
              !this.mob.attackerMemory.isDeadOrDying() &&
              this.mob.attackerMemory.blockPosition().distManhattan(this.mob.blockPosition()) < 25)
         {
            if (isFacingMob(this.mob, this.mob.attackerMemory)) {
               return false;
            }
         }

         Level level = this.mob.level();
         RandomSource randomSource = this.mob.getRandom();
         BlockPos chosenPos = this.mob.blockPosition().offset(
                 randomSource.nextInt(11) - 5,
                 randomSource.nextInt(5) - 2,
                 randomSource.nextInt(11) - 5);

         if (!level.isEmptyBlock(chosenPos)) {
            return false;
         }

         BlockState belowState = level.getBlockState(chosenPos.below());
         if (!belowState.isCollisionShapeFullBlock(level, chosenPos.below())) {
            return false;
         }

         List<Entity> existingRootmins = level.getEntities(
                 this.mob,
                 new AABB(
                         chosenPos.getX(),
                         chosenPos.getY(),
                         chosenPos.getZ(),
                         chosenPos.getX() + 1,
                         chosenPos.getY() + 1,
                         chosenPos.getZ() + 1
                 ),
                 (entity) -> entity.getType() == BzEntities.ROOTMIN.get()
         );

         if (!existingRootmins.isEmpty()) {
            return false;
         }

         this.destination = new Vec3(chosenPos.getX() + 0.5d, chosenPos.getY(), chosenPos.getZ() + 0.5d);
         this.path = this.pathNav.createPath(chosenPos.getX() + 0.5d, chosenPos.getY(), chosenPos.getZ() + 0.5d, 0);
         return this.path != null;
      }

      @Override
      public boolean canContinueToUse() {
         return !this.pathNav.isDone() && !this.mob.isDeadOrDying();
      }

      @Override
      public void start() {
         this.pathNav.moveTo(this.path,
                 (this.mob.takePotShot || this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 200) ?
                 2.0 : 1.0);
      }

      @Override
      public void stop() {
         if (this.destination != null && this.mob.position().subtract(this.destination).length() < 1) {
            this.mob.moveTo(this.destination);
         }
         this.mob.setDeltaMovement(Vec3.ZERO);
         this.mob.takePotShot = false;
         this.mob.hideAsBlock();
      }

      @Override
      public void tick() {
         boolean isFleeing = (this.mob.takePotShot || this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 200);
         this.mob.getNavigation().setSpeedModifier(isFleeing ? 2.0 : 1.0);
      }
   }

   private static class HiddenGoal extends Goal {
      protected final RootminEntity mob;
      protected int unhidingTimer = 0;
      protected int stayHidingTimer = 200;

      public HiddenGoal(RootminEntity pathfinderMob) {
         this.mob = pathfinderMob;
         this.setFlags(EnumSet.of(Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         return this.mob.isHidden;
      }

      @Override
      public boolean canContinueToUse() {
         return (this.unhidingTimer > 0 || this.mob.isHidden) && this.mob.hurtTime == 0 && !this.mob.isDeadOrDying();
      }

      @Override
      public void start() {
         this.stayHidingTimer = 200;
         this.unhidingTimer = 0;
      }

      @Override
      public void stop() {
      }

      @Override
      public void tick() {
         float lookAngle = this.mob.getYRot();
         Direction direction = Direction.fromYRot(lookAngle);
         Vec3 lookVec = Vec3.atLowerCornerOf(direction.getNormal()).add(this.mob.position());
         this.mob.getLookControl().setLookAt(lookVec.x(), lookVec.y(), lookVec.z(), 60, 0);
         this.mob.setYRot(direction.toYRot());

         if (this.stayHidingTimer == 0) {
            if (this.unhidingTimer > 0) {
               this.unhidingTimer--;
            }
            else if (this.unhidingTimer == 0) {
               LivingEntity target = this.mob.getTarget();
               if (target != null) {
                  int distance = target.blockPosition().distManhattan(this.mob.blockPosition());
                  if (distance >= 8 && distance <= 26) {
                     if (!isFacingMob(this.mob, target)) {
                        this.unhidingTimer = 20;
                        this.mob.exposeFromBlock();
                        this.mob.exposedTimer = 160;
                        this.mob.takePotShot = true;
                     }
                  }
               }
            }
         }
         else {
            this.stayHidingTimer--;
         }
      }
   }

   private static class AvoidEntityGoal extends Goal {
      protected final RootminEntity mob;
      private final double walkSpeedModifier;
      private final double sprintSpeedModifier;
      @Nullable
      protected LivingEntity toAvoid;
      protected final float maxDist;
      @Nullable
      protected Path path;
      protected final PathNavigation pathNav;
      protected final TagKey<EntityType<?>> avoidTag;
      protected final Predicate<LivingEntity> avoidPredicate;
      protected final Predicate<LivingEntity> predicateOnAvoidEntity;
      private final TargetingConditions avoidEntityTargeting;

      public AvoidEntityGoal(RootminEntity pathfinderMob, TagKey<EntityType<?>> typeTagKey, float range, double walkSpeedModifier, double runSpeedModifier) {
         this(pathfinderMob, typeTagKey, livingEntity -> true, range, walkSpeedModifier, runSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
      }

      public AvoidEntityGoal(RootminEntity pathfinderMob, TagKey<EntityType<?>> typeTagKey, Predicate<LivingEntity> predicate, float range, double walkSpeedModifier, double runSpeedModifier, Predicate<LivingEntity> predicate2) {
         this.mob = pathfinderMob;
         this.avoidTag = typeTagKey;
         this.avoidPredicate = predicate;
         this.maxDist = range;
         this.walkSpeedModifier = walkSpeedModifier;
         this.sprintSpeedModifier = runSpeedModifier;
         this.predicateOnAvoidEntity = predicate2;
         this.pathNav = pathfinderMob.getNavigation();
         this.setFlags(EnumSet.of(Goal.Flag.MOVE));
         this.avoidEntityTargeting = TargetingConditions.forCombat().range(range).selector(predicate2.and(predicate));
      }

      @Override
      public boolean canUse() {
         if (this.mob.animationTimeBetweenHiding > 0 || this.mob.isHidden) {
            return false;
         }

         if (this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 1200 && this.mob.attackerMemory != null) {
            this.toAvoid = this.mob.attackerMemory;
         }
         else if (this.mob.tickCount - this.mob.getLastHurtByMobTimestamp() < 1200 && this.mob.getLastHurtByMob() != null) {
            this.toAvoid = this.mob.getLastHurtByMob();
            this.mob.attackerMemory = this.mob.getLastHurtByMob();
         }
         else {
            this.toAvoid = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(
                            LivingEntity.class,
                            this.mob.getBoundingBox().inflate(this.maxDist, 3.0, this.maxDist),
                            livingEntity -> livingEntity.getType().is(this.avoidTag)),
                    this.avoidEntityTargeting,
                    this.mob,
                    this.mob.getX(),
                    this.mob.getY(),
                    this.mob.getZ());
         }

         if (this.toAvoid == null) {
            return false;
         }
         Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 26, 8, this.toAvoid.position());
         if (vec3 == null) {
            return false;
         }
         if (this.toAvoid.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.toAvoid.distanceToSqr(this.mob)) {
            return false;
         }
         this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
         return this.path != null;
      }

      @Override
      public boolean canContinueToUse() {
         if (this.pathNav.isDone() || (this.toAvoid != null && this.toAvoid.isDeadOrDying())) {
            return false;
         }
         else if (this.mob.animationTimeBetweenHiding > 0 || this.mob.isHidden) {
            return false;
         }
         else {
            this.mob.rootminToLookAt = null;
            return true;
         }
      }

      @Override
      public void start() {
         this.pathNav.moveTo(this.path, this.mob.distanceToSqr(this.toAvoid) < (14 * 14) ? this.sprintSpeedModifier : this.walkSpeedModifier);
      }

      @Override
      public void stop() {
         this.toAvoid = null;
      }

      @Override
      public void tick() {
         if (this.mob.distanceToSqr(this.toAvoid) < (14 * 14)) {
            this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);

            if (POSES_THAT_CAN_BE_FEAR_INTERRUPTED.contains(this.mob.getRootminPose())) {
               this.mob.setRootminPose(RootminPose.RUN);
            }
         }
         else {
            this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);

            if (POSES_THAT_CAN_BE_FEAR_INTERRUPTED.contains(this.mob.getRootminPose())) {
               this.mob.setRootminPose(RootminPose.WALK);
            }
         }

      }
   }

   private static class NearestAttackableTargetGoal extends TargetGoal {
      private static final int DEFAULT_RANDOM_INTERVAL = 10;
      protected final int randomInterval;
      @Nullable
      protected LivingEntity target;
      protected TargetingConditions targetConditions;

      public NearestAttackableTargetGoal(Mob mob, boolean mustSee) {
         this(mob, DEFAULT_RANDOM_INTERVAL, mustSee, null);
      }

      public NearestAttackableTargetGoal(Mob mob, boolean mustSee, Predicate<LivingEntity> targetPredicate) {
         this(mob, DEFAULT_RANDOM_INTERVAL, mustSee, targetPredicate);
      }

      public NearestAttackableTargetGoal(Mob mob, int randomInterval, boolean mustSee, @Nullable Predicate<LivingEntity> targetPredicate) {
         super(mob, mustSee, false);
         this.randomInterval = reducedTickDelay(randomInterval);
         this.setFlags(EnumSet.of(Goal.Flag.TARGET));
         this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(targetPredicate);
      }

      @Override
      public boolean canUse() {
         if ((this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) || ((this.mob instanceof RootminEntity rootminEntity && rootminEntity.disableAttackGoals))) {
            return false;
         }
         this.findTarget();
         return this.target != null;
      }

      protected AABB getTargetSearchArea(double d) {
         return this.mob.getBoundingBox().inflate(d, 4.0, d);
      }

      protected void findTarget() {
         this.target = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(
                         LivingEntity.class,
                         this.getTargetSearchArea(this.getFollowDistance()),
                         livingEntity -> {
                            boolean canTarget = (BeeAggression.doesBeesHateEntity(livingEntity) || livingEntity.getType().is(BzTags.ROOTMIN_TARGETS)) &&
                                    !livingEntity.getType().is(BzTags.ROOTMIN_FORCED_DO_NOT_TARGET);

                            if (canTarget && livingEntity instanceof Player player) {
                               if (BeeArmor.getBeeThemedGearCount(player) > 0) {
                                  return false;
                               }
                            }
                            return canTarget;
                         }),
                 this.targetConditions,
                 this.mob,
                 this.mob.getX(),
                 this.mob.getEyeY(),
                 this.mob.getZ());
      }

      @Override
      public void start() {
         this.mob.setTarget(this.target);
         super.start();
      }

      public void setTarget(@Nullable LivingEntity livingEntity) {
         this.target = livingEntity;
      }
   }

   private static class RangedAttackGoal extends Goal {
      private final Mob mob;
      private final RootminEntity rootminEntity;
      @Nullable
      private LivingEntity target;
      private int attackTime = -1;
      private final double speedModifier;
      private int seeTime;
      private final int attackIntervalMin;
      private final int attackIntervalMax;
      private final float attackRadius;
      private final float attackRadiusSqr;

      public RangedAttackGoal(RootminEntity rootminEntity, double speedModifier, int attackInterval, float attackRadius) {
         this(rootminEntity, speedModifier, attackInterval, attackInterval, attackRadius);
      }

      public RangedAttackGoal(RootminEntity rootminEntity, double speedModifier, int attackIntervalMax, int attackIntervalMin, float attackRadius) {
         this.rootminEntity = rootminEntity;
         this.mob = rootminEntity;
         this.speedModifier = speedModifier;
         this.attackIntervalMin = attackIntervalMax;
         this.attackIntervalMax = attackIntervalMin;
         this.attackRadius = attackRadius;
         this.attackRadiusSqr = attackRadius * attackRadius;
         this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         LivingEntity livingEntity = this.mob.getTarget();
         if (livingEntity == null || !livingEntity.isAlive() || (this.mob instanceof RootminEntity rootminEntity && rootminEntity.disableAttackGoals)) {
            return false;
         }
         this.target = livingEntity;
         return true;
      }

      @Override
      public boolean canContinueToUse() {
         return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
      }

      @Override
      public void stop() {
         this.target = null;
         this.seeTime = 0;
         this.attackTime = -1;
      }

      @Override
      public boolean requiresUpdateEveryTick() {
         return true;
      }

      @Override
      public void tick() {
         double d = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
         boolean bl = this.mob.getSensing().hasLineOfSight(this.target);
         this.seeTime = bl ? ++this.seeTime : 0;
         if (d > (double) this.attackRadiusSqr || this.seeTime < 5) {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
         }
         else {
            this.mob.getNavigation().stop();
         }
         this.mob.getLookControl().setLookAt(this.target, 30.0f, 30.0f);
         if (--this.attackTime == 0) {
            if (!bl) {
               return;
            }
            float f = (float) Math.sqrt(d) / this.attackRadius;
            this.rootminEntity.runShoot(this.target);
            this.rootminEntity.exposedTimer = 0;
            this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
         }
         else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d) / (double) this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
         }
      }
   }

   private static class HurtByTargetGoal extends TargetGoal {
      private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
      private int timestamp;

    public HurtByTargetGoal(PathfinderMob mob){
         super(mob, true);
         this.setFlags(EnumSet.of(Goal.Flag.TARGET));
      }

      public boolean canUse () {
         int lastHurtByMobTimestamp = this.mob.getLastHurtByMobTimestamp();
         LivingEntity livingEntity = this.mob.getLastHurtByMob();
         if (lastHurtByMobTimestamp != this.timestamp && livingEntity != null) {
            if (livingEntity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
               return false;
            }
            else {
               if (livingEntity instanceof RootminEntity rootminEntityAttacker &&
                    this.mob instanceof RootminEntity rootminEntity &&
                    !rootminEntity.isInvulnerable())
               {
                  rootminEntity.runCurse();
                  rootminEntityAttacker.runEmbarrassed();

                  rootminEntity.disableAttackGoals = true;
                  rootminEntityAttacker.disableAttackGoals = true;

                  rootminEntity.rootminToLookAt = rootminEntityAttacker;
                  rootminEntityAttacker.rootminToLookAt = rootminEntity;

                  this.timestamp = this.mob.getLastHurtByMobTimestamp();
                  return false;
               }

               return this.canAttack(livingEntity, HURT_BY_TARGETING);
            }
         }
         else {
            return false;
         }
      }

      public void start () {
         this.mob.setTarget(this.mob.getLastHurtByMob());
         this.targetMob = this.mob.getTarget();
         this.timestamp = this.mob.getLastHurtByMobTimestamp();
         this.unseenMemoryTicks = 300;
         super.start();
      }
   }

   private static class LookAtPlayerGoal extends Goal {
      public static final float DEFAULT_PROBABILITY = 0.02f;
      protected final Mob mob;
      @Nullable
      protected Entity lookAt;
      protected final float lookDistance;
      private int lookTime;
      protected final float probability;
      private final boolean onlyHorizontal;
      protected final Class<? extends LivingEntity> lookAtType;
      protected final TargetingConditions lookAtContext;

      public LookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> class_, float f) {
         this(mob, class_, f, DEFAULT_PROBABILITY);
      }

      public LookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> class_, float f, float g) {
         this(mob, class_, f, g, false);
      }

      public LookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> class_, float f, float g, boolean bl) {
         this.mob = mob;
         this.lookAtType = class_;
         this.lookDistance = f;
         this.probability = g;
         this.onlyHorizontal = bl;
         this.setFlags(EnumSet.of(Goal.Flag.LOOK));
         this.lookAtContext = class_ == Player.class ? TargetingConditions.forNonCombat().range(f).selector(livingEntity -> EntitySelector.notRiding(mob).test((Entity)livingEntity)) : TargetingConditions.forNonCombat().range(f);
      }

      @Override
      public boolean canUse() {
         if (this.mob instanceof RootminEntity rootminEntity && rootminEntity.isHidden) {
            return false;
         }
         if (this.mob.getRandom().nextFloat() >= this.probability) {
            return false;
         }
         if (this.mob.getTarget() != null) {
            this.lookAt = this.mob.getTarget();
         }
         this.lookAt = this.lookAtType == Player.class ? this.mob.level().getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ()) : this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate(this.lookDistance, 3.0, this.lookDistance), livingEntity -> true), this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
         return this.lookAt != null;
      }

      @Override
      public boolean canContinueToUse() {
         if (!this.lookAt.isAlive()) {
            return false;
         }
         if (this.mob.distanceToSqr(this.lookAt) > (double)(this.lookDistance * this.lookDistance)) {
            return false;
         }
         return this.lookTime > 0;
      }

      @Override
      public void start() {
         this.lookTime = this.adjustedTickDelay(40 + this.mob.getRandom().nextInt(40));
      }

      @Override
      public void stop() {
         this.lookAt = null;
      }

      @Override
      public void tick() {
         if (!this.lookAt.isAlive()) {
            return;
         }
         double d = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
         this.mob.getLookControl().setLookAt(this.lookAt.getX(), d, this.lookAt.getZ());
         --this.lookTime;
      }
   }

   private static class RandomLookAroundGoal extends Goal {
      private final Mob mob;
      private double relX;
      private double relZ;
      private int lookTime;

      public RandomLookAroundGoal(Mob mob) {
         this.mob = mob;
         this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         if (this.mob instanceof RootminEntity rootminEntity && rootminEntity.isHidden) {
            return false;
         }
         return this.mob.getRandom().nextFloat() < 0.02f;
      }

      @Override
      public boolean canContinueToUse() {
         return this.lookTime >= 0;
      }

      @Override
      public void start() {
         double d = Math.PI * 2 * this.mob.getRandom().nextDouble();
         this.relX = Math.cos(d);
         this.relZ = Math.sin(d);
         this.lookTime = 20 + this.mob.getRandom().nextInt(20);
      }

      @Override
      public boolean requiresUpdateEveryTick() {
         return true;
      }

      @Override
      public void tick() {
         --this.lookTime;
         this.mob.getLookControl().setLookAt(this.mob.getX() + this.relX, this.mob.getEyeY(), this.mob.getZ() + this.relZ);
      }
   }
}