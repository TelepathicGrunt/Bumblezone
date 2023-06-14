package com.telepathicgrunt.the_bumblezone.blocks;

import com.google.common.collect.MapMaker;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;


public class HeavyAir extends Block {
    private static final ConcurrentMap<UUID, Integer> APPLIED_PUSH_FOR_ENTITY = new MapMaker().concurrencyLevel(2).weakKeys().makeMap();

    public HeavyAir() {
        super(Properties.of()
                .strength(-1.0f, 3600000.8f)
                .noCollission()
                .replaceable()
                .noLootTable()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return collisionContext.isHoldingItem(BzItems.HEAVY_AIR.get()) ? Shapes.block() : Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1.0f;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof Bee || entity instanceof BeehemothEntity || entity instanceof Projectile || entity instanceof Ghast) {
            return;
        }

        if (APPLIED_PUSH_FOR_ENTITY.getOrDefault(entity.getUUID(), -1) == level.getGameTime()) {
            return;
        }

        if (entity instanceof LivingEntity livingEntity && livingEntity.tickCount % 10 == 0) {
            if (livingEntity.hasEffect(MobEffects.LEVITATION)) {
                livingEntity.removeEffect(MobEffects.LEVITATION);
            }
            if (livingEntity.hasEffect(MobEffects.SLOW_FALLING)) {
                livingEntity.removeEffect(MobEffects.SLOW_FALLING);
            }
            if (livingEntity.hasEffect(MobEffects.JUMP)) {
                livingEntity.removeEffect(MobEffects.JUMP);
            }
        }

        if (entity instanceof Player player) {
            if ((player.isCreative() && player.getAbilities().flying) || player.isSpectator()) {
                return;
            }
            else if (player.getAbilities().flying) {
                player.getAbilities().flying = false;
            }
        }

        entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.015, 0));
        APPLIED_PUSH_FOR_ENTITY.put(entity.getUUID(), entity.tickCount);
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextFloat() < 0.04f) {
            level.addParticle(
                    BzParticles.DUST_PARTICLE.get(),
                    (double)blockPos.getX() + randomSource.nextDouble(),
                    (double)blockPos.getY() + randomSource.nextDouble(),
                    (double)blockPos.getZ() + randomSource.nextDouble(),
                    randomSource.nextGaussian() * 0.003d,
                    randomSource.nextGaussian() * 0.0002d,
                    randomSource.nextGaussian() * 0.003d);
        }
    }
}
