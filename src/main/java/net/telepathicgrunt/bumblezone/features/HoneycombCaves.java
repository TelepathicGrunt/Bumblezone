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
import net.telepathicgrunt.bumblezone.utils.OpenSimplexNoise;


public class HoneycombCaves extends Feature<NoFeatureConfig>
{
	public HoneycombCaves(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}

	protected long seed;
	protected OpenSimplexNoise noiseGen;
	public void setSeed(long seed)
	{
		if (this.seed != seed || this.noiseGen == null)
		{
			this.noiseGen = new OpenSimplexNoise(seed);
			this.seed = seed;
		}
	}
	//https://github.com/Deadrik/TFC2
	
	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> changedBlock, Random rand, BlockPos position, NoFeatureConfig config)
	{
//		setSeed(world.getSeed());
//		BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(position);
//		BlockState blockState;
//		
//		for(int x = 0; x < 16; x++) {
//			for(int z = 0; z < 16; z++) {
//				for(int y = 5; y < 250; y++) {
//					blockState = world.getBlockState(mutableBlockPos.setPos(position).move(x, y, z));
//					
//					if(blockState.getBlock() != Blocks.AIR && !isNextToLiquid(world, mutableBlockPos)) {
//						double noise = this.noiseGen.eval(mutableBlockPos.getX() * 0.005D, mutableBlockPos.getZ() * 0.005D, mutableBlockPos.getY() * 0.01D);
//						if(noise > 0.9f) {
//							world.setBlockState(mutableBlockPos, CAVE_AIR, 3);
//						}
//					}
//				}
//			}
//		}
//		
		
		return true;
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
}