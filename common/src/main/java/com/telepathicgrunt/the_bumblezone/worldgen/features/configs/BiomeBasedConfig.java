package com.telepathicgrunt.the_bumblezone.worldgen.features.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BiomeBasedConfig implements FeatureConfiguration {
    public static final Codec<BiomeBasedConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        Biome.CODEC.fieldOf("biome").forGetter((config) -> config.biome)
    ).apply(instance, BiomeBasedConfig::new));

    public final Holder<Biome> biome;

    public BiomeBasedConfig(Holder<Biome> biome)
    {
        this.biome = biome;
    }
}
