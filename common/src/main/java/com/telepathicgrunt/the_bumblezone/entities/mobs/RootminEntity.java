package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminAngryGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminAntiGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminAvoidEntityGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminCuriosityGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminEmbarrassedCurseGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminHiddenGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminHideGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminHurtByTargetGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminNearestAttackableTargetGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.RootminRangedAttackGoal;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.DirtPelletEntity;
import com.telepathicgrunt.the_bumblezone.items.BeeArmor;
import com.telepathicgrunt.the_bumblezone.items.FlowerHeadwearHelmet;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
   public boolean isHidden = false;
   public boolean disableAttackGoals = false;
   public RootminEntity rootminToLookAt = null;
   public LivingEntity attackerMemory = null;
   public UUID superHatedPlayer = null;
   private int delayTillIdle = -1;
   public boolean takePotShot = false;
   public int exposedTimer = 0;
   public int curiosityCooldown = 60;
   public int stayHidingTimer = 200;
   private UUID essenceController = null;
   private BlockPos essenceControllerBlockPos = null;
   private ResourceKey<Level> essenceControllerDimension = null;
   public int animationTimeBetweenHiding = 0;

   public static final Set<RootminPose> POSES_THAT_CANT_BE_MOTION_INTERRUPTED = Set.of(
           RootminPose.ANGRY,
           RootminPose.CURIOUS,
           RootminPose.CURSE,
           RootminPose.EMBARRASSED,
           RootminPose.SHOOT,
           RootminPose.SHOCK,
           RootminPose.BLOCK_TO_ENTITY,
           RootminPose.ENTITY_TO_BLOCK
   );

   public static final Set<RootminPose> POSES_THAT_CAN_BE_FEAR_INTERRUPTED = Set.of(
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
      setMaxUpStep(1);
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
                 blockList.get(this.getRandom().nextInt(blockList.size())).defaultBlockState();

         if (state != null && state.isAir()) {
            state = null;
         }

         setFlowerBlock(state);
         this.checkedDefaultFlowerTag = true;
      }
      return state;
   }

   public UUID getEssenceController() {
      return essenceController;
   }

   public void setEssenceController(UUID essenceController) {
      this.essenceController = essenceController;
   }

   public BlockPos getEssenceControllerBlockPos() {
      return essenceControllerBlockPos;
   }

   public void setEssenceControllerBlockPos(BlockPos essenceControllerBlockPos) {
      this.essenceControllerBlockPos = essenceControllerBlockPos;
   }

   public ResourceKey<Level> getEssenceControllerDimension() {
      return essenceControllerDimension;
   }

   public void setEssenceControllerDimension(ResourceKey<Level> essenceControllerDimension) {
      this.essenceControllerDimension = essenceControllerDimension;
   }

   public void setRootminPose(RootminPose rootminPose) {
      this.entityData.set(ROOTMIN_POSE, rootminPose);
   }

   public RootminPose getRootminPose() {
      return this.entityData.get(ROOTMIN_POSE);
   }

   public void runAngry() {
      if (this.getRootminPose() != RootminPose.ANGRY) {
         this.playSound(BzSounds.ROOTMIN_ANGRY.get(), 1.0F, (this.getRandom().nextFloat() * 0.2F) + 0.8F);
      }

      this.delayTillIdle = 80;
      setRootminPose(RootminPose.ANGRY);
   }

   public void runCurious() {
      if (this.getRootminPose() != RootminPose.CURIOUS) {
         this.playSound(BzSounds.ROOTMIN_CURIOUS.get(), 1.0F, (this.getRandom().nextFloat() * 0.2F) + 0.8F);
      }

      this.delayTillIdle = 28;
      setRootminPose(RootminPose.CURIOUS);
   }

   public void runCurse() {
      if (this.getRootminPose() != RootminPose.CURSE) {
         this.playSound(BzSounds.ROOTMIN_CURSING.get(), 1.0F, (this.getRandom().nextFloat() * 0.2F) + 0.8F);
      }

      this.delayTillIdle = 40;
      setRootminPose(RootminPose.CURSE);
   }

   public void runEmbarrassed() {
      if (this.getRootminPose() != RootminPose.EMBARRASSED) {
         this.playSound(BzSounds.ROOTMIN_EMBARRASSED.get(), 1.0F, (this.getRandom().nextFloat() * 0.2F) + 0.8F);
      }

      this.delayTillIdle = 60;
      setRootminPose(RootminPose.EMBARRASSED);
   }

   public void runShock() {
      if (this.getRootminPose() != RootminPose.SHOCK) {
         this.playSound(BzSounds.ROOTMIN_SHOCK.get(), 1.0F, (this.getRandom().nextFloat() * 0.2F) + 0.8F);
      }

      this.delayTillIdle = 10;
      setRootminPose(RootminPose.SHOCK);
   }

   public void runShoot(@Nullable LivingEntity target, float speedMultipiler, boolean isHoming) {
      if (isHoming && target != null) {
         this.shootHomingDirt(target, speedMultipiler);
      }
      else {
         this.shootDirt(target, speedMultipiler);
      }
      this.delayTillIdle = 8;
      setRootminPose(RootminPose.SHOOT);
   }

   public void runMultiShoot(@Nullable LivingEntity target, float speedMultipiler, int projectiles) {
      this.shootDirt(target, speedMultipiler, projectiles);
      this.delayTillIdle = 8;
      setRootminPose(RootminPose.SHOOT);
   }

   public void exposeFromBlock() {
      this.isHidden = false;
      this.delayTillIdle = 20;
      this.animationTimeBetweenHiding = 20;
      setRootminPose(RootminPose.BLOCK_TO_ENTITY);
   }

   public void hideAsBlock(Vec3 destination) {
      this.isHidden = true;
      this.delayTillIdle = -1;
      this.animationTimeBetweenHiding = 20;
      setRootminPose(RootminPose.ENTITY_TO_BLOCK);
      this.getNavigation().stop();

      if (destination != null && this.position().subtract(destination).length() < 1) {
         this.moveTo(destination);
      }
      else {
         this.moveTo(Vec3.atCenterOf(this.blockPosition()));
      }
      this.setDeltaMovement(Vec3.ZERO);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new RootminAntiGoal(this));
      this.goalSelector.addGoal(2, new RootminAngryGoal(this));
      this.goalSelector.addGoal(3, new RootminEmbarrassedCurseGoal(this));
      this.goalSelector.addGoal(4, new RootminCuriosityGoal(this));
      this.goalSelector.addGoal(5, new RootminHiddenGoal(this));
      this.goalSelector.addGoal(6, new RootminAvoidEntityGoal(this, BzTags.ROOTMIN_PANIC_AVOID, 24.0f, 1.75, 2.5));
      this.goalSelector.addGoal(7, new RootminHideGoal(this));
      this.goalSelector.addGoal(8, new RootminRangedAttackGoal(this, 1.25, 20, 15, 30.0f));
      this.targetSelector.addGoal(9, new RootminHurtByTargetGoal(this));
      this.targetSelector.addGoal(10, new RootminNearestAttackableTargetGoal(this, true));
   }

   public static AttributeSupplier.Builder getAttributeBuilder() {
      return Mob.createMobAttributes()
              .add(Attributes.MAX_HEALTH, 10.0D)
              .add(Attributes.MOVEMENT_SPEED, 0.18D)
              .add(Attributes.ATTACK_DAMAGE, 3.0D)
              .add(Attributes.FOLLOW_RANGE, 30.0D);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compoundTag) {
      super.addAdditionalSaveData(compoundTag);
      BlockState blockState = this.getFlowerBlock();
      if (blockState != null) {
         compoundTag.put("flowerBlock", NbtUtils.writeBlockState(blockState));
      }
      compoundTag.putBoolean("hidden", this.isHidden);
      compoundTag.putInt("delayTillIdle", this.delayTillIdle);
      compoundTag.putString("animationState", this.getRootminPose().name());
      if (this.superHatedPlayer != null) {
         compoundTag.putUUID("superHatedPlayer", this.superHatedPlayer);
      }
      if (this.getEssenceController() != null) {
         compoundTag.putUUID("essenceController", this.getEssenceController());
      }
      if (this.getEssenceControllerBlockPos() != null) {
         compoundTag.put("essenceControllerBlockPos", NbtUtils.writeBlockPos(this.getEssenceControllerBlockPos()));
      }
      if (this.getEssenceControllerDimension() != null) {
         compoundTag.putString("essenceControllerDimension", this.getEssenceControllerDimension().location().toString());
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compoundTag) {
      super.readAdditionalSaveData(compoundTag);
      BlockState blockState = null;
      if (compoundTag.contains("flowerBlock", 10) &&
              (blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK),
                      compoundTag.getCompound("flowerBlock"))).isAir()) {
         blockState = null;
      }

      if (blockState == null) {
         this.getFlowerBlock();
      } else {
         this.setFlowerBlock(blockState);
      }

      this.isHidden = compoundTag.getBoolean("hidden");
      this.delayTillIdle = compoundTag.getInt("delayTillIdle");
      if (compoundTag.contains("superHatedPlayer")) {
         this.superHatedPlayer = compoundTag.getUUID("superHatedPlayer");
      }
      if (this.isHidden) {
         this.setRootminPose(RootminPose.ENTITY_TO_BLOCK);
      } else {
         if (compoundTag.contains("animationState")) {
            this.setRootminPose(RootminPose.valueOf(compoundTag.getString("animationState")));
         }
      }

      if (compoundTag.contains("essenceController")) {
         this.setEssenceController(compoundTag.getUUID("essenceController"));
      }
      if (compoundTag.contains("essenceControllerBlockPos")) {
         this.setEssenceControllerBlockPos(NbtUtils.readBlockPos(compoundTag.getCompound("essenceControllerBlockPos")));
      }
      if (compoundTag.contains("essenceControllerDimension")) {
         this.setEssenceControllerDimension(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(compoundTag.getString("essenceControllerDimension"))));
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
         if (pose == RootminPose.BLOCK_TO_ENTITY || pose == RootminPose.ENTITY_TO_BLOCK) {
            this.animationTimeBetweenHiding = 20;
         }

         setAnimationState(pose, RootminPose.NONE, this.idleAnimationState, this.tickCount - 27);
         setAnimationState(pose, RootminPose.ANGRY, this.angryAnimationState, BzParticles.ANGRY_PARTICLE.get(), 75, 1D);
         setAnimationState(pose, RootminPose.CURIOUS, this.curiousAnimationState, BzParticles.CURIOUS_PARTICLE.get(), 23, 1D);
         setAnimationState(pose, RootminPose.CURSE, this.curseAnimationState, BzParticles.CURSING_PARTICLE.get(), 35, 1D);
         setAnimationState(pose, RootminPose.EMBARRASSED, this.embarassedAnimationState, BzParticles.EMBARRASSED_PARTICLE.get(), 55, 1D);
         setAnimationState(pose, RootminPose.SHOCK, this.shockAnimationState);
         setAnimationState(pose, RootminPose.SHOOT, this.shootAnimationState);
         setAnimationState(pose, RootminPose.RUN, this.runAnimationState);
         setAnimationState(pose, RootminPose.WALK, this.walkAnimationState);
         setAnimationState(pose, RootminPose.BLOCK_TO_ENTITY, this.blockToEntityAnimationState);
         setAnimationState(pose, RootminPose.ENTITY_TO_BLOCK, this.entityToBlockAnimationState, this.tickCount <= 2 ? -100000 : this.tickCount);

      }

      super.onSyncedDataUpdated(entityDataAccessor);
   }

   private void setAnimationState(RootminPose pose, RootminPose poseToCheckFor, AnimationState animationState) {
      setAnimationState(pose, poseToCheckFor, animationState, null, 0, 0);
   }

   private void setAnimationState(RootminPose pose, RootminPose poseToCheckFor, AnimationState animationState, int tickCount) {
      setAnimationState(pose, poseToCheckFor, animationState, tickCount, null, 0, 0);
   }

   private void setAnimationState(RootminPose pose, RootminPose poseToCheckFor, AnimationState animationState, ParticleOptions particleType, int particleLifeSpan, double yOffset) {
      setAnimationState(pose, poseToCheckFor, animationState, this.tickCount, particleType, particleLifeSpan, yOffset);
   }

   private void setAnimationState(RootminPose pose, RootminPose poseToCheckFor, AnimationState animationState, int tickCount, ParticleOptions particleType, int particleLifeSpan, double yOffset) {
      if (pose == poseToCheckFor) {
         if (!animationState.isStarted()) {
            animationState.start(tickCount);

            if (particleType != null && this.level() instanceof ServerLevel serverLevel) {
               serverLevel.sendParticles(
                       particleType,
                       this.getX(),
                       this.getBoundingBox().maxY + yOffset,
                       this.getZ(),
                       0,
                       1,
                       1,
                       1,
                       particleLifeSpan);
            }
         }
      } else {
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

      if (itemstack.getItem() instanceof BlockItem blockItem &&
            (instantBuild ||
            (player instanceof ServerPlayer serverPlayer && EssenceOfTheBees.hasEssence(serverPlayer)) ||
            BeeArmor.getBeeThemedWearablesCount(player) > 0 ||
            !FlowerHeadwearHelmet.getFlowerHeadwear(player).isEmpty()))
      {
         BlockState blockState = blockItem.getBlock().defaultBlockState();

         if (blockState.is(BzTags.ROOTMIN_ALLOWED_FLOWERS) &&
              !blockState.is(BzTags.ROOTMIN_FORCED_DISALLOWED_FLOWERS) &&
              (this.getFlowerBlock() == null || this.getFlowerBlock() != blockState))
         {
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

               if (blockState.getBlock() instanceof DoublePlantBlock) {
                  blockState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER);
               }

               int shrinkAmount = 1;
               if (blockState.hasProperty(BlockStateProperties.FLOWER_AMOUNT)) {
                  shrinkAmount = Math.min(Math.max(1, itemstack.getCount()), 4);
                  blockState = blockState.setValue(BlockStateProperties.FLOWER_AMOUNT, shrinkAmount);
               }

               this.setFlowerBlock(blockState);
               player.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
               if (!instantBuild) {
                  itemstack.shrink(shrinkAmount);
               }

               if (player instanceof ServerPlayer serverPlayer) {
                  BzCriterias.ROOTMIN_FLOWER_SWAP_TRIGGER.trigger(serverPlayer);
               }
            }
            return InteractionResult.SUCCESS;
         }
      }

      // For debugging animations
