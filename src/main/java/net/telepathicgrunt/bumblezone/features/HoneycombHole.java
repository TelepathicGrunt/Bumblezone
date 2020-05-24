package net.telepathicgrunt.bumblezone.features;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;

import java.util.Random;
import java.util.function.Function;


public class HoneycombHole extends Feature<DefaultFeatureConfig>
{
	public HoneycombHole(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configFactory)
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

	private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState();
	private static final BlockState HONEY_BLOCK = Blocks.HONEY_BLOCK.getDefaultState();
	private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();
	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
	

	public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> changedBlock, Random rand, BlockPos position, DefaultFeatureConfig config)
	{
		
		BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(position.getX(), position.getY(), position.getZ());
		
		generateSlice(world, mutableBlockPos, endCapLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), smallHoneyLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), largeHoneyLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), bodyLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), largeHoneyLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), smallHoneyLayout, rand);
		generateSlice(world, mutableBlockPos.setOffset(Direction.EAST), endCapLayout, rand);
		
			
		return true;
	}
	
	private void generateSlice(IWorld world, BlockPos.Mutable centerPos, int[][] slice, Random rand)
	{
		//setOffset to the position where the corner of the slice will begin at
		BlockPos currentPositionOffsetted = new BlockPos(centerPos.add(-5, slice.length/2, -slice[0].length/2));
		BlockPos.Mutable currentPosition = new BlockPos.Mutable(currentPositionOffsetted.getX(), currentPositionOffsetted.getY(), currentPositionOffsetted.getZ());

		//go through each row and column while replacing each solid block
		for(int y = 0; y < slice.length; y++)
		{
			for(int z = 0; z < slice[0].length; z++) 
			{
				//finds solid block
				if(world.getBlockState(currentPosition).isOpaque()) 
				{
					//replace solid block with the slice's blocks
					int sliceBlock = slice[y][z];
					if(sliceBlock == 1)
					{
						//extra check so the ends of the hole exposed will not have this block
						if(world.getBlockState(currentPosition.west()).isOpaque() && world.getBlockState(currentPosition.east()).isOpaque()) 
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
						if(currentPosition.getY() >= 40)
						{
							world.setBlockState(currentPosition, AIR, 2);
						}
						else
						{
							world.setBlockState(currentPosition, WATER, 2);
						}
					}
				}
				
				//setOffset down the row
				currentPosition.setOffset(Direction.SOUTH);
			}
			
			//setOffset back to start of row and down 1 column
			currentPosition.setOffset(0, -1, -slice[0].length);
		}
	}
}