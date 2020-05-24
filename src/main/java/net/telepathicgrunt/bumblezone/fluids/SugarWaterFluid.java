package net.telepathicgrunt.bumblezone.fluids;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.items.BzItems;

import java.util.Random;

import static net.minecraft.state.property.Properties.LEVEL_1_8;


public abstract class SugarWaterFluid extends BaseFluid {
    public Fluid getFlowing() {
        return BzBlocks.SUGAR_WATER_FLUID_FLOWING;
    }

    public Fluid getStill() {
        return BzBlocks.SUGAR_WATER_FLUID;
    }

    public Item getBucketItem() {
        return BzItems.SUGAR_WATER_BUCKET;
    }

    @Override
    public void onRandomTick(World world, BlockPos position, FluidState state, Random random) {
        //only attempts to grow sugar cane 50% of the time.
        if (random.nextBoolean() || !world.isRegionLoaded(position, position))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light

        //check one of the spot next to sugar water for sugar cane to grow
        BlockPos.Mutable blockPos = new BlockPos.Mutable().set(position.up());
        blockPos.setOffset(Direction.fromHorizontal(random.nextInt(4)));
        BlockState blockstate = world.getBlockState(blockPos);

        if (blockstate.getBlock() == Blocks.SUGAR_CANE) {
            int height = 1;
            blockstate = world.getBlockState(blockPos.setOffset(Direction.UP));

            //find top of sugar cane or
            while (blockstate.getBlock() == Blocks.SUGAR_CANE && height < 5) {
                blockstate = world.getBlockState(blockPos.setOffset(Direction.UP));
                height++;
            }

            //at top of sugar cane. Time to see if it can grow more
            if (height < 5 && blockstate.getMaterial() == Material.AIR) {
                world.setBlockState(blockPos, Blocks.SUGAR_CANE.getDefaultState(), 3);
            }
        }
    }


    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, FluidState state, Random random) {
        if (!state.isStill() && !state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                worldIn.playSound(
                        (double) pos.getX() + 0.5D,
                        (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D,
                        SoundEvents.BLOCK_WATER_AMBIENT,
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
    public ParticleEffect getParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }


    @Override
    protected boolean hasRandomTicks() {
        return true;
    }


    @Override
    public int getTickRate(WorldView world) {
        return 5;
    }

    @Override
    protected float getBlastResistance() {
        return 100.0F;
    }

    @Override
    protected void beforeBreakingBlock(IWorld world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.getBlock().hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world.getWorld(), pos, blockEntity);
    }

    @Override
    public int method_15733(WorldView world) {
        return 4;
    }

    @Override
    public int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }


    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid.isIn(FluidTags.WATER);
    }

    public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
    }

    public BlockState toBlockState(FluidState state) {
        return BzBlocks.SUGAR_WATER_BLOCK.getDefaultState().with(FluidBlock.LEVEL, method_15741(state));
    }

    public static class Flowing extends SugarWaterFluid {
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL_1_8);
        }


        public int getLevel(FluidState state) {
            return state.get(LEVEL_1_8);
        }


        public boolean isStill(FluidState state) {
            return false;
        }

        protected boolean isInfinite() {
            return true;
        }
    }

    public static class Source extends SugarWaterFluid {

        public int getLevel(FluidState state) {
            return 8;
        }


        public boolean isStill(FluidState state) {
            return true;
        }


        protected boolean isInfinite() {
            return false;
        }
    }
}
