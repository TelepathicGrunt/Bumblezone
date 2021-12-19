package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LEVEL_FLOWING;


public abstract class SugarWaterFluid extends ForgeFlowingFluid  {

    protected SugarWaterFluid(Properties properties) {
        super(properties);
    }

    @Override
    public Fluid getFlowing() {
        return BzFluids.SUGAR_WATER_FLUID_FLOWING.get();
    }

    @Override
    public Fluid getSource() {
        return BzFluids.SUGAR_WATER_FLUID.get();
    }

    @Override
    public Item getBucket() {
        return BzItems.SUGAR_WATER_BUCKET.get();
    }

    @Override
    public void randomTick(Level world, BlockPos position, FluidState state, Random random) {
        //only attempts to grow sugar cane 50% of the time.
        if (random.nextBoolean() || !world.hasChunksAt(position, position))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light

        //check one of the spot next to sugar water for sugar cane to grow
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos().set(position.above());
        blockPos.move(Direction.from2DDataValue(random.nextInt(4)));
        BlockState blockstate = world.getBlockState(blockPos);

        if (blockstate.getBlock() == Blocks.SUGAR_CANE) {
            int height = 1;
            blockstate = world.getBlockState(blockPos.move(Direction.UP));

            //find top of sugar cane or
            while (blockstate.getBlock() == Blocks.SUGAR_CANE && height < 5) {
                blockstate = world.getBlockState(blockPos.move(Direction.UP));
                height++;
            }

            //at top of sugar cane. Time to see if it can grow more
            if (height < 5 && blockstate.getMaterial() == Material.AIR) {
                world.setBlock(blockPos, Blocks.SUGAR_CANE.defaultBlockState(), 3);
            }
        }
    }


    @Override
    public void animateTick(Level worldIn, BlockPos pos, FluidState state, Random random) {
        if (!state.isSource() && !state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                worldIn.playLocalSound(
                        (double) pos.getX() + 0.5D,
                        (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D,
                        SoundEvents.WATER_AMBIENT,
                        SoundSource.BLOCKS,
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
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }


    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }


    @Override
    public int getTickDelay(LevelReader world) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
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

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid.is(FluidTags.WATER);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    public static class Flowing extends SugarWaterFluid {
        public Flowing(Properties properties) {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL_FLOWING);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL_FLOWING);
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

    public static class Source extends SugarWaterFluid {

        public Source(ForgeFlowingFluid.Properties properties) {
            super(properties);
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
