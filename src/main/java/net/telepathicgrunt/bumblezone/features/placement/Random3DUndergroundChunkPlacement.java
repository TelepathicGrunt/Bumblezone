package net.telepathicgrunt.bumblezone.features.placement;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class Random3DUndergroundChunkPlacement extends Placement<FrequencyConfig> {

    public Random3DUndergroundChunkPlacement(Function<Dynamic<?>, ? extends FrequencyConfig> configFactory) {
	super(configFactory);
    }

    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGenerator,
	    Random random, FrequencyConfig placementConfig, BlockPos pos) {

	ArrayList<BlockPos> blockPosList = new ArrayList<BlockPos>();

	// finds the origin of the 16x16x16 area we will be picking from
	BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();

	for (int chunkNum = 0; chunkNum <= placementConfig.count; chunkNum++) {
	    
	    // tries 24 times to find a chunk center in cave air or fluid
	    int attempts = 0;
	    mutableBlockPos.setPos(pos.getX(), random.nextInt(240), pos.getZ());
	    
	    while ((world.getBlockState(mutableBlockPos.add(8, 8, 8)).getBlock() != Blocks.CAVE_AIR
		    && !world.getBlockState(mutableBlockPos.add(8, 8, 8)).getFluidState().isEmpty()) && attempts < 10) {
		
		mutableBlockPos.setPos(pos.getX(), random.nextInt(240), pos.getZ());
	    }
	    
	    // failed to find a valid spot.
	    if (attempts == 24)
		continue;

	    //returns 180 crystal locations in the 16x16x16 area
	    for (int crystalcount = 0; crystalcount <= 180; crystalcount++) {
		int x = random.nextInt(16);
		int z = random.nextInt(16);
		int y = random.nextInt(16);
		blockPosList.add(mutableBlockPos.add(x, y, z));
	    }

	}
	return blockPosList.stream();
    }
}
