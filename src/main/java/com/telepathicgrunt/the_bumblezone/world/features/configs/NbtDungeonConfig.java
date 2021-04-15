package com.telepathicgrunt.the_bumblezone.world.features.configs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.List;

public class NbtDungeonConfig implements IFeatureConfig {
    public static final Codec<NbtDungeonConfig> CODEC = RecordCodecBuilder.<NbtDungeonConfig>create((configInstance) -> configInstance.group(
            Codec.INT.fieldOf("structure_y_offset").orElse(0).forGetter(nbtFeatureConfig -> nbtFeatureConfig.structureYOffset),
            ResourceLocation.CODEC.fieldOf("rs_spawner_resourcelocation").forGetter(nbtDungeonConfig -> nbtDungeonConfig.rsSpawnerResourcelocation),
            ResourceLocation.CODEC.fieldOf("processors").forGetter(nbtDungeonConfig -> nbtDungeonConfig.processor),
            Codec.mapPair(ResourceLocation.CODEC.fieldOf("resourcelocation"), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight")).codec().listOf().fieldOf("dungeon_nbt_entries").forGetter(nbtFeatureConfig -> nbtFeatureConfig.nbtResourcelocationsAndWeights)
    ).apply(configInstance, NbtDungeonConfig::new));

    public final List<Pair<ResourceLocation, Integer>> nbtResourcelocationsAndWeights;
    public final ResourceLocation rsSpawnerResourcelocation;
    public final ResourceLocation processor;
    public final int structureYOffset;

    public NbtDungeonConfig(int structureYOffset, ResourceLocation rsSpawnerResourceLocation, ResourceLocation processor, List<Pair<ResourceLocation, Integer>> nbtResourceLocationsAndWeights) {
        this.nbtResourcelocationsAndWeights = nbtResourceLocationsAndWeights;
        this.rsSpawnerResourcelocation = rsSpawnerResourceLocation;
        this.processor = processor;
        this.structureYOffset = structureYOffset;
    }
}
