package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public interface SuperCandle {
    default boolean canBeLit(Level level, BlockState state, BlockPos pos) {
        BlockState aboveState = level.getBlockState(pos.above());
        return aboveState.is(BzBlocks.SUPER_CANDLE_WICK.get()) &&
                !state.getValue(BlockStateProperties.WATERLOGGED) &&
                !state.getValue(BlockStateProperties.LIT) &&
                !aboveState.getValue(BlockStateProperties.WATERLOGGED) &&
                !aboveState.getValue(BlockStateProperties.LIT);
    }

    default void placeWickIfPossible(LevelAccessor levelAccessor, BlockPos blockPos, boolean lit) {
        BlockPos abovePos = blockPos.above();
        BlockState aboveState = levelAccessor.getBlockState(abovePos);
        if (!aboveState.is(BzBlocks.SUPER_CANDLE_WICK.get()) && (aboveState.getMaterial().isReplaceable() || aboveState.isAir())) {
            boolean wickWaterlogged = aboveState.getFluidState().is(FluidTags.WATER);
            BlockState candleWick = BzBlocks.SUPER_CANDLE_WICK.get().defaultBlockState()
                    .setValue(BlockStateProperties.LIT, lit)
                    .setValue(BlockStateProperties.WATERLOGGED, wickWaterlogged);
            levelAccessor.setBlock(abovePos, candleWick, 3);

            if (wickWaterlogged) {
                BlockState currentState = levelAccessor.getBlockState(blockPos);
                if (currentState.getBlock() instanceof SuperCandle) {
                    levelAccessor.setBlock(blockPos, currentState.setValue(BlockStateProperties.WATERLOGGED, true), 3);
                }
            }
        }
    }
}
