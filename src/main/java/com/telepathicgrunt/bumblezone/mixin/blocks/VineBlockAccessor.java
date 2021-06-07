package com.telepathicgrunt.bumblezone.mixin.blocks;

import net.minecraft.block.VineBlock;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VineBlock.class)
public interface VineBlockAccessor {

    @Accessor("UP_SHAPE")
    static VoxelShape thebumblezone_getUP_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("NORTH_SHAPE")
    static VoxelShape thebumblezone_getNORTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("EAST_SHAPE")
    static VoxelShape thebumblezone_getEAST_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("SOUTH_SHAPE")
    static VoxelShape thebumblezone_getSOUTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("WEST_SHAPE")
    static VoxelShape thebumblezone_getWEST_SHAPE() {
        throw new UnsupportedOperationException();
    }
}