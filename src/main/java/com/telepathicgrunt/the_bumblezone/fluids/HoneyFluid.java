package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.mixin.FlowingFluidAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Random;

public abstract class HoneyFluid extends ForgeFlowingFluid {

    public static final IntegerProperty BOTTOM_LEVEL = HoneyFluidBlock.BOTTOM_LEVEL;

    protected HoneyFluid(Properties properties) {
        super(properties);
    }

    @Override
    public Fluid getFlowing() {
        return BzFluids.HONEY_FLUID_FLOWING.get();
    }

    @Override
    public Fluid getSource() {
        return BzFluids.HONEY_FLUID.get();
    }

    @Override
    public Item getBucket() {
        return BzItems.HONEY_BUCKET.get();
    }

    @Override
    public void animateTick(World worldIn, BlockPos pos, FluidState state, Random random) {
        if (random.nextInt(82) == 0) {
            worldIn.addParticle(BzParticles.HONEY_PARTICLE.get(),
                    pos.getX() + random.nextFloat(),
                    pos.getY() + random.nextFloat(),
                    pos.getZ() + random.nextFloat(),
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public IParticleData getDripParticle() {
        return BzParticles.HONEY_PARTICLE.get();
    }

    @Override
    protected float getExplosionResistance() {
        return 120.0F;
    }

    @Override
    protected void beforeDestroyingBlock(IWorld world, BlockPos pos, BlockState state) {
        TileEntity blockEntity = state.getBlock().isEntityBlock() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    @Override
    public int getSlopeFindDistance(IWorldReader world) {
        return 4;
    }

    @Override
    public int getDropOff(IWorldReader world) {
        return 1;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid.is(BzFluidTags.HONEY_FLUID);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, IBlockReader world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        int level = state.isSource() ? 0 : state.getAmount();
        int bottomLevel = state.isSource() ? 0 : state.getValue(BOTTOM_LEVEL);
        return BzFluids.HONEY_FLUID_BLOCK.get().defaultBlockState().setValue(FlowingFluidBlock.LEVEL, level).setValue(BOTTOM_LEVEL, bottomLevel);
    }

    @Override
    public void tick(World world, BlockPos blockPos, FluidState fluidState) {
        if (!fluidState.isSource()) {
            FluidState fluidstate = this.getNewLiquid(world, blockPos, world.getBlockState(blockPos));
            int spreadDelay = this.getSpreadDelay(world, blockPos, fluidState, fluidstate);
            if (fluidstate.isEmpty()) {
                fluidState = fluidstate;
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            } else if (!fluidstate.equals(fluidState)) {
                fluidState = fluidstate;
                BlockState blockstate = fluidstate.createLegacyBlock();
                world.setBlock(blockPos, blockstate, 2);
                world.getLiquidTicks().scheduleTick(blockPos, fluidstate.getType(), spreadDelay);
                world.updateNeighborsAt(blockPos, blockstate.getBlock());
            }
        }

        this.spread(world, blockPos, fluidState);
    }

    @Override
    protected void spread(IWorld world, BlockPos blockPos, FluidState fluidState) {
        if (!fluidState.isEmpty()) {
            int bottomFluidLevel = fluidState.getValue(BOTTOM_LEVEL);
            if(bottomFluidLevel == 0) {
                BlockState blockState = world.getBlockState(blockPos);
                BlockPos belowBlockPos = blockPos.below();
                BlockState belowBlockState = world.getBlockState(belowBlockPos);
                FluidState belowFluidState = this.getNewLiquid(world, belowBlockPos, belowBlockState);
                if (!belowBlockState.getFluidState().is(BzFluidTags.HONEY_FLUID) && this.canSpreadTo(world, blockPos, blockState, Direction.DOWN, belowBlockPos, belowBlockState, world.getFluidState(belowBlockPos), belowFluidState.getType())) {
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

    protected void spreadDown(IWorld world, BlockPos blockPos, BlockState blockState, Direction direction, FluidState fluidState) {
        if (!blockState.isAir()) {
            this.beforeDestroyingBlock(world, blockPos, blockState);
        }
        world.setBlock(blockPos, fluidState.createLegacyBlock(), 3);
    }

    @Override
    protected FluidState getNewLiquid(IWorldReader worldReader, BlockPos blockPos, BlockState blockState) {
        int lowestNeighboringFluidLevel = blockState.getBlock() instanceof HoneyFluidBlock ? blockState.getValue(BOTTOM_LEVEL) : 8;
        int highestNeighboringFluidLevel = blockState.getBlock() instanceof HoneyFluidBlock ? blockState.getValue(HoneyFluidBlock.LEVEL): 0;
        int neighboringFluidSource = 0;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos sideBlockPos = blockPos.relative(direction);
            BlockState sideBlockState = worldReader.getBlockState(sideBlockPos);
            FluidState sideFluidState = sideBlockState.getFluidState();
            if (sideFluidState.getType().isSame(this) && ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(direction, worldReader, blockPos, blockState, sideBlockPos, sideBlockState)) {
                if (sideFluidState.isSource() && net.minecraftforge.event.ForgeEventFactory.canCreateFluidSource(worldReader, sideBlockPos, sideBlockState, this.canConvertToSource())) {
                    ++neighboringFluidSource;
                }

                highestNeighboringFluidLevel = Math.max(highestNeighboringFluidLevel, sideFluidState.getAmount());
                if(sideFluidState.getType() instanceof HoneyFluid){
                    lowestNeighboringFluidLevel = Math.min(lowestNeighboringFluidLevel, sideFluidState.getValue(BOTTOM_LEVEL));
                }
            }
        }

        if (neighboringFluidSource >= 2) {
            BlockState belowBlockPos = worldReader.getBlockState(blockPos.below());
            FluidState belowFluidState = belowBlockPos.getFluidState();
            if (belowBlockPos.getMaterial().isSolid() || (belowFluidState.getType().isSame(this) && belowFluidState.isSource())) {
                return this.getSource(false);
            }
        }

        BlockPos aboveBlockPos = blockPos.above();
        BlockState aboveBlockState = worldReader.getBlockState(aboveBlockPos);
        FluidState aboveFluidState = aboveBlockState.getFluidState();
        int newBottomFluidLevel = Math.max(lowestNeighboringFluidLevel - 1, 0);
        if (!aboveFluidState.isEmpty() && aboveFluidState.getType().isSame(this) && ((FlowingFluidAccessor)this).thebumblezone_callCanPassThroughWall(Direction.UP, worldReader, blockPos, blockState, aboveBlockPos, aboveBlockState)) {
            int newFluidLevel = 8;
            if(aboveFluidState.getValue(BOTTOM_LEVEL) != 0) {
                newFluidLevel = highestNeighboringFluidLevel - this.getDropOff(worldReader);
            }
            return this.getFlowing(newFluidLevel, true).setValue(BOTTOM_LEVEL, newBottomFluidLevel);
        } else {
            int newFluidLevel = highestNeighboringFluidLevel - this.getDropOff(worldReader);
            return newFluidLevel <= 0 ? Fluids.EMPTY.defaultFluidState() : this.getFlowing(newFluidLevel, false).setValue(BOTTOM_LEVEL, newBottomFluidLevel);
        }
    }

    public static class Flowing extends HoneyFluid {
        public Flowing(Properties properties) {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7).setValue(BOTTOM_LEVEL, 0));
        }

        protected void createFluidStateDefinition(StateContainer.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
            builder.add(BOTTOM_LEVEL);
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

    public static class Source extends HoneyFluid {

        public Source(Properties properties) {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7).setValue(BOTTOM_LEVEL, 0));
        }

        protected void createFluidStateDefinition(StateContainer.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
            builder.add(BOTTOM_LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return state.getValue(BOTTOM_LEVEL) == 0;
        }

        @Override
        protected boolean canConvertToSource() {
            return false;
        }
    }
}
