package net.telepathicgrunt.bumblezone.features.decorators;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class Random3DUndergroundChunkPlacement extends Decorator<CountDecoratorConfig> {

    public Random3DUndergroundChunkPlacement(Codec<CountDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(WorldAccess world, ChunkGenerator chunkGenerator, Random random, CountDecoratorConfig placementConfig, BlockPos pos) {

        ArrayList<BlockPos> blockPosList = new ArrayList<BlockPos>();

        // finds the origin of the 16x16x16 area we will be picking from
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();

        for (int chunkNum = 0; chunkNum <= placementConfig.count; chunkNum++) {

            // tries 24 times to find a chunk center in cave air or fluid
            int attempts = 0;
            mutableBlockPos.set(pos.getX(), random.nextInt(240), pos.getZ());

            while ((world.getBlockState(mutableBlockPos.add(8, 8, 8)).getBlock() != Blocks.CAVE_AIR
                    && !world.getBlockState(mutableBlockPos.add(8, 8, 8)).getFluidState().isEmpty()) && attempts < 10) {

                mutableBlockPos.set(pos.getX(), random.nextInt(240), pos.getZ());
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
