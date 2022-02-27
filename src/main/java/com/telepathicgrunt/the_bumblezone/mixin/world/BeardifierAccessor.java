package com.telepathicgrunt.the_bumblezone.mixin.world;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Beardifier.class)
public interface BeardifierAccessor {
    @Accessor("rigids")
    ObjectList<StructurePiece> getRigids();

    @Accessor("junctions")
    ObjectList<JigsawJunction> getJunctions();

    @Accessor("pieceIterator")
    ObjectListIterator<StructurePiece> getPieceIterator();

    @Invoker("getBuryContribution")
    static double callGetBuryContribution(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }

    @Invoker("getBeardContribution")
    static double callGetBeardContribution(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }

    @Accessor("junctionIterator")
    ObjectListIterator<JigsawJunction> getJunctionIterator();
}
