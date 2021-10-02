package com.telepathicgrunt.bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzFluids;
import com.telepathicgrunt.bumblezone.utils.OpenSimplex2F;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;


public class HoneycombCaves extends Feature<DefaultFeatureConfig> {
    //https://github.com/Deadrik/TFC2

    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState();
    private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();
    private static final BlockState SUGAR_WATER = BzFluids.SUGAR_WATER_BLOCK.getDefaultState();


    protected long seed;
    protected static OpenSimplex2F noiseGen;
    protected static OpenSimplex2F noiseGen2;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            noiseGen2 = new OpenSimplex2F(seed + 1000);
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

    public HoneycombCaves(Codec<DefaultFeatureConfig> configFactory) {
        super(configFactory);
    }


    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        setSeed(context.getWorld().getSeed());
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable().set(context.getOrigin());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 15; y < 241; y++) {
                    mutableBlockPos.set(context.getOrigin()).move(x, y, z);

                    double noise1 = noiseGen.noise3_Classic(mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    if(noise1 >= 0.0360555127546399D){
                        continue;
                    }

                    double noise2 = noiseGen2.noise3_Classic(mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    double finalNoise = noise1 * noise1 + noise2 * noise2;

                    if (finalNoise < 0.0013f) {
                        hexagon(context.getWorld(), context.getGenerator(), mutableBlockPos, context.getRandom(), noise1);
                    }
                }
            }
        }


        return true;
    }


    private static void hexagon(StructureWorldAccess world, ChunkGenerator generator, BlockPos position, Random random, double noise) {
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable().set(position);
        BlockState blockState;
        int index = (int) (((noise * 0.5D) + 0.5D) * 7);
        BlockPos.Mutable tempMutable = new BlockPos.Mutable();

        for (int x = 0; x < 14; x++) {
            for (int z = 0; z < 11; z++) {
                int posResult = hexagonArray[index][z][x];

                if (posResult != 0) {
                    blockState = world.getBlockState(mutableBlockPos.set(position).move(x - 7, 0, z - 5));
                    carveAtBlock(world, generator, random, mutableBlockPos, tempMutable, blockState, posResult);

                    blockState = world.getBlockState(mutableBlockPos.set(position).move(0, x - 7, z - 5));
                    carveAtBlock(world, generator, random, mutableBlockPos, tempMutable, blockState, posResult);

                    blockState = world.getBlockState(mutableBlockPos.set(position).move(z - 5, x - 7, 0));
                    carveAtBlock(world, generator, random, mutableBlockPos, tempMutable, blockState, posResult);
                }
            }
        }
    }

    private static void carveAtBlock(StructureWorldAccess world, ChunkGenerator generator, Random random,
                                     BlockPos blockPos, BlockPos.Mutable mutable, BlockState blockState, int posResult) {
        if (blockState.isOpaque()) {
            boolean isNextToAir = shouldCloseOff(world, blockPos, mutable, true);
            if(blockPos.getY() >= generator.getSeaLevel() && isNextToAir) return;

            if (posResult == 2) {
                if (blockPos.getY() < generator.getSeaLevel()) {
                    boolean isNextToDrySpace = shouldCloseOff(world, blockPos, mutable, false);
                    if(isNextToAir || isNextToDrySpace)
                        world.setBlockState(blockPos, FILLED_POROUS_HONEYCOMB, 3);
                    else
                        world.setBlockState(blockPos, SUGAR_WATER, 3);
                }
                else {
                    world.setBlockState(blockPos, CAVE_AIR, 3);
                }
            } else if (posResult == 1) {
                if (random.nextInt(3) == 0) {
                    world.setBlockState(blockPos, HONEYCOMB_BLOCK, 3);
                } else {
                    world.setBlockState(blockPos, FILLED_POROUS_HONEYCOMB, 3);
                }
            }
        }
    }

    private static boolean shouldCloseOff(StructureWorldAccess world, BlockPos position,
                                          BlockPos.Mutable position2, boolean checkAbove) {
        BlockState blockState;
        for (Direction direction : Direction.values()) {
            if(!checkAbove && direction == Direction.UP) continue;
            blockState = world.getBlockState(position2.set(position).move(direction));
            if (checkAbove ? blockState.isOf(Blocks.AIR) : (!blockState.isOpaque() && blockState.getFluidState().isEmpty())) {
                return true;
            }
        }
        return false;
    }
}