//      if (itemstack.isEmpty() && !this.level().isClientSide()) {
//         runEmbarrassed();
//      }

      return super.mobInteract(player, hand);
   }

   // Add homing missile dirt
   public void shootHomingDirt(LivingEntity livingEntity, float speedMultipiler) {
      if (!this.level().isClientSide()) {
         DirtPelletEntity pelletEntity = new DirtPelletEntity(this.level(), this);
         pelletEntity.setPos(pelletEntity.position().add(this.getLookAngle().x(), 0, this.getLookAngle().z()));

         if (this.getEssenceController() != null) {
            pelletEntity.setEventBased(true);
         }

         pelletEntity.setHoming(true);
         pelletEntity.setHomingTargetUUID(livingEntity.getUUID());

         double x = livingEntity.getX() - this.getX();
         double y = livingEntity.getY(1.333333 - speedMultipiler) - pelletEntity.getY();
         double z = livingEntity.getZ() - this.getZ();
         double archOffset = Math.sqrt(x * x + z * z);
         Vec3 lookAngle = this.getLookAngle();
         pelletEntity.shoot(lookAngle.x(), y + archOffset * (double) 0.01f, lookAngle.z(), 1.5f * speedMultipiler, 1);

         this.playSound(BzSounds.ROOTMIN_SHOOT.get(), 1.0F, (this.getRandom().nextFloat() * 0.2F) + 0.8F);
         this.level().addFreshEntity(pelletEntity);
      }
   }

   public void shootDirt(@Nullable LivingEntity livingEntity) {
      this.shootDirt(livingEntity, 1);
   }

   public void shootDirt(@Nullable LivingEntity livingEntity, float speedMultipiler) {
      this.shootDirt(livingEntity, 1, 1);
   }

   public void shootDirt(@Nullable LivingEntity livingEntity, float speedMultipiler, int totalProjectiles) {
      if (!this.level().isClientSide()) {
         for (int currentProjectile = 0; currentProjectile < totalProjectiles; currentProjectile++) {
            DirtPelletEntity pelletEntity = new DirtPelletEntity(this.level(), this);
            pelletEntity.setPos(pelletEntity.position().add(this.getLookAngle().x(), 0, this.getLookAngle().z()));

            if (this.getEssenceController() != null) {
               pelletEntity.setEventBased(true);
            }

            Vec3 shootAngle;
            if (livingEntity != null) {
               double x = livingEntity.getX() - this.getX();
               double y = livingEntity.getY(1.3 - speedMultipiler * 1.3) - pelletEntity.getY();
               double z = livingEntity.getZ() - this.getZ();
               shootAngle = new Vec3(x, y, z);
            } else {
               double defaultSpeed = 5;
               double x = this.getLookAngle().x() * defaultSpeed;
               double y = 0.3333333333333333;
               double z = this.getLookAngle().z() * defaultSpeed;
               shootAngle = new Vec3(x, y, z);
            }

            double archOffset = Math.sqrt((shootAngle.x() * shootAngle.x()) + (shootAngle.z() * shootAngle.z()));

            Vec3 vec3 = this.getUpVector(1.0f);
            int angle = (currentProjectile - (int) (totalProjectiles / 2f)) * 3;
            Quaternionf quaternionf = new Quaternionf().setAngleAxis(angle * ((float) Math.PI / 180), vec3.x, vec3.y, vec3.z);
            Vector3f rotatedShootAngle = shootAngle.toVector3f().rotate(quaternionf);

            pelletEntity.shoot(
                    rotatedShootAngle.x(),
                    rotatedShootAngle.y() + archOffset * (double) 0.2f,
                    rotatedShootAngle.z(),
                    1.5f * speedMultipiler,
                    1);

            this.playSound(BzSounds.ROOTMIN_SHOOT.get(), 1.0F, (this.getRandom().nextFloat() * 0.2F) + 0.8F);
            this.level().addFreshEntity(pelletEntity);
         }
      }
   }

   @Override
   public boolean isInvulnerableTo(DamageSource damageSource) {
      if (this.getEssenceController() != null) {
         if (damageSource.getDirectEntity() instanceof DirtPelletEntity dirtPelletEntity) {
            if (dirtPelletEntity.isEventBased()) {
               return super.isInvulnerableTo(damageSource);
            } else if (dirtPelletEntity.getOwner() instanceof ServerPlayer serverPlayer &&
                    EssenceOfTheBees.hasEssence(serverPlayer) &&
                    this.getRootminPose() != RootminPose.ANGRY &&
                    this.getRootminPose() != RootminPose.CURSE &&
                    this.getRootminPose() != RootminPose.SHOCK &&
                    this.hurtTime == 0) {
               return super.isInvulnerableTo(damageSource);
            }
         }
         return true;
      }

      return super.isInvulnerableTo(damageSource);
   }

   @Override
   public void tick() {
      super.tick();

      if (this.hurtTime == 9) {
         if (!this.level().isClientSide()) {
            this.isHidden = false;
            if (this.getRootminPose() != RootminPose.CURSE &&
                    this.getRootminPose() != RootminPose.SHOCK) {
               runShock();
            }
         }
      }

      if (!this.level().isClientSide()) {
         double horizontalSpeed = this.getDeltaMovement().horizontalDistance();
         if (!POSES_THAT_CANT_BE_MOTION_INTERRUPTED.contains(getRootminPose())) {
            if (horizontalSpeed > 0.2d || this.hurtTime > 0) {
               setRootminPose(RootminPose.RUN);
            } else if (horizontalSpeed > 0.01d) {
               setRootminPose(RootminPose.WALK);
            }
         }

         if (this.getRootminPose() == RootminPose.ENTITY_TO_BLOCK && this.curiosityCooldown >= 0) {
            this.curiosityCooldown--;
         }

         if (this.delayTillIdle >= 0) {
            if (delayTillIdle == 0) {
               setRootminPose(RootminPose.NONE);
            }
            this.delayTillIdle--;
         } else {
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

      if (this.level() instanceof ServerLevel serverLevel) {
         if (this.getRootminPose() == RootminPose.SHOCK && this.delayTillIdle == 8) {
            serverLevel.sendParticles(
                    BzParticles.SHOCK_PARTICLE.get(),
                    this.getX(),
                    this.getBoundingBox().maxY + 1.5D,
                    this.getZ(),
                    0,
                    1,
                    1,
                    1,
                    8);
         }
      } else {
         if (this.getRootminPose() == RootminPose.ENTITY_TO_BLOCK &&
                 this.animationTimeBetweenHiding == 0 &&
                 this.yHeadRotO % 90 != 0 &&
                 this.yHeadRot % 90 != 0 &&
                 this.yBodyRotO % 90 != 0 &&
                 this.yBodyRot % 90 != 0) {
            if (!this.isPassenger()) {
               Vec3 lookDirection = this.getLookAngle();
               float closestDir = Direction.getNearest(lookDirection.x(), lookDirection.y(), lookDirection.z()).toYRot();
               this.yHeadRotO = closestDir;
               this.yHeadRot = closestDir;
               this.yBodyRotO = closestDir;
               this.yBodyRot = closestDir;
            }
         }
      }
   }

   @Override
   public void aiStep() {
      super.aiStep();

      if (this.isAlive() && (this.getRootminPose() == RootminPose.WALK || this.getRootminPose() == RootminPose.RUN)) {
         Vec3 frontPos = this.position().add(Vec3.atLowerCornerOf(this.getDirection().getNormal()));
         List<RootminEntity> list = this.level().getEntitiesOfClass(
                 RootminEntity.class,
                 this.getBoundingBox().inflate(0.3),
                 rootmin -> rootmin != this &&
                         rootmin.getRootminPose() == RootminPose.ENTITY_TO_BLOCK &&
                         rootmin.position().closerThan(frontPos, 1.3) &&
                         rootmin.getY() - this.position().y() >= -0.5
         );
         if (!list.isEmpty() && this.onGround()) {
            this.jumpFromGround();
         }
      }
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
   public boolean canChangeDimensions() {
      return super.canChangeDimensions() && this.getEssenceController() == null;
   }

   @Override
   public Entity changeDimension(ServerLevel serverLevel) {
      if (this.getEssenceController() != null) {
         return this;
      }
      return super.changeDimension(serverLevel);
   }

   @Override
   public int getPortalCooldown() {
      return this.getEssenceController() == null ? super.getPortalCooldown() : Integer.MAX_VALUE;
   }

   @Override
   protected boolean shouldDropLoot() {
      return this.getEssenceController() == null ||
              (this.getLastDamageSource() != null && this.getLastDamageSource().getDirectEntity() instanceof DirtPelletEntity);
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

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return BzSounds.ROOTMIN_SHOCK.get();
   }

   @Override
   protected SoundEvent getDeathSound() {
      return BzSounds.ROOTMIN_SHOCK.get();
   }

   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   public static boolean isFacingMob(RootminEntity rootminEntity, LivingEntity target) {
      Vec3 targetView = target.getLookAngle().normalize();
      Vec3 currentDirection = rootminEntity.position().subtract(target.position()).normalize();

      double dotProduct =
              (currentDirection.x() * targetView.x()) +
                      (currentDirection.y() * targetView.y()) +
                      (currentDirection.z() * targetView.z());

      return dotProduct >= 0;
   }

   public boolean canTarget(LivingEntity livingEntity) {
      boolean canTarget = (BeeAggression.doesBeesHateEntity(livingEntity) || livingEntity.getType().is(BzTags.ROOTMIN_TARGETS)) &&
              !livingEntity.getType().is(BzTags.ROOTMIN_FORCED_DO_NOT_TARGET);

      if (canTarget && livingEntity instanceof Player player) {
         if (player.isCreative() || player.isSpectator() || player.isDeadOrDying()) {
            if (player.getUUID().equals(this.superHatedPlayer)) {
               this.superHatedPlayer = null;
            }
            return false;
         }

         if (player.getUUID().equals(this.superHatedPlayer)) {
            this.stayHidingTimer = 0;
            return true;
         }

         if (BeeArmor.getBeeThemedWearablesCount(player) > 0 || !FlowerHeadwearHelmet.getFlowerHeadwear(player).isEmpty()) {
            return false;
         }
      }
      return canTarget;
   }

   public static void considerHiddenRootminsInPath(Path path, RootminEntity mob) {
      if (path != null && !path.isDone() && path.getNodeCount() > 0 && path.getNodeCount() > path.getNextNodeIndex()) {
         BlockPos targetPos = path.getNodePos(path.getNextNodeIndex());
         AABB aabb = new AABB(
                 targetPos.getX() - 0.2D,
                 targetPos.getY() - 0.2D,
                 targetPos.getZ() - 0.2D,
                 targetPos.getX() + 1.2D,
                 targetPos.getY() + 1.2D,
                 targetPos.getZ() + 1.2D
         );
         List<RootminEntity> list = mob.level().getEntitiesOfClass(
                 RootminEntity.class,
                 aabb,
                 rootmin2 -> rootmin2 != mob && rootmin2.getRootminPose() == RootminPose.ENTITY_TO_BLOCK
         );

         if (!list.isEmpty()) {
            path.advance();
         }
      }
   }

   public static void jumpFix(Path path, RootminEntity mob) {
      if (!mob.jumping && path != null && !path.isDone() && path.getNodeCount() > 0 && path.getNodeCount() > path.getNextNodeIndex()) {
         BlockPos targetPos = path.getNodePos(path.getNextNodeIndex());
         if (targetPos.getY() > mob.blockPosition().getY()) {
            BlockPos frontPos = mob.blockPosition().relative(mob.getDirection());
            BlockState frontState = mob.level().getBlockState(frontPos);
            if (frontState.isCollisionShapeFullBlock(mob.level(), frontPos)) {
               mob.jumpFromGround();
            }
         }
      }
   }
}