package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.mixin.blocks.FlowingFluidAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import static com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock.ABOVE_FLUID;
import static com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock.BOTTOM_LEVEL;

public abstract class RoyalJellyFluid extends FlowingFluid {

    protected RoyalJellyFluid() { }

    @Override
    public Fluid getFlowing() {
        return BzFluids.ROYAL_JELLY_FLUID_FLOWING;
    }

    @Override
    public Fluid getSource() {
        return BzFluids.ROYAL_JELLY_FLUID;
    }

    @Override
    public Item getBucket() {
        return BzItems.ROYAL_JELLY_BUCKET;
    }

    @Override
    public void animateTick(Level worldIn, BlockPos pos, FluidState state, RandomSource random) {
        if (random.nextInt(82) == 0) {
            worldIn.addParticle(BzParticles.ROYAL_JELLY_PARTICLE,
                    pos.getX() + random.nextFloat(),
                    pos.getY() + random.nextFloat(),
                    pos.getZ() + random.nextFloat(),
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public ParticleOptions getDripParticle() {
        return BzParticles.ROYAL_JELLY_PARTICLE;
    }

    @Override
    protected float getExplosionResistance() {
        return 120.0F;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    @Override
    public int getSlopeFindDistance(LevelReader world) {
        return 4;
    }

    @Override
    public int getDropOff(LevelReader world) {
        return 1;
    }

    public int getTickDelay(LevelReader world) {
        return 30;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid.is(BzTags.VISUAL_HONEY_FLUID);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return BzFluids.ROYAL_JELLY_FLUID_BLOCK.defaultBlockState()
                .setValue(LiquidBlock.LEVEL, state.isSource() ? 0 : state.getAmount())
                .setValue(BOTTOM_LEVEL, state.isSource() ? 0 : state.getValue(BOTTOM_LEVEL))
                .setValue(FALLING, !state.isSource() && state.getValue(FALLING))
                .setValue(ABOVE_FLUID, state.getValue(ABOVE_FLUID));
    }

    @Override
    public void tick(Level world, BlockPos blockPos, FluidState fluidState) {
        boolean justFilledBottom = false;
        // removes self if not source and is not fed.
        // otherwise, schedule fluid tick and update flow.
        if (!fluidState.isSource()) {
            FluidState newFluidState = this.getNewLiquid(world, blockPos, world.getBlockState(blockPos));
            int spreadDelay = this.getSpreadDelay(world, blockPos, fluidState, newFluidState);
            if (newFluidState.isEmpty()) {
                fluidState = newFluidState;
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            }
            else if (!newFluidState.equals(fluidState)) {
                if(fluidState.getValue(BOTTOM_LEVEL) != 0 && (newFluidState.isSource() || newFluidState.getValue(BOTTOM_LEVEL) == 0))
                    justFilledBottom = true;

                fluidState = newFluidState;
                BlockState blockstate = newFluidState.createLegacyBlock();
                world.setBlock(blockPos, blockstate, 2);
                world.scheduleTick(blockPos, newFluidState.getType(), spreadDelay);
                world.updateNeighborsAt(blockPos, blockstate.getBlock());
            }
        }

        // For spreading downward and to the side.
        // Is basically the spread method but with justFilledBottom boolean
        // used so new fluid is not made in same tick as when fluid just reached bottom layer = 0.
        if (!fluidState.isEmpty()) {
            int bottomFluidLevel = fluidState.isSource() ? 0 : fluidState.getValue(BOTTOM_LEVEL);
            if(bottomFluidLevel == 0) {
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos belowBlockPos = blockPos.below();
                BlockState belowBlockState = world.getBlockState(belowBlockPos);
                FluidState belowFluidState = this.getNewLiquid(world, belowBlockPos, belowBlockState);
                if (!belowBlockState.getFluidState().is(BzTags.ROYAL_JELLY_FLUID) &&
                    this.canSpreadTo(world, blockPos, blockState, Direction.DOWN, belowBlockPos, belowBlockState, world.getFluidState(belowBlockPos), belowFluidState.getType())) {

                    if(!justFilledBottom) {
                        this.spreadDown(world, belowBlockPos, belowBlockState, Direction.DOWN, belowFluidState);
                        if (((FlowingFluidAccessor)this).thebumblezone_callSourceNeighborCount(world, blockPos) >= 3) {
                            ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                        }
                    }
                }
                else if (fluidState.isSource() || !belowBlockState.getFluidState().getType().isSame(this)) {
                    ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                }
            }
        }
    }

    @Override
    protected void spread(LevelAccessor world, BlockPos blockPos, FluidState fluidState) {
        if (!fluidState.isEmpty()) {
            int bottomFluidLevel = fluidState.getValue(BOTTOM_LEVEL);
            if(bottomFluidLevel == 0) {
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos belowBlockPos = blockPos.below();
                BlockState belowBlockState = world.getBlockState(belowBlockPos);
                FluidState belowFluidState = this.getNewLiquid(world, belowBlockPos, belowBlockState);
                if (!belowBlockState.getFluidState().is(BzTags.ROYAL_JELLY_FLUID) && this.canSpreadTo(world, blockPos, blockState, Direction.DOWN, belowBlockPos, belowBlockState, world.getFluidState(belowBlockPos), belowFluidState.getType())) {
                    this.spreadDown(world, belowBlockPos, belowBlockState, Direction.DOWN, belowFluidState);
                    if (((FlowingFluidAccessor)this).thebumblezone_callSourceNeighborCount(world, blockPos) >= 3) {
                        ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                    }
                }
                else if (fluidState.isSource() || !belowBlockState.getFluidState().getType().isSame(this)) {
                    ((FlowingFluidAccessor)this).thebumblezone_callSpreadToSides(world, blockPos, fluidState, blockState);
                }
            }
        }
    }

    protected void spreadDown(LevelAccessor world, BlockPos blockPos, BlockState blockState, Direction direction, FluidState fluidState) {
        if (!blockState.isAir()) {
            this.beforeDestroyingBlock(world, blockPos, blockState);
        }
        world.setBlock(blockPos, fluidState.createLegacyBlock(), 3);
    }

    @Override
    protected FluidState getNewLiquid(LevelReader worldReader, BlockPos blockPos, BlockState blockState) {
        boolean isBzFluidBlock = blockState.hasProperty(BOTTOM_LEVEL) && blockState.hasProperty(LiquidBlock.LEVEL);
        int lowestNeighboringFluidLevel = isBzFluidBlock ? blockState.getValue(BOTTOM_LEVEL) : HoneyFluidBlock.maxBottomLayer;
        int currentFluidLevel = isBzFluidBlock ? blockState.getValue(LiquidBlock.LEVEL) : 0;
        int highestNeighboringFluidLevel = currentFluidLevel;
        int neighboringFluidSource = 0;
        boolean hasAboveFluid = isBzFluidBlock ? blockState.getValue(ABOVE_FLUID) : false;

        BlockPos aboveBlockPos = blockPos.above();
        BlockState aboveBlockState = worldReader.getBlockState(aboveBlockPos);
        BlockState belowBlockState = worldReader.getBlockState(blockPos.below());
        boolean canPassThroughBelow = ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(Direction.DOWN, worldReader, blockPos, blockState, blockPos.below(), belowBlockState);

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos sideBlockPos = blockPos.relative(direction);
            BlockState sideBlockState = worldReader.getBlockState(sideBlockPos);
            FluidState sideFluidState = sideBlockState.getFluidState();
            if (sideFluidState.getType().isSame(this) && ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(direction, worldReader, blockPos, blockState, sideBlockPos, sideBlockState)) {
                if (sideFluidState.isSource()) {
                    ++neighboringFluidSource;
                }

                highestNeighboringFluidLevel = Math.max(highestNeighboringFluidLevel, sideFluidState.getAmount());
                if(sideFluidState.is(BzTags.BOTTOM_LAYER_FLUIDS) && !(canPassThroughBelow && !sideFluidState.isSource() && sideBlockState.getValue(FALLING) && aboveBlockState.getFluidState().is(BzTags.BZ_HONEY_FLUID))) {
                    lowestNeighboringFluidLevel = Math.min(lowestNeighboringFluidLevel, sideFluidState.isSource() ? 0 : sideFluidState.getValue(BOTTOM_LEVEL));
                }
            }
        }

        FluidState aboveFluidState = aboveBlockState.getFluidState();
        boolean aboveFluidIsThisFluid = !aboveFluidState.isEmpty() && aboveFluidState.getType().isSame(this);
        int newBottomFluidLevel = Math.max(lowestNeighboringFluidLevel - 1, 0);
        boolean isFalling = true;
        int newFluidLevel = 8;
        int dropOffValue = this.getDropOff(worldReader);
        if(hasAboveFluid && !aboveFluidIsThisFluid) {
            dropOffValue = 0;
        }

        if (aboveFluidIsThisFluid && ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(Direction.UP, worldReader, blockPos, blockState, aboveBlockPos, aboveBlockState)) {
            if(!aboveFluidState.isSource() && aboveFluidState.is(BzTags.BOTTOM_LAYER_FLUIDS) && aboveFluidState.getValue(BOTTOM_LEVEL) != 0) {
                newFluidLevel = highestNeighboringFluidLevel - dropOffValue;
            }
        }
        else {
            isFalling = aboveFluidState.isEmpty() && neighboringFluidSource == 0 && highestNeighboringFluidLevel <= currentFluidLevel && canPassThroughBelow;
            newFluidLevel = highestNeighboringFluidLevel - dropOffValue;
        }

        return newFluidLevel <= 0 ?
                Fluids.EMPTY.defaultFluidState() :
                this.getFlowing(newFluidLevel, isFalling)
                        .setValue(BOTTOM_LEVEL, newBottomFluidLevel)
                        .setValue(ABOVE_FLUID, aboveFluidIsThisFluid && (aboveFluidState.isSource() || aboveFluidState.getValue(BOTTOM_LEVEL) == 0));
    }

    @Override
    public float getHeight(FluidState fluidState, BlockGetter world, BlockPos blockPos) {
        BlockPos aboveBlockPos = blockPos.above();
        BlockState aboveBlockState = world.getBlockState(aboveBlockPos);
        FluidState aboveFluidState = aboveBlockState.getFluidState();
        boolean aboveFluidIsThisFluid =
                    !aboveFluidState.isEmpty() &&
                    aboveFluidState.getType().isSame(this) &&
                    (aboveFluidState.isSource() || !aboveFluidState.is(BzTags.BOTTOM_LAYER_FLUIDS) || aboveFluidState.getValue(BOTTOM_LEVEL) == 0);

        return fluidState.getValue(ABOVE_FLUID) || aboveFluidIsThisFluid ? 1.0f : fluidState.getOwnHeight();
    }

    public static class Flowing extends RoyalJellyFluid {
        public Flowing() {
            registerDefaultState(getStateDefinition().any()
                    .setValue(LEVEL, 8)
                    .setValue(BOTTOM_LEVEL, 0)
                    .setValue(ABOVE_FLUID, false)
            );
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
            builder.add(BOTTOM_LEVEL);
            builder.add(ABOVE_FLUID);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        protected boolean canConvertToSource() {
            return true;
        }
    }

    public static class Source extends RoyalJellyFluid {

        public Source() {
            registerDefaultState(getStateDefinition().any().setValue(ABOVE_FLUID, false));
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(ABOVE_FLUID);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        protected boolean canConvertToSource() {
            return false;
        }
    }
}
