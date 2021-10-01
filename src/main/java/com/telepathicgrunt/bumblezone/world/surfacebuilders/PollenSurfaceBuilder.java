package com.telepathicgrunt.bumblezone.world.surfacebuilders;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.utils.OpenSimplex2F;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;


public class PollenSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    protected long seed;
    private OpenSimplex2F noiseGenerator = null;

    public PollenSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
        super(codec);
    }

    @Override
    public void initSeed(long seed) {
        if (this.seed != seed || noiseGenerator == null) {
            noiseGenerator = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    @Override
    public void generate(Random random, Chunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int minY, long seed, TernarySurfaceConfig config) {
        //creates the default surface normally
        SurfaceBuilder.DEFAULT.generate(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, minY, seed, config);

        int xpos = x & 15;
        int zpos = z & 15;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        float xzScale = 0.035f;
        float yScale = 0.015f;

        // Vary the pollen piles
        for (int ypos = startHeight; ypos >= minY; --ypos) {
            mutable.set(xpos, ypos, zpos);
            BlockState currentBlockState = chunkIn.getBlockState(mutable);

            if (currentBlockState.isOf(BzBlocks.PILE_OF_POLLEN)) {
                double noiseVal = noiseGenerator.noise3_Classic(x * xzScale, mutable.getY() * yScale, z * xzScale);
                int layerHeight = Math.max(0, (int) (((noiseVal / 2D) + 0.5D) * 8D));
                layerHeight = Math.min(8, layerHeight + currentBlockState.get(PileOfPollen.LAYERS));
                chunkIn.setBlockState(mutable, currentBlockState.with(PileOfPollen.LAYERS, layerHeight), false);
                chunkIn.getBlockTickScheduler().schedule(mutable, currentBlockState.getBlock(), 0);
            }
        }
    }
}