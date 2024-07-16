package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record HoneyCompassTargetData(Optional<String> targetBlock, Optional<String> targetStructureTag, Optional<BlockPos> targetPos, Optional<ResourceKey<Level>> targetDimension) {
    public static final Codec<HoneyCompassTargetData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.STRING.optionalFieldOf("targetBlock").forGetter(HoneyCompassTargetData::targetBlock),
            Codec.STRING.optionalFieldOf("targetStructureTag").forGetter(HoneyCompassTargetData::targetStructureTag),
            BlockPos.CODEC.optionalFieldOf("targetPos").forGetter(HoneyCompassTargetData::targetPos),
            ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("targetDimension").forGetter(HoneyCompassTargetData::targetDimension)
    ).apply(instance, HoneyCompassTargetData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HoneyCompassTargetData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), HoneyCompassTargetData::targetBlock,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), HoneyCompassTargetData::targetStructureTag,
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC), HoneyCompassTargetData::targetPos,
            ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DIMENSION)), HoneyCompassTargetData::targetDimension,
            HoneyCompassTargetData::new);

    public HoneyCompassTargetData() {
        this(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    public Optional<Block> getStoredBlock() {
        return targetBlock.flatMap(block -> BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(block)));
    }

    public boolean isDifferent(Optional<String> targetBlock, Optional<String> targetStructureTag, Optional<BlockPos> targetPos, Optional<ResourceKey<Level>> targetDimension) {
        return !this.targetBlock().equals(targetBlock) ||
                !this.targetStructureTag().equals(targetStructureTag) ||
                !this.targetPos().equals(targetPos) ||
                !this.targetDimension().equals(targetDimension);
    }
}
