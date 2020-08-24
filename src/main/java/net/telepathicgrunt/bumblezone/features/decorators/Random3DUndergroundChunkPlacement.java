package net.telepathicgrunt.bumblezone.features.decorators;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class Random3DUndergroundChunkPlacement extends Decorator<CountConfig> {

    public Random3DUndergroundChunkPlacement(Codec<CountConfig> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, CountConfig placementConfig, BlockPos pos) {

        ArrayList<BlockPos> blockPosList = new ArrayList<BlockPos>();

        // finds the origin of the 16x16x16 area we will be picking from
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();

        for (int chunkNum = 0; chunkNum <= placementConfig.getCount().getValue(random); chunkNum++) {

            // Tries 24 times to find a chunk's center that is in cave air or fluid.
            // Nice quick way to only generate clusters of crystals within a chunk without
            // going over chunk edges.
            int attempts = 0;
            mutableBlockPos.set(pos.getX(), random.nextInt(240), pos.getZ());

            for(; attempts < 24; attempts++){
                if ((context.getBlockState(mutableBlockPos.add(8, 8, 8)).getBlock() != Blocks.CAVE_AIR
                        && !context.getBlockState(mutableBlockPos.add(8, 8, 8)).getFluidState().isEmpty())) {

                    mutableBlockPos.set(pos.getX(), random.nextInt(253) + 1, pos.getZ());
                    break;
                }
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
