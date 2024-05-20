package com.telepathicgrunt.the_bumblezone.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record AbilityEssenceAbilityData(int abilityUseRemaining) {
    public static final Codec<AbilityEssenceAbilityData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("abilityUseRemaining").forGetter(AbilityEssenceAbilityData::abilityUseRemaining)
    ).apply(instance, AbilityEssenceAbilityData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityEssenceAbilityData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AbilityEssenceAbilityData::abilityUseRemaining,
            AbilityEssenceAbilityData::new);

    public AbilityEssenceAbilityData() {
        this(0);
    }
}
