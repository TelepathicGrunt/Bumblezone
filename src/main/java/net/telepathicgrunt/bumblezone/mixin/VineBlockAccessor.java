package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.VineBlock;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VineBlock.class)
public interface VineBlockAccessor {

    @Accessor("UP_SHAPE")
    static VoxelShape bz_getUP_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("NORTH_SHAPE")
    static VoxelShape bz_getNORTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("EAST_SHAPE")
    static VoxelShape bz_getEAST_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("SOUTH_SHAPE")
    static VoxelShape bz_getSOUTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("WEST_SHAPE")
    static VoxelShape bz_getWEST_SHAPE() {
        throw new UnsupportedOperationException();
    }
}