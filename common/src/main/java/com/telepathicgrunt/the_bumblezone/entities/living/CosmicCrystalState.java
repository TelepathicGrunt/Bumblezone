package com.telepathicgrunt.the_bumblezone.entities.living;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum CosmicCrystalState {
    NORMAL(0),
    TRACKING_SMASHING_ATTACK(1),
    TRACKING_SPINNING_ATTACK(2),
    VERTICAL_LASER(3),
    HORIZONTAL_LASER(4),
    SWEEP_LASER(5),
    TRACKING_LASER(6);
    
    public static final IntFunction<CosmicCrystalState> BY_ID = ByIdMap.continuous(CosmicCrystalState::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, CosmicCrystalState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CosmicCrystalState::id);
    private final int id;

    private CosmicCrystalState(final int j) {
        this.id = j;
    }

    public int id() {
        return this.id;
    }
}