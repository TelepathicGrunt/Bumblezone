package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzDamageSources;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PurpleSpikeEntity extends Entity {
    private static final EntityDataAccessor<Boolean> DATA_ID_SPIKE_CHARGE = SynchedEntityData.defineId(PurpleSpikeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_SPIKE_ACTIVE = SynchedEntityData.defineId(PurpleSpikeEntity.class, EntityDataSerializers.BOOLEAN);

    public int spikeChargeTimer = 0;
    public int spikeTimer = 0;
    public boolean spikeChargeClientPhaseTracker = false;
    public int spikeChargeClientTimeTracker = 0;

    public PurpleSpikeEntity(EntityType<? extends PurpleSpikeEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ID_SPIKE_CHARGE, spikeChargeTimer > 0);
        this.entityData.define(DATA_ID_SPIKE_ACTIVE, spikeTimer > 0);
    }

    public boolean hasSpikeCharge() {
        return this.entityData.get(DATA_ID_SPIKE_CHARGE);
    }

    protected void setHasSpikeCharge(boolean hasSpikeCharge) {
        this.entityData.set(DATA_ID_SPIKE_CHARGE, hasSpikeCharge);
    }

    public boolean hasSpike() {
        return this.entityData.get(DATA_ID_SPIKE_ACTIVE);
    }

    protected void setHasSpike(boolean hasSpike) {
        this.entityData.set(DATA_ID_SPIKE_ACTIVE, hasSpike);
    }

    public int getSpikeChargeTimer() {
        return spikeChargeTimer;
    }

    public void setSpikeChargeTimer(int spikeChargeTimer) {
        // Keep spike active. Don't reset to previous state.
        if (this.getSpikeTimer() > 0 && this.spikeChargeTimer == 0) {
            this.addSpikeTimer(spikeChargeTimer);
            return;
        }
        this.spikeChargeTimer = spikeChargeTimer;
    }

    public int getSpikeTimer() {
        return spikeTimer;
    }

    public void setSpikeTimer(int spikeTimer) {
        this.spikeTimer = spikeTimer;
    }

    public void addSpikeTimer(int spikeTimer) {
        this.spikeTimer += spikeTimer;
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return 0;
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.tickCount % 5 == 0 && this.hasSpikeCharge()) {
                this.makeParticle(1, false);
            }
            else if (!this.hasSpikeCharge() && this.hasSpike()){
                this.makeParticle(3, false);
            }

            if (this.spikeChargeClientPhaseTracker != this.hasSpikeCharge()) {
                if (!this.spikeChargeClientPhaseTracker && this.hasSpike()) {
                    this.spikeChargeClientTimeTracker = 0;
                }
                this.spikeChargeClientPhaseTracker = this.hasSpikeCharge();
            }

            if (!this.hasSpikeCharge() && !this.hasSpike()) {
                this.spikeChargeClientTimeTracker = 0;
            }
            else {
                this.spikeChargeClientTimeTracker++;
            }

        }
        else {
            this.setHasSpike(this.getSpikeTimer() > 0);
            this.setHasSpikeCharge(this.getSpikeChargeTimer() > 0);

            if (this.spikeChargeTimer > 0) {
                this.spikeChargeTimer--;
            }
            else if (this.spikeTimer > 0) {
                this.spikeTimer--;
            }
        }

        if (this.hasSpike() && !hasSpikeCharge()) {
            List<Entity> list = this.level().getEntities(this, this.getBoundingBox());
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity instanceof LivingEntity livingEntity) {
                        float damageAmount;

                        if (entity instanceof ServerPlayer serverPlayer) {
                            if (serverPlayer.isCreative()) {
                                continue;
                            }

                            if (EssenceOfTheBees.hasEssence(serverPlayer)) {
                                damageAmount = serverPlayer.getMaxHealth() / 5;
                            }
                            else {
                                damageAmount = serverPlayer.getMaxHealth() / 3;
                            }
                        }
                        else {
                            damageAmount = livingEntity.getMaxHealth() / 10;
                        }

                        livingEntity.hurt(level().damageSources().source(BzDamageSources.SPIKE_TYPE), damageAmount);
                        this.makeParticle(1, true);
                    }
                }
            }
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    private void makeParticle(int particlesToSpawn, boolean hit) {
        if (particlesToSpawn > 0) {
            if (hit) {
                Vec3 center = this.blockPosition().getCenter();
                for(int i = 0; i < particlesToSpawn; ++i) {
                    this.level().addParticle(
                            ParticleTypes.CRIT,
                            center.x(),
                            center.y() - 0.5,
                            center.z(),
                            (this.random.nextFloat() - 0.5f),
                            (this.random.nextFloat() + 0.5f),
                            (this.random.nextFloat() - 0.5f));
                    this.level().addParticle(
                            ParticleTypes.DAMAGE_INDICATOR,
                            center.x(),
                            center.y() - 0.5,
                            center.z(),
                            (this.random.nextFloat() - 0.5f),
                            (this.random.nextFloat() + 0.5f),
                            (this.random.nextFloat() - 0.5f));
                }
            }
            else {
                double size = this.getBoundingBox().getSize();
                double x = this.getX() + ((this.random.nextFloat() - 0.5f) * size);
                double y = this.getY() + (size / 3) + ((this.random.nextFloat() - 0.5f) * size);
                double z = this.getZ() + ((this.random.nextFloat() - 0.5f) * size);

                for(int i = 0; i < particlesToSpawn; ++i) {
                    this.level().addParticle(
                            ParticleTypes.ENCHANTED_HIT,
                            x,
                            y,
                            z,
                            (this.random.nextFloat() - 0.5f) * 0.05f,
                            (this.random.nextFloat() + 0.5f) * 0.5f,
                            (this.random.nextFloat() - 0.5f) * 0.05f);
                }
            }
        }
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;//this.hasSpike() || this.hasSpikeCharge();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("spike_timer", this.getSpikeTimer());
        compoundTag.putInt("spike_charge_timer", this.getSpikeChargeTimer());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        this.setSpikeTimer(compoundTag.getInt("spike_timer"));
        this.setSpikeChargeTimer(compoundTag.getInt("spike_charge_timer"));
    }
}