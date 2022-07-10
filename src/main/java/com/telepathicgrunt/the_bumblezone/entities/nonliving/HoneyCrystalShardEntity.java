package com.telepathicgrunt.the_bumblezone.entities.nonliving;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;

public class HoneyCrystalShardEntity extends AbstractArrow {

    public HoneyCrystalShardEntity(EntityType<? extends HoneyCrystalShardEntity> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(3);
        this.setKnockback(1);
        this.setCritArrow(true);
    }

    public HoneyCrystalShardEntity(Level level, LivingEntity livingEntity) {
        super(BzEntities.HONEY_CRYSTAL_SHARD.get(), livingEntity, level);
        this.setBaseDamage(3);
        this.setKnockback(1);
        this.setCritArrow(true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (!this.inGround && this.level.getGameTime() % 5 == this.random.nextInt(3)) {
                this.makeParticle(1);
            }
        }
        else if (this.inGround && this.inGroundTime != 0 && this.inGroundTime >= 600) {
            this.level.broadcastEntityEvent(this, (byte)0);
        }
    }

    private void makeParticle(int particlesToSpawn) {
        if (particlesToSpawn > 0) {
            double red = 0.8d;
            double green = 0.6d;
            double blue = 0.1d;

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
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean tryPickup(Player player) {
        if (player.position().closerThan(this.position(), 0.85f)) {
            if(this.level instanceof ServerLevel serverLevel) {
                showParticles(serverLevel, this, 5);
                level.playSound(null,
                        player.blockPosition(),
                        BzSounds.HONEY_CRYSTAL_SHARD_SHATTER.get(),
                        SoundSource.PLAYERS,
                        1.0F,
                        (level.getRandom().nextFloat() * 0.2F) + 0.6F);
            }
            this.remove(RemovalReason.DISCARDED);
            return false;
        }

        return false;
    }

    private static void showParticles(ServerLevel serverLevel, Entity entity, int particleNumber) {
        BlockState blockstate = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();

        serverLevel.sendParticles(
                new BlockParticleOption(ParticleTypes.BLOCK, blockstate),
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                particleNumber,
                serverLevel.getRandom().nextFloat() * 0.5 - 0.25f,
                serverLevel.getRandom().nextFloat() * 0.2f + 0.2f,
                serverLevel.getRandom().nextFloat() * 0.5 - 0.25f,
                serverLevel.getRandom().nextFloat() * 0.4 + 0.2f);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return BzSounds.HONEY_CRYSTAL_SHARD_HIT.get();
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}