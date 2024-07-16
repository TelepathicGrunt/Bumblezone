package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record AbilityEssenceLastChargeData(long lastChargeTime) {
    public static final Codec<AbilityEssenceLastChargeData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.LONG.fieldOf("lastChargeTime").forGetter(AbilityEssenceLastChargeData::lastChargeTime)
    ).apply(instance, AbilityEssenceLastChargeData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityEssenceLastChargeData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, AbilityEssenceLastChargeData::lastChargeTime,
            AbilityEssenceLastChargeData::new);

    public AbilityEssenceLastChargeData() {
        this(0);
    }
}
