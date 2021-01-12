package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.entities.BzEntities;
import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveHelperController;
import com.telepathicgrunt.the_bumblezone.entities.goals.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HoneySlimeEntity extends AnimalEntity implements IAngerable, IMob {
   /**
    * Special thanks to Bagel for the Honey Slime code and texture!
    */

   private static final DataParameter<Boolean> IN_HONEY = EntityDataManager.createKey(HoneySlimeEntity.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Integer> IN_HONEY_GROWTH_TIME = EntityDataManager.createKey(HoneySlimeEntity.class, DataSerializers.VARINT);
   private static final Ingredient BREEDING_ITEM = Ingredient.fromItems(Items.SUGAR);
   private static final DataParameter<Integer> ANGRY_TIMER = EntityDataManager.createKey(HoneySlimeEntity.class, DataSerializers.VARINT);
   private static final RangedInteger MAX_ANGER_DURATION = TickRangeConverter.convertRange(10, 22);
   private UUID target_UUID;


   private static final HashSet<Block> HONEY_BASED_BLOCKS = Stream.of(
           Blocks.HONEY_BLOCK,
           BzBlocks.FILLED_POROUS_HONEYCOMB.get(),
           BzBlocks.HONEYCOMB_BROOD.get(),
           BzBlocks.STICKY_HONEY_REDSTONE.get(),
           BzBlocks.STICKY_HONEY_RESIDUE.get())
           .collect(Collectors.toCollection(HashSet::new));

   public float squishAmount;
   public float squishFactor;
   public float prevSquishFactor;
   private boolean wasOnGround;

   public HoneySlimeEntity(World worldIn) {
      super(BzEntities.HONEY_SLIME.get(), worldIn);
   }

   public HoneySlimeEntity(EntityType<? extends HoneySlimeEntity> type, World worldIn) {
      super(type, worldIn);
      this.moveController = new HoneySlimeMoveHelperController(this);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.targetSelector.addGoal(1, new FacingRevengeGoal(this));
      this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
      this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, BREEDING_ITEM));
      this.goalSelector.addGoal(4, new HopGoal(this));
      this.goalSelector.addGoal(4, new FaceRandomGoal(this));
   }

   @Override
   public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
      this.setSlimeSize(2, true);
      return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
   }

   @Override
   protected void registerData() {
      super.registerData();
      this.dataManager.register(IN_HONEY, false);
      this.dataManager.register(IN_HONEY_GROWTH_TIME, 0);
      this.dataManager.register(ANGRY_TIMER, 0);
   }

   @Override
   public void notifyDataManagerChange(DataParameter<?> key) {
      this.recalculateSize();
      this.rotationYaw = this.rotationYawHead;
      this.renderYawOffset = this.rotationYawHead;
      if (this.isInWater() && this.rand.nextInt(20) == 0) {
         this.doWaterSplashEffect();
      }
      super.notifyDataManagerChange(key);
   }

   @Override
   public void writeAdditional(CompoundNBT compound) {
      super.writeAdditional(compound);
      compound.putBoolean("inHoney", this.isInHoney());
      compound.putInt("inHoneyGrowthTimer", this.getInHoneyGrowthTime());
      compound.putBoolean("wasOnGround", this.wasOnGround);
   }

   @Override
   public void readAdditional(CompoundNBT compound) {
      super.readAdditional(compound);
      this.setInHoney(compound.getBoolean("inHoney"));
      this.setInHoneyGrowthTime(compound.getInt("inHoneyGrowthTimer"));
      this.wasOnGround = compound.getBoolean("wasOnGround");
   }

   public static AttributeModifierMap.MutableAttribute getAttributeBuilder() {
	 return MobEntity.func_233666_p_()
             .createMutableAttribute(Attributes.MAX_HEALTH, 40.0D)
             .createMutableAttribute(Attributes.MOVEMENT_SPEED, 2.0D)
             .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D);
   }

   @Override
   public boolean canSpawn(IWorld world, SpawnReason spawnReason) {
      return true;
   }

   protected void setSlimeSize(int size, boolean resetHealth) {
      this.recenterBoundingBox();
      this.recalculateSize();
      Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(size * size);
      Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue((0.2F + 0.1F * (float)size)*2);
      Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(size);
      if (resetHealth) {
         this.setHealth(this.getMaxHealth());
      }

      this.experienceValue = size;
   }

   public boolean isInHoney() {
      return this.dataManager.get(IN_HONEY);
   }

   public void setInHoney(boolean value) {
      this.dataManager.set(IN_HONEY, value);
   }

   public int getInHoneyGrowthTime() {
      return this.dataManager.get(IN_HONEY_GROWTH_TIME);
   }

   public void setInHoneyGrowthTime(int value) {
      this.dataManager.set(IN_HONEY_GROWTH_TIME, value);
   }

   @Override
   public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
      ItemStack itemstack = player.getHeldItem(hand);
      World world = player.getEntityWorld();
      if (!this.isChild() && this.isInHoney()) {
         //Bottling
         if (itemstack.getItem() == Items.GLASS_BOTTLE) {
            world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            if (!player.abilities.isCreativeMode) {
               itemstack.shrink(1);
               if (itemstack.isEmpty()) {
                  player.setHeldItem(hand, new ItemStack(Items.HONEY_BOTTLE));
               } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.HONEY_BOTTLE))) {
                  player.dropItem(new ItemStack(Items.HONEY_BOTTLE), false);
               }
            }

            this.setRevengeTarget(player);
            getHoneyFromSlime(this);
            return ActionResultType.SUCCESS;
         }
      }
      return super.func_230254_b_(player, hand);
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
            float f = this.rand.nextFloat() * ((float) Math.PI * 2F);
            float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
            float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
            float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
            this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Blocks.HONEY_BLOCK)), this.getPosX() + (double) f2, this.getPosY(), this.getPosZ() + (double) f3, 0.0D, 0.0D, 0.0D);
         }

         this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         this.squishAmount = -0.5F;
      } else if (!this.onGround && this.wasOnGround) {
         this.squishAmount = 1.0F;
      }

      this.wasOnGround = this.onGround;
      this.alterSquishAmount();
   }



   @Override
   public void livingTick() {
      super.livingTick();
      if (this.isAlive()) {
         if (!isInHoney()) {
            setInHoneyGrowthTime(getInHoneyGrowthTime() + 1);

            if(!this.world.isRemote && HONEY_BASED_BLOCKS.contains(this.world.getBlockState(this.getPosition().down()).getBlock())){
               if(this.rand.nextFloat() < 0.001)
                  setInHoneyGrowthTime(0);
            }
         }
         setInHoney(getInHoneyGrowthTime() >= 0);
      }
   }

   @Override
   protected void updateAITasks() {
      if (!this.world.isRemote) {
         this.func_241359_a_((ServerWorld)this.world, false);
      }
   }

   @Override
   public boolean isBreedingItem(ItemStack stack) {
      return BREEDING_ITEM.test(stack);
   }

   @Override
   public AgeableEntity func_241840_a(ServerWorld worldIn, AgeableEntity ageable) {
      HoneySlimeEntity childHoneySlimeEntity = BzEntities.HONEY_SLIME.get().create(worldIn);

      if (childHoneySlimeEntity != null)
         childHoneySlimeEntity.setSlimeSize(1, true);

      return childHoneySlimeEntity;
   }

   @Override
   protected void onGrowingAdult() {
      super.onGrowingAdult();
      if (!this.isChild()) {
         this.setSlimeSize(2, true);
      }
   }

   protected void dealDamage(LivingEntity entityIn) {
      if (this.isAlive()) {
         int i = 2;
         if (this.getDistanceSq(entityIn) < 0.6D * (double) i * 0.6D * (double) i && this.canEntityBeSeen(entityIn) && entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackStrength())) {
            this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.applyEnchantments(this, entityIn);
         }
      }
   }

   public boolean canDamagePlayer() {
      return !this.isChild() && this.isServerWorld();
   }

   @Override
   public void onCollideWithPlayer(PlayerEntity entityIn) {
      if (this.canDamagePlayer() && this.getAttackTarget() == entityIn) {
         this.dealDamage(entityIn);
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public EntityType<? extends HoneySlimeEntity> getType() {
      return (EntityType<? extends HoneySlimeEntity>) super.getType();
   }

   @Override
   protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
      return 0.625F * sizeIn.height;
   }

   @Override
   public void recalculateSize() {
      double d0 = this.getPosX();
      double d1 = this.getPosY();
      double d2 = this.getPosZ();
      super.recalculateSize();
      this.setPosition(d0, d1, d2);
   }

   @Override
   public boolean canBeLeashedTo(PlayerEntity player) {
      return !this.getLeashed() && this.attackingPlayer != player;
   }

   protected void alterSquishAmount() {
      this.squishAmount *= 0.6F;
   }

   public int getJumpDelay() {
      return this.rand.nextInt(20) + 10;
   }

   protected float getAttackStrength() {
      return (float) Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).getValue();
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return this.isChild() ? SoundEvents.ENTITY_SLIME_HURT_SMALL : SoundEvents.ENTITY_SLIME_HURT;
   }

   protected SoundEvent getDeathSound() {
      return this.isChild() ? SoundEvents.ENTITY_SLIME_DEATH_SMALL : SoundEvents.ENTITY_SLIME_DEATH;
   }

   protected SoundEvent getSquishSound() {
      return this.isChild() ? SoundEvents.ENTITY_SLIME_SQUISH_SMALL : SoundEvents.ENTITY_SLIME_SQUISH;
   }

   @Override
   protected ResourceLocation getLootTable() {
      return this.isChild() ? this.getType().getLootTable() : LootTables.EMPTY;
   }

   @Override
   public float getSoundVolume() {
      return 0.4F * (float) (isChild() ? 1 : 2);
   }

   @Override
   public int getVerticalFaceSpeed() {
      return 0;
   }

   public boolean makesSoundOnJump() {
      return !this.isChild();
   }

   protected void jump() {
      Vector3d vec3d = this.getMotion();
      this.setMotion(vec3d.x, this.getJumpUpwardsMotion(), vec3d.z);
      this.isAirBorne = true;
   }

   public SoundEvent getJumpSound() {
      return this.isChild() ? SoundEvents.ENTITY_SLIME_JUMP_SMALL : SoundEvents.ENTITY_SLIME_JUMP;
   }

   protected boolean spawnCustomParticles() {
      return false;
   }

   @Override
   public int getAngerTime() {
      return this.dataManager.get(ANGRY_TIMER);
   }

   @Override
   public void setAngerTime(int ticks) {
      this.dataManager.set(ANGRY_TIMER, ticks);
   }

   @Override
   public UUID getAngerTarget() {
      return this.target_UUID;
   }

   @Override
   public void setAngerTarget(UUID uuid) {
      this.target_UUID = uuid;
   }

   @Override
   public void func_230258_H__() {
      if(MAX_ANGER_DURATION != null)
         this.setAngerTime(MAX_ANGER_DURATION.getRandomWithinRange(this.rand));
   }
}