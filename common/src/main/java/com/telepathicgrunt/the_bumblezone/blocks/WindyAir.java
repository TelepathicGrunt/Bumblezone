package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class WindyAir extends ProperFacingBlock {
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

        //TODO: Mark entity pushed for tick and reset at end of tick
        if (!entity.getBoundingBox().intersects(new AABB(blockPos, blockPos.offset(1, 1, 1)))) {
            return;
        }

        Direction windDirection = blockState.getValue(FACING);
        double strength = windDirection == Direction.UP ? 0.05D : 0.0275D;
        double size = entity.getBoundingBox().getSize();
        if (size <= 1) {
            strength = strength * (1 / (size / 2 + 0.5d));
        }
        else {
            strength = strength * (1 / (size * 2));
        }


        Vec3 pushPower = Vec3.atLowerCornerOf(windDirection.getNormal()).scale(strength);
        Vec3 newVelocity = entity.getDeltaMovement().add(pushPower);
        if (!entity.onGround() && newVelocity.y() < 0 && windDirection != Direction.DOWN) {
            newVelocity = newVelocity.add(0, -newVelocity.y() + 0.04f, 0);
        }
        entity.setDeltaMovement(newVelocity);
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
        }
    }
}
