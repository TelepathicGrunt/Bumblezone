package com.telepathicgrunt.the_bumblezone.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record AbilityEssenceActivityData(boolean isInInventory, boolean isActive, boolean isLocked) {
    public static final Codec<AbilityEssenceActivityData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("isInInventory").forGetter(AbilityEssenceActivityData::isInInventory),
            Codec.BOOL.fieldOf("isActive").forGetter(AbilityEssenceActivityData::isActive),
            Codec.BOOL.fieldOf("isLocked").forGetter(AbilityEssenceActivityData::isLocked)
    ).apply(instance, AbilityEssenceActivityData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityEssenceActivityData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, AbilityEssenceActivityData::isInInventory,
            ByteBufCodecs.BOOL, AbilityEssenceActivityData::isActive,
            ByteBufCodecs.BOOL, AbilityEssenceActivityData::isLocked,
            AbilityEssenceActivityData::new);

    public AbilityEssenceActivityData() {
        this(false, false, false);
    }

    public boolean isDifferent(boolean isInInventory, boolean isActive, boolean isLocked) {
        return this.isInInventory() != isInInventory ||
                this.isActive() != isActive ||
                this.isLocked() != isLocked;
    }
}
