package com.telepathicgrunt.the_bumblezone.blocks;

import com.google.common.collect.MapMaker;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;


public class WindyAir extends ProperFacingBlock {
    private static final ConcurrentMap<UUID, Map<Direction, Integer>> APPLIED_PUSH_FOR_ENTITY = new MapMaker().concurrencyLevel(2).weakKeys().makeMap();

    public WindyAir() {
        super(Properties.of()
                .strength(-1.0f, 3600000.8f)
                .noCollission()
                .replaceable()
                .noLootTable()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY));

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add().add(FACING);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return collisionContext.isHoldingItem(BzItems.WINDY_AIR.get()) ? Shapes.block() : Shapes.empty();
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof Player player) {
            if ((player.isCreative() && player.getAbilities().flying) || player.isSpectator()) {
                return;
            }
        }
        else if (entity.getType().is(BzTags.WINDY_AIR_IMMUNE)) {
            return;
        }

        if (APPLIED_PUSH_FOR_ENTITY.size() >= 200) {
            APPLIED_PUSH_FOR_ENTITY.clear();
        }

        if (!APPLIED_PUSH_FOR_ENTITY.containsKey(entity.getUUID())) {
            APPLIED_PUSH_FOR_ENTITY.put(entity.getUUID(), new HashMap<>());
        }

        Direction windDirection = blockState.getValue(FACING);
        if (APPLIED_PUSH_FOR_ENTITY.get(entity.getUUID()).getOrDefault(windDirection, -1) == entity.tickCount) {
            return;
        }

        double strength = windDirection == Direction.UP ? 0.0885D : 0.0275D;
        double size = entity.getBoundingBox().getSize();
        if (size <= 1) {
            strength = strength * (1 / (size / 2 + 0.5D));
        }
        else {
            strength = strength * (1 / (size * 2));
        }

        Vec3 pushPower = Vec3.atLowerCornerOf(windDirection.getNormal()).scale(strength);
        Vec3 newVelocity = entity.getDeltaMovement();
        newVelocity = newVelocity.add(pushPower);
        if (!entity.onGround() && newVelocity.y() < 0 && windDirection != Direction.DOWN) {
            newVelocity = newVelocity.add(0, -newVelocity.y() + 0.04F, 0);
        }
        entity.setDeltaMovement(newVelocity);
        APPLIED_PUSH_FOR_ENTITY.get(entity.getUUID()).put(windDirection, entity.tickCount);
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextFloat() < 0.25f) {
            Direction windDirection = blockState.getValue(FACING);
            double strength = 0.1D;
            Vec3 pushPower = Vec3.atLowerCornerOf(windDirection.getNormal()).scale(strength);

            level.addParticle(
                    BzParticles.WIND_PARTICLE.get(),
                    (double)blockPos.getX() + randomSource.nextDouble(),
                    (double)blockPos.getY() + randomSource.nextDouble(),
                    (double)blockPos.getZ() + randomSource.nextDouble(),
                    pushPower.x() + (randomSource.nextGaussian() * 0.003d),
                    pushPower.y() + (randomSource.nextGaussian() * 0.003d),
                    pushPower.z() + (randomSource.nextGaussian() * 0.003d));

            Player nearestPlayer = level.getNearestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 3, true);
            if (nearestPlayer != null) {
                level.playSound(null, blockPos, BzSounds.WINDY_AIR_BLOWS.get(), SoundSource.AMBIENT, (randomSource.nextFloat() * 0.05F) + 0.5F, (randomSource.nextFloat() * 0.1F) + 0.8F);
            }
        }
    }
}
