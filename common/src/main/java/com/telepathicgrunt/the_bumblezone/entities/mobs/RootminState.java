package com.telepathicgrunt.the_bumblezone.entities.mobs;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum RootminState {
    NONE(0),
    ANGRY(1),
    CURIOUS(2),
    CURSE(3),
    EMBARRASSED(4),
    SHOCK(5),
    SHOOT(6),
    RUN(7),
    WALK(8),
    ENTITY_TO_BLOCK(9),
    BLOCK_TO_ENTITY(10);

    public static final IntFunction<RootminState> BY_ID = ByIdMap.continuous(RootminState::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, RootminState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, RootminState::id);
    private final int id;

    private RootminState(final int j) {
        this.id = j;
    }

    public int id() {
        return this.id;
    }
}