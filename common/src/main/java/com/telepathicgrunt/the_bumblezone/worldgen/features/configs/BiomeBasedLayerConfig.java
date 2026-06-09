package com.telepathicgrunt.the_bumblezone.worldgen.features.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
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
            Identifier.CODEC.optionalFieldOf("suspicious_block_loot").forGetter((config) -> config.suspiciousBlockLoot),
            Biome.CODEC.fieldOf("biome").forGetter((config) -> config.biome),
            TagKey.codec(Registries.BIOME).fieldOf("biome_tag_to_not_fuzz_into").forGetter((config) -> config.biomeTagToNotFuzzInto)
        ).apply(instance, BiomeBasedLayerConfig::new));

    public final int height;
    public final BlockState state;
    public final Optional<BlockState> rareState;
    public final float rareStateChance;
    public final Optional<Identifier> suspiciousBlockLoot;
    public final Holder<Biome> biome;
    public final TagKey<Biome> biomeTagToNotFuzzInto;

    public BiomeBasedLayerConfig(
            int height,
            BlockState state,
            Optional<BlockState> rareState,
            float rareStateChance,
            Optional<Identifier> suspiciousBlockLoot,
            Holder<Biome> biome,
            TagKey<Biome> biomeTagToNotFuzzInto)
    {
        this.height = height;
        this.state = state;
        this.rareState = rareState;
        this.rareStateChance = rareStateChance;
        this.suspiciousBlockLoot = suspiciousBlockLoot;
        this.biome = biome;
        this.biomeTagToNotFuzzInto = biomeTagToNotFuzzInto;
    }
}
