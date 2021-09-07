package com.telepathicgrunt.the_bumblezone.world.surfacebuilders;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

import java.util.Random;


public class PollenSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {
    protected long seed;
    private OpenSimplex2F noiseGenerator = null;

    public PollenSurfaceBuilder(Codec<SurfaceBuilderConfig> codec) {
        super(codec);
    }

    @Override
    public void initNoise(long seed) {
        if (this.seed != seed || noiseGenerator == null) {
            noiseGenerator = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    @Override
    public void apply(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
        //creates the default surface normally
        SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, config);

        int xpos = x & 15;
        int zpos = z & 15;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        float xzScale = 0.035f;
        float yScale = 0.015f;

        // Vary the pollen piles
        for (int ypos = startHeight; ypos >= 0; --ypos) {
            mutable.set(xpos, ypos, zpos);
            BlockState currentBlockState = chunkIn.getBlockState(mutable);

            if (currentBlockState.is(BzBlocks.PILE_OF_POLLEN.get())) {
                double noiseVal = noiseGenerator.noise3_Classic(x * xzScale, mutable.getY() * yScale, z * xzScale);
                int layerHeight = Math.max(0, (int) (((noiseVal / 2D) + 0.5D) * 8D));
                layerHeight = Math.min(8, layerHeight + currentBlockState.getValue(PileOfPollen.LAYERS));
                chunkIn.setBlockState(mutable, currentBlockState.setValue(PileOfPollen.LAYERS, layerHeight), false);
                chunkIn.getBlockTicks().scheduleTick(mutable, currentBlockState.getBlock(), 0);
            }
        }
    }
}