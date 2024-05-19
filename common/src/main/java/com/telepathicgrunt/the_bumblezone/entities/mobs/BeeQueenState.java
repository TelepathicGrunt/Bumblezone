package com.telepathicgrunt.the_bumblezone.entities.mobs;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum BeeQueenState {
    NONE(0),
    ATTACKING(1),
    ITEM_THROW(2),
    ITEM_REJECT(3);

    public static final IntFunction<BeeQueenState> BY_ID = ByIdMap.continuous(BeeQueenState::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, BeeQueenState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, BeeQueenState::id);
    private final int id;

    private BeeQueenState(final int j) {
        this.id = j;
    }

    public int id() {
        return this.id;
    }
}