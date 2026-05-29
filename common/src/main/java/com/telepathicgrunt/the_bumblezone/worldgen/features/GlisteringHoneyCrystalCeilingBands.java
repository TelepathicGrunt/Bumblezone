package com.telepathicgrunt.the_bumblezone.worldgen.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.GlisteringHoneyCrystal;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import com.telepathicgrunt.the_bumblezone.utils.UnsafeBulkSectionAccess;
import com.telepathicgrunt.the_bumblezone.worldgen.features.configs.BiomeBasedConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;


public class GlisteringHoneyCrystalCeilingBands extends Feature<BiomeBasedConfig> {
    protected long seed;
    protected static OpenSimplex2F noiseGen;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    public GlisteringHoneyCrystalCeilingBands(Codec<BiomeBasedConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<BiomeBasedConfig> context) {
        WorldGenLevel level = context.level();
        setSeed(level.getSeed());
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(context.origin());
        Holder<Biome> targetBiome = context.config().biome;

        int orgX = context.origin().getX();
        int orgZ = context.origin().getZ();

        UnsafeBulkSectionAccess bulkSectionAccess = new UnsafeBulkSectionAccess(context.level());
        fillChunkWithRoads(context, mutable, orgX, orgZ, bulkSectionAccess, targetBiome);

        return true;
    }

    private void fillChunkWithRoads(FeaturePlaceContext<BiomeBasedConfig> context, BlockPos.MutableBlockPos mutable, int orgX, int orgZ, UnsafeBulkSectionAccess bulkSectionAccess, Holder<Biome> targetBiome) {
        BlockState currentBlockState;
        BlockState previousBlockState;
        int minY = 100;
        for (int xOffset = 0; xOffset <= 15; xOffset++) {
            for (int zOffset = 0; zOffset <= 15; zOffset++) {
                mutable.set(orgX + xOffset, minY, orgZ + zOffset);
                if (!bulkSectionAccess.getBiome(mutable, context.level()).is(targetBiome)) {
                    continue;
                }

                previousBlockState = null;
                while (mutable.getY() <= 220) {
                    currentBlockState = bulkSectionAccess.getBlockState(mutable);

                    if (!currentBlockState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get()) ||
                        previousBlockState == null ||
                        !previousBlockState.isAir())
                    {
                        previousBlockState = currentBlockState;
                        mutable.move(Direction.UP);
                        continue;
                    }

                    double noise1 = noiseGen.noise3_Classic(
                            mutable.getX() * 0.015D,
                            mutable.getZ() * 0.015D,
                            0);

                    double threshold = 0.0175f;
                    double distanceFromThreshold = noise1 - 0.21f;
                    double finalNoise = noise1 * noise1;

                    if (distanceFromThreshold > 0) {
                        zOffset = zSkipping(zOffset, distanceFromThreshold);
                        break;
                    }

                    if (finalNoise < threshold) {
                        fillUpward(mutable, bulkSectionAccess, BzBlocks.GLISTERING_HONEY_CRYSTAL.get().defaultBlockState().setValue(GlisteringHoneyCrystal.FACING, Direction.DOWN));
                        break;
                    }

                    break;
                }
            }
        }
    }

    private static void fillUpward(BlockPos.MutableBlockPos mutable, UnsafeBulkSectionAccess bulkSectionAccess, BlockState fillState) {
        BlockState groundCurrentBlockState;
        for (int i = 0; i < 7; i++) {
            groundCurrentBlockState = bulkSectionAccess.getBlockState(mutable);
            if (groundCurrentBlockState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get())) {
                bulkSectionAccess.setBlockState(
                        mutable,
                        fillState,
                        false);
                mutable.move(Direction.UP);
            }
            else {
                return;
            }
        }
    }

    /// Noise generators giving a value very far from our threshold means there a large area where the noise value will remain too far.
    /// This attempts to skip those area in hopes we land into a spot that is much closer to our threshold where we can then be checking every block.
    /// Noise generators can be expensive to run so this is a neat small optimization. Values were chosen based on visual testing.
    private int zSkipping(int z, double noiseDistanceFromThreshold) {
        if (noiseDistanceFromThreshold >= 0.8) {
            z += 5;
        }
        else if (noiseDistanceFromThreshold >= 0.7) {
            z += 4;
        }
        else if (noiseDistanceFromThreshold >= 0.6) {
            z += 3;
        }
        else if (noiseDistanceFromThreshold >= 0.5) {
            z += 2;
        }
        else if (noiseDistanceFromThreshold >= 0.4) {
            z += 1;
        }
        return z;
    }
}