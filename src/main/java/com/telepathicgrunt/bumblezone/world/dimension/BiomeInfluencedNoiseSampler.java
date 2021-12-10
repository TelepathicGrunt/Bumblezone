package com.telepathicgrunt.bumblezone.world.dimension;

import com.telepathicgrunt.bumblezone.mixin.world.NoiseChunkAccessor;
import com.telepathicgrunt.bumblezone.modinit.BzBiomeHeightRegistry;
import net.minecraft.Util;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraft.world.level.levelgen.TerrainInfo;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import org.jetbrains.annotations.Nullable;

public class BiomeInfluencedNoiseSampler extends NoiseSampler {

    /**
     * Table of weights used to weight faraway biomes less than nearby biomes.
     */
    private static final int RADIUS = 2;
    private static final float[] BIOME_WEIGHT_TABLE = Util.make(new float[(int) Math.pow((RADIUS * 2) + 1, 2)], (array) -> {
        for(int x = -RADIUS; x <= RADIUS; ++x) {
            for(int z = -RADIUS; z <= RADIUS; ++z) {
                float weight = 10.0F / Mth.sqrt((float)(x * x + z * z) + 0.2F);
                array[x + RADIUS + (z + RADIUS) * 5] = weight;
            }
        }
    });

    private final NoiseSettings noiseSettings;
    private final NoiseChunk.InterpolatableNoise baseNoise;
    private final BlendedNoise blendedNoise;
    @Nullable
    private final SimplexNoise islandNoise;
    private final BiomeSource biomeSource;
    private final Registry<Biome> biomeRegistry;

    public BiomeInfluencedNoiseSampler(NoiseSettings noiseSettings, boolean bl, long l, Registry<NormalNoise.NoiseParameters> registry, WorldgenRandom.Algorithm algorithm, BiomeSource biomeSource, Registry<Biome> biomeRegistry) {
        super(noiseSettings, bl, l, registry, algorithm);
        this.noiseSettings = noiseSettings;
        this.biomeSource = biomeSource;
        this.biomeRegistry = biomeRegistry;
        this.baseNoise = noiseChunk -> ((NoiseChunkAccessor)noiseChunk).thebumblezone_callCreateNoiseInterpolator((ix, jx, kx) ->
                this.calculateBaseNoise(ix, jx, kx, noiseChunk.noiseData(QuartPos.fromBlock(ix), QuartPos.fromBlock(kx)).terrainInfo(), noiseChunk.getBlender()));
        if (noiseSettings.islandNoiseOverride()) {
            RandomSource randomSource = algorithm.newInstance(l);
            randomSource.consumeCount(17292);
            this.islandNoise = new SimplexNoise(randomSource);
        }
        else {
            this.islandNoise = null;
        }

        this.blendedNoise = new BlendedNoise(algorithm.newInstance(l), noiseSettings.noiseSamplingSettings(), noiseSettings.getCellWidth(), noiseSettings.getCellHeight());
    }

    private double calculateBaseNoise(int x, int y, int z, TerrainInfo terrainInfo, Blender blender) {
        double d = this.blendedNoise.calculateNoise(x, y, z);
        return this.calculateBaseNoise(x, y, z, terrainInfo, d, true, true, blender);
    }

    private double calculateBaseNoise(int x, int y, int z, TerrainInfo terrainInfo, double d, boolean bl, boolean bl2, Blender blender) {
        double e;
        if (this.islandNoise != null) {
            e = ((double) TheEndBiomeSource.getHeightValue(this.islandNoise, x / 8, z / 8) - 8.0) / 128.0;
        }
        else {
            double f = 0.0;
            double g = (this.computeBaseDensity(y, terrainInfo) + f) * terrainInfo.factor();
            e = g * (double)(g > 0.0 ? 4 : 1);
        }

        double f = e + d;
        double totalDepth;
        double m = -64.0;
        totalDepth = f;

        float totalScale = 0.0F;
        float totalHeight = 0.0F;
        float depth = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(this.biomeRegistry.getKey(
                        this.biomeSource.getNoiseBiome(x, 64, z, this))).orElse(new BzBiomeHeightRegistry.BiomeTerrain(1, 0)).depth();

        for(int weightX = -2; weightX <= 2; ++weightX) {
            for(int weightZ = -2; weightZ <= 2; ++weightZ) {
                BzBiomeHeightRegistry.BiomeTerrain biomeTerrain = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(this.biomeRegistry.getKey(
                        this.biomeSource.getNoiseBiome(x + weightX, 64, z + weightZ, this))).orElse(new BzBiomeHeightRegistry.BiomeTerrain(1, 0));
                float neighborDepth = biomeTerrain.depth();
                float neighborScale = biomeTerrain.scale();

                float contribution = neighborDepth > depth ? 0.5F : 1.0F;
                float weight = contribution * BIOME_WEIGHT_TABLE[weightX + 2 + (weightZ + 2) * 5] / (neighborDepth + 2.0F);
                totalScale += neighborScale * weight;
                totalDepth += neighborDepth * weight;
                totalHeight += weight;
            }
        }
        double finalScale = 1 + totalScale;
        double finalBiomeHeight = totalHeight * 20;

        double n = Math.max(totalDepth, m);
        n = this.applySlide(n, y / this.noiseSettings.getCellHeight());
        n = blender.blendDensity((int)(x * finalScale), y, (int)(z * finalScale), n) + finalBiomeHeight;
        return Mth.clamp(n, -64.0, 64.0);
    }

