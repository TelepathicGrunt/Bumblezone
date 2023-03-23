package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeAngerAttackingGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeFaceRandomGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeFloatGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeHopGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeRevengeGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeTemptGoal;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
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

public class HoneySlimeEntity extends Animal implements NeutralMob, Enemy {
   /**
    * Special thanks to Bagel for the Honey Slime code and texture!
    */

   private static final EntityDataAccessor<Boolean> IN_HONEY = SynchedEntityData.defineId(HoneySlimeEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> IN_HONEY_GROWTH_TIME = SynchedEntityData.defineId(HoneySlimeEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> ANGRY_TIMER = SynchedEntityData.defineId(HoneySlimeEntity.class, EntityDataSerializers.INT);
   private static final UniformInt MAX_ANGER_DURATION = TimeUtil.rangeOfSeconds(22, 36);
   private static final Ingredient LURING_BREEDING_ITEM = Ingredient.of(BzTags.HONEY_SLIME_DESIRED_ITEMS);
   private UUID target_UUID;


   private static final HashSet<Block> HONEY_BASED_BLOCKS = new HashSet<>();

   public float squishAmount;
   public float squishFactor;
   public float prevSquishFactor;
   private boolean wasOnGround;

   public HoneySlimeEntity(Level worldIn) {
      super(BzEntities.HONEY_SLIME.get(), worldIn);
   }

   public HoneySlimeEntity(EntityType<? extends HoneySlimeEntity> type, Level worldIn) {
      super(type, worldIn);
      this.moveControl = new HoneySlimeMoveHelperController(this);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new HoneySlimeFloatGoal(this));
      this.targetSelector.addGoal(1, new HoneySlimeRevengeGoal(this));
      this.targetSelector.addGoal(1, new HoneySlimeAngerAttackingGoal(this));
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
      this.goalSelector.addGoal(3, new HoneySlimeTemptGoal(this, 1.2D, LURING_BREEDING_ITEM));
      this.goalSelector.addGoal(4, new HoneySlimeHopGoal(this));
      this.goalSelector.addGoal(4, new HoneySlimeFaceRandomGoal(this));
   }

   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
      this.setupHoneySlime(this.isBaby(), true);
      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(IN_HONEY, true);
      this.entityData.define(IN_HONEY_GROWTH_TIME, 0);
      this.entityData.define(ANGRY_TIMER, 0);
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
      compound.putBoolean("inHoney", this.isInHoney());
      compound.putInt("inHoneyGrowthTimer", this.getInHoneyGrowthTime());
      compound.putBoolean("wasOnGround", this.wasOnGround);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setInHoney(compound.getBoolean("inHoney"));
      this.setInHoneyGrowthTime(compound.getInt("inHoneyGrowthTimer"));
      this.wasOnGround = compound.getBoolean("wasOnGround");
   }

   public static AttributeSupplier.Builder getAttributeBuilder() {
	 return Mob.createMobAttributes()
             .add(Attributes.MAX_HEALTH, 8.0D)
             .add(Attributes.MOVEMENT_SPEED, 2.0D)
             .add(Attributes.ATTACK_DAMAGE, 1.0D);
   }

   @Override
   public boolean checkSpawnRules(LevelAccessor world, MobSpawnType spawnReason) {
      return true;
   }

   protected void setupHoneySlime(boolean isBaby, boolean resetHealth) {
      this.reapplyPosition();
      this.refreshDimensions();
      Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(isBaby ? 2 : 8);
      Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue((0.2F + 0.1F * (float)(isBaby ? 1 : 2))*2);
      Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(isBaby ? 1 : 3);
      if (resetHealth) {
         this.setHealth(this.getMaxHealth());
      }

      this.xpReward = isBaby ? 1 : 2;
   }

