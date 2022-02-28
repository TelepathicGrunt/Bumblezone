package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.mixin.MaterialRuleListAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.world.NoiseChunkAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBiomeHeightRegistry;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.blending.Blender;
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


    public BiomeInfluencedNoiseSampler(int i, int j, int k, NoiseRouter noiseRouter, int l, int m, DensityFunctions.BeardifierOrMarker beardifierOrMarker, NoiseGeneratorSettings noiseGeneratorSettings, Aquifer.FluidPicker fluidPicker, Blender blender, BiomeSource biomeSource, Registry<Biome> biomeRegistry) {
        super(i, j, k, noiseRouter, l, m, beardifierOrMarker, noiseGeneratorSettings, fluidPicker, blender);
        ((MaterialRuleListAccessor)((NoiseChunkAccessor)this).getBlockStateRule()).getMaterialRuleList().add(new BiomeNoiseStateFiller(biomeSource, biomeRegistry));
    }

    public static class BiomeNoiseStateFiller implements NoiseChunk.BlockStateFiller {
        private final BiomeSource biomeSource;
        private final Registry<Biome> biomeRegistry;

        public BiomeNoiseStateFiller(BiomeSource biomeSource, Registry<Biome> biomeRegistry) {
            this.biomeSource = biomeSource;
            this.biomeRegistry = biomeRegistry;
        }

        @Nullable
        @Override
        public BlockState calculate(DensityFunction.FunctionContext functionContext) {
            functionContext.getBlender().blendDensity()

            return null;
        }


        private double calculateBaseNoise(int x, int y, int z, Climate.Sampler sampler, double d, boolean bl2, Blender blender) {

            BzBiomeHeightRegistry.BiomeTerrain centerBiomeInfo = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(this.biomeRegistry.getKey(
                    this.biomeSource.getNoiseBiome(x >> 2, 40, z >> 2, sampler).value())).orElse(new BzBiomeHeightRegistry.BiomeTerrain(4, 1));

            float totalHeight = 0.0F;
            for(int xOffset = -2; xOffset <= 2; ++xOffset) {
                for(int zOffset = -2; zOffset <= 2; ++zOffset) {
                    BzBiomeHeightRegistry.BiomeTerrain biomeTerrain = BzBiomeHeightRegistry.BIOME_HEIGHT_REGISTRY.getOptional(this.biomeRegistry.getKey(
                            this.biomeSource.getNoiseBiome((x >> 2) + xOffset, 40, (z >> 2) + zOffset, sampler).value())).orElse(new BzBiomeHeightRegistry.BiomeTerrain(4, 1));
                    float biomeDepth = biomeTerrain.depth;
                    float weight = BIOME_WEIGHT_TABLE[xOffset + 2 + (zOffset + 2) * 5];
                    if(biomeDepth != centerBiomeInfo.depth) {
                        biomeDepth = Mth.lerp(centerBiomeInfo.weightModifier, biomeDepth, centerBiomeInfo.depth);
                    }

                    totalHeight += (biomeDepth * weight);
                }
            }

            return totalHeight / 400f;
        }
    }
}
