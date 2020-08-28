package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.VineBlock;
import net.minecraft.util.math.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VineBlock.class)
public interface VineBlockAccessor {

    @Accessor("UP_AABB")
    static VoxelShape getUP_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("NORTH_AABB")
    static VoxelShape getNORTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("EAST_AABB")
    static VoxelShape getEAST_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("SOUTH_AABB")
    static VoxelShape getSOUTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("WEST_AABB")
    static VoxelShape getWEST_SHAPE() {
        throw new UnsupportedOperationException();
    }
}