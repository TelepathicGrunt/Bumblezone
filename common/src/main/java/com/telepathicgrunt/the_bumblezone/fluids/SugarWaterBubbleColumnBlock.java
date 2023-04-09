package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;

import java.util.Optional;


public class SugarWaterBubbleColumnBlock extends BubbleColumnBlock {

    public SugarWaterBubbleColumnBlock() {
        super(BlockBehaviour.Properties.of(Material.BUBBLE_COLUMN).noCollission().noLootTable());
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
        return new ItemStack(BzItems.SUGAR_WATER_BUCKET.get());
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return BzFluids.SUGAR_WATER_FLUID.get().getSource(false);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return BzFluids.SUGAR_WATER_FLUID.get().getPickupSound();
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockState belowState = levelReader.getBlockState(blockPos.below());
        return belowState.is(BzFluids.SUGAR_WATER_BUBBLE_COLUMN_BLOCK.get()) || belowState.is(Blocks.BUBBLE_COLUMN) || belowState.is(BzTags.DOWNWARD_BUBBLE_COLUMN_CAUSING) || belowState.is(BzTags.UPWARD_BUBBLE_COLUMN_CAUSING);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        levelAccessor.scheduleTick(blockPos, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickDelay(levelAccessor));
        if (!blockState.canSurvive(levelAccessor, blockPos) || direction == Direction.DOWN || direction == Direction.UP && !blockState2.is(Blocks.BUBBLE_COLUMN) && canExistIn(blockState2)) {
            levelAccessor.scheduleTick(blockPos, BzFluids.SUGAR_WATER_BUBBLE_COLUMN_BLOCK.get(), 5);
        }

        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        updateColumn(serverLevel, blockPos, blockState, serverLevel.getBlockState(blockPos.below()));
    }

    public static void updateColumn(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        updateColumn(levelAccessor, blockPos, levelAccessor.getBlockState(blockPos), blockState);
    }

    public static void updateColumn(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, BlockState blockState2) {
        if (canExistIn(blockState)) {
            BlockState sugarWaterColumnState = getColumnState(blockState2);
            levelAccessor.setBlock(blockPos, sugarWaterColumnState, 2);
            BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable().move(Direction.UP);
            boolean isBubblePlacing = sugarWaterColumnState.is(BzFluids.SUGAR_WATER_BUBBLE_COLUMN_BLOCK.get());

            BlockState currentState = levelAccessor.getBlockState(mutableBlockPos);
            boolean isVanilla = currentState.is(Blocks.WATER) || currentState.is(Blocks.BUBBLE_COLUMN);
            while(canExistIn(currentState) || isVanilla) {
                if (isVanilla) {
                    if (!levelAccessor.setBlock(mutableBlockPos, !isBubblePlacing ? Blocks.WATER.defaultBlockState() : Blocks.BUBBLE_COLUMN.defaultBlockState().setValue(DRAG_DOWN, sugarWaterColumnState.getValue(DRAG_DOWN)), 2)) {
                        return;
                    }
                }
                else if (!levelAccessor.setBlock(mutableBlockPos, sugarWaterColumnState, 2)) {
                    return;
                }

                mutableBlockPos.move(Direction.UP);
                currentState = levelAccessor.getBlockState(mutableBlockPos);
                isVanilla = currentState.is(Blocks.WATER) || currentState.is(Blocks.BUBBLE_COLUMN);
            }
        }
    }

    private static BlockState getColumnState(BlockState blockState) {
        if (blockState.is(BzFluids.SUGAR_WATER_BUBBLE_COLUMN_BLOCK.get())) {
            return blockState;
        }
        else if (blockState.is(BzTags.UPWARD_BUBBLE_COLUMN_CAUSING) || (blockState.is(Blocks.BUBBLE_COLUMN) && !blockState.getValue(BubbleColumnBlock.DRAG_DOWN))) {
            return BzFluids.SUGAR_WATER_BUBBLE_COLUMN_BLOCK.get().defaultBlockState().setValue(DRAG_DOWN, false);
        }
        else if (blockState.is(BzTags.DOWNWARD_BUBBLE_COLUMN_CAUSING) || (blockState.is(Blocks.BUBBLE_COLUMN) && blockState.getValue(BubbleColumnBlock.DRAG_DOWN))) {
            return BzFluids.SUGAR_WATER_BUBBLE_COLUMN_BLOCK.get().defaultBlockState().setValue(DRAG_DOWN, true);
        }
        else {
            return BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState();
        }
    }

    private static boolean canExistIn(BlockState blockState) {
        return blockState.is(BzFluids.SUGAR_WATER_BUBBLE_COLUMN_BLOCK.get()) || blockState.is(BzFluids.SUGAR_WATER_BLOCK.get()) && blockState.getFluidState().getAmount() >= 8 && blockState.getFluidState().isSource();
    }

    /**
     * Heal bees slightly if they are in Sugar Water and aren't taking damage.
     */
    @Deprecated
    @Override
    public void entityInside(BlockState state, Level world, BlockPos position, Entity entity) {
        if (entity instanceof Bee beeEntity) {
            if (beeEntity.hurtMarked) beeEntity.heal(1);
        }

        super.entityInside(state, world, position, entity);
    }
}
