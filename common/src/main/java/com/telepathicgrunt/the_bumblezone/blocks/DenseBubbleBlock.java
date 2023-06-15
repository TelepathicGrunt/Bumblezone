package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;


public class DenseBubbleBlock extends Block implements BucketPickup {

    public DenseBubbleBlock() {
        super(Properties.of()
                .mapColor(MapColor.WATER)
                .liquid()
                .noCollission()
                .strength(100.0F, 100.0F)
                .noLootTable()
                .replaceable()
                .sound(SoundType.EMPTY)
                .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
        return new ItemStack(Items.WATER_BUCKET);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Fluids.WATER.getPickupSound();
    }

    @Deprecated
    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setAirSupply(Math.min(livingEntity.getMaxAirSupply(), livingEntity.getAirSupply() + 2));
        }

        Vec3 vec3 = entity.getDeltaMovement();
        double newUpwardSpeed = Math.min(0.075, vec3.y + 0.01);
        entity.setDeltaMovement(vec3.x, newUpwardSpeed, vec3.z);

        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel)level;
            BlockPos entityPos = entity.blockPosition();
            for (int i = 0; i < 2; ++i) {
                serverLevel.sendParticles(ParticleTypes.BUBBLE, (double)entityPos.getX() + level.random.nextDouble(), entityPos.getY() + 0.5f + level.random.nextDouble(), (double)entityPos.getZ() + level.random.nextDouble(), 1, 0.0, 0.01, 0.0, 0.2);
            }
        }

        super.entityInside(state, level, blockPos, entity);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        double d = blockPos.getX();
        double e = blockPos.getY();
        double f = blockPos.getZ();

        level.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, d + 0.5, e, f + 0.5, 0.0, 0.04, 0.0);
        for (int i = 0; i < 5; ++i) {
            level.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, d + (double) randomSource.nextFloat(), e + (double) randomSource.nextFloat(), f + (double) randomSource.nextFloat(), 0.0, 0.04, 0.0);
        }
        if (randomSource.nextInt(150) == 0) {
            level.playLocalSound(d, e, f, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.2f + randomSource.nextFloat() * 0.2f, 0.9f + randomSource.nextFloat() * 0.15f, false);
        }
    }
}
