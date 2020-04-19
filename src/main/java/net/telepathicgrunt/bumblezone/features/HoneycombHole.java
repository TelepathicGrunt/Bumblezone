package net.telepathicgrunt.bumblezone.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombLarvaBlock;


public class HoneycombHole extends Feature<NoFeatureConfig>
{
	public HoneycombHole(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}


	private static final int[][] bodyLayout = 
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
	
	private static final int[][] largeHoneyLayout = 
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
	
	private static final int[][] smallHoneyLayout = 
		{
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
		 {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
		 {0, 0, 1, 1, 2, 5, 5, 2, 1, 1, 0, 0},
		 {0, 0, 1, 1, 2, 5, 5, 2, 1, 1, 0, 0},
		 {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
		 {0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
		 {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
	
	private static final int[][] endCapLayout = 
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

	private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState();
	private static final BlockState HONEY_BLOCK = Blocks.HONEY_BLOCK.getDefaultState();
	private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();
	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	private static final BlockState SUGAR_WATER = BzBlocks.SUGAR_WATER_BLOCK.get().getDefaultState();
	

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> changedBlock, Random rand, BlockPos position, NoFeatureConfig config)
	{
		BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(position);
		
		generateSlice(world, mutableBlockPos, endCapLayout, rand, true);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), smallHoneyLayout, rand, true);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), largeHoneyLayout, rand, true);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand, true);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand, true);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand, false);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand, false);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, rand, false);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), largeHoneyLayout, rand, false);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), smallHoneyLayout, rand, false);
		generateSlice(world, mutableBlockPos.move(Direction.EAST), endCapLayout, rand, false);
		
			
		return true;
	}
	
	private void generateSlice(IWorld world, BlockPos.Mutable centerPos, int[][] slice, Random rand, boolean westEnd)
	{
		//move to the position where the corner of the slice will begin at
		BlockPos.Mutable currentPosition = new BlockPos.Mutable(centerPos.add(-5, slice.length/2, -slice[0].length/2));
		BlockState blockState;
		
		//go through each row and column while replacing each solid block
		for(int y = 0; y < slice.length; y++) 
		{
			for(int z = 0; z < slice[0].length; z++) 
			{
				//finds solid block
				blockState = world.getBlockState(currentPosition);
				if(world.getBlockState(currentPosition).getMaterial() != Material.AIR && blockState.getFluidState().isEmpty()) 
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
							world.setBlockState(currentPosition, CAVE_AIR, 2);
						}
						else
						{
							world.setBlockState(currentPosition, SUGAR_WATER, 2);
						}
					}
					else if(sliceBlock == 5)
					{
						//reduced HONEY_BLOCK spawn rate 
						int chance = rand.nextInt(10);
						if(chance <= 3)
						{
							Direction facing;
							if(westEnd)
								facing = Direction.WEST;
							else
								facing = Direction.EAST;
								
							world.setBlockState(currentPosition, BzBlocks.HONEYCOMB_LARVA.get().getDefaultState().with(HoneycombLarvaBlock.STAGE, Integer.valueOf(rand.nextInt(3))).with(HoneycombLarvaBlock.FACING, facing), 2);
						}
						else if(chance <= 6)
						{
							world.setBlockState(currentPosition, FILLED_POROUS_HONEYCOMB, 2);
						}
						else
						{
							world.setBlockState(currentPosition, HONEY_BLOCK, 2);
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