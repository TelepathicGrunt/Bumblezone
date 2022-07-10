package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class BeeStingerEntity extends AbstractArrow {

    public BeeStingerEntity(EntityType<? extends BeeStingerEntity> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(0.5d);
    }

    public BeeStingerEntity(Level level, LivingEntity livingEntity) {
        super(BzEntities.BEE_STINGER_ENTITY.get(), livingEntity, level);
        this.setBaseDamage(0.5d);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (!this.inGround) {
                this.makeParticle(4);
            }
        }
        else if (this.inGround && this.inGroundTime != 0 && this.inGroundTime >= 600) {
            this.level.broadcastEntityEvent(this, (byte)0);
        }
    }

    private void makeParticle(int particlesToSpawn) {
        if (particlesToSpawn > 0) {
            double red = 0.3d;
            double green = 0.3d;
            double blue = 0.3d;

            for(int i = 0; i < particlesToSpawn; ++i) {
                this.level.addParticle(
                        ParticleTypes.ENTITY_EFFECT,
                        this.getRandomX(0.5D),
                        this.getRandomY(),
                        this.getRandomZ(0.5D),
                        red,
                        green,
                        blue);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        Entity entity = entityHitResult.getEntity();

        if(entity instanceof LivingEntity livingEntity &&
            livingEntity.isDeadOrDying() &&
            this.getOwner() instanceof ServerPlayer serverPlayer)
        {
            BzCriterias.STINGER_SPEAR_LONG_RANGE_KILL_TRIGGER.trigger(serverPlayer); // TODO: make bee stinger kill advancement
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity livingEntity) {
        if (livingEntity.getMobType() != MobType.UNDEAD) {
            boolean isPoisoned = livingEntity.hasEffect(MobEffects.POISON);
            boolean isSlowed = livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN);
            boolean isWeakened = livingEntity.hasEffect(MobEffects.WEAKNESS);
            boolean isParalyzed = livingEntity.hasEffect(BzEffects.PARALYZED.get());

            livingEntity.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    120,
                    0,
                    true,
                    true,
                    true));

            if (!isParalyzed && isPoisoned && livingEntity.getRandom().nextFloat() < 0.35f) {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        180,
                        0,
                        true,
                        true,
                        true));
            }

            if (!isParalyzed && isPoisoned && isSlowed && livingEntity.getRandom().nextFloat() < 0.3f) {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS,
                        200,
                        0,
                        true,
                        true,
                        true));
            }

            if (isPoisoned && isSlowed && isWeakened && livingEntity.getRandom().nextFloat() < 0.25f) {
                livingEntity.addEffect(new MobEffectInstance(
                        BzEffects.PARALYZED.get(),
                        100,
                        0,
                        true,
                        true,
                        true));

                livingEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                livingEntity.removeEffect(MobEffects.WEAKNESS);
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(BzItems.BEE_STINGER.get());
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return BzSounds.BEE_STINGER_HIT.get();
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}