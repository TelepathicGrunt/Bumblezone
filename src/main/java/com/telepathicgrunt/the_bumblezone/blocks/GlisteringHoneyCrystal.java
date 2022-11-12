package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class GlisteringHoneyCrystal extends ProperFacingBlock {
    public GlisteringHoneyCrystal() {
        super(BlockBehaviour.Properties.of(Material.GLASS, MaterialColor.TERRACOTTA_YELLOW).lightLevel((blockState) -> 11).strength(0.4F, 0.4f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add().add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
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
    public boolean shouldDisplayFluidOverlay(BlockState blockState, BlockAndTintGetter level, BlockPos blockPos, FluidState fluidState) {
        return blockState.is(this);
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
                world.setBlock(blockPos.relative(direction), BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState(), 3);
            }
        }
    }


    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos position, RandomSource random) {
        if (random.nextFloat() < 0.09f) {
            this.spawnSparkleParticles(world, position, random);
        }
    }

    private void spawnSparkleParticles(Level world, BlockPos position, RandomSource random) {
        int chosenFace = random.nextInt(3);
        int min = -1;
        int max = 16;
        double x = random.nextDouble() + (random.nextBoolean() ? min : max) * (chosenFace != 0 ? random.nextDouble() : 1);
        double y = random.nextDouble() + (random.nextBoolean() ? min : max) * (chosenFace != 1 ? random.nextDouble() : 1);
        double z = random.nextDouble() + (random.nextBoolean() ? min : max) * (chosenFace != 2 ? random.nextDouble() : 1);

        world.addParticle(BzParticles.SPARKLE_PARTICLE.get(),
                (x / 16) + position.getX(),
                (y / 16) + position.getY(),
                (z / 16) + position.getZ(),
                0.0D,
                0.0D,
                0.0D);
    }
}
