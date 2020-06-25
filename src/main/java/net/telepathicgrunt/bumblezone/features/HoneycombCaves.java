package net.telepathicgrunt.bumblezone.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.utils.OpenSimplexNoise;

import java.util.Random;
import java.util.function.Function;


public class HoneycombCaves extends Feature<DefaultFeatureConfig> {
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

    public HoneycombCaves(Codec<DefaultFeatureConfig> configFactory) {
        super(configFactory);
    }


    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos position, DefaultFeatureConfig config) {
        setSeed(world.getSeed());
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable().set(position);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 15; y < 241; y++) {
                    mutableBlockPos.set(position).move(x, y, z);
                    double noise1 = noiseGen.eval(mutableBlockPos.getX() * 0.02D,
                            mutableBlockPos.getZ() * 0.02D,
                            mutableBlockPos.getY() * 0.04D);

                    double noise2 = noiseGen2.eval(mutableBlockPos.getX() * 0.02D,
                            mutableBlockPos.getZ() * 0.02D,
                            mutableBlockPos.getY() * 0.04D);

                    double finalNoise = noise1 * noise1 + noise2 * noise2;

                    if (finalNoise < 0.0009f) {
                        hexagon(world, mutableBlockPos, random, noise1);
                    }
                }
            }
        }


        return true;
    }


    private static void hexagon(ServerWorldAccess world, BlockPos position, Random random, double noise) {
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable().set(position);
        BlockState blockState;
        int index = (int) (((noise * 0.5D) + 0.5D) * 7);

        for (int x = 0; x < 14; x++) {
            for (int z = 0; z < 11; z++) {
                int posResult = hexagonArray[index][z][x];

                if (posResult != 0) {
                    blockState = world.getBlockState(mutableBlockPos.set(position).move(x - 7, 0, z - 5));
                    carveAtBlock(world, random, mutableBlockPos, blockState, posResult);

                    blockState = world.getBlockState(mutableBlockPos.set(position).move(0, x - 7, z - 5));
                    carveAtBlock(world, random, mutableBlockPos, blockState, posResult);

                    blockState = world.getBlockState(mutableBlockPos.set(position).move(z - 5, x - 7, 0));
                    carveAtBlock(world, random, mutableBlockPos, blockState, posResult);
                }
            }
        }
    }

    private static void carveAtBlock(ServerWorldAccess world, Random random, BlockPos blockPos, BlockState blockState, int posResult) {
        if (blockPos.getY() < BzDimension.getSeaLevel() || !isNextToLiquidOrAir(world, blockPos)) {
            if (posResult == 2) {
                if (blockPos.getY() < BzDimension.getSeaLevel()) {
                    world.setBlockState(blockPos, SUGAR_WATER, 3);
                } else {
                    world.setBlockState(blockPos, CAVE_AIR, 3);
                }
            } else if (posResult == 1 && world.getBlockState(blockPos).isOpaque()) {
                if (random.nextInt(3) == 0) {
                    world.setBlockState(blockPos, HONEYCOMB_BLOCK, 3);
                } else {
                    world.setBlockState(blockPos, FILLED_POROUS_HONEYCOMB, 3);
                }
            }
        }
    }

    private static boolean isNextToLiquidOrAir(ServerWorldAccess world, BlockPos pos) {
        BlockState blockState;
        for (Direction direction : Direction.values()) {
            blockState = world.getBlockState(pos.offset(direction));
            if (pos.offset(direction).getY() >= BzDimension.getSeaLevel() && blockState == Blocks.AIR.getDefaultState()) {
                return true;
            }
        }
        return false;
    }

}