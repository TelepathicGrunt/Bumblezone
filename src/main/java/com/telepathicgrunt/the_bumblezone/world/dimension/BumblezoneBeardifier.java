package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.BeardifierAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.SinglePoolElementAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.levelgen.feature.structures.JigsawJunction;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public class BumblezoneBeardifier extends Beardifier {
    public BumblezoneBeardifier(StructureFeatureManager structureFeatureManager, ChunkAccess chunk) {
        super(structureFeatureManager, chunk);
    }

    @Override
    public double calculateNoise(int x, int y, int z) {
        double noiseResult = 0.0D;

        while(((BeardifierAccessor)this).getPieceIterator().hasNext()) {
            StructurePiece structurepiece = ((BeardifierAccessor)this).getPieceIterator().next();
            BoundingBox boundingbox = structurepiece.getBoundingBox();
            int x2 = Math.max(0, Math.max(boundingbox.minX() - x, x - boundingbox.maxX()));
            int y2 = y - (boundingbox.minY() + (structurepiece instanceof PoolElementStructurePiece ? ((PoolElementStructurePiece)structurepiece).getGroundLevelDelta() : 0));
            int z2 = Math.max(0, Math.max(boundingbox.minZ() - z, z - boundingbox.maxZ()));
            NoiseEffect noiseeffect = structurepiece.getNoiseEffect();
            boolean isBzStructure = (structurepiece instanceof PoolElementStructurePiece poolElementStructurePiece &&
                    poolElementStructurePiece.getElement() instanceof SinglePoolElement singlePoolElement &&
                    ((SinglePoolElementAccessor)singlePoolElement).getTemplate().left().orElseGet(() -> new ResourceLocation("")).getNamespace().equals(Bumblezone.MODID));

            if (noiseeffect == NoiseEffect.BURY || isBzStructure)
            {
                noiseResult += BeardifierAccessor.callGetBuryContribution(x2, y2, z2);
            }
            else if (noiseeffect == NoiseEffect.BEARD) {
                noiseResult += BeardifierAccessor.callGetBeardContribution(x2, y2, z2) * 0.8D;
            }
        }

        ((BeardifierAccessor)this).getPieceIterator().back(((BeardifierAccessor)this).getRigids().size());

        while(((BeardifierAccessor)this).getJunctionIterator().hasNext()) {
            JigsawJunction jigsawjunction = ((BeardifierAccessor)this).getJunctionIterator().next();
            int x3 = x - jigsawjunction.getSourceX();
            int y3 = y - jigsawjunction.getSourceGroundY();
            int z3 = z - jigsawjunction.getSourceZ();
            noiseResult += BeardifierAccessor.callGetBeardContribution(x3, y3, z3) * 0.4D;
        }

        ((BeardifierAccessor)this).getJunctionIterator().back(((BeardifierAccessor)this).getJunctions().size());
        return noiseResult;
    }

}
