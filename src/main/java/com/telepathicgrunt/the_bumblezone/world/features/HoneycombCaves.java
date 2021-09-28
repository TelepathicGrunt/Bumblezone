package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;


public class HoneycombCaves extends Feature<NoFeatureConfig> {
    //https://github.com/Deadrik/TFC2

    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
    private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState();
    private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.defaultBlockState();
    private static final BlockState SUGAR_WATER = BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState();

    protected long seed;
    protected static OpenSimplex2F noiseGen;
    protected static OpenSimplex2F noiseGen2;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            noiseGen2 = new OpenSimplex2F(seed + 3451);
            this.seed = seed;
        }
    }

    private static final int[][] hexagon7 =
            {
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                    {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
                    {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon6 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon5 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon4 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon3 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon2 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon1 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][][] hexagonArray = new int[][][]{hexagon1, hexagon2, hexagon3, hexagon4, hexagon5, hexagon6, hexagon7};

    public HoneycombCaves(Codec<NoFeatureConfig> configFactory) {
        super(configFactory);
    }


    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NoFeatureConfig config) {
        setSeed(world.getSeed());
        BlockPos.Mutable mutableBlockPos = position.mutable();
        BlockPos.Mutable mutableBlockPos2 = position.mutable();
        BlockPos.Mutable mutableBlockPos3 = position.mutable();
        double noise1;
        double noise2;
        double finalNoise;

        for (int x = 0; x < 16; x++) {
            for (int y = 15; y < generator.getGenDepth() - 14; y++) {
                for (int z = 0; z < 16; z++) {
                    mutableBlockPos.set(position).move(x, y, z);

                    noise1 = noiseGen.noise3_Classic(mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    if(noise1 >= 0.0360555127546399D){
                        continue;
                    }

                    noise2 = noiseGen2.noise3_Classic(mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    finalNoise = noise1 * noise1 + noise2 * noise2;

                    if (finalNoise < 0.0013f) {
                        hexagon(world, generator, mutableBlockPos, mutableBlockPos2, mutableBlockPos3, random, noise1);
                    }
                }
            }
        }

        return true;
    }


    private static void hexagon(ISeedReader world, ChunkGenerator generator, BlockPos.Mutable position,
                                BlockPos.Mutable position2, BlockPos.Mutable position3, Random random, double noise) {
        BlockState blockState;
        int index = (int) (((noise * 0.5D) + 0.5D) * 7);

        for (int x = 0; x < 14; x++) {
            for (int z = 0; z < 11; z++) {
                int posResult = hexagonArray[index][z][x];

                if (posResult != 0) {
                    blockState = world.getBlockState(position2.set(position).move(x - 7, 0, z - 5));
                    carveAtBlock(world, generator, random, position2, position3, blockState, posResult);

                    blockState = world.getBlockState(position2.set(position).move(0, x - 7, z - 5));
                    carveAtBlock(world, generator, random, position2, position3, blockState, posResult);

                    blockState = world.getBlockState(position2.set(position).move(z - 5, x - 7, 0));
                    carveAtBlock(world, generator, random, position2, position3, blockState, posResult);
                }
            }
        }
    }

    private static void carveAtBlock(ISeedReader world, ChunkGenerator generator, Random random, BlockPos.Mutable position,
                                     BlockPos.Mutable position2, BlockState blockState, int posResult) {
        if (blockState.canOcclude()) {
            boolean isNextToAir = shouldCloseOff(world, position, position2, true);
            if(position.getY() >= generator.getSeaLevel() && isNextToAir) return;

            if (posResult == 2) {
                if (position.getY() < generator.getSeaLevel()) {
                    boolean isNextToDrySpace = shouldCloseOff(world, position, position2, false);
                    if(isNextToAir || isNextToDrySpace)
                        world.setBlock(position, FILLED_POROUS_HONEYCOMB, 3);
                    else
                        world.setBlock(position, SUGAR_WATER, 3);
                }
                else {
                    world.setBlock(position, CAVE_AIR, 3);
                }
            }
            else if (posResult == 1) {
                if (random.nextInt(3) == 0) {
                    world.setBlock(position, HONEYCOMB_BLOCK, 3);
                }
                else {
                    world.setBlock(position, FILLED_POROUS_HONEYCOMB, 3);
                }
            }
        }
    }

    private static boolean shouldCloseOff(ISeedReader world, BlockPos.Mutable position,
                                          BlockPos.Mutable position2, boolean checkAbove) {
        BlockState blockState;
        for (Direction direction : Direction.values()) {
            if(!checkAbove && direction == Direction.UP) continue;
            blockState = world.getBlockState(position2.set(position).move(direction));
            if (checkAbove ? blockState.is(Blocks.AIR) : (!blockState.canOcclude() && blockState.getFluidState().isEmpty())) {
                return true;
            }
        }
        return false;
    }

}