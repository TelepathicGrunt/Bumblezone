package com.telepathicgrunt.the_bumblezone.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public record BumbleBeeChestplateData(boolean isFlying, int flyCounter, Optional<Integer> forcedMaxFlyingTickTime, Optional<Integer> requiredWearablesCountForForcedFlyingTime) {
    public static final Codec<BumbleBeeChestplateData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("isFlying").forGetter(BumbleBeeChestplateData::isFlying),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("flyCounter").forGetter(BumbleBeeChestplateData::flyCounter),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("forcedMaxFlyingTickTime").forGetter(BumbleBeeChestplateData::forcedMaxFlyingTickTime),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("requiredWearablesCountForForcedFlyingTime").forGetter(BumbleBeeChestplateData::requiredWearablesCountForForcedFlyingTime)
    ).apply(instance, BumbleBeeChestplateData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BumbleBeeChestplateData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, BumbleBeeChestplateData::isFlying,
            ByteBufCodecs.VAR_INT, BumbleBeeChestplateData::flyCounter,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), BumbleBeeChestplateData::forcedMaxFlyingTickTime,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), BumbleBeeChestplateData::requiredWearablesCountForForcedFlyingTime,
            BumbleBeeChestplateData::new);

    public BumbleBeeChestplateData() {
        this(false, 0, Optional.empty(), Optional.empty());
    }

    public boolean isDifferent(boolean isFlying, int flyCounter, Optional<Integer> forcedMaxFlyingTickTime, Optional<Integer> requiredWearablesCountForForcedFlyingTime) {
        return this.isFlying() != isFlying ||
                this.flyCounter() != flyCounter ||
                !this.forcedMaxFlyingTickTime().equals(forcedMaxFlyingTickTime) ||
                !this.requiredWearablesCountForForcedFlyingTime().equals(requiredWearablesCountForForcedFlyingTime);
    }
}
