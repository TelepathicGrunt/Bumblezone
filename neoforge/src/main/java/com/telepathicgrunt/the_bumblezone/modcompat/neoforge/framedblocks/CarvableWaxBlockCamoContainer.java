package com.telepathicgrunt.the_bumblezone.modcompat.neoforge.framedblocks;

import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.camo.block.AbstractBlockCamoContainer;
import xfacthd.framedblocks.api.camo.block.AbstractBlockCamoContainerFactory;

import java.util.Set;

final class CarvableWaxBlockCamoContainer extends AbstractBlockCamoContainer<CarvableWaxBlockCamoContainer> {

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
    public AbstractBlockCamoContainerFactory<CarvableWaxBlockCamoContainer> getFactory() {
        return FramedBlocksCompat.WAX_BLOCK_CAMO_FACTORY.value();
    }
}
