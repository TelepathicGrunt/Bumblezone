package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantmentApplication;
import com.telepathicgrunt.the_bumblezone.enchantments.PotentPoisonEnchantmentApplication;
import com.telepathicgrunt.the_bumblezone.enchantments.datacomponents.ParalyzeMarker;
import com.telepathicgrunt.the_bumblezone.items.StingerSpearItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownStingerSpearEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownStingerSpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownStingerSpearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final ItemStack DEFAULT_SPEAR_STACK = new ItemStack(BzItems.STINGER_SPEAR.get());
    private boolean dealtDamage;
    public int clientSideReturnSpearTickCount;

    public ThrownStingerSpearEntity(EntityType<? extends ThrownStingerSpearEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownStingerSpearEntity(Level level, LivingEntity livingEntity, ItemStack ammo, ItemStack weaponItem) {
        super(BzEntities.THROWN_STINGER_SPEAR_ENTITY.get(), livingEntity, level, ammo, weaponItem);
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(weaponItem));
        this.entityData.set(ID_FOIL, weaponItem.hasFoil());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte)0);
        builder.define(ID_FOIL, false);
    }

    private byte getLoyaltyFromItem(ItemStack itemStack) {
        Level var3 = this.level();
        return var3 instanceof ServerLevel serverLevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, itemStack, this), 0, 127)
                : 0;
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int loyalty = this.entityData.get(ID_LOYALTY);
        if (loyalty > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            }
            else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double)loyalty, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double returnSpeed = 0.05D * (double)loyalty;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(returnSpeed)));
                if (this.clientSideReturnSpearTickCount == 0) {
                    this.playSound(BzSounds.STINGER_SPEAR_RETURN.get(), 10.0F, 1.0F);
                }

                ++this.clientSideReturnSpearTickCount;
            }
        }

        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        Level level = this.level();

        float damageAmount = StingerSpearItem.BASE_THROWN_DAMAGE;
        DamageSource damageSource = this.damageSources().trident(this, owner == null ? this : owner);
        if (level instanceof ServerLevel serverLevel) {
            damageAmount += EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), entity, damageSource, damageAmount);
        }

        DamageSource damagesource = damageSources().trident(this, owner == null ? this : owner);
        dealtDamage = true;
        if (entity.hurt(damagesource, damageAmount)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity hitEntity) {
                if (level instanceof ServerLevel serverLevel) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damageSource, this.getWeaponItem());
                }

                if (hitEntity instanceof LivingEntity livingEntity) {
                    this.doKnockback(livingEntity, damageSource);
                    this.doPostHurtEffects(livingEntity);
                }

                this.doPostHurtEffects(hitEntity);
            }
        }

        if(entity instanceof LivingEntity livingEntity &&
            livingEntity.isDeadOrDying() &&
            this.getOwner() instanceof ServerPlayer serverPlayer)
        {
            if (!serverPlayer.blockPosition().closerThan(this.blockPosition(), 50)) {
                BzCriterias.STINGER_SPEAR_LONG_RANGE_KILL_TRIGGER.get().trigger(serverPlayer);
            }

            if (entity.getType() == EntityType.WITHER && PlayerDataHandler.rootAdvancementDone(serverPlayer)) {
                BzCriterias.STINGER_SPEAR_KILLED_WITH_WITHER_TRIGGER.get().trigger(serverPlayer);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        this.playSound(BzSounds.STINGER_SPEAR_HIT.get(), 1.0F, 1.0F);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity victim) {
       if (!victim.getType().is(EntityTypeTags.UNDEAD)) {
           PotentPoisonEnchantmentApplication.doPostAttackBoostedPoison(this.getPickupItemStackOrigin(), victim);

            if (this.getOwner() instanceof ServerPlayer serverPlayer) {
                BzCriterias.STINGER_SPEAR_POISONING_TRIGGER.get().trigger(serverPlayer);
            }

            if (this.getOwner() instanceof LivingEntity ownerEntity && !victim.getType().is(BzTags.PARALYZED_IMMUNE)) {
                Pair<ParalyzeMarker, Integer> neurotoxin = NeurotoxinsEnchantmentApplication.getNeurotoxinEnchantLevel(this.getPickupItemStackOrigin());
                if (neurotoxin != null && neurotoxin.getSecond() > 0) {
                    this.getPickupItemStackOrigin().hurtAndBreak(neurotoxin.getFirst().durabilityDrainOnValidTargetHit(), ownerEntity, EquipmentSlot.MAINHAND);
                }
            }
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.75F;
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        }
        else {
            return false;
        }
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    protected EntityHitResult findHitEntity(Vec3 vec3, Vec3 vec31) {
        return this.dealtDamage ? null : super.findHitEntity(vec3, vec31);
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return BzItems.STINGER_SPEAR.get().getDefaultInstance();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return BzSounds.STINGER_SPEAR_HIT_GROUND.get();
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    @Override
    public void tickDespawn() {
        int loyalty = this.entityData.get(ID_LOYALTY);
        if (!this.isInvulnerable() && (this.pickup != Pickup.ALLOWED || loyalty <= 0)) {
            super.tickDespawn();
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        dealtDamage = compoundTag.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(this.getWeaponItem()));
        this.entityData.set(ID_FOIL, compoundTag.getBoolean("IsFoil"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("DealtDamage", dealtDamage);
        compoundTag.putBoolean("IsFoil", isFoil());
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}