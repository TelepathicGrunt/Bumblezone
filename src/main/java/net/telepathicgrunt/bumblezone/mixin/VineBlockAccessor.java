package net.telepathicgrunt.bumblezone.mixin;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.block.VineBlock;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VineBlock.class)
public interface VineBlockAccessor {

    @Accessor("UP_SHAPE")
    static VoxelShape getUP_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("NORTH_SHAPE")
    static VoxelShape getNORTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("EAST_SHAPE")
    static VoxelShape getEAST_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("SOUTH_SHAPE")
    static VoxelShape getSOUTH_SHAPE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("WEST_SHAPE")
    static VoxelShape getWEST_SHAPE() {
        throw new UnsupportedOperationException();
    }
}