package com.telepathicgrunt.the_bumblezone.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record StinglessBeeHelmetData(boolean hasBeeRider, long beeRiderStartTime) {
    public static final Codec<StinglessBeeHelmetData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("hasBeeRider").forGetter(StinglessBeeHelmetData::hasBeeRider),
            Codec.LONG.fieldOf("beeRiderStartTime").forGetter(StinglessBeeHelmetData::beeRiderStartTime)
    ).apply(instance, StinglessBeeHelmetData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StinglessBeeHelmetData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, StinglessBeeHelmetData::hasBeeRider,
            ByteBufCodecs.VAR_LONG, StinglessBeeHelmetData::beeRiderStartTime,
            StinglessBeeHelmetData::new);

    public StinglessBeeHelmetData() {
        this(false, -1);
    }

    public boolean isDifferent(boolean hasBeeRider, long beeRiderStartTime) {
        return this.hasBeeRider() != hasBeeRider || this.beeRiderStartTime() != beeRiderStartTime;
    }
}
