package com.telepathicgrunt.the_bumblezone.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CarpenterBeeBootsHangingData(long hangStartTime, long hangCooldownStartTime) {
    public static final Codec<CarpenterBeeBootsHangingData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.LONG.fieldOf("hangStartTime").forGetter(CarpenterBeeBootsHangingData::hangStartTime),
            Codec.LONG.fieldOf("hangCooldownStartTime").forGetter(CarpenterBeeBootsHangingData::hangCooldownStartTime)
    ).apply(instance, CarpenterBeeBootsHangingData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CarpenterBeeBootsHangingData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, CarpenterBeeBootsHangingData::hangStartTime,
            ByteBufCodecs.VAR_LONG, CarpenterBeeBootsHangingData::hangCooldownStartTime,
            CarpenterBeeBootsHangingData::new);

    public CarpenterBeeBootsHangingData() {
        this(0, -1);
    }

    public boolean isDifferent(long hangStartTime, long hangCooldownStartTime) {
        return this.hangStartTime() != hangStartTime ||
                this.hangCooldownStartTime() != hangCooldownStartTime;
    }
}
