package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;


public class SugarInfusedStone extends Block {

    public SugarInfusedStone() {
        super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F));
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos1, boolean b) {
        sugarifyNeighboringWater(level, blockPos);
        super.neighborChanged(blockState, level, blockPos, block, blockPos1, b);
    }

    @Override
    public void onPlace(BlockState blockState, Level world, BlockPos blockPos, BlockState previousBlockState, boolean notify) {
        sugarifyNeighboringWater(world, blockPos);
        super.onPlace(blockState, world, blockPos, previousBlockState, notify);
    }

    private static void sugarifyNeighboringWater(LevelAccessor level, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            BlockPos sidePos = blockPos.relative(direction);
            FluidState sideFluid = level.getFluidState(sidePos);
            if(sideFluid.is(BzTags.CONVERTIBLE_TO_SUGAR_WATER) && !sideFluid.is(BzTags.SUGAR_WATER_FLUID) && sideFluid.isSource() && level.getBlockState(sidePos).getShape(level, sidePos).isEmpty()) {
                level.setBlock(sidePos, BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState(), 3);
            }
        }
    }
}
