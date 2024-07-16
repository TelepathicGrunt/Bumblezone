package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public record HoneyCrystalShieldDefinedLevelsData(List<Integer> shieldDurabilityBoostPerLevel) {
    public static final Codec<HoneyCrystalShieldDefinedLevelsData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.listOf().fieldOf("shieldDurabilityBoostPerLevel").forGetter(HoneyCrystalShieldDefinedLevelsData::shieldDurabilityBoostPerLevel)
    ).apply(instance, HoneyCrystalShieldDefinedLevelsData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HoneyCrystalShieldDefinedLevelsData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()), HoneyCrystalShieldDefinedLevelsData::shieldDurabilityBoostPerLevel,
            HoneyCrystalShieldDefinedLevelsData::new);

    public HoneyCrystalShieldDefinedLevelsData(int initialDurability) {
        this(List.of(initialDurability, 20, 45, 75, 110, 150, 195, 245, 316, 632));
    }

    public int maxLevel() {
        return shieldDurabilityBoostPerLevel.size();
    }

    /**
     * Param is actual levels starting at 1. Do not pass in 0.
     */
    public int getDurabilityForLevel(int level) {
        return shieldDurabilityBoostPerLevel.get(level - 1);
    }
}
