package com.telepathicgrunt.the_bumblezone.world.features.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;

public class BiomeBasedLayerConfig implements FeatureConfiguration {
    public static final Codec<BiomeBasedLayerConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter((config) -> config.height),
            BlockState.CODEC.fieldOf("state").forGetter((config) -> config.state),
            ResourceLocation.CODEC.fieldOf("biome_resourcelocation").forGetter((config) -> config.biomeRL)
        ).apply(instance, BiomeBasedLayerConfig::new));
    public final int height;
    public final BlockState state;
    public final ResourceLocation biomeRL;

    public BiomeBasedLayerConfig(int height, BlockState state, ResourceLocation biomeRL) {
        this.height = height;
        this.state = state;
        this.biomeRL = biomeRL;
    }
}
