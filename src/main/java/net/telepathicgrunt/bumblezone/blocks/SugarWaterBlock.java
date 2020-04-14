package net.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;


public class SugarWaterBlock extends FlowingFluidBlock
{

	@SuppressWarnings("deprecation")
	public SugarWaterBlock(FlowingFluid fluid, Block.Properties properties)
	{
		super(fluid, properties);
	}


	public SugarWaterBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties properties)
	{
		super(supplier, properties);
	}


	@Override
	public boolean reactWithNeighbors(World world, BlockPos pos, BlockState state)
	{
		boolean flag = false;

		for (Direction direction : Direction.values())
		{
			if (direction != Direction.DOWN && world.getFluidState(pos.offset(direction)).isTagged(FluidTags.LAVA))
			{
				flag = true;
				break;
			}
		}

		if (flag)
		{
			IFluidState ifluidstate = world.getFluidState(pos);
			if (ifluidstate.isSource())
			{
				world.setBlockState(pos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos, pos, BzBlocks.SUGAR_INFUSED_STONE.get().getDefaultState()));
				this.triggerMixEffects(world, pos);
				return false;
			}

			if (ifluidstate.getActualHeight(world, pos) >= 0.44444445F)
			{
				world.setBlockState(pos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos, pos, BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().getDefaultState()));
				this.triggerMixEffects(world, pos);
				return false;
			}
		}

		return true;
	}


	private void triggerMixEffects(IWorld world, BlockPos pos)
	{
		world.playEvent(1501, pos, 0);
	}


}
