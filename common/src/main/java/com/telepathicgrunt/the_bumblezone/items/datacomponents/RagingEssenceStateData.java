package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record RagingEssenceStateData(short rageStateLevel) {
    public static final Codec<RagingEssenceStateData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.SHORT.fieldOf("rageStateLevel").forGetter(RagingEssenceStateData::rageStateLevel)
    ).apply(instance, RagingEssenceStateData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RagingEssenceStateData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.SHORT, RagingEssenceStateData::rageStateLevel,
            RagingEssenceStateData::new);

    public RagingEssenceStateData() {
        this((short)0);
    }
}
