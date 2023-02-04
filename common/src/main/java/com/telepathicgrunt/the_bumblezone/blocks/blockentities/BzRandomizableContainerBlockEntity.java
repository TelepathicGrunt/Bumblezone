package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class BzRandomizableContainerBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    protected BzRandomizableContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public abstract Direction getInputDirection();

    public Direction getOutputDirection() {
        return getInputDirection().getOpposite();
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack stack, Direction direction) {
        return direction == getInputDirection() && stack.getItem().canFitInsideContainerItems();
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack stack, @NotNull Direction direction) {
        return direction == getOutputDirection();
    }
}
