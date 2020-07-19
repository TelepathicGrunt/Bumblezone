package net.telepathicgrunt.bumblezone.entities.mobs;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.entities.BzEntities;
import net.telepathicgrunt.bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import net.telepathicgrunt.bumblezone.entities.goals.*;

import java.util.Objects;

public class HoneySlimeEntity extends AnimalEntity implements Monster {
   private static final TrackedData<Boolean> IN_HONEY = DataTracker.registerData(HoneySlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
   private static final TrackedData<Integer> IN_HONEY_GROWTH_TIME = DataTracker.registerData(HoneySlimeEntity.class, TrackedDataHandlerRegistry.INTEGER);
   private static final Ingredient BREEDING_ITEM = Ingredient.ofItems(Items.SUGAR);

   public float squishAmount;
   public float squishFactor;
   public float prevSquishFactor;
   private boolean wasOnGround;

   public HoneySlimeEntity(World worldIn) {
      super(BzEntities.HONEY_SLIME, worldIn);
   }

   public HoneySlimeEntity(EntityType<? extends HoneySlimeEntity> type, World worldIn) {
      super(type, worldIn);
      this.moveControl = new HoneySlimeMoveHelperController(this);
   }

   protected void initGoals() {
	  this.goalSelector.add(0, new FloatGoal(this));
      this.goalSelector.add(1, new BreedGoal(this, 1.0D));
      this.goalSelector.add(2, new TemptGoal(this, 1.2D, BREEDING_ITEM));
      this.goalSelector.add(4, new HopGoal(this));
      this.goalSelector.add(5, new FaceRandomGoal(this));
      this.goalSelector.add(6, new RevengeGoal(this));
      this.goalSelector.add(7, new RevengeGoal(this));
   }

   public EntityData initialize(WorldAccess worldIn, LocalDifficulty difficultyIn, SpawnReason reason, EntityData spawnDataIn, CompoundTag dataTag) {
      this.setSlimeSize(2, true);
      return super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
   }

   public static DefaultAttributeContainer.Builder func_234200_m_() {
	 return MobEntity.createMobAttributes().
			 add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D);
   }

   protected void setSlimeSize(int size, boolean resetHealth) {
      this.refreshPosition();
      this.calculateDimensions();
      this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(size * size);
      this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * (float)size);
      this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(size);
      if (resetHealth) {
         this.setHealth(this.getMaxHealth());
      }

