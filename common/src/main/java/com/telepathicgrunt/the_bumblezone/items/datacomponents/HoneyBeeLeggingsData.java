package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record HoneyBeeLeggingsData(boolean pollinated) {
    public static final Codec<HoneyBeeLeggingsData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("pollinated").forGetter(HoneyBeeLeggingsData::pollinated)
    ).apply(instance, HoneyBeeLeggingsData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HoneyBeeLeggingsData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, HoneyBeeLeggingsData::pollinated,
            HoneyBeeLeggingsData::new);

    public HoneyBeeLeggingsData() {
        this(false);
    }
}
