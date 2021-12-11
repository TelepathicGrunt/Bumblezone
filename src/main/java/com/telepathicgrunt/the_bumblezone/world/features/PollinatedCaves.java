package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


public class PollinatedCaves extends Feature<NoneFeatureConfiguration> {
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

    public PollinatedCaves(Codec<NoneFeatureConfiguration> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        setSeed(context.level().getSeed());
        BlockPos.MutableBlockPos mutableBlockPos = context.origin().mutable();
        double noise1;
        double noise2;
        double finalNoise;

        for (int x = 0; x < 16; x++) {
            for (int y = 15; y < context.chunkGenerator().getGenDepth() - 14; y++) {
                for (int z = 0; z < 16; z++) {
                    mutableBlockPos.set(context.origin()).move(x, y, z);

                    noise1 = noiseGen.noise3_Classic(mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    if(noise1 >= 0.0360555127546399D) {
                        continue;
                    }

                    noise2 = noiseGen2.noise3_Classic(mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    double heightPressure = Math.max((30f - y) / 90f, 0);
                    finalNoise = (noise1 * noise1) + (noise2 * noise2) + heightPressure;

                    if (finalNoise < 0.01305f) {
                        carve(context.level(), mutableBlockPos, finalNoise, noise1);
                    }
                }
            }
        }

        return true;
    }

    private static void carve(WorldGenLevel world, BlockPos.MutableBlockPos position, double finalNoise, double noise) {
        BlockState currentState = world.getBlockState(position);
        if(!currentState.isAir() && currentState.getFluidState().isEmpty() && !currentState.is(BzBlocks.PILE_OF_POLLEN.get())) {
            // varies the surface of the cave surface
            if(finalNoise > 0.0105f) {
                if((noise * 3) % 2 < 0.35D) {
                    world.setBlock(position, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), 3);
                }
                return;
            }

            // cannot carve next to fluids
            BlockPos.MutableBlockPos sidePos = new BlockPos.MutableBlockPos();
            for(Direction direction : Direction.values()) {
                sidePos.set(position).move(direction);
                if(!world.getBlockState(sidePos).getFluidState().isEmpty()) {
                    return;
                }
            }

            // places cave air or pollen pile
            position.move(Direction.DOWN);
            BlockState belowState = world.getBlockState(position);
            position.move(Direction.UP);

            if(!belowState.isAir() && belowState.getFluidState().isEmpty() && belowState.getMaterial().blocksMotion()) {
                world.setBlock(position, BzBlocks.PILE_OF_POLLEN.get().defaultBlockState().setValue(PileOfPollen.LAYERS, (int)Math.max(Math.min((noise + 1D) * 3D, 8), 1)), 3);
                world.scheduleTick(position, BzBlocks.PILE_OF_POLLEN.get(), 0);

                int carveHeight = Math.abs((int) ((noise * 1000) % 0.8D)) * 2 + 1;
                for(int i = 0; i < carveHeight; i++) {
                    position.move(Direction.UP);
                    // cannot carve next to fluids
                    for(Direction direction : Direction.values()) {
                        sidePos.set(position).move(direction);
                        if(!world.getBlockState(sidePos).getFluidState().isEmpty()) {
                            return;
                        }
                    }
                    world.setBlock(position, Blocks.CAVE_AIR.defaultBlockState(), 3);
                }
                position.move(Direction.DOWN, carveHeight);
            }
            else {
                world.setBlock(position, Blocks.CAVE_AIR.defaultBlockState(), 3);
            }
        }
    }
}