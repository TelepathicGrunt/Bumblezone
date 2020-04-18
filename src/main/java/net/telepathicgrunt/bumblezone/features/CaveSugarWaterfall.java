package net.telepathicgrunt.bumblezone.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;


public class CaveSugarWaterfall extends Feature<NoFeatureConfig>
{

	public CaveSugarWaterfall(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}

	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> changedBlock, Random rand, BlockPos position, NoFeatureConfig config)
	{
		//creates a waterfall 
		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable(position);
		BlockState blockstate = world.getBlockState(blockpos$Mutable.up());

		if (!blockstate.isSolid())
		{
			return false;
		}
		else
		{
			//checks if we are in the side of a wall with air exposed on one side

			int numberOfSolidSides = 0;
			int neededNumberOfSides = 0;
			blockstate = world.getBlockState(blockpos$Mutable.down());

			if (blockstate.isSolid())
			{
				neededNumberOfSides = 3;
			}
			else if(blockstate.getBlock() == CAVE_AIR.getBlock())
			{
				neededNumberOfSides = 4;
			}
			else {
				return false;
			}


			for (Direction face : Direction.Plane.HORIZONTAL)
			{
				blockstate = world.getBlockState(blockpos$Mutable.offset(face));
				if (blockstate.isSolid())
				{
					++numberOfSolidSides;
				}
				else if(blockstate.getBlock() != CAVE_AIR.getBlock())
				{
					return false;
				}
			}

			//position valid. begin making waterfall
			if (numberOfSolidSides == neededNumberOfSides)
			{
				world.setBlockState(blockpos$Mutable, BzBlocks.SUGAR_WATER_BLOCK.get().getDefaultState(), 2);
				world.getPendingFluidTicks().scheduleTick(blockpos$Mutable, BzBlocks.SUGAR_WATER_BLOCK.get().getFluid(), 0);
			}
			return true;
		}
	}

}