      this.experiencePoints = size;
   }

   protected void initDataTracker() {
      super.initDataTracker();
      this.dataTracker.startTracking(IN_HONEY, false);
      this.dataTracker.startTracking(IN_HONEY_GROWTH_TIME, 0);
   }

   public void onTrackedDataSet(TrackedData<?> key) {
      this.calculateDimensions();
      this.yaw = this.headYaw;
      this.bodyYaw = this.headYaw;
      if (this.isTouchingWater() && this.random.nextInt(20) == 0) {
         this.onSwimmingStart();
      }
      super.onTrackedDataSet(key);
   }

   public void writeCustomDataToTag(CompoundTag compound) {
      super.writeCustomDataToTag(compound);
      compound.putBoolean("inHoney", this.isInHoney());
      compound.putInt("inHoneyGrowthTimer", this.getInHoneyGrowthTime());
      compound.putBoolean("wasOnGround", this.wasOnGround);
   }

   public void readCustomDataFromTag(CompoundTag compound) {
      super.readCustomDataFromTag(compound);
      this.setInHoney(compound.getBoolean("inHoney"));
      this.setInHoneyGrowthTime(compound.getInt("inHoneyGrowthTimer"));
      this.wasOnGround = compound.getBoolean("wasOnGround");
   }

   public boolean isInHoney() {
      return this.dataTracker.get(IN_HONEY);
   }

   public void setInHoney(boolean value) {
      this.dataTracker.set(IN_HONEY, value);
   }

   public int getInHoneyGrowthTime() {
      return this.dataTracker.get(IN_HONEY_GROWTH_TIME);
   }

   public void setInHoneyGrowthTime(int value) {
      this.dataTracker.set(IN_HONEY_GROWTH_TIME, value);
   }

   @Override
   public ActionResult interactMob(PlayerEntity player, Hand hand) {
      ItemStack itemstack = player.getStackInHand(hand);
      World world = player.getEntityWorld();
      if (!this.isBaby() && this.isInHoney()) {
         //Bottling
         if (itemstack.getItem() == Items.GLASS_BOTTLE) {
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            if (!player.abilities.creativeMode) {
               itemstack.decrement(1);
               if (itemstack.isEmpty()) {
                  player.setStackInHand(hand, new ItemStack(Items.HONEY_BOTTLE));
               } else if (!player.inventory.insertStack(new ItemStack(Items.HONEY_BOTTLE))) {
                  player.dropItem(new ItemStack(Items.HONEY_BOTTLE), false);
               }
            }

            this.setAttacker(player);
            getHoneyFromSlime(this);
            return ActionResult.SUCCESS;
         }
      }
      return super.interactMob(player, hand);
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
            float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
            float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
            this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Blocks.HONEY_BLOCK)), this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
         }

         this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         this.squishAmount = -0.5F;
      } else if (!this.onGround && this.wasOnGround) {
         this.squishAmount = 1.0F;
      }

      this.wasOnGround = this.onGround;
      this.alterSquishAmount();
   }

   @Override
   public void tickMovement() {
      super.tickMovement();
      if (this.isAlive()) {
         if (!isInHoney()) {
            setInHoneyGrowthTime(getInHoneyGrowthTime() + 1);
         }
         setInHoney(getInHoneyGrowthTime() >= 0);
      }
   }

   @Override
   public boolean isBreedingItem(ItemStack stack) {
      return BREEDING_ITEM.test(stack);
   }

   @Override
   public PassiveEntity createChild(PassiveEntity ageable) {
      HoneySlimeEntity childHoneySlimeEntity = BzEntities.HONEY_SLIME.create(this.world);
      childHoneySlimeEntity.setSlimeSize(1, true);
      return childHoneySlimeEntity;
   }

   protected void onGrowUp() {
      super.onGrowUp();
      if (!this.isBaby()) {
         this.setSlimeSize(2, true);
      }
   }

   protected void dealDamage(LivingEntity entityIn) {
      if (this.isAlive()) {
         int i = 2;
         if (this.squaredDistanceTo(entityIn) < 0.6D * (double) i * 0.6D * (double) i && this.canSee(entityIn) && entityIn.damage(DamageSource.mob(this), this.func_225512_er_())) {
            this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.dealDamage(this, entityIn);
         }
      }
   }

   public boolean canDamagePlayer() {
      return !this.isBaby() && this.canMoveVoluntarily();
   }

   public void onPlayerCollision(PlayerEntity entityIn) {
      if (this.canDamagePlayer() && this.getAttacker() == entityIn) {
         this.dealDamage(entityIn);
      }
   }

   @SuppressWarnings("unchecked")
   public EntityType<? extends HoneySlimeEntity> getType() {
      return (EntityType<? extends HoneySlimeEntity>) super.getType();
   }

   protected float getActiveEyeHeight(EntityPose poseIn, EntityDimensions sizeIn) {
      return 0.625F * sizeIn.height;
   }

   public void calculateDimensions() {
      double d0 = this.getX();
      double d1 = this.getY();
      double d2 = this.getZ();
      super.calculateDimensions();
      this.updatePosition(d0, d1, d2);
   }

   @Override
   public boolean canBeLeashedBy(PlayerEntity player) {
      return !this.isLeashed() && this.attackingPlayer != player;
   }

   protected void alterSquishAmount() {
      this.squishAmount *= 0.6F;
   }

   public int getJumpDelay() {
      return this.random.nextInt(20) + 10;
   }

   protected float func_225512_er_() {
      return (float) Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).getValue();
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return this.isBaby() ? SoundEvents.ENTITY_SLIME_HURT_SMALL : SoundEvents.ENTITY_SLIME_HURT;
   }

   protected SoundEvent getDeathSound() {
      return this.isBaby() ? SoundEvents.ENTITY_SLIME_DEATH_SMALL : SoundEvents.ENTITY_SLIME_DEATH;
   }

   protected SoundEvent getSquishSound() {
      return this.isBaby() ? SoundEvents.ENTITY_SLIME_SQUISH_SMALL : SoundEvents.ENTITY_SLIME_SQUISH;
   }

   protected Identifier getLootTableId() {
      return this.isBaby() ? this.getType().getLootTableId() : LootTables.EMPTY;
   }

   public float getSoundVolume() {
      return 0.4F * (float) (isBaby() ? 1 : 2);
   }

   public int getLookPitchSpeed() {
      return 0;
   }

   public boolean makesSoundOnJump() {
      return !this.isBaby();
   }

   protected void jump() {
      Vec3d vec3d = this.getVelocity();
      this.setVelocity(vec3d.x, this.getJumpVelocity(), vec3d.z);
      this.velocityDirty = true;
   }

   public SoundEvent getJumpSound() {
      return this.isBaby() ? SoundEvents.ENTITY_SLIME_JUMP_SMALL : SoundEvents.ENTITY_SLIME_JUMP;
   }

   protected boolean spawnCustomParticles() {
      return false;
   }


}