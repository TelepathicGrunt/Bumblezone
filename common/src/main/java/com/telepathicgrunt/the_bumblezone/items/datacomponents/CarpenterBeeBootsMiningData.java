package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CarpenterBeeBootsMiningData(int itemStackId, int lastSentState, int miningStartTime) {
    public static final Codec<CarpenterBeeBootsMiningData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("itemStackId").forGetter(CarpenterBeeBootsMiningData::itemStackId),
            Codec.INT.fieldOf("lastSentState").forGetter(CarpenterBeeBootsMiningData::lastSentState),
            Codec.INT.fieldOf("miningStartTime").forGetter(CarpenterBeeBootsMiningData::miningStartTime)
    ).apply(instance, CarpenterBeeBootsMiningData::new));

    public CarpenterBeeBootsMiningData() {
        this(0, 0, 0);
    }

    public boolean isDifferent(int itemstackId, int lastSentState, int miningStartTime) {
        return this.itemStackId() != itemstackId ||
                this.lastSentState() != lastSentState ||
                this.miningStartTime() != miningStartTime;
    }
}
