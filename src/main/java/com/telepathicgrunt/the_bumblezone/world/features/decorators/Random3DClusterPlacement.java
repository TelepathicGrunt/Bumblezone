package com.telepathicgrunt.the_bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Random3DClusterPlacement extends RepeatingPlacement {

    private final IntProvider count;
    private final boolean allowUnderwater;

    public static final Codec<Random3DClusterPlacement> CODEC = RecordCodecBuilder.create((configInstance) -> configInstance.group(
            IntProvider.codec(0, 100000).fieldOf("count").forGetter(nbtFeatureConfig -> nbtFeatureConfig.count),
            Codec.BOOL.fieldOf("allow_underwater").forGetter(nbtFeatureConfig -> nbtFeatureConfig.allowUnderwater)
    ).apply(configInstance, Random3DClusterPlacement::new));

    private Random3DClusterPlacement(IntProvider intProvider, boolean allowUnderwater) {
        this.count = intProvider;
        this.allowUnderwater = allowUnderwater;
    }

    @Override
    public PlacementModifierType<?> type() {
        return BzPlacements.RANDOM_3D_CLUSTER_PLACEMENT.get();
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

                BlockState state = placementContext.getBlockState(mutableBlockPos);
                if (state.isAir() || (this.allowUnderwater && !state.getFluidState().is(FluidTags.WATER))) {
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
