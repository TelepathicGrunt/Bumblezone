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
import net.telepathicgrunt.bumblezone.blocks.BzBlocksInit;


public class HoneycombHole extends Feature<NoFeatureConfig>
{
	public HoneycombHole(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}


	private int[][] bodyLayout = 
		{
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 1, 3, 3, 3, 3, 1, 0, 0, 0},
		 {0, 0, 1, 3, 3, 3, 3, 3, 3, 1, 0, 0},
		 {0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0},
		 {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1},
		 {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1},
		 {0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0},
		 {0, 0, 1, 3, 3, 3, 3, 3, 3, 1, 0, 0},
		 {0, 0, 0, 1, 3, 3, 3, 3, 1, 0, 0, 0},
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0}
		};
	
	private int[][] largeHoneyLayout = 
		{
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
		 {0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 0, 0},
		 {0, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1, 0},
		 {1, 1, 2, 2, 3, 3, 3, 3, 2, 2, 1, 1},
		 {1, 1, 2, 2, 3, 3, 3, 3, 2, 2, 1, 1},
		 {0, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1, 0},
		 {0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 0, 0},
		 {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0}
		};
	
	private int[][] smallHoneyLayout = 
		{
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
		 {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
		 {0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 0, 0},
		 {0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 0, 0},
		 {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
		 {0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
	
	private int[][] endCapLayout = 
		{
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};

	private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocksInit.FILLED_POROUS_HONEYCOMB.get().getDefaultState();
	private static final BlockState HONEY_BLOCK = Blocks.field_226907_mc_.getDefaultState();
	private static final BlockState HONEYCOMB_BLOCK = Blocks.field_226908_md_.getDefaultState();
	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
	

	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> changedBlock, Random rand, BlockPos position, NoFeatureConfig config)
	{
		
		BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(position);
		
		generateSlice(world, mutableBlockPos, endCapLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), smallHoneyLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), largeHoneyLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), largeHoneyLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), smallHoneyLayout, rand);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), endCapLayout, rand);
		
			
		return true;
	}
	
	private void generateSlice(IWorld world, BlockPos.Mutable centerPos, int[][] slice, Random rand)
	{
		//move to the position where the corner of the slice will begin at
		BlockPos.Mutable currentPosition = new BlockPos.Mutable(centerPos.add(-5, slice.length/2, -slice[0].length/2));
		
		//go through each row and column while replacing each solid block
		for(int y = 0; y < slice.length; y++) 
		{
			for(int z = 0; z < slice[0].length; z++) 
			{
				//finds solid block
				if(world.getBlockState(currentPosition).isSolid()) 
				{
					//replace solid block with the slice's blocks
					int sliceBlock = slice[y][z];
					if(sliceBlock == 0)
					{
						//do nothing. 
					}
					else if(sliceBlock == 1)
					{
						//extra check so the ends of the hole exposed will not have this block
						if(world.getBlockState(currentPosition.west()).isSolid() && world.getBlockState(currentPosition.east()).isSolid()) 
						{
							//reduced FILLED_POROUS_HONEYCOMB spawn rate
							if(rand.nextInt(3) == 0)
							{
								world.setBlockState(currentPosition, HONEYCOMB_BLOCK, 2);
							}
							else
							{
								world.setBlockState(currentPosition, FILLED_POROUS_HONEYCOMB, 2);
							}
						}
					}
					else if(sliceBlock == 2)
					{
						//reduced HONEY_BLOCK spawn rate
						if(rand.nextInt(3) == 0)
						{
							world.setBlockState(currentPosition, FILLED_POROUS_HONEYCOMB, 2);
						}
						else
						{
							world.setBlockState(currentPosition, HONEY_BLOCK, 2);
						}
					}
					else if(sliceBlock == 3)
					{
						if(currentPosition.getY() >= world.getSeaLevel())
						{
							world.setBlockState(currentPosition, AIR, 2);
						}
						else
						{
							world.setBlockState(currentPosition, WATER, 2);
						}
					}
				}
				
				//move down the row
				currentPosition.move(Direction.SOUTH);
			}
			
			//move back to start of row and down 1 column
			currentPosition.move(0, -1, -slice[0].length);
		}
	}
}