package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class AxisFacingBlock extends RotatedPillarBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected AxisFacingBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return super.rotate(state, rotation).setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return super.mirror(state, mirror).rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction clickFaceDirection = blockPlaceContext.getClickedFace();
        Direction horizontalDirection = blockPlaceContext.getHorizontalDirection();

        Direction.Axis axis = Direction.Axis.Y;
        if (clickFaceDirection.getAxis() == Direction.Axis.Y) {
            axis = horizontalDirection.getAxis();
        }

        Direction direction;
        if (clickFaceDirection.getAxis() != Direction.Axis.Y) {
            direction = horizontalDirection;
        }
        else {
            direction = blockPlaceContext.getNearestLookingVerticalDirection();
        }

        return this.defaultBlockState()
                .setValue(AXIS, axis)
                .setValue(FACING, direction);
    }
}
