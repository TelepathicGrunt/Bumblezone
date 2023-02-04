package com.telepathicgrunt.the_bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Random3DUndergroundChunkPlacement extends RepeatingPlacement {

    private final IntProvider count;
    public static final Codec<Random3DUndergroundChunkPlacement> CODEC = IntProvider.codec(0, 100000)
            .fieldOf("count").xmap(Random3DUndergroundChunkPlacement::new, placement -> placement.count).codec();

    private Random3DUndergroundChunkPlacement(IntProvider intProvider) {
        this.count = intProvider;
    }

    public static Random3DUndergroundChunkPlacement of(IntProvider intProvider) {
        return new Random3DUndergroundChunkPlacement(intProvider);
    }

    public static Random3DUndergroundChunkPlacement of(int i) {
        return of(ConstantInt.of(i));
    }

    @Override
    public PlacementModifierType<?> type() {
        return BzPlacements.RANDOM_3D_UNDERGROUND_CHUNK_PLACEMENT.get();
    }

    @Override
    protected int count(RandomSource random, BlockPos blockPos) {
        return this.count.sample(random);
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos blockPos) {
        ArrayList<BlockPos> blockPosList = new ArrayList<>();

        // finds the origin of the 16x16x16 area we will be picking from
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int chunkNum = 0; chunkNum <= this.count(random, blockPos); chunkNum++) {

            // Tries 24 times to find a chunk's center that is in cave air or fluid.
            // Nice quick way to only generate clusters of crystals within a chunk without
            // going over chunk edges.
            int attempts = 0;
            for(; attempts < 8; attempts++) {
                mutableBlockPos.set(blockPos.getX(), 0, blockPos.getZ())
                        .move(random.nextInt(4) + 8,
                                random.nextInt(253) + 1,
                                random.nextInt(4) + 8);

                if ((placementContext.getBlockState(mutableBlockPos).getBlock() == Blocks.CAVE_AIR
                        || placementContext.getBlockState(mutableBlockPos).getFluidState().is(FluidTags.WATER))) {
                    mutableBlockPos.set(blockPos.getX(), mutableBlockPos.getY(), blockPos.getZ());
                    break;
                }
            }

            // failed to find a valid spot.
            if (attempts == 8)
                continue;

            //returns 180 crystal locations in the 16x16x16 area
            for (int crystalcount = 0; crystalcount <= 180; crystalcount++) {
                int x = random.nextInt(16);
                int z = random.nextInt(16);
                int y = random.nextInt(16);
                blockPosList.add(mutableBlockPos.offset(x, y, z));
            }

        }
        return blockPosList.stream();
    }
}
