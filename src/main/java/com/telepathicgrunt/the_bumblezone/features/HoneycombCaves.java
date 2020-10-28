package com.telepathicgrunt.the_bumblezone.features;

import java.util.Random;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.fluids.BzFluids;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplexNoise;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Lazy;


public class HoneycombCaves extends Feature<NoFeatureConfig> {
    //https://github.com/Deadrik/TFC2

    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState();
    private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();
    private static final Lazy<BlockState> SUGAR_WATER = Lazy.of(() -> BzFluids.SUGAR_WATER_BLOCK.get().getDefaultState());

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


//    private static final double THETA0 = Math.PI * (0.0 / 180.0);
//    private static final double THETA1 = Math.PI * (60.0 / 180.0);
//    private static final double THETA2 = Math.PI * (120.0 / 180.0);
    private static final double THETA0 = Math.PI * (45.0 / 180.0);
    private static final double THETA1 = Math.PI * (105.0 / 180.0);
    private static final double THETA2 = Math.PI * (165.0 / 180.0);
    private static final double SIN0 = Math.sin(THETA0), COS0 = Math.cos(THETA0);
    private static final double SIN1 = Math.sin(THETA1), COS1 = Math.cos(THETA1);
    private static final double SIN2 = Math.sin(THETA2), COS2 = Math.cos(THETA2);


    public HoneycombCaves(Codec<NoFeatureConfig> configFactory) {
        super(configFactory);
    }


    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NoFeatureConfig config) {
        setSeed(world.getSeed());
        BlockPos.Mutable mutableBlockPos = position.mutableCopy();
        BlockPos.Mutable mutableBlockPos2 = position.mutableCopy();

        for (int x = 0; x < 16; x++) {
            for (int y = 15; y < 241; y++) {
                for (int z = 0; z < 16; z++) {
                    mutableBlockPos.setPos(position).move(x, y, z);

                    BlockState state = world.getBlockState(mutableBlockPos);
                    if(state.isSolid()) continue; // Skip carving non-solid spots

                    double noise1 = noiseGen.eval(mutableBlockPos.getX() * 0.02D,
                            mutableBlockPos.getZ() * 0.02D,
                            mutableBlockPos.getY() * 0.04D);

                    double noise2 = noiseGen2.eval(mutableBlockPos.getX() * 0.02D,
                            mutableBlockPos.getZ() * 0.02D,
                            mutableBlockPos.getY() * 0.04D);


                    double finalNoise0 = noise1 * SIN0 + noise2 * COS0;
                    double finalNoise1 = noise1 * SIN1 + noise2 * COS1;
                    double finalNoise2 = noise1 * SIN2 + noise2 * COS2;
                    double finalNoise = Math.min(finalNoise0, Math.min(finalNoise1, finalNoise2));

                    if (finalNoise < 0.12) {
                        carveAtBlock(world, generator, random, mutableBlockPos, mutableBlockPos2, state, finalNoise < 0.1);
                    }
                }
            }
        }

        return true;
    }

    private static void carveAtBlock(ISeedReader world, ChunkGenerator generator, Random random, BlockPos.Mutable position,
                                     BlockPos.Mutable position2, BlockState blockState, boolean edge) {
        if (blockState.isSolid() && (position.getY() < generator.getSeaLevel() || !isNextToLiquidOrAir(world, generator, position, position2)))
        {
            if (!edge) {
                if (position.getY() < 40) {
                    world.setBlockState(position, SUGAR_WATER.get(), 3);
                } else {
                    world.setBlockState(position, CAVE_AIR, 3);
                }
            }
            else if (blockState.isSolid()) {
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