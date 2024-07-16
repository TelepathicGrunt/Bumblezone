package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record HoneyCrystalShieldCurrentLevelData(int currentLevel) {
    public static final Codec<HoneyCrystalShieldCurrentLevelData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("currentLevel").orElse(1).forGetter(HoneyCrystalShieldCurrentLevelData::currentLevel)
    ).apply(instance, HoneyCrystalShieldCurrentLevelData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HoneyCrystalShieldCurrentLevelData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, HoneyCrystalShieldCurrentLevelData::currentLevel,
            HoneyCrystalShieldCurrentLevelData::new);

    public HoneyCrystalShieldCurrentLevelData() {
        this(1);
    }
}
