package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

public class DirectionFacingBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);

    protected DirectionFacingBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(ROTATION, 1)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        Direction facingDirection = state.getValue(FACING);
        int rotationIndex = state.getValue(ROTATION);

        Direction newFacingDirection = rotation.rotate(facingDirection);
        int newRotationIndex = getRotatedRotation(facingDirection, rotationIndex, rotation);

        return state
                .setValue(FACING, newFacingDirection)
                .setValue(ROTATION, newRotationIndex);
    }

    private static int getRotatedRotation(Direction attachmentFace, int rotationIndex, Rotation rotation) {
        if (attachmentFace == Direction.DOWN) {
            return (rotationIndex + rotation.ordinal()) % 4;
        }
        else if (attachmentFace == Direction.UP) {
            return (rotationIndex + 4 - rotation.ordinal()) % 4;
        }
        else {
            return rotationIndex;
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction facingDirection = state.getValue(FACING);
        int rotationIndex = state.getValue(ROTATION);

        Direction newFacingDirection = mirror.mirror(facingDirection);
        int newRotationIndex = getMirroredRotation(facingDirection, rotationIndex, mirror);

        return state
                .setValue(FACING, newFacingDirection)
                .setValue(ROTATION, newRotationIndex);
    }

    private static int getMirroredRotation(Direction attachmentFace, int rotationIndex, Mirror mirror) {
        if (mirror == Mirror.NONE) {
            return rotationIndex;
        }

        boolean specialCase = (mirror == Mirror.LEFT_RIGHT && attachmentFace.getAxis() == Direction.Axis.Y);
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
        builder.add(FACING);
        builder.add(ROTATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction clickFaceDirection = blockPlaceContext.getClickedFace();

        Vec3 clickedPos = blockPlaceContext.getClickLocation().subtract(blockPlaceContext.getClickedPos().getCenter());
        double xSpot = clickedPos.x();
        double ySpot = clickedPos.y();
        double zSpot = clickedPos.z();
        int rotation;
        if (clickFaceDirection.getAxis() == Direction.Axis.Y) {
            if (Math.abs(xSpot) > Math.abs(zSpot)) {
                if (xSpot > 0) {
                    rotation = 1;
                }
                else {
                    rotation = 3;
                }
            }
            else {
                if (zSpot > 0) {
                    rotation = 0;
                }
                else {
                    rotation = 2;
                }
            }
        }
        else if (clickFaceDirection.getAxis() == Direction.Axis.X) {
            if (Math.abs(ySpot) > Math.abs(zSpot)) {
                if (ySpot > 0) {
                    rotation = 0;
                }
                else {
                    rotation = 3;
                }
            }
            else {
                if (zSpot > 0) {
                    rotation = 1;
                }
                else {
                    rotation = 2;
                }
            }
        }
        else {
            if (Math.abs(ySpot) > Math.abs(xSpot)) {
                if (ySpot > 0) {
                    rotation = 0;
                }
                else {
                    rotation = 2;
                }
            }
            else {
                if (xSpot > 0) {
                    rotation = 1;
                }
                else {
                    rotation = 3;
                }
            }
        }

        return this.defaultBlockState()
                .setValue(FACING, clickFaceDirection)
                .setValue(ROTATION, rotation);
    }

}
