package com.telepathicgrunt.the_bumblezone.worldgen.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import com.telepathicgrunt.the_bumblezone.utils.UnsafeBulkSectionAccess;
import com.telepathicgrunt.the_bumblezone.worldgen.features.configs.BiomeBasedConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Random;


public class WaxAndStoneSurfaceBands extends Feature<BiomeBasedConfig> {
    protected long seed;
    protected static OpenSimplex2F noiseGen;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    public WaxAndStoneSurfaceBands(Codec<BiomeBasedConfig> configFactory) {
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
        int maxY = 60;
        for (int xOffset = 0; xOffset <= 15; xOffset++) {
            for (int zOffset = 0; zOffset <= 15; zOffset++) {
                mutable.set(orgX + xOffset, maxY, orgZ + zOffset);
                if (!bulkSectionAccess.getBiome(mutable, context.level()).is(targetBiome.unwrapKey().get())) {
                    continue;
                }

                previousBlockState = null;
                while (mutable.getY() >= 20) {
                    currentBlockState = bulkSectionAccess.getBlockState(mutable);

                    if (!currentBlockState.canOcclude() ||
                        previousBlockState == null ||
                        !previousBlockState.getCollisionShape(context.level(), mutable).isEmpty())
                    {
                        previousBlockState = currentBlockState;
                        mutable.move(Direction.DOWN);
                        continue;
                    }

                    double noise1 = noiseGen.noise3_Classic(
                            mutable.getX() * 0.01D,
                            mutable.getZ() * 0.01D,
                            0);

                    double finalNoise = noise1 * noise1;

                    if (finalNoise < 0.008f) {
                        fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.GRATE));
                        break;
                    }
                    else if (finalNoise < 0.023f) {
                        fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.BRICKS));
                        break;
                    }
                    else if (finalNoise > 0.3f) {
                        if (finalNoise < 0.4f) {
                            fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.BRICKS));
                            break;
                        }
                        else if (finalNoise < 0.6f) {
                            fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.GRATE));
                            break;
                        }
                        else if (finalNoise < 0.7f) {
                            fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.BRICKS));
                            break;
                        }
                    }

                    double noise2 = noiseGen.noise3_Classic(
                            mutable.getX() * -0.011D,
                            mutable.getZ() * -0.011D,
                            0);

                    double finalNoise2 = noise2 * noise2;

                    if (finalNoise2 < 0.01f) {
                        fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState());
                        break;
                    }
                    else if (finalNoise2 < 0.04f) {
                        fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.SUGAR_INFUSED_STONE.get().defaultBlockState());
                        break;
                    }
                    else if (finalNoise2 > 0.3f) {
                        if (finalNoise2 < 0.4f) {
                            fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.SUGAR_INFUSED_STONE.get().defaultBlockState());
                            break;
                        }
                        else if (finalNoise2 < 0.6f) {
                            fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState());
                            break;
                        }
                        else if (finalNoise2 < 0.7f) {
                            fillDownward(mutable, bulkSectionAccess, context.random(), BzBlocks.SUGAR_INFUSED_STONE.get().defaultBlockState());
                            break;
                        }
                    }


                    break;
                }
            }
        }
    }

    private static void fillDownward(BlockPos.MutableBlockPos mutable, UnsafeBulkSectionAccess bulkSectionAccess, RandomSource randomSource, BlockState fillState) {
        BlockState groundCurrentBlockState;
        int depth = randomSource.nextInt(3) + 4;
        for (int i = 0; i < depth; i++) {
            groundCurrentBlockState = bulkSectionAccess.getBlockState(mutable);
            if (groundCurrentBlockState.canOcclude()) {
                bulkSectionAccess.setBlockState(
                        mutable,
                        fillState,
                        false);
                mutable.move(Direction.DOWN);
            }
            else {
                return;
            }
        }
    }
}