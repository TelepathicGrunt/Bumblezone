package net.telepathicgrunt.bumblezone.features;

import java.util.Random;
import java.util.Vector;
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
import net.telepathicgrunt.bumblezone.utils.OpenSimplexNoise;


public class HoneycombCaves extends Feature<NoFeatureConfig>
{
	//https://github.com/Deadrik/TFC2
	
	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState();
	private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();
	private static final BlockState SUGAR_WATER = BzBlocks.SUGAR_WATER_BLOCK.get().getDefaultState();

	private static final Vector<Double> V1 = new Vector<Double>();
	private static final Vector<Double> V2 = new Vector<Double>();
	private static final Vector<Double> V3 = new Vector<Double>();
	private static final Vector<Double> V4 = new Vector<Double>();
	private static final Vector<Double> V5 = new Vector<Double>();
	private static final Vector<Double> V6 = new Vector<Double>();

	protected long seed;
	protected OpenSimplexNoise noiseGen;
	protected OpenSimplexNoise noiseGen2;
	public void setSeed(long seed)
	{
		if (this.seed != seed || this.noiseGen == null)
		{
			this.noiseGen = new OpenSimplexNoise(seed);
			this.noiseGen2 = new OpenSimplexNoise(seed + 1000);
			this.seed = seed;
		}
	}
	
	
	public HoneycombCaves(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
		
		V1.add(Math.sin(0));
		V1.add(Math.cos(0));
		
		V2.add(Math.sin(2*Math.PI/3));
		V2.add(Math.cos(2*Math.PI/3));
		
		V3.add(Math.sin(4*Math.PI/3));
		V3.add(Math.cos(4*Math.PI/3));
		
		V4.add(Math.sin(2*Math.PI));
		V4.add(Math.cos(2*Math.PI));
		
		V5.add(Math.sin(8*Math.PI/3));
		V5.add(Math.cos(8*Math.PI/3));
		
		V6.add(Math.sin(10*Math.PI/3));
		V6.add(Math.cos(10*Math.PI/3));
	}

	
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> changedBlock, Random rand, BlockPos position, NoFeatureConfig config)
	{
		setSeed(world.getSeed());
		BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(position);
		BlockState blockState;
		
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				for(int y = 5; y < 250; y++) {
					blockState = world.getBlockState(mutableBlockPos.setPos(position).move(x, y, z));
					
					if(blockState.getBlock() != Blocks.AIR && isNextToLiquid(world, mutableBlockPos)) {
						
						double noise1 = this.noiseGen.eval(mutableBlockPos.getX() * 0.02D, 
															mutableBlockPos.getZ() * 0.02D, 
															mutableBlockPos.getY() * 0.03D);
						
						double noise2 = this.noiseGen2.eval(mutableBlockPos.getX() * 0.02D, 
															mutableBlockPos.getZ() * 0.02D, 
															mutableBlockPos.getY() * 0.03D);
						
						//double finalNoise = noise1 * noise1 + noise2 * noise2;
						double finalNoise = 
								Math.abs(dotProduct(V1.get(0), V1.get(1), noise1, noise2)) +
								Math.abs(dotProduct(V2.get(0), V2.get(1), noise1, noise2)) +
								Math.abs(dotProduct(V3.get(0), V3.get(1), noise1, noise2)) +
								Math.abs(dotProduct(V4.get(0), V4.get(1), noise1, noise2)) +
								Math.abs(dotProduct(V5.get(0), V5.get(1), noise1, noise2)) +
								Math.abs(dotProduct(V6.get(0), V6.get(1), noise1, noise2));
						
						if(finalNoise < 0.5f) {
							world.setBlockState(mutableBlockPos, Blocks.GREEN_STAINED_GLASS_PANE.getDefaultState(), 2);
						}
					}
				}
			}
		}
		
		
		return true;
	}
	
	private static double dotProduct(double x1, double y1, double x2, double y2) {
		return x1*x2 + y1*y2;
	}
	
	private static boolean isNextToLiquid(IWorld world, BlockPos pos) {
		for(Direction direction : Direction.values()) {
			if(!world.getBlockState(pos.offset(direction)).getFluidState().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
//    private static double honeycomb(double x, double y, double reduce) {
//        double xx = x * 0.7071067811865476;
//        double yy = y * 1.224744871380249;
//        double xs = yy + xx, ys = yy - xx;
//        int xsb = (int)xs; if (xs < xsb) xsb -= 1;
//        int ysb = (int)ys; if (ys < ysb) ysb -= 1;
//        double xsi = xs - xsb, ysi = ys - ysb;
//        
//        double p = 2 * xsi - ysi;
//        double q = 2 * ysi - xsi;
//        double r = xsi + ysi;
//        if (r > 1) {
//            p -= 1; q -= 1; r -= 2;
//            if (p < -1) {
//                p += 2; q -= 1; r += 1;
//            } else if (q < -1) {
//                p -= 1; q += 2; r += 1;
//            }
//        } else {
//            if (p > 1) {
//                p -= 2; q += 1; r -= 1;
//            } else if (q > 1) {
//                p += 1; q -= 2; r -= 1;
//            }
//        }
//        
//        p *= reduce; q *= reduce; r *= reduce;
//        if (reduce > 1) {
//            if (p > 1) p = 1; else if (p < -1) p = -1;
//            if (q > 1) q = 1; else if (q < -1) q = -1;
//            if (r > 1) r = 1; else if (r < -1) r = -1;
//        }
//        
//        return (1 - p * p) * (1 - q * q) * (1 - r * r);
//}
//
//            double value = honeycomb((x + OFF_X) * FREQ, (y + OFF_Y) * FREQ, 1.25);
//            if (value < 0.03) value = 1; else value = -1;
	

//
//	private static final int[][] bodyLayout = 
//		{
//		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
//		 {0, 0, 0, 1, 3, 3, 3, 3, 1, 0, 0, 0},
//		 {0, 0, 1, 3, 3, 3, 3, 3, 3, 1, 0, 0},
//		 {0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0},
//		 {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1},
//		 {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1},
//		 {0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0},
//		 {0, 0, 1, 3, 3, 3, 3, 3, 3, 1, 0, 0},
//		 {0, 0, 0, 1, 3, 3, 3, 3, 1, 0, 0, 0},
//		 {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0}
//		};
//
//	private void generateSlice(IWorld world, BlockPos.Mutable centerPos, int[][] slice, int orientation, Random rand)
//	{
//		int length1 = bodyLayout.length;
//		int length2 = bodyLayout[0].length;
//		
//		//move to the position where the corner of the slice will begin at
//		BlockPos.Mutable currentPosition = new BlockPos.Mutable();
//		if(orientation == 0)
//			currentPosition.setPos(centerPos.add(-5, length1/2, -length2/2));
//		else if(orientation == 1)
//			currentPosition.setPos(centerPos.add(length1/2, -5, -length2/2));
//		else if(orientation == 2)
//			currentPosition.setPos(centerPos.add(length1/2, -length2/2, -5));
//		
//		BlockState blockState;
//		
//		//go through each row and column while replacing each solid block
//		for(int y = 0; y < length1; y++) 
//		{
//			for(int z = 0; z < length2; z++) 
//			{
//				//finds solid block
//				blockState = world.getBlockState(currentPosition);
//				if(blockState.getMaterial() != Material.AIR && blockState.getFluidState().isEmpty() && !isNextToLiquid(world, currentPosition)) 
//				{
//					//replace solid block with the slice's blocks
//					int sliceBlock = slice[y][z];
//					if(sliceBlock == 0)
//					{
//						//do nothing. 
//					}
//					else if(sliceBlock == 1)
//					{
//						//reduced FILLED_POROUS_HONEYCOMB spawn rate
//						if(rand.nextInt(3) == 0)
//						{
//							world.setBlockState(currentPosition, HONEYCOMB_BLOCK, 2);
//						}
//						else
//						{
//							world.setBlockState(currentPosition, FILLED_POROUS_HONEYCOMB, 2);
//						}
//					}
//					else if(sliceBlock == 3)
//					{
//						if(currentPosition.getY() >= world.getSeaLevel())
//						{
//							world.setBlockState(currentPosition, CAVE_AIR, 2);
//						}
//						else
//						{
//							world.setBlockState(currentPosition, SUGAR_WATER, 2);
//						}
//					}
//				}
//				
//				//move down the row
//				if(orientation == 0)
//					currentPosition.move(Direction.SOUTH);
//				else if(orientation == 1)
//					currentPosition.move(Direction.WEST);
//				else if(orientation == 2)
//					currentPosition.move(Direction.WEST);
//			}
//			
//			//move back to start of row and down 1 column
//			if(orientation == 0)
//				currentPosition.move(0, -1, -length2);
//			else if(orientation == 1)
//				currentPosition.move(-1, 0, -length2);
//			else if(orientation == 2)
//				currentPosition.move(-1, -length2, 0);
//		}
//	}
}