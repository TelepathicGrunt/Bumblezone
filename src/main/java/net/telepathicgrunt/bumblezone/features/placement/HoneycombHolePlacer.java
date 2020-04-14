package net.telepathicgrunt.bumblezone.features.placement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class HoneycombHolePlacer extends Placement<NoPlacementConfig>
{
	private enum SliceState { NEITHER, AIR, SOLID};
	
	public HoneycombHolePlacer(Function<Dynamic<?>, ? extends NoPlacementConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGenerator, Random random, NoPlacementConfig placementConfig, BlockPos pos)
	{
		//Start at top
		BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(pos.getX()-4, 236, pos.getZ()+4);
		List<BlockPos> blockPosList = new ArrayList<BlockPos>();
		boolean alternate = false;
		
		//Repeats twice with an offset on second pass
		for(int repeat = 0; repeat < 2; repeat++)
		{
			//Makes 23 holes from y = 236 to y = 52
			for(int count = 0; count < 23; count++) 
			{
				//Moves back and forth in z coordinate so the holes alternate to make this layer of holes
				if(alternate) 
				{
					mutableBlockPos.move(0, -8, -8);
				}
				else
				{
					mutableBlockPos.move(0, -8, 8);
				}
				
				alternate = !alternate;
				
				//Makes sure the place for holes is valid
				if(isPlaceValid(world, mutableBlockPos))
				{
					blockPosList.add(mutableBlockPos.toImmutable());
				}
			} 
			
			//set it back to the top but with an offset for the second layer of holes 
			mutableBlockPos.setPos(pos.getX()+4, 236, pos.getZ()+12);
		}
		
		return blockPosList.stream();
	}
	
	/**
	 * Checks the entire body length of where the hole would go to make sure that any 
	 * circular slice within it is entirely solid (so we don't spawn holes in mid-air or 
	 * have the ends only be made in land. A good chunk of the hole's body must be made).
	 * 
	 * It also needs one slide to be invalid so that the hole is not 
	 * placed inside the terrain and cutoff from the outside.
	 */
	private boolean isPlaceValid(IWorld world, BlockPos pos) 
	{
		boolean completelySolidSlice = false;
		boolean airInSlice = false;
		
		for(int x = -3; x <= 3; x++)
		{
			SliceState state = StateOfThisSlice(world, pos.west(x));
			if(state == SliceState.SOLID)
			{
				completelySolidSlice = true;
			}
			else if(state == SliceState.AIR)
			{
				airInSlice = true;
			}
		}
		
		return completelySolidSlice && airInSlice;
	}
	
	/**
	 * Checks if the circular slice here is entirely solid land.
	 */
	private SliceState StateOfThisSlice(IWorld world, BlockPos pos) 
	{
		BlockState blockState;
		double distanceSq = 0;
		
		for(double z = -4.5; z <= 4.5; z++)
		{
			for(double y = -3.5; y <= 3.5; y++)
			{
				distanceSq = z * z + y * y;
				if(distanceSq > 5 && distanceSq < 18)
				{
					blockState = world.getBlockState(pos.add(0, y+1, z));
					if(!blockState.isSolid())
					{
						//only count Air and not Cave Air so holes aren't placed in caves
						if(blockState.getBlock() == Blocks.AIR)
							return SliceState.AIR;
						else
							return SliceState.NEITHER;
					}
					
					//Visual debugging
					//world.setBlockState(pos.add(0, y+1, z), Blocks.REDSTONE_BLOCK.getDefaultState(), 2);
				}
			}
		}
		
		return SliceState.SOLID;
	}
}
