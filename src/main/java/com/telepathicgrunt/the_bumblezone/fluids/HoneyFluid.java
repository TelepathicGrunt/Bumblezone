package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.lwjgl.system.CallbackI;

import java.util.Random;

public abstract class HoneyFluid extends ForgeFlowingFluid {

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
        if (!state.isSource() && !state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                worldIn.playLocalSound(
                        (double) pos.getX() + 0.5D,
                        (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D,
                        SoundEvents.WATER_AMBIENT,
                        SoundCategory.BLOCKS,
                        random.nextFloat() * 0.25F + 0.75F,
                        random.nextFloat() + 0.5F, false);
            }
        } else if (random.nextInt(10) == 0) {
            worldIn.addParticle(ParticleTypes.UNDERWATER,
                    (double) pos.getX() + (double) random.nextFloat(),
                    (double) pos.getY() + (double) random.nextFloat(),
                    (double) pos.getZ() + (double) random.nextFloat(),
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public IParticleData getDripParticle() {
        return ParticleTypes.DRIPPING_HONEY;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
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
        return fluid.is(FluidTags.WATER);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, IBlockReader world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return BzFluids.HONEY_FLUID_BLOCK.get().defaultBlockState().setValue(FlowingFluidBlock.LEVEL, getLegacyLevel(state));
    }

    public static class Flowing extends HoneyFluid {
        public Flowing(Properties properties) {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateContainer.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
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
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateContainer.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
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
