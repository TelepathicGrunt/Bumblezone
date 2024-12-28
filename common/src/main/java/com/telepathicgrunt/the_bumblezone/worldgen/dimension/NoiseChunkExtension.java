package com.telepathicgrunt.the_bumblezone.worldgen.dimension;

import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

public interface NoiseChunkExtension {

    void the_bumblezone$setBiomeSource(BiomeSource resolver);

    BiomeSource the_bumblezone$getBiomeSource();

    void the_bumblezone$setCachedClimateSampler(Climate.Sampler sampler);

    Climate.Sampler the_bumblezone$getCachedClimateSampler();

}