   public void remove(Entity.RemovalReason removalReason) {
      if (!this.level.isClientSide() && this.isDeadOrDying()) {
         if (!this.isBaby()) {

            Component component = this.getCustomName();
            boolean flag = this.isNoAi();
            int splitAmount = 2 + this.random.nextInt(3);

            for(int currentNewSlime = 0; currentNewSlime < splitAmount; ++currentNewSlime) {
               float xOffset = ((float)(currentNewSlime % 2) - 0.5F) * 0.5F;
               float zOffset = ((float)(currentNewSlime / 2) - 0.5F) * 0.5F;
               if (isInHoney()) {
                  HoneySlimeEntity honeySlime = BzEntities.HONEY_SLIME.get().create(this.level);
                  if (honeySlime != null) {
                     if (this.isPersistenceRequired()) {
                        honeySlime.setPersistenceRequired();
                     }

                     honeySlime.setBaby(true);
                     honeySlime.setCustomName(component);
                     honeySlime.setNoAi(flag);
                     honeySlime.setInvulnerable(this.isInvulnerable());
                     honeySlime.setupHoneySlime(honeySlime.isBaby(), true);
                     honeySlime.moveTo(this.getX() + (double)xOffset, this.getY() + 0.5, this.getZ() + (double)zOffset, this.random.nextFloat() * 360.0F, 0.0F);
                     this.level.addFreshEntity(honeySlime);
                  }
               }
               else {
                  Slime slime = EntityType.SLIME.create(this.level);
                  if (slime != null) {
                     if (this.isPersistenceRequired()) {
                        slime.setPersistenceRequired();
                     }

                     slime.setCustomName(component);
                     slime.setNoAi(flag);
                     slime.setInvulnerable(this.isInvulnerable());
                     slime.setSize(1, true);
                     slime.moveTo(this.getX() + (double)xOffset, this.getY() + 0.5, this.getZ() + (double)zOffset, this.random.nextFloat() * 360.0F, 0.0F);
                     this.level.addFreshEntity(slime);
                  }
               }
            }
         }

         if (this.getLastHurtByMob() != null && !(this.getLastHurtByMob() instanceof Player player && player.isCreative())) {
            List<HoneySlimeEntity> honeySlimes = this.level.getEntitiesOfClass(HoneySlimeEntity.class, this.getBoundingBox().inflate(24));
            for (HoneySlimeEntity honeySlime : honeySlimes) {
               honeySlime.startPersistentAngerTimer();
               honeySlime.setPersistentAngerTarget(this.getLastHurtByMob().getUUID());
               honeySlime.setTarget(this.getLastHurtByMob());
            }
         }
      }

      super.remove(removalReason);
   }
   public boolean isInHoney() {
      return this.entityData.get(IN_HONEY);
   }

   public void setInHoney(boolean value) {
      this.entityData.set(IN_HONEY, value);
   }

   public int getInHoneyGrowthTime() {
      return this.entityData.get(IN_HONEY_GROWTH_TIME);
   }

   public void setInHoneyGrowthTime(int value) {
      this.entityData.set(IN_HONEY_GROWTH_TIME, value);
   }

   @Override
   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
      if (distance > 1.0F) {
         this.playSound(this.isBaby() ? BzSounds.HONEY_SLIME_SQUISH_SMALL.get() : BzSounds.HONEY_SLIME_SQUISH.get(), 0.4F, 1.0F);
      }

      int fallDamage = this.calculateFallDamage(distance, damageMultiplier);
      if(this.isInHoney()) {
         fallDamage = (int)((fallDamage * 0.35f) - 3);
      }

      if (fallDamage <= 0) {
         return false;
      } else {
         this.hurt(damageSource, (float)fallDamage);
         this.playBlockFallSound();
         return true;
      }
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (!this.isBaby() && this.isInHoney()) {
         //Bottling
         if (itemstack.getItem() == Items.GLASS_BOTTLE) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.isCreative()) {
               GeneralUtils.givePlayerItem(player, hand, new ItemStack(Items.HONEY_BOTTLE), false, true);
            }

