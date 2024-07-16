package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record RagingEssenceTimerData(long empoweredTimestamp) {
    public static final Codec<RagingEssenceTimerData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.LONG.fieldOf("empoweredTimestamp").forGetter(RagingEssenceTimerData::empoweredTimestamp)
    ).apply(instance, RagingEssenceTimerData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RagingEssenceTimerData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, RagingEssenceTimerData::empoweredTimestamp,
            RagingEssenceTimerData::new);

    public RagingEssenceTimerData() {
        this(0L);
    }
}
