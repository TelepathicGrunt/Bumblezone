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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

public class RotationAxisBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);

    protected RotationAxisBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(ROTATION, 0)
                .setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        int rotationIndex = state.getValue(ROTATION);

        Direction.Axis axis = state.getValue(AXIS);
        BlockState newState = RotatedPillarBlock.rotatePillar(state, rotation);

        int newRotationIndex = getRotatedRotation(axis, rotationIndex, rotation);
        return newState.setValue(ROTATION, newRotationIndex);
    }

    private static int getRotatedRotation(Direction.Axis axis, int rotationIndex, Rotation rotation) {
        for (int i = 0; i < rotation.ordinal(); i++) {
            if ((axis == Direction.Axis.Z && i % 2 == 1) ||
                (axis == Direction.Axis.X && i % 2 == 0))
            {
                if (rotationIndex % 2 == 0) {
                    rotationIndex += 1;
                }
                else {
                    rotationIndex -= 1;
                }
            }
        }
        return rotationIndex;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction.Axis axis = state.getValue(AXIS);
        int rotationIndex = state.getValue(ROTATION);

        int newRotationIndex = getMirroredRotation(axis, rotationIndex, mirror);
        return state.setValue(ROTATION, newRotationIndex);
    }

    private static int getMirroredRotation(Direction.Axis axis, int rotationIndex, Mirror mirror) {
        if (mirror == Mirror.NONE) {
            return rotationIndex;
        }

        boolean specialCase = (mirror == Mirror.LEFT_RIGHT && axis != Direction.Axis.Y);
        boolean rotationIsEven = (rotationIndex % 2 == 0);
        if ((specialCase && rotationIsEven) || (!specialCase && !rotationIsEven)) {
            return (rotationIndex+2) % 4;
        }
        else {
            return rotationIndex;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
        builder.add(ROTATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction clickFaceDirection = blockPlaceContext.getClickedFace();

        Direction horizontalDirection = blockPlaceContext.getHorizontalDirection();
        Direction.Axis axis = Direction.Axis.Y;
        int rotation = 0;

        if (clickFaceDirection.getAxis() == Direction.Axis.Y) {
            axis = horizontalDirection.getAxis();
            if (horizontalDirection == Direction.SOUTH || horizontalDirection == Direction.WEST) {
                rotation = 1;
            }
        }

        return this.defaultBlockState()
                .setValue(AXIS, axis)
                .setValue(ROTATION, rotation);
    }
}
