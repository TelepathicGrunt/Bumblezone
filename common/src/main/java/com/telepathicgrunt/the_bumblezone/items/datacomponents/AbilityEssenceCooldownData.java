package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record AbilityEssenceCooldownData(int cooldownTime, boolean forcedCooldown) {
    public static final Codec<AbilityEssenceCooldownData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("cooldownTime").forGetter(AbilityEssenceCooldownData::cooldownTime),
            Codec.BOOL.fieldOf("forcedCooldown").forGetter(AbilityEssenceCooldownData::forcedCooldown)
    ).apply(instance, AbilityEssenceCooldownData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityEssenceCooldownData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, AbilityEssenceCooldownData::cooldownTime,
            ByteBufCodecs.BOOL, AbilityEssenceCooldownData::forcedCooldown,
            AbilityEssenceCooldownData::new);

    public AbilityEssenceCooldownData() {
        this(0, false);
    }

    public boolean isDifferent(int cooldownTime, boolean forcedCooldown) {
        return this.cooldownTime() != cooldownTime ||
                this.forcedCooldown() != forcedCooldown;
    }
}
