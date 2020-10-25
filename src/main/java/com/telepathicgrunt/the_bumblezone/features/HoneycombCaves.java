package com.telepathicgrunt.the_bumblezone.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplexNoise;
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

    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState();
    private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();
    private static final BlockState SUGAR_WATER = BzBlocks.SUGAR_WATER_BLOCK.getDefaultState();


    protected long seed;
    protected static OpenSimplexNoise noiseGen;
    protected static OpenSimplexNoise noiseGen2;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplexNoise(seed);
            noiseGen2 = new OpenSimplexNoise(seed + 1000);
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
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NoFeatureConfig config) {
        setSeed(world.getSeed());
        BlockPos.Mutable mutableBlockPos = position.mutableCopy();
        BlockPos.Mutable mutableBlockPos2 = position.mutableCopy();
        BlockPos.Mutable mutableBlockPos3 = position.mutableCopy();

        for (int x = 0; x < 16; x++) {
            for (int y = 15; y < 241; y++) {
                for (int z = 0; z < 16; z++) {
                    mutableBlockPos.setPos(position).move(x, y, z);
                    double noise1 = noiseGen.eval(mutableBlockPos.getX() * 0.02D,
                            mutableBlockPos.getZ() * 0.02D,
                            mutableBlockPos.getY() * 0.04D);

                    double noise2 = noiseGen2.eval(mutableBlockPos.getX() * 0.02D,
                            mutableBlockPos.getZ() * 0.02D,
                            mutableBlockPos.getY() * 0.04D);

                    double finalNoise = noise1 * noise1 + noise2 * noise2;

                    if (finalNoise < 0.0009f) {
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
                    blockState = world.getBlockState(position2.setPos(position).move(x - 7, 0, z - 5));
                    carveAtBlock(world, generator, random, position2, position3, blockState, posResult);

                    blockState = world.getBlockState(position2.setPos(position).move(0, x - 7, z - 5));
                    carveAtBlock(world, generator, random, position2, position3, blockState, posResult);

                    blockState = world.getBlockState(position2.setPos(position).move(z - 5, x - 7, 0));
                    carveAtBlock(world, generator, random, position2, position3, blockState, posResult);
                }
            }
        }
    }

    private static void carveAtBlock(ISeedReader world, ChunkGenerator generator, Random random, BlockPos.Mutable position,
                                     BlockPos.Mutable position2, BlockState blockState, int posResult) {
        if (position.getY() < generator.getSeaLevel() || !isNextToLiquidOrAir(world, generator, position, position2)) {
            if (posResult == 2 && !blockState.isSolid()) {
                if (position.getY() < 40) {
                    world.setBlockState(position, SUGAR_WATER, 3);
                } else {
                    world.setBlockState(position, CAVE_AIR, 3);
                }
            } else if (posResult == 1 && blockState.isSolid()) {
                if (random.nextInt(3) == 0) {
                    world.setBlockState(position, HONEYCOMB_BLOCK, 3);
                } else {
                    world.setBlockState(position, FILLED_POROUS_HONEYCOMB, 3);
                }
            }
        }
    }

    private static boolean isNextToLiquidOrAir(ISeedReader world, ChunkGenerator generator,
                                               BlockPos.Mutable position, BlockPos.Mutable position2) {
        BlockState blockState;
        for (Direction direction : Direction.values()) {
            blockState = world.getBlockState(position2.setPos(position).move(direction));
            if (position2.getY() >= generator.getSeaLevel() && blockState == Blocks.AIR.getDefaultState()) {
                return true;
            }
        }
        return false;
    }

}