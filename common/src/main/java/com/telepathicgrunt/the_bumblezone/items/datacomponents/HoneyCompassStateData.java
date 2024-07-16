package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;
import java.util.UUID;

public record HoneyCompassStateData(boolean locked, Optional<UUID> searchId, boolean isLoading, boolean isFailed, boolean locatedSpecialStructure) {
    public static final Codec<HoneyCompassStateData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("locked").orElse(false).forGetter(HoneyCompassStateData::locked),
            UUIDUtil.CODEC.optionalFieldOf("searchId").forGetter(HoneyCompassStateData::searchId),
            Codec.BOOL.fieldOf("isLoading").orElse(false).forGetter(HoneyCompassStateData::isLoading),
            Codec.BOOL.fieldOf("isFailed").orElse(false).forGetter(HoneyCompassStateData::isFailed),
            Codec.BOOL.fieldOf("locatedSpecialStructure").orElse(false).forGetter(HoneyCompassStateData::locatedSpecialStructure)
    ).apply(instance, HoneyCompassStateData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HoneyCompassStateData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, HoneyCompassStateData::locked,
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), HoneyCompassStateData::searchId,
            ByteBufCodecs.BOOL, HoneyCompassStateData::isLoading,
            ByteBufCodecs.BOOL, HoneyCompassStateData::isFailed,
            ByteBufCodecs.BOOL, HoneyCompassStateData::locatedSpecialStructure,
            HoneyCompassStateData::new);

    public HoneyCompassStateData() {
        this(false, Optional.empty(), false, false, false);
    }

    public boolean isDifferent(boolean locked, Optional<UUID> searchId, boolean isLoading, boolean isFailed, boolean locatedSpecialStructure) {
        return this.locked() != locked ||
                !this.searchId().equals(searchId) ||
                this.isLoading() != isLoading ||
                this.isFailed() != isFailed ||
                this.locatedSpecialStructure() != locatedSpecialStructure;
    }
}
