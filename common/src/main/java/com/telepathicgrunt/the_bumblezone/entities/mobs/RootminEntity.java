package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenPose;
import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

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

   public static final EntityDataSerializer<RootminPose> ROOTMIN_POSE_SERIALIZER = EntityDataSerializer.simpleEnum(RootminPose.class);
   private static final EntityDataAccessor<RootminPose> ROOTMIN_POSE = SynchedEntityData.defineId(RootminEntity.class, ROOTMIN_POSE_SERIALIZER);

   public RootminEntity(Level worldIn) {
      super(BzEntities.ROOTMIN.get(), worldIn);
   }

   public RootminEntity(EntityType<? extends RootminEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   public void setFlowerBlock(@Nullable BlockState blockState) {
      this.entityData.set(FLOWER_BLOCK_STATE, Optional.ofNullable(blockState));
   }

   @Nullable
   public BlockState getFlowerBlock() {
      return this.entityData.get(FLOWER_BLOCK_STATE).orElse(null);
   }

   public void setQueenPose(RootminPose rootminPose) {
      this.entityData.set(ROOTMIN_POSE, rootminPose);
   }

   public RootminPose getRootminPose() {
      return this.entityData.get(ROOTMIN_POSE);
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
      this.refreshDimensions();
      this.setYRot(this.yHeadRot);
      this.setYBodyRot(this.yHeadRot);
      if (this.isInWater() && this.random.nextInt(20) == 0) {
         this.doWaterSplashEffect();
      }

      if (ROOTMIN_POSE.equals(entityDataAccessor)) {
         RootminPose pose = this.getRootminPose();
         setAnimationState(pose, RootminPose.ANGRY, this.angryAnimationState);
         setAnimationState(pose, RootminPose.CURIOUS, this.curiousAnimationState);
         setAnimationState(pose, RootminPose.CURSE, this.curseAnimationState);
         setAnimationState(pose, RootminPose.EMBARASSED, this.embarassedAnimationState);
         setAnimationState(pose, RootminPose.SHOCK, this.shockAnimationState);
         setAnimationState(pose, RootminPose.SHOOT, this.shootAnimationState);
         setAnimationState(pose, RootminPose.RUN, this.runAnimationState);
         setAnimationState(pose, RootminPose.WALK, this.walkAnimationState);
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

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      BlockState blockState = this.getFlowerBlock();
      if (blockState != null) {
         compound.put("flowerBlock", NbtUtils.writeBlockState(blockState));
      }
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

      if (itemstack.getItem() instanceof BlockItem blockItem) {
         BlockState blockState = blockItem.getBlock().defaultBlockState();

         if (blockState.is(BzTags.ROOTMIN_ALLOWED_FLOWER) && !blockState.is(BzTags.ROOTMIN_FORCED_DISALLOWED_FLOWER)) {
            if (!this.level().isClientSide()) {
               if (!player.getAbilities().instabuild && this.getFlowerBlock() != null) {
                  ItemStack itemStack = new ItemStack(Items.DIAMOND_AXE);
                  itemStack.enchant(Enchantments.SILK_TOUCH, 1);
                  LootParams.Builder builder = new LootParams.Builder((ServerLevel)this.level()).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.TOOL, itemStack).withOptionalParameter(LootContextParams.THIS_ENTITY, this);
                  List<ItemStack> flowerDrops = this.getFlowerBlock().getDrops(builder);
                  for (ItemStack flowerDrop : flowerDrops) {
                     this.spawnAtLocation(flowerDrop);
                  }
               }

               this.setFlowerBlock(blockState);
               player.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
               if (!player.getAbilities().instabuild) {
                  itemstack.shrink(1);
               }
            }
            return InteractionResult.SUCCESS;
         }
      }

      return super.mobInteract(player, hand);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.isAlive()) {
         this.idleAnimationState.startIfStopped(this.tickCount);
      }
      else {
         this.idleAnimationState.stop();
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
   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return 0.65F * sizeIn.height;
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