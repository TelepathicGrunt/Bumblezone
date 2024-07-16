package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record HoneyCompassBaseData(String compassType, Optional<String> customName, Optional<String> customDescription) {
    public static final Codec<HoneyCompassBaseData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.STRING.fieldOf("compassType").orElse("").forGetter(HoneyCompassBaseData::compassType),
            Codec.STRING.optionalFieldOf("customName").forGetter(HoneyCompassBaseData::customName),
            Codec.STRING.optionalFieldOf("customDescription").forGetter(HoneyCompassBaseData::customDescription)
    ).apply(instance, HoneyCompassBaseData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HoneyCompassBaseData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, HoneyCompassBaseData::compassType,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), HoneyCompassBaseData::customName,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), HoneyCompassBaseData::customDescription,
            HoneyCompassBaseData::new);

    public HoneyCompassBaseData() {
        this("", Optional.empty(), Optional.empty());
    }

    public boolean isBlockCompass() {
        return compassType.equals("block");
    }

    public boolean isStructureCompass() {
        return compassType.equals("structure");
    }

    public boolean isDifferent(String compassType, Optional<String> customName, Optional<String> customDescription) {
        return !this.compassType().equals(compassType) ||
                !this.customName().equals(customName) ||
                !this.customDescription().equals(customDescription);
    }
}
