package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VineBlock.class)
public interface VineBlockAccessor {

    @Accessor("UP_AABB")
    static VoxelShape thebumblezone_getUP_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("NORTH_AABB")
    static VoxelShape thebumblezone_getNORTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("EAST_AABB")
    static VoxelShape thebumblezone_getEAST_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("SOUTH_AABB")
    static VoxelShape thebumblezone_getSOUTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("WEST_AABB")
    static VoxelShape thebumblezone_getWEST_SHAPE() {
        throw new UnsupportedOperationException();
    }
}