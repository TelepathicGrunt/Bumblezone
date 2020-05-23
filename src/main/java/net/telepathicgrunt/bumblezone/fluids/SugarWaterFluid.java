package net.telepathicgrunt.bumblezone.fluids;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;


public abstract class SugarWaterFluid extends ForgeFlowingFluid
{
	protected SugarWaterFluid(Properties properties)
	{
		super(properties);
	}

	@Override
	public void randomTick(World world, BlockPos position, IFluidState state, Random random)
	{
		//only attempts to grow sugar cane 50% of the time.
		if (random.nextBoolean() || !world.isAreaLoaded(position, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light

		//check one of the spot next to sugar water for sugar cane to grow
		BlockPos.Mutable blockPos = new BlockPos.Mutable(position.up());
		blockPos.move(Direction.byHorizontalIndex(random.nextInt(4)));
		BlockState blockstate = world.getBlockState(blockPos);
		
		if (blockstate.getBlock() == Blocks.SUGAR_CANE)
		{
			int height = 1;
			blockstate = world.getBlockState(blockPos.move(Direction.UP));

			//find top of sugar cane or 
			while (blockstate.getBlock() == Blocks.SUGAR_CANE && height < 5)
			{
				blockstate = world.getBlockState(blockPos.move(Direction.UP));
				height++;
			}

			//at top of sugar cane. Time to see if it can grow more
			if (height < 5 && blockstate.getMaterial() == Material.AIR)
			{
				world.setBlockState(blockPos, Blocks.SUGAR_CANE.getDefaultState(), 3);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(World worldIn, BlockPos pos, IFluidState state, Random random)
	{
		if (!state.isSource() && !state.get(FALLING))
		{
			if (random.nextInt(64) == 0)
			{
				worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
			}
		}
		else if (random.nextInt(10) == 0)
		{
			worldIn.addParticle(ParticleTypes.UNDERWATER, (double) pos.getX() + (double) random.nextFloat(), (double) pos.getY() + (double) random.nextFloat(), (double) pos.getZ() + (double) random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Nullable
	@OnlyIn(Dist.CLIENT)
	@Override
	public IParticleData getDripParticleData()
	{
		return ParticleTypes.DRIPPING_WATER;
	}

	@Override
	protected boolean ticksRandomly()
	{
		return true;
	}

	@Override
	public int getTickRate(IWorldReader p_205569_1_)
	{
		return 5;
	}

	@Override
	public boolean isEquivalentTo(Fluid fluidIn)
	{
		return fluidIn == BzBlocks.SUGAR_WATER_FLUID.get() || fluidIn == BzBlocks.SUGAR_WATER_FLUID_FLOWING.get();
	}
	
	

	public static class Flowing extends SugarWaterFluid
	{
		public Flowing(Properties properties)
		{
			super(properties);
			setDefaultState(getStateContainer().getBaseState().with(LEVEL_1_8, 7));
		}


		@Override
		protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(LEVEL_1_8);
		}


		@Override
		public int getLevel(IFluidState state)
		{
			return state.get(LEVEL_1_8);
		}


		@Override
		public boolean isSource(IFluidState state)
		{
			return false;
		}
	}

	public static class Source extends SugarWaterFluid
	{

		public Source(Properties properties)
		{
			super(properties);
		}


		@Override
		public int getLevel(IFluidState state)
		{
			return 8;
		}


		@Override
		public boolean isSource(IFluidState state)
		{
			return true;
		}
	}
}
