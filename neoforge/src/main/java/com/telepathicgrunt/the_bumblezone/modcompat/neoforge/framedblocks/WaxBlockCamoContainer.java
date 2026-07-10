package com.telepathicgrunt.the_bumblezone.modcompat.neoforge.framedblocks;

import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import io.github.xfacthd.framedblocks.api.camo.block.AbstractBlockCamoContainer;
import io.github.xfacthd.framedblocks.api.camo.block.AbstractBlockCamoContainerFactory;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.Set;

/**
 * Container for camos made of {@link CarvableWax}
 */
final class WaxBlockCamoContainer extends AbstractBlockCamoContainer<WaxBlockCamoContainer> {

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

    WaxBlockCamoContainer(BlockState state) {
        super(state);
    }

    /**
     * {@return whether the camo state held by this camo container can be rotated with the framed screwdriver}
     */
    @Override
    public boolean canRotateCamo() {
        if (getState().getBlock() instanceof CarvableWax) {
            CarvableWax.Carving carving = getState().getValue(CarvableWax.CARVING);
            if (!ROTATABLE.contains(carving)) {
                return false;
            }
        }
        return super.canRotateCamo();
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != WaxBlockCamoContainer.class) {
            return false;
        }
        return content.equals(((WaxBlockCamoContainer) obj).content);
    }

    @Override
    public String toString() {
        return "WaxBlockCamoContainer{" + content + "}";
    }

    /**
     * {@return the camo container factory used to create and manage camo containers of this type}
     */
    @Override
    public AbstractBlockCamoContainerFactory<WaxBlockCamoContainer> getFactory() {
        return FramedBlocksCompat.WAX_BLOCK_CAMO_FACTORY.value();
    }
}
