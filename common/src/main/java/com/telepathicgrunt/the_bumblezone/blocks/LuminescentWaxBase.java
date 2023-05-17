package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public interface LuminescentWaxBase {
    enum COLOR {
        RED,
        YELLOW,
        GREEN,
        BLUE,
        PURPLE,
        WHITE,
        NONE
    }

    default void applyEntityEffects(BlockState currentState, Entity collidingEntity) {

    }
}
