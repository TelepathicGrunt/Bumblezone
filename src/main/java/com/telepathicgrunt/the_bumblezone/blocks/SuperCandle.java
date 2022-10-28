package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;


public interface SuperCandle {
    static boolean canBeLit(Level level, BlockState state, BlockPos pos) {
        BlockState aboveState = level.getBlockState(pos.above());
        return aboveState.is(BzTags.CANDLE_WICKS) &&
                !state.getValue(BlockStateProperties.WATERLOGGED) &&
                !state.getValue(BlockStateProperties.LIT) &&
                !aboveState.getValue(BlockStateProperties.WATERLOGGED) &&
                !aboveState.getValue(BlockStateProperties.LIT);
    }

    static void placeWickIfPossible(LevelAccessor levelAccessor, BlockPos blockPos, boolean lit) {
        ChunkAccess chunkAccess = levelAccessor.getChunk(blockPos);
        BlockPos wickPosition = SuperCandleWick.getLitWickPositionAbove(levelAccessor, blockPos);
        BlockState aboveState = chunkAccess.getBlockState(blockPos.above());

        boolean wickSpotAvaliableAbove = false;
        if (wickPosition == null) {
            if (!aboveState.is(BzTags.CANDLE_WICKS) && (aboveState.getMaterial().isReplaceable() || aboveState.isAir())) {
                wickPosition = blockPos.above();
                wickSpotAvaliableAbove = true;
            }
            else {
                return;
            }
        }

        BlockState wickState = chunkAccess.getBlockState(wickPosition);
        boolean isBelowSoul = SuperCandleWick.isSoulBelowInRange(levelAccessor, blockPos.below());
        boolean wickWaterlogged = wickState.getFluidState().is(FluidTags.WATER);

        if ((wickState.is(BzBlocks.SUPER_CANDLE_WICK.get()) && isBelowSoul) ||
            (wickState.is(BzBlocks.SUPER_CANDLE_WICK_SOUL.get()) && !isBelowSoul) ||
            wickSpotAvaliableAbove)
        {
            if (wickState.is(BzTags.CANDLE_WICKS)) {
                lit = wickState.getValue(BlockStateProperties.LIT);
            }
            Block wickBlock = (isBelowSoul && lit) ? BzBlocks.SUPER_CANDLE_WICK_SOUL.get() : BzBlocks.SUPER_CANDLE_WICK.get();
            BlockState candleWick = wickBlock.defaultBlockState()
                    .setValue(BlockStateProperties.LIT, lit)
                    .setValue(BlockStateProperties.WATERLOGGED, wickWaterlogged);
            levelAccessor.setBlock(wickPosition, candleWick, 3);

            if (wickWaterlogged) {
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                mutableBlockPos.set(wickPosition.below());
                for (int i = 0; i < chunkAccess.getMaxBuildHeight() - mutableBlockPos.getY(); i++) {
                    BlockState currentState = chunkAccess.getBlockState(mutableBlockPos);
                    if (currentState.getBlock() instanceof SuperCandle) {
                        levelAccessor.setBlock(blockPos, currentState.setValue(BlockStateProperties.WATERLOGGED, true), 3);
                    }
                    else {
                        break;
                    }
                    mutableBlockPos.move(Direction.DOWN);
                }
            }
        }
    }
}
