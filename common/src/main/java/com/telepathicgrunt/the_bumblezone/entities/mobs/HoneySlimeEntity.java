package com.telepathicgrunt.the_bumblezone.entities.mobs;

import com.telepathicgrunt.the_bumblezone.entities.controllers.HoneySlimeMoveController;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeAngerAttackingGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeBreedGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeFaceRandomGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeFloatGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeHopGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeRevengeGoal;
import com.telepathicgrunt.the_bumblezone.entities.goals.HoneySlimeTemptGoal;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.mixin.entities.AgeableMobAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.ConversionType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HoneySlimeEntity extends Animal implements NeutralMob {
    /**
     * Special thanks to Bagel for the Honey Slime code and texture!
     */

    private static final EntityDataAccessor<Boolean> IN_HONEY = SynchedEntityData.defineId(HoneySlimeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> IN_HONEY_GROWTH_TIME = SynchedEntityData.defineId(HoneySlimeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ANGER_END_TIME = SynchedEntityData.defineId(HoneySlimeEntity.class, EntityDataSerializers.LONG);
    private static final UniformInt MAX_ANGER_DURATION = TimeUtil.rangeOfSeconds(22, 36);
    private EntityReference<LivingEntity> persistentAngerTarget;

    private static final HashSet<Block> HONEY_BASED_BLOCKS = new HashSet<>();

    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;

    public HoneySlimeEntity(Level worldIn) {
       this(BzEntities.HONEY_SLIME.get(), worldIn);
    }

    public HoneySlimeEntity(EntityType<? extends HoneySlimeEntity> type, Level worldIn) {
       super(type, worldIn);
       this.moveControl = new HoneySlimeMoveController(this);
    }

    @Override
    protected void registerGoals() {
       this.goalSelector.addGoal(1, new HoneySlimeFloatGoal(this));
       this.targetSelector.addGoal(1, new HoneySlimeRevengeGoal(this));
       this.targetSelector.addGoal(1, new HoneySlimeAngerAttackingGoal(this));
       this.goalSelector.addGoal(2, new HoneySlimeBreedGoal(this, 1.0D));
       this.goalSelector.addGoal(3, new HoneySlimeTemptGoal(this));
       this.goalSelector.addGoal(4, new HoneySlimeHopGoal(this));
       this.goalSelector.addGoal(4, new HoneySlimeFaceRandomGoal(this));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, EntitySpawnReason reason, SpawnGroupData spawnDataIn) {
       this.setupHoneySlime(this.isBaby(), true);
       return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
       super.defineSynchedData(builder);
       builder.define(IN_HONEY, true);
       builder.define(IN_HONEY_GROWTH_TIME, 0);
       builder.define(ANGER_END_TIME, 0L);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
       if (AgeableMobAccessor.bumblezone$getDATA_BABY_ID().equals(key)) {
          this.refreshDimensions();
          if (!this.isPassenger()) {
             this.setYRot(this.yHeadRot);
             this.setYBodyRot(this.yHeadRot);
          }
          if (this.isInWater() && this.random.nextInt(20) == 0) {
             this.doWaterSplashEffect();
          }
       }
       super.onSyncedDataUpdated(key);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("inHoney", this.isInHoney());
        output.putInt("inHoneyGrowthTimer", this.getInHoneyGrowthTime());
        output.putBoolean("wasOnGround", this.wasOnGround);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
       super.readAdditionalSaveData(input);
       this.setInHoney(input.getBooleanOr("inHoney", false));
       this.setInHoneyGrowthTime(input.getIntOr("inHoneyGrowthTimer", 0));
       this.wasOnGround = input.getBooleanOr("wasOnGround", true);
    }

    public static AttributeSupplier.Builder getAttributeBuilder() {
 	 return Mob.createMobAttributes()
              .add(Attributes.MAX_HEALTH, 8.0D)
              .add(Attributes.MOVEMENT_SPEED, 2.0D)
              .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, EntitySpawnReason spawnReason) {
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

    @Override
    public void remove(Entity.RemovalReason removalReason) {
       if (this.level() instanceof ServerLevel serverLevel && this.isDeadOrDying()) {
          if (!this.isBaby()) {

             Component component = this.getCustomName();
             boolean flag = this.isNoAi();
             int splitAmount = 2 + this.random.nextInt(3);
             PlayerTeam team = this.getTeam();

             ArrayList<Mob> children = new ArrayList<>();
             for(int currentNewSlime = 0; currentNewSlime < splitAmount; ++currentNewSlime) {
                float xOffset = ((float)(currentNewSlime % 2) - 0.5F) * 0.5F;
                float zOffset = ((float)(currentNewSlime / 2) - 0.5F) * 0.5F;
                if (isInHoney()) {
                    this.convertTo(BzEntities.HONEY_SLIME.get(), new ConversionParams(ConversionType.SPLIT_ON_DEATH, false, false, team), EntitySpawnReason.TRIGGERED, honeySlime -> {
                        honeySlime.setupHoneySlime(honeySlime.isBaby(), true);
                        honeySlime.snapTo(this.getX() + xOffset, this.getY() + 0.5, this.getZ() + zOffset, this.random.nextFloat() * 360.0F, 0.0F);
                    });
                }
                else {
                    this.convertTo(EntityType.SLIME, new ConversionParams(ConversionType.SPLIT_ON_DEATH, false, false, team), EntitySpawnReason.TRIGGERED, slime -> {
                        slime.setSize(1, true);
                        slime.snapTo(this.getX() + xOffset, this.getY() + 0.5, this.getZ() + zOffset, this.random.nextFloat() * 360.0F, 0.0F);
                    });
                }
             }

             if (PlatformService.INSTANCE.shouldMobSplit(this, children)) {
                children.forEach(this.level()::addFreshEntity);
             }
          }

          if (this.getLastAttacker() != null && !(this.getLastAttacker() instanceof Player player && player.isCreative())) {
             List<HoneySlimeEntity> honeySlimes = this.level().getEntitiesOfClass(HoneySlimeEntity.class, this.getBoundingBox().inflate(24));
             for (HoneySlimeEntity honeySlime : honeySlimes) {
                honeySlime.startPersistentAngerTimer();
                honeySlime.setPersistentAngerTarget(EntityReference.of(this.getLastAttacker()));
                honeySlime.setTarget(this.getLastAttacker());
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
       ItemStack itemstack = player.getItemInHand(hand);
       if (!this.isBaby() && this.isInHoney()) {
          //Bottling
          if (itemstack.getItem() == Items.GLASS_BOTTLE) {
             level().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
             GeneralUtils.givePlayerItem(player, hand, new ItemStack(Items.HONEY_BOTTLE), false, true);

             getHoneyFromSlime(this);
             if(player instanceof ServerPlayer serverPlayer) {
                if(!EssenceOfTheBees.hasEssence(serverPlayer)) {
                   this.setLastHurtByMob(player);
                }

                BzCriterias.HONEY_SLIME_HARVEST_TRIGGER.get().trigger(serverPlayer);
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
       if (this.onGround() && !this.wasOnGround) {
          int i = 2;

          if (spawnCustomParticles()) i = 0; // don't spawn particles if it's handled by the implementation itself
          for (int j = 0; j < i * 8; ++j) {
             float f = this.random.nextFloat() * ((float) Math.PI * 2F);
             float f1 = this.random.nextFloat() * 0.5F + 0.5F;
             float f2 = Mth.sin(f) * (float) i * 0.5F * f1;
             float f3 = Mth.cos(f) * (float) i * 0.5F * f1;
             this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(new ItemStack(Blocks.HONEY_BLOCK))), this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
          }

          this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
          this.squishAmount = -0.5F;
       }
       else if (!this.onGround() && this.wasOnGround) {
          this.squishAmount = 1.0F;
       }

       this.wasOnGround = this.onGround();
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

             if(!this.level().isClientSide() && HONEY_BASED_BLOCKS.contains(this.level().getBlockState(this.blockPosition().below()).getBlock())) {
                if(this.random.nextFloat() < 0.001)
                   setInHoneyGrowthTime(0);
             }
          }
          setInHoney(getInHoneyGrowthTime() >= 0);
       }
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
      this.updatePersistentAnger(serverLevel, false);
    }

    @Override
    public boolean isFood(ItemStack stack) {
       return stack.is(BzTags.HONEY_SLIME_DESIRED_ITEMS);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageable) {
       HoneySlimeEntity childHoneySlimeEntity = BzEntities.HONEY_SLIME.get().create(level, EntitySpawnReason.BREEDING);

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

    @Override
    protected int calculateFallDamage(double fallDistance, float damageModifier) {
        if (this.is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
            return 0;
        }
        else {
            double baseDamage = this.calculateFallPower(fallDistance);
            if(this.isInHoney()) {
                baseDamage = (int)((baseDamage * 0.35f) - 3);
            }
            return Mth.floor(baseDamage * damageModifier * this.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER));
        }
    }

    private double calculateFallPower(double fallDistance) {
        return fallDistance + 1.0E-6 - this.getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
    }

    protected void dealDamage(LivingEntity entityIn) {
       if (this.isAlive()) {
          int i = 2;
          if (this.distanceToSqr(entityIn) < 0.6D * (double) i * 0.6D * (double) i && this.hasLineOfSight(entityIn) && entityIn.hurtOrSimulate(damageSources().mobAttack(this), this.getAttackStrength())) {
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
    public void refreshDimensions() {
       double x = this.getX();
       double y = this.getY();
       double z = this.getZ();
       super.refreshDimensions();
       this.snapTo(x, y, z);
    }

    @Override
    public boolean canBeLeashed() {
       return !this.isLeashed();
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

    @Override
    public void jumpFromGround() {
        Vec3 movement = this.getDeltaMovement();
        this.setDeltaMovement(movement.x, this.getJumpPower(), movement.z);
        this.needsSync = true;
    }

    protected boolean spawnCustomParticles() {
       return false;
    }

     @Override
     public long getPersistentAngerEndTime() {
         return this.entityData.get(ANGER_END_TIME);
     }

     @Override
     public void setPersistentAngerEndTime(long endTime) {
         this.entityData.set(ANGER_END_TIME, endTime);
     }

     @Override
     public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
         return this.persistentAngerTarget;
     }

     @Override
     public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> persistentAngerTarget) {
         this.persistentAngerTarget = persistentAngerTarget;
     }

     @Override
     public void startPersistentAngerTimer() {
         this.setTimeToRemainAngry(MAX_ANGER_DURATION.sample(this.random));
     }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
       return this.isBaby() ? BzSounds.HONEY_SLIME_HURT_SMALL.get() : BzSounds.HONEY_SLIME_HURT.get();
    }

    @Override
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