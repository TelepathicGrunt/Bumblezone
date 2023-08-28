package com.telepathicgrunt.the_bumblezone.world.features.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class TwoToneSpikeFeatureConfig implements FeatureConfiguration {
    public static final Codec<TwoToneSpikeFeatureConfig> CODEC = RecordCodecBuilder.create((configInstance) -> configInstance.group(
            TagKey.codec(Registries.BLOCK).fieldOf("allowed_base_block_copies").forGetter(nbtFeatureConfig -> nbtFeatureConfig.allowedBaseBlockCopies),
            BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("tip_blocks").forGetter(nbtFeatureConfig -> nbtFeatureConfig.tipBlocks),
            IntProvider.codec(0, 1000).fieldOf("height_range").forGetter(config -> config.heightRange)
    ).apply(configInstance, TwoToneSpikeFeatureConfig::new));

    public final TagKey<Block> allowedBaseBlockCopies;
    public final List<Block> tipBlocks;
    public final IntProvider heightRange;

    public TwoToneSpikeFeatureConfig(TagKey<Block> allowedBaseBlockCopies, List<Block> tipBlocks, IntProvider heightRange) {
        this.allowedBaseBlockCopies = allowedBaseBlockCopies;
        this.tipBlocks = tipBlocks;
        this.heightRange = heightRange;
    }
}
