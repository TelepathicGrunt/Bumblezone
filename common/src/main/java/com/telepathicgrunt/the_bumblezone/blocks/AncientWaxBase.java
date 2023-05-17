package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;


public interface AncientWaxBase {

    default void applyEntityEffects(BlockState currentState, Entity collidingEntity) {

    }
}
