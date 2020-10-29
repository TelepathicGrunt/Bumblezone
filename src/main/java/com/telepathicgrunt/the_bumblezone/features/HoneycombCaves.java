package com.telepathicgrunt.the_bumblezone.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.fluids.BzFluids;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;


public class HoneycombCaves extends Feature<NoFeatureConfig> {
    //https://github.com/Deadrik/TFC2

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


    public HoneycombCaves(Codec<NoFeatureConfig> configFactory) {
        super(configFactory);
    }


    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NoFeatureConfig config) {
        setSeed(world.getSeed());
        BlockPos.Mutable mutableBlockPos = position.mutableCopy();
        BlockPos.Mutable mutableBlockPos2 = position.mutableCopy();
        double threshold = 0.012D;

        for (int x = 0; x < 16; x++) {
            for (int y = 15; y < 241; y++) {
                for (int z = 0; z < 16; z++) {
                    mutableBlockPos.setPos(position).move(x, y, z);
                    BlockState state = world.getBlockState(mutableBlockPos);

                    // Skip carving non-solid spots
                    if(state.isSolid()) {

                        double noise1 = noiseGen.noise3_Classic(mutableBlockPos.getX() * 0.005D,
                                mutableBlockPos.getZ() * 0.005D,
                                mutableBlockPos.getY() * 0.014D);

                        double noise2 = noiseGen2.noise3_Classic(mutableBlockPos.getX() * 0.011D,
                                mutableBlockPos.getZ() * 0.011D,
                                mutableBlockPos.getY() * 0.014D);


                        double finalNoise = noise1 * noise1 + noise2 * noise2;
                        if (finalNoise < threshold) {
                            carveAtBlock(world, generator, random, mutableBlockPos, mutableBlockPos2, state, finalNoise > threshold - 0.006D);
                        }
                    }
                }
            }
        }

        return true;
    }

    private static void carveAtBlock(ISeedReader world, ChunkGenerator generator, Random random, BlockPos.Mutable position,
                                     BlockPos.Mutable position2, BlockState blockState, boolean edge)
    {
        if (position.getY() < generator.getSeaLevel() || !isNextToOpenAir(world, generator, position, position2))
        {
            if (!edge) {
                if (position.getY() < 40) {
                    world.setBlockState(position, BzFluids.SUGAR_WATER_BLOCK.get().getDefaultState(), 3);
                } else {
                    world.setBlockState(position, Blocks.CAVE_AIR.getDefaultState(), 3);
                }
            }
            else {
                if (random.nextInt(3) == 0) {
                    world.setBlockState(position, Blocks.HONEYCOMB_BLOCK.getDefaultState(), 3);
                } else {
                    world.setBlockState(position, BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState(), 3);
                }
            }
        }
    }

    private static boolean isNextToOpenAir(ISeedReader world, ChunkGenerator generator, BlockPos.Mutable position, BlockPos.Mutable position2)
    {
        Block block;
        for (Direction direction : Direction.values()) {
            position2.setPos(position).move(direction);

            // We have to check manually for the block as world.getBlockState has a shortcircuit that will return AIR
            // if the entire chunk is any combination of blocks that makes isAir return true. God. Why Mojang?!
            int y = position2.getY();
            if (World.isYOutOfBounds(y)) {
                block = Blocks.VOID_AIR;
            }
            else {
                ChunkSection chunksection = world.getChunk(position2.getX() >> 4, position2.getZ() >> 4).getSections()[position2.getY() >> 4];

                if(chunksection == null){
                    block = Blocks.VOID_AIR;
                }
                else {
                    block = chunksection.getBlockState(position2.getX() & 15, y & 15, position2.getZ() & 15).getBlock();
                }
            }

            if (position2.getY() >= generator.getSeaLevel() && block == Blocks.AIR) {
                return true;
            }
        }
        return false;
    }

}