            getHoneyFromSlime(this);
            if(player instanceof ServerPlayer serverPlayer) {
               if(!EssenceOfTheBees.hasEssence(serverPlayer)) {
                  this.setLastHurtByMob(player);
               }

               BzCriterias.HONEY_SLIME_HARVEST_TRIGGER.trigger(serverPlayer);
            }
            return InteractionResult.SUCCESS;
         }
      }
      return super.mobInteract(player, hand);
   }


   private void getHoneyFromSlime(LivingEntity entity) {
      if (entity instanceof HoneySlimeEntity) {
         this.setInHoney(false);
         this.setInHoneyGrowthTime(-14400);
      }
   }

   @Override
   public void tick() {
      this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
      this.prevSquishFactor = this.squishFactor;
      super.tick();
      if (this.onGround && !this.wasOnGround) {
         int i = 2;

         if (spawnCustomParticles()) i = 0; // don't spawn particles if it's handled by the implementation itself
         for (int j = 0; j < i * 8; ++j) {
            float f = this.random.nextFloat() * ((float) Math.PI * 2F);
            float f1 = this.random.nextFloat() * 0.5F + 0.5F;
            float f2 = Mth.sin(f) * (float) i * 0.5F * f1;
            float f3 = Mth.cos(f) * (float) i * 0.5F * f1;
            this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Blocks.HONEY_BLOCK)), this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
         }

         this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         this.squishAmount = -0.5F;
      }
      else if (!this.onGround && this.wasOnGround) {
         this.squishAmount = 1.0F;
      }

      this.wasOnGround = this.onGround;
      this.alterSquishAmount();
   }



   @Override
   public void aiStep() {
      super.aiStep();
      if (this.isAlive()) {
         if (!isInHoney()) {
            setInHoneyGrowthTime(getInHoneyGrowthTime() + 1);

            if(HONEY_BASED_BLOCKS.isEmpty()) {
               HONEY_BASED_BLOCKS.addAll(Stream.of(
                               Blocks.HONEY_BLOCK,
                               BzBlocks.FILLED_POROUS_HONEYCOMB.get(),
                               BzBlocks.HONEYCOMB_BROOD.get(),
                               BzBlocks.STICKY_HONEY_REDSTONE.get(),
                               BzBlocks.STICKY_HONEY_RESIDUE.get())
                       .collect(Collectors.toCollection(HashSet::new)));
            }

            if(!this.level.isClientSide() && HONEY_BASED_BLOCKS.contains(this.level.getBlockState(this.blockPosition().below()).getBlock())) {
               if(this.random.nextFloat() < 0.001)
                  setInHoneyGrowthTime(0);
            }
         }
         setInHoney(getInHoneyGrowthTime() >= 0);
      }
   }

   @Override
   protected void customServerAiStep() {
      if (!this.level.isClientSide()) {
         this.updatePersistentAnger((ServerLevel)this.level, false);
      }
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return LURING_BREEDING_ITEM.test(stack);
   }

   @Override
   public AgeableMob getBreedOffspring(ServerLevel worldIn, AgeableMob ageable) {
      HoneySlimeEntity childHoneySlimeEntity = BzEntities.HONEY_SLIME.get().create(worldIn);

      if (childHoneySlimeEntity != null)
         childHoneySlimeEntity.setupHoneySlime(true, true);

      return childHoneySlimeEntity;
   }

   @Override
   protected void ageBoundaryReached() {
      super.ageBoundaryReached();
      if (!this.isBaby()) {
         this.setupHoneySlime(false, true);
      }
   }

   protected void dealDamage(LivingEntity entityIn) {
      if (this.isAlive()) {
         int i = 2;
         if (this.distanceToSqr(entityIn) < 0.6D * (double) i * 0.6D * (double) i && this.hasLineOfSight(entityIn) && entityIn.hurt(DamageSource.mobAttack(this), this.getAttackStrength())) {
            this.playSound(BzSounds.HONEY_SLIME_ATTACK.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.dealDamage(entityIn);
         }
      }
   }

   public boolean canDamagePlayer() {
      return !this.isBaby() && this.isEffectiveAi();
   }

   @Override
   public void playerTouch(Player entityIn) {
      if (this.canDamagePlayer() && this.getTarget() == entityIn) {
         this.dealDamage(entityIn);
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public EntityType<? extends HoneySlimeEntity> getType() {
      return (EntityType<? extends HoneySlimeEntity>) super.getType();
   }

   @Override
   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return 0.625F * sizeIn.height;
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
      return !this.isLeashed() && this.lastHurtByPlayer != player;
   }

   protected void alterSquishAmount() {
      this.squishAmount *= 0.6F;
   }

   public int getJumpDelay() {
      return this.random.nextInt(20) + 10;
   }

   protected float getAttackStrength() {
      return (float) Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).getValue();
   }

   @Override
   protected boolean shouldDropLoot() {
      return true;
   }

   @Override
   protected ResourceLocation getDefaultLootTable() {
      return this.getType().getDefaultLootTable();
   }

   @Override
   public float getSoundVolume() {
      return 0.4F * (float) (isBaby() ? 1 : 2);
   }

   @Override
   public int getMaxHeadXRot() {
      return 0;
   }

   public boolean makesSoundOnJump() {
      return !this.isBaby();
   }

   protected void jumpFromGround() {
      Vec3 vec3d = this.getDeltaMovement();
      if(this.isAngry()) {
         this.setDeltaMovement(vec3d.x * 5, this.getJumpPower() * 1.3f, vec3d.z * 5);
      }
      else  {
         this.setDeltaMovement(vec3d.x, this.getJumpPower(), vec3d.z);
      }
      this.hasImpulse = true;
   }
   protected boolean spawnCustomParticles() {
      return false;
   }

   @Override
   public int getRemainingPersistentAngerTime() {
      return this.entityData.get(ANGRY_TIMER);
   }

   @Override
   public void setRemainingPersistentAngerTime(int ticks) {
      this.entityData.set(ANGRY_TIMER, ticks);
   }

   @Override
   public UUID getPersistentAngerTarget() {
      return this.target_UUID;
   }

   @Override
   public void setPersistentAngerTarget(UUID uuid) {
      this.target_UUID = uuid;
   }

   @Override
   public void startPersistentAngerTimer() {
      this.setRemainingPersistentAngerTime(MAX_ANGER_DURATION.sample(this.random));
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return this.isBaby() ? BzSounds.HONEY_SLIME_HURT_SMALL.get() : BzSounds.HONEY_SLIME_HURT.get();
   }

   protected SoundEvent getDeathSound() {
      return this.isBaby() ? BzSounds.HONEY_SLIME_DEATH_SMALL.get() : BzSounds.HONEY_SLIME_DEATH.get();
   }

   protected SoundEvent getSquishSound() {
      return this.isBaby() ? BzSounds.HONEY_SLIME_SQUISH_SMALL.get() : BzSounds.HONEY_SLIME_SQUISH.get();
   }

   public SoundEvent getJumpSound() {
      return this.isBaby() ?  BzSounds.HONEY_SLIME_JUMP_SMALL.get() : BzSounds.HONEY_SLIME_JUMP.get();
   }
}