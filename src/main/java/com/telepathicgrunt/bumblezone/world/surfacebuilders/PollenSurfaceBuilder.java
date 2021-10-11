package com.telepathicgrunt.bumblezone.world.surfacebuilders;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.utils.OpenSimplex2F;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

import java.util.Random;


public class PollenSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    protected long seed;
    private OpenSimplex2F noiseGenerator = null;

    public PollenSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> codec) {
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
    public void apply(Random random, ChunkAccess chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int minY, long seed, SurfaceBuilderBaseConfiguration config) {
        //creates the default surface normally
        SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, minY, seed, config);

        int xpos = x & 15;
        int zpos = z & 15;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        float xzScale = 0.035f;
        float yScale = 0.015f;

        // Vary the pollen piles
        for (int ypos = startHeight; ypos >= minY; --ypos) {
            mutable.set(xpos, ypos, zpos);
            BlockState currentBlockState = chunkIn.getBlockState(mutable);

            if (currentBlockState.is(BzBlocks.PILE_OF_POLLEN)) {
                double noiseVal = noiseGenerator.noise3_Classic(x * xzScale, mutable.getY() * yScale, z * xzScale);
                int layerHeight = Math.max(0, (int) (((noiseVal / 2D) + 0.5D) * 8D));
                layerHeight = Math.min(8, layerHeight + currentBlockState.getValue(PileOfPollen.LAYERS));
                chunkIn.setBlockState(mutable, currentBlockState.setValue(PileOfPollen.LAYERS, layerHeight), false);
                chunkIn.getBlockTicks().scheduleTick(mutable, currentBlockState.getBlock(), 0);
            }
        }
    }
}