package com.telepathicgrunt.the_bumblezone.modcompat.neoforge.framedblocks;

import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.camo.block.AbstractBlockCamoContainer;
import xfacthd.framedblocks.api.camo.block.AbstractBlockCamoContainerFactory;

import java.util.Set;

/**
 * Container for camos made of {@link CarvableWax}
 */
final class CarvableWaxBlockCamoContainer extends AbstractBlockCamoContainer<CarvableWaxBlockCamoContainer> {

    /**
     * Set of carvable wax patterns which change visually when rotated
     */
    private static final Set<CarvableWax.Carving> ROTATABLE = Set.of(
            CarvableWax.Carving.WAVY,
            CarvableWax.Carving.CHISELED,
            CarvableWax.Carving.BRICKS,
            CarvableWax.Carving.CHAINS,
            CarvableWax.Carving.MUSIC
    );

    CarvableWaxBlockCamoContainer(BlockState state) {
        super(state);
    }

    /**
     * {@return whether the camo state held by this camo container can be rotated with the framed screwdriver}
     */
    @Override
    public boolean canRotateCamo() {
        CarvableWax.Carving carving = getState().getValue(CarvableWax.CARVING);
        return ROTATABLE.contains(carving) && super.canRotateCamo();
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != CarvableWaxBlockCamoContainer.class) return false;
        return content.equals(((CarvableWaxBlockCamoContainer) obj).content);
    }

    @Override
    public String toString()
    {
        return "CarvableWaxBlockCamoContainer{" + content + "}";
    }

    /**
     * {@return the camo container factory used to create and manage camo containers of this type}
     */
    @Override
    public AbstractBlockCamoContainerFactory<CarvableWaxBlockCamoContainer> getFactory() {
        return FramedBlocksCompat.WAX_BLOCK_CAMO_FACTORY.value();
    }
}