    private double computeBaseDensity(int i, TerrainInfo terrainInfo) {
        double d = 1.0 - (double)i / 128.0;
        return d + terrainInfo.offset();
    }

    private double applySlide(double d, int i) {
        int j = i - this.noiseSettings.getMinCellY();
        d = this.noiseSettings.topSlideSettings().applySlide(d, this.noiseSettings.getCellCountY() - j);
        return this.noiseSettings.bottomSlideSettings().applySlide(d, j);
    }

    protected NoiseChunk.BlockStateFiller makeBaseNoiseFiller(NoiseChunk noiseChunk, NoiseChunk.NoiseFiller noiseFiller, boolean bl) {
        NoiseChunk.Sampler sampler = this.baseNoise.instantiate(noiseChunk);
        return (i, j, k) -> {
            double d = sampler.sample();
            double e = Mth.clamp(d * 0.64, -1.0, 1.0);
            e = e / 2.0 - e * e * e / 24.0;
            e += noiseFiller.calculateNoise(i, j, k);
            return noiseChunk.aquifer().computeSubstance(i, j, k, d, e);
        };
    }

    protected NoiseChunk.BlockStateFiller makeOreVeinifier(NoiseChunk noiseChunk, boolean bl) {
            return (i, j, k) -> null;
//        if (!bl) {
//            return (i, j, k) -> null;
//        }
//        else {
//            NoiseChunk.Sampler sampler = this.veininess.instantiate(noiseChunk);
//            NoiseChunk.Sampler sampler2 = this.veinA.instantiate(noiseChunk);
//            NoiseChunk.Sampler sampler3 = this.veinB.instantiate(noiseChunk);
//            BlockState blockState = null;
//            return (i, j, k) -> {
//                RandomSource randomSource = this.oreVeinsPositionalRandomFactory.at(i, j, k);
//                double d = sampler.sample();
//                NoiseSampler.VeinType veinType = this.getVeinType(d, j);
//                if (veinType == null) {
//                    return blockState;
//                }
//                else if (randomSource.nextFloat() > 0.7F) {
//                    return blockState;
//                }
//                else if (this.isVein(sampler2.sample(), sampler3.sample())) {
//                    double e = Mth.clampedMap(Math.abs(d), 0.4F, 0.6F, 0.1F, 0.3F);
//                    if ((double)randomSource.nextFloat() < e && this.gapNoise.getValue((double)i, (double)j, (double)k) > -0.3F) {
//                        return randomSource.nextFloat() < 0.02F ? veinType.rawOreBlock : veinType.ore;
//                    }
//                    else {
//                        return veinType.filler;
//                    }
//                }
//                else {
//                    return blockState;
//                }
//            };
//        }
    }

    protected int getPreliminarySurfaceLevel(int i, int j, TerrainInfo terrainInfo) {
        for(int k = this.noiseSettings.getMinCellY() + this.noiseSettings.getCellCountY(); k >= this.noiseSettings.getMinCellY(); --k) {
            int l = k * this.noiseSettings.getCellHeight();
            double e = this.calculateBaseNoise(i, l, j, terrainInfo, -0.703125, true, false, Blender.empty());
            if (e > 0.390625) {
                return l;
            }
        }

        return Integer.MAX_VALUE;
    }

    protected Aquifer createAquifer(NoiseChunk noiseChunk, int i, int j, int k, int l, Aquifer.FluidPicker fluidPicker, boolean bl) {
        return Aquifer.createDisabled(fluidPicker);
    }
}
