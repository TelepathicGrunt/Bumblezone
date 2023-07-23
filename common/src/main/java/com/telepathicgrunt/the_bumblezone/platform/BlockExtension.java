package com.telepathicgrunt.the_bumblezone.platform;

import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

/**
 * This class is used to provide the same methods as forge has in their IForgeBlock interface.
 * It may also include extra methods so make sure when you edit this class, you look at what forge has
 * in their IForgeBlock interface first.
 */
public interface BlockExtension {


    default BlockPathTypes bz$getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, Mob mob) {
        return state.getBlock() == Blocks.LAVA ? BlockPathTypes.LAVA : state.getBlock() == Blocks.FIRE ? BlockPathTypes.DAMAGE_FIRE : null;
    }

    default boolean bz$isStickyBlock(BlockState state) {
        return false;
    }

    default OptionalBoolean bz$canStickTo(BlockState state, BlockState other) {
        return OptionalBoolean.EMPTY;
    }

    default OptionalBoolean bz$shouldNotDisplayFluidOverlay() {
        return OptionalBoolean.EMPTY;
    }
}
