package com.telepathicgrunt.the_bumblezone.entities.living;

import com.google.common.base.Objects;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.rendering.boundlesscrystal.BoundlessCrystalState;
import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.mixin.entities.LivingEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzDamageSources;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.HashSet;
import java.util.List;

public class BoundlessCrystalEntity extends LivingEntity {
    public static final EntityDataSerializer<BoundlessCrystalState> BOUNDLESS_CRYSTAL_STATE_SERIALIZER = EntityDataSerializer.simpleEnum(BoundlessCrystalState.class);
    private static final EntityDataAccessor<BoundlessCrystalState> BOUNDLESS_CRYSTAL_STATE = SynchedEntityData.defineId(BoundlessCrystalEntity.class, BOUNDLESS_CRYSTAL_STATE_SERIALIZER);

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(0, ItemStack.EMPTY);

    public final AnimationState rotateAnimationState = new AnimationState();

    public BoundlessCrystalEntity(EntityType<? extends BoundlessCrystalEntity> entityType, Level level) {
        super(entityType, level);
        rotateAnimationState.start(tickCount);
    }

    public static AttributeSupplier.Builder getAttributeBuilder() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOUNDLESS_CRYSTAL_STATE, BoundlessCrystalState.NORMAL);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
    }

    public void tick() {
        super.tick();

        if (this.level().isClientSide() && (this.tickCount % 5 == 0 || this.hurtTime > 0)) {
            Vec3 center = this.getBoundingBox().getCenter();
            spawnFancyParticle(center);
            if (this.hurtTime == 8) {
                for (int i = 0; i < 50; i++) {
                    spawnFancyParticle(center);
                }
            }
        }
    }

    private void spawnFancyParticle(Vec3 center) {
        this.level().addParticle(
                ParticleTypes.END_ROD,
                center.x() + this.random.nextGaussian() / 5,
                center.y() + this.random.nextGaussian() / 2.5,
                center.z() + this.random.nextGaussian() / 5,
                (this.random.nextFloat() * this.random.nextGaussian() / 15),
                (this.random.nextFloat() * this.random.nextGaussian() / 15),
                (this.random.nextFloat() * this.random.nextGaussian() / 15));
    }

    @Override
    public void baseTick() {

        if (this.isPassenger()) {
            this.stopRiding();
        }

        this.walkDistO = this.walkDist;
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();

        this.clearFire();
        this.extinguishFire();

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
        if (this.invulnerableTime > 0) {
            --this.invulnerableTime;
        }
        if (this.isDeadOrDying() && this.level().shouldTickDeath(this)) {
            this.tickDeath();
        }
        if (this.lastHurtByPlayerTime > 0) {
            --this.lastHurtByPlayerTime;
        }
        else {
            this.lastHurtByPlayer = null;
        }
        if (this.getLastHurtMob() != null && !this.getLastHurtMob().isAlive()) {
            this.setLastHurtByMob(null);
        }
        if (this.getLastHurtByMob() != null) {
            if (!this.getLastHurtByMob().isAlive()) {
                this.setLastHurtByMob(null);
            }
            else if (this.tickCount - this.getLastHurtByMobTimestamp() > 100) {
                this.setLastHurtByMob(null);
            }
        }
        this.animStepO = this.animStep;
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.yHeadRot;
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        this.tickEffects();
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isControlledByLocalInstance()) {
            double d = 0.08;
            boolean bl = this.getDeltaMovement().y <= 0.0;
            if (bl && this.hasEffect(MobEffects.SLOW_FALLING)) {
                d = 0.01;
            }
            BlockPos blockPos = this.getBlockPosBelowThatAffectsMyMovement();
            float p = this.level().getBlockState(blockPos).getBlock().getFriction();
            float f = this.onGround() ? p * 0.91f : 0.91f;
            Vec3 vec37 = this.handleRelativeFrictionAndCalculateMovement(vec3, p);

            double q = vec37.y;
            if (!this.level().isClientSide || this.level().hasChunkAt(blockPos)) {
                if (!this.isNoGravity()) {
                    q -= d;
                }
            }
            else {
                q = this.getY() > (double)this.level().getMinBuildHeight() ? -0.1 : 0.0;
            }

            this.setDeltaMovement(vec37.x * (double)f, q * (double)0.98f, vec37.z * (double)f);
        }

        this.calculateEntityAnimation(true);
    }

    @Override
    protected void pushEntities() {
        if (this.level().isClientSide()) {
            this.level().getEntities(EntityTypeTest.forClass(Player.class), this.getBoundingBox(), EntitySelector.pushableBy(this)).forEach(this::doPush);
            return;
        }
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            int entityIndex;
            int maxCrammingLimit = this.level().getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
            if (maxCrammingLimit > 0 && list.size() > maxCrammingLimit - 1 && this.random.nextInt(4) == 0) {
                entityIndex = 0;
                for (Entity entity : list) {
                    if (entity.isPassenger()) continue;
                    ++entityIndex;
                }
            }

            for (entityIndex = 0; entityIndex < list.size(); ++entityIndex) {
                Entity entity = list.get(entityIndex);
                this.doPush(entity);

                if (entity instanceof LivingEntity livingEntity && !(entity instanceof BoundlessCrystalEntity)) {
                    float damageAmount;
                    float maxHealth = Math.max(livingEntity.getHealth(), livingEntity.getMaxHealth());

                    if (livingEntity instanceof ServerPlayer serverPlayer) {
                        if (serverPlayer.isCreative()) {
                            continue;
                        }

                        if (EssenceOfTheBees.hasEssence(serverPlayer)) {
                            damageAmount = maxHealth / 8;
                        }
                        else {
                            damageAmount = maxHealth / 4;
                        }
                    }
                    else {
                        damageAmount = maxHealth / 8;
                    }

                    livingEntity.hurt(this.level().damageSources().source(BzDamageSources.BOUNDLESS_CRYSTAL_TYPE), damageAmount);

                    for(MobEffect mobEffect : new HashSet<>(livingEntity.getActiveEffectsMap().keySet())) {
                        if (mobEffect.isBeneficial()) {
                            livingEntity.removeEffect(mobEffect);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void dropExperience() {}

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return armorItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {}

    @Override
    public boolean shouldShowName() {
        return false;
    }

    @Override
    public boolean canDisableShield() {
        return true;
    }

    @Override
    public boolean isOnPortalCooldown() {
        return true;
    }

    @Override
    public void lavaHurt() {}

    @Override
    public void setSecondsOnFire(int i) {
        int j = i * 20;
        j = ProtectionEnchantment.getFireAfterDampener(this, j);
        if (j > 5) {
            this.setRemainingFireTicks(Math.max(j, 20));
        }
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    protected void handleNetherPortal() { }

    @Override
    public boolean isCurrentlyGlowing() {
        return true;
    }

    @Override
    public Entity changeDimension(ServerLevel serverLevel) {
        return this;
    }

    @Override
    protected void checkInsideBlocks() {
        AABB aABB = this.getBoundingBox();
        BlockPos blockPos = BlockPos.containing(aABB.minX + 1.0E-7, aABB.minY + 1.0E-7, aABB.minZ + 1.0E-7);
        BlockPos blockPos2 = BlockPos.containing(aABB.maxX - 1.0E-7, aABB.maxY - 1.0E-7, aABB.maxZ - 1.0E-7);
        if (this.level().hasChunksAt(blockPos, blockPos2)) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
                for (int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
                    for (int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
                        mutableBlockPos.set(i, j, k);
                        BlockState blockState = this.level().getBlockState(mutableBlockPos);
                        if (!blockState.isAir() &&
                            !blockState.getCollisionShape(this.level(), mutableBlockPos).isEmpty() &&
                            !this.level().isClientSide())
                        {
                            this.level().destroyBlock(mutableBlockPos, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        Entity entity2;
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        }
        if (this.level().isClientSide) {
            return false;
        }
        if (this.isDeadOrDying()) {
            return false;
        }
        if (damageSource.is(DamageTypeTags.IS_FIRE) && this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        }
        if (this.isSleeping() && !this.level().isClientSide) {
            this.stopSleeping();
        }
        this.noActionTime = 0;
        float g = f;
        boolean bl = false;
        float h = 0.0f;
        if (f > 0.0f && this.isDamageSourceBlocked(damageSource)) {
            Entity entity;
            this.hurtCurrentlyUsedShield(f);
            h = f;
            f = 0.0f;
            if (!damageSource.is(DamageTypeTags.IS_PROJECTILE) && (entity = damageSource.getDirectEntity()) instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                this.blockUsingShield(livingEntity);
            }
            bl = true;
        }
        if (damageSource.is(DamageTypeTags.IS_FREEZING) && this.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
            f *= 5.0f;
        }
        this.walkAnimation.setSpeed(1.5f);
        boolean bl2 = true;
        if ((float)this.invulnerableTime > 10.0f && !damageSource.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
            if (f <= this.lastHurt) {
                return false;
            }
            this.actuallyHurt(damageSource, f - this.lastHurt);
            this.lastHurt = f;
            bl2 = false;
        }
        else {
            this.lastHurt = f;
            this.invulnerableTime = 20;
            this.actuallyHurt(damageSource, f);
            this.hurtTime = this.hurtDuration = 10;
        }
        if (damageSource.is(DamageTypeTags.DAMAGES_HELMET) && !this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            this.hurtHelmet(damageSource, f);
            f *= 0.75f;
        }
        if ((entity2 = damageSource.getEntity()) != null) {
            Wolf wolf;
            if (entity2 instanceof LivingEntity livingEntity2) {
                if (!damageSource.is(DamageTypeTags.NO_ANGER)) {
                    this.setLastHurtByMob(livingEntity2);
                }
            }
            if (entity2 instanceof Player player) {
                this.lastHurtByPlayerTime = 100;
                this.lastHurtByPlayer = player;
            }
            else if (entity2 instanceof Wolf && (wolf = (Wolf)entity2).isTame()) {
                this.lastHurtByPlayerTime = 100;
                LivingEntity livingEntity = wolf.getOwner();
                this.lastHurtByPlayer = livingEntity instanceof Player ? (Player)livingEntity : null;
            }
        }
        if (bl2) {
            if (bl) {
                this.level().broadcastEntityEvent(this, (byte)29);
            }
            else {
                this.level().broadcastDamageEvent(this, damageSource);
            }
            if (!(damageSource.is(DamageTypeTags.NO_IMPACT) || bl && !(f > 0.0f))) {
                this.markHurt();
            }
            if (entity2 != null && !damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
                double d = entity2.getX() - this.getX();
                double e = entity2.getZ() - this.getZ();
                while (d * d + e * e < 1.0E-4) {
                    d = (Math.random() - Math.random()) * 0.01;
                    e = (Math.random() - Math.random()) * 0.01;
                }
                if (!bl) {
                    this.indicateDamage(d, e);
                }
            }
        }
        if (this.isDeadOrDying()) {
            SoundEvent soundEvent = this.getDeathSound();
            if (bl2 && soundEvent != null) {
                this.playSound(soundEvent, this.getSoundVolume(), this.getVoicePitch());
            }
            this.die(damageSource);
        }
        else if (bl2) {
            this.playHurtSound(damageSource);
        }
        boolean bl3 = !bl || f > 0.0f;
        if (bl3) {
            ((LivingEntityAccessor)this).setLastDamageSource(damageSource);
            ((LivingEntityAccessor)this).setLastDamageStamp(this.level().getGameTime());
        }
        if (entity2 instanceof ServerPlayer) {
            CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)entity2, this, damageSource, g, f, bl);
        }
        return bl3;
    }
}