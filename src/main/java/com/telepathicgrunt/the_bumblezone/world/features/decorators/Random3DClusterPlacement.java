package com.telepathicgrunt.the_bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class Random3DClusterPlacement extends RepeatingPlacement {

    private final IntProvider count;
    public static final Codec<Random3DClusterPlacement> CODEC = IntProvider.codec(0, 100000)
            .fieldOf("count").xmap(Random3DClusterPlacement::new, placement -> placement.count).codec();

    private Random3DClusterPlacement(IntProvider intProvider) {
        this.count = intProvider;
    }

    public static Random3DClusterPlacement of(IntProvider intProvider) {
        return new Random3DClusterPlacement(intProvider);
    }

    public static Random3DClusterPlacement of(int i) {
        return of(ConstantInt.of(i));
    }

    @Override
    public PlacementModifierType<?> type() {
        return BzPlacements.RANDOM_3D_CLUSTER_PLACEMENT;
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

            // Tries 24 times to find a chunk's center that is in air
            // Nice quick way to only generate clusters of residue within a chunk without
            // going over chunk edges.
            int attempts = 0;
            for(; attempts < 8; attempts++) {
                mutableBlockPos.set(blockPos.getX(), 0, blockPos.getZ())
                        .move(random.nextInt(4) + 8,
                                random.nextInt(253) + 1,
                                random.nextInt(4) + 8);

                if (placementContext.getBlockState(mutableBlockPos).isAir()) {
                    mutableBlockPos.set(blockPos.getX(), mutableBlockPos.getY(), blockPos.getZ());
                    break;
                }
            }

            // failed to find a valid spot.
            if (attempts == 8)
                continue;

            int maxRadius = 8;
            for(int x = 0; x <= maxRadius; x++) {
                for(int z = 0; z <= maxRadius; z++) {
                    for(int y = 0; y <= maxRadius; y++) {
                        float manhattenDistance = Math.abs(x) + Math.abs(y) + Math.abs(z);
                        if(manhattenDistance <= 12 && random.nextFloat() <= (maxRadius - (manhattenDistance/1.5f)) / maxRadius) {
                            blockPosList.add(mutableBlockPos.offset(x + 8, y + 8, z + 8));
                        }
                    }
                }
            }
        }
        return blockPosList.stream();
    }
}
