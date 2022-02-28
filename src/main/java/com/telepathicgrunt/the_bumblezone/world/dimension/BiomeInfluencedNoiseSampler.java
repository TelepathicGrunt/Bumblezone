package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.mixin.world.NoiseChunkAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBiomeHeightRegistry;
import net.minecraft.Util;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import org.jetbrains.annotations.Nullable;

public class BiomeInfluencedNoiseSampler extends NoiseChunk {

    /**
     * Table of weights used to weight faraway biomes less than nearby biomes.
     */
    private static final int RADIUS = 2;
    private static final float[] BIOME_WEIGHT_TABLE = Util.make(new float[(int) Math.pow((RADIUS * 2) + 1, 2)], (array) -> {
        for(int x = -RADIUS; x <= RADIUS; ++x) {
            for(int z = -RADIUS; z <= RADIUS; ++z) {
                float weight = 10.0F / Mth.sqrt((float)(x * x + z * z) + 0.2F);
                array[x + RADIUS + (z + RADIUS) * 5] = weight / 22f;
            }
        }
    });

    private final BiomeSource biomeSource;
    private final Registry<Biome> biomeRegistry;

    public BiomeInfluencedNoiseSampler(int i, int j, int k, NoiseRouter noiseRouter, int l, int m, DensityFunctions.BeardifierOrMarker beardifierOrMarker, NoiseGeneratorSettings noiseGeneratorSettings, Aquifer.FluidPicker fluidPicker, Blender blender, BiomeSource biomeSource, Registry<Biome> biomeRegistry) {
        super(i, j, k, noiseRouter, l, m, beardifierOrMarker, noiseGeneratorSettings, fluidPicker, blender);
        this.biomeSource = biomeSource;
        this.biomeRegistry = biomeRegistry;
    }

    @Override
    protected BlockState getInterpolatedState() {
        return this.blockStateRule.calculate(this);
    }

    private double calculateBaseNoise(int x, int y, int z, TerrainInfo terrainInfo, Blender blender) {
        double d = this.blendedNoise.calculateNoise(x, y, z);
        return this.calculateBaseNoise(x, y, z, terrainInfo, d, true, blender);
    }

    private double calculateBaseNoise(int x, int y, int z, TerrainInfo terrainInfo, double d, boolean bl2, Blender blender) {
        double e;
        if (this.islandNoise != null) {
            e = ((double) TheEndBiomeSource.getHeightValue(this.islandNoise, x / 8, z / 8) - 8.0) / 128.0;
        }
        else {
            double f = bl2 ? this.sampleJaggedNoise(terrainInfo.jaggedness(), x, z) : 0.0;
            double g = (this.computeBaseDensity(y, terrainInfo) + f) * terrainInfo.factor();
            e = g * (double)(g > 0.0 ? 4 : 1);
        }

        double f = e + d;
        double m = -64.0;

        BzBiomeHeightRegistry.BiomeTerrain centerBiomeInfo = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(this.biomeRegistry.getKey(
                this.biomeSource.getNoiseBiome(x >> 2, 40, z >> 2, this))).orElse(new BzBiomeHeightRegistry.BiomeTerrain(4, 1));

        float totalHeight = 0.0F;
        for(int xOffset = -2; xOffset <= 2; ++xOffset) {
            for(int zOffset = -2; zOffset <= 2; ++zOffset) {
                BzBiomeHeightRegistry.BiomeTerrain biomeTerrain = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(this.biomeRegistry.getKey(
                        this.biomeSource.getNoiseBiome((x >> 2) + xOffset, 40, (z >> 2) + zOffset, this))).orElse(new BzBiomeHeightRegistry.BiomeTerrain(4, 1));
                float biomeDepth = biomeTerrain.depth;
                float weight = BIOME_WEIGHT_TABLE[xOffset + 2 + (zOffset + 2) * 5];
                if(biomeDepth != centerBiomeInfo.depth) {
                    biomeDepth = Mth.lerp(centerBiomeInfo.weightModifier, biomeDepth, centerBiomeInfo.depth);
                }

                totalHeight += (biomeDepth * weight);
            }
        }
        double finalBiomeHeight = totalHeight / 400f;

        double n = Math.max(f, m);
        n = this.applySlide(n, y / this.noiseSettings.getCellHeight());
        n = blender.blendDensity(x, y, z, n);
        n += finalBiomeHeight;
        return Mth.clamp(n, -64.0, 64.0);
    }

    private double computeBaseDensity(int i, TerrainInfo terrainInfo) {
        double d = 1.0 - (double)i / 128.0;
        return d + terrainInfo.offset();
    }
}
