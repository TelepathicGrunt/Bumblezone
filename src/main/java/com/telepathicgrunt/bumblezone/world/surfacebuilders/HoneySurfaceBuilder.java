package com.telepathicgrunt.bumblezone.world.surfacebuilders;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraft.world.level.material.Material;

import java.util.Random;


public class HoneySurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    public HoneySurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
    }

    @Override
    public void apply(Random random, ChunkAccess chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int minSurfaceLevel, long seed, SurfaceBuilderBaseConfiguration config) {
        //creates the default surface normally
        SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, minSurfaceLevel, seed, config);

        int xpos = x & 15;
        int zpos = z & 15;
        BlockPos.MutableBlockPos blockpos$Mutable = new BlockPos.MutableBlockPos();
        int depth = 0;

        // Adds underwater surface blocks that default surface builder cant do.
        for (int ypos = startHeight; ypos >= minSurfaceLevel; --ypos) {
            blockpos$Mutable.set(xpos, ypos, zpos);
            BlockState currentBlockState = chunkIn.getBlockState(blockpos$Mutable);

            if (currentBlockState.getMaterial() != Material.AIR && currentBlockState.getFluidState().isEmpty()) {

                if (ypos <= seaLevel + 2 + Math.max(noise, 0) + random.nextInt(2)) {
                    /*
                    if (depth == 0 &&
                            ModChecker.beeBetterPresent &&
                            noise + random.nextInt(2) < -1)
                    {
                        chunkIn.setBlockState(blockpos$Mutable, BeeBetterRedirection.getBeeswaxBlock(), false);
                    }
                    else
                    */

                    if (currentBlockState == config.getTopMaterial() || currentBlockState == config.getUnderMaterial()) {
                        chunkIn.setBlockState(blockpos$Mutable, config.getUnderwaterMaterial(), false);
                    }
                }

                depth++;
            }
            else{
                depth = 0;
            }
        }
    }
}