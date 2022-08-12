package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class GlisteringHoneyCrystal extends RotatedPillarBlock {
    public GlisteringHoneyCrystal() {
        super(Properties.of(Material.GLASS, MaterialColor.COLOR_ORANGE).lightLevel((blockState) -> 11).strength(0.4F, 0.4f).noOcclusion());
    }

    @Override
    public boolean skipRendering(BlockState blockState, BlockState blockState1, Direction direction) {
        return blockState1.is(this) || super.skipRendering(blockState, blockState1, direction);
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 0.85F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public void onPlace(BlockState blockState, Level world, BlockPos blockPos, BlockState previousBlockState, boolean notify) {
        super.onPlace(blockState, world, blockPos, previousBlockState, notify);
        setNeighboringSugarWater(world, blockPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos blockPos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborChanged(state, world, blockPos, block, fromPos, notify);
        setNeighboringSugarWater(world, blockPos);
    }

    private void setNeighboringSugarWater(Level world, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            BlockPos sidePos = blockPos.relative(direction);
            FluidState fluidState = world.getFluidState(sidePos);
            if(fluidState.is(BzTags.CONVERTIBLE_TO_SUGAR_WATER) &&
                    fluidState.isSource() &&
                    world.getBlockState(sidePos).getCollisionShape(world, sidePos).isEmpty())
            {
                world.setBlock(blockPos.relative(direction), BzFluids.SUGAR_WATER_BLOCK.defaultBlockState(), 3);
            }
        }
    }
}
