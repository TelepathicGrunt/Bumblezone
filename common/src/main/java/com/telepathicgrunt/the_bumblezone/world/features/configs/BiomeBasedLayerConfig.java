package com.telepathicgrunt.the_bumblezone.world.features.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.Optional;

public class BiomeBasedLayerConfig implements FeatureConfiguration {
    public static final Codec<BiomeBasedLayerConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter((config) -> config.height),
            BlockState.CODEC.fieldOf("state").forGetter((config) -> config.state),
            BlockState.CODEC.optionalFieldOf("rare_state").forGetter((config) -> config.rareState),
            Codec.floatRange(0, 1).fieldOf("rare_state_chance").orElse(0F).forGetter((config) -> config.rareStateChance),
            ResourceLocation.CODEC.optionalFieldOf("suspicious_block_loot").forGetter((config) -> config.suspiciousBlockLoot),
            ResourceLocation.CODEC.fieldOf("biome_resourcelocation").forGetter((config) -> config.biomeRL)
        ).apply(instance, BiomeBasedLayerConfig::new));

    public final int height;
    public final BlockState state;
    public final Optional<BlockState> rareState;
    public final float rareStateChance;
    public final Optional<ResourceLocation> suspiciousBlockLoot;
    public final ResourceLocation biomeRL;

    public BiomeBasedLayerConfig(
            int height,
            BlockState state,
            Optional<BlockState> rareState,
            float rareStateChance,
            Optional<ResourceLocation> suspiciousBlockLoot,
            ResourceLocation biomeRL)
    {
        this.height = height;
        this.state = state;
        this.rareState = rareState;
        this.rareStateChance = rareStateChance;
        this.suspiciousBlockLoot = suspiciousBlockLoot;
        this.biomeRL = biomeRL;
    }
}
