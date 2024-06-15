package com.telepathicgrunt.the_bumblezone.worldgen.features.configs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class TreeDungeonFeatureConfig extends NbtFeatureConfig implements FeatureConfiguration {
    public static final Codec<TreeDungeonFeatureConfig> CODEC = RecordCodecBuilder.create((configInstance) -> configInstance.group(
            ResourceLocation.CODEC.fieldOf("tree_configured_feature").forGetter(nbtFeatureConfig -> nbtFeatureConfig.treeConfiguredFeature),
            ResourceLocation.CODEC.fieldOf("processors").forGetter(nbtFeatureConfig -> nbtFeatureConfig.processor),
            ResourceLocation.CODEC.fieldOf("post_processors").orElse(ResourceLocation.fromNamespaceAndPath("minecraft:empty")).forGetter(nbtFeatureConfig -> nbtFeatureConfig.postProcessor),
            Codec.mapPair(ResourceLocation.CODEC.fieldOf("resourcelocation"), ExtraCodecs.POSITIVE_INT.fieldOf("weight")).codec().listOf().fieldOf("nbt_entries").forGetter(nbtFeatureConfig -> nbtFeatureConfig.nbtResourcelocationsAndWeights),
            Codec.INT.fieldOf("structure_y_offset").orElse(0).forGetter(nbtFeatureConfig -> nbtFeatureConfig.structureYOffset)
    ).apply(configInstance, TreeDungeonFeatureConfig::new));

    public final ResourceLocation treeConfiguredFeature;

    public TreeDungeonFeatureConfig(ResourceLocation treeConfiguredFeature,
                                    ResourceLocation processor,
                                    ResourceLocation postProcessor,
                                    List<Pair<ResourceLocation, Integer>> nbtIdentifiersAndWeights,
                                    int structureYOffset)
    {
        super(processor, postProcessor, nbtIdentifiersAndWeights, structureYOffset);
        this.treeConfiguredFeature = treeConfiguredFeature;
    }
}
