package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.SinglePoolElementAccessor;
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
    public double calculateNoise(int p_188452_, int p_188453_, int p_188454_) {
        double d0 = 0.0D;

        while(this.pieceIterator.hasNext()) {
            StructurePiece structurepiece = this.pieceIterator.next();
            BoundingBox boundingbox = structurepiece.getBoundingBox();
            int i = Math.max(0, Math.max(boundingbox.minX() - p_188452_, p_188452_ - boundingbox.maxX()));
            int j = p_188453_ - (boundingbox.minY() + (structurepiece instanceof PoolElementStructurePiece ? ((PoolElementStructurePiece)structurepiece).getGroundLevelDelta() : 0));
            int k = Math.max(0, Math.max(boundingbox.minZ() - p_188454_, p_188454_ - boundingbox.maxZ()));
            NoiseEffect noiseeffect = structurepiece.getNoiseEffect();
            if (noiseeffect == NoiseEffect.BURY ||
                (structurepiece instanceof PoolElementStructurePiece poolElementStructurePiece &&
                poolElementStructurePiece.getElement() instanceof SinglePoolElement singlePoolElement &&
                ((SinglePoolElementAccessor)singlePoolElement).getTemplate().left().orElseGet(() -> new ResourceLocation("")).getPath().equals(Bumblezone.MODID)))
            {
                d0 += getBuryContribution(i, j, k);
            }
            else if (noiseeffect == NoiseEffect.BEARD) {
                d0 += getBeardContribution(i, j, k) * 0.8D;
            }
        }

        this.pieceIterator.back(this.rigids.size());

        while(this.junctionIterator.hasNext()) {
            JigsawJunction jigsawjunction = this.junctionIterator.next();
            int l = p_188452_ - jigsawjunction.getSourceX();
            int i1 = p_188453_ - jigsawjunction.getSourceGroundY();
            int j1 = p_188454_ - jigsawjunction.getSourceZ();
            d0 += getBeardContribution(l, i1, j1) * 0.4D;
        }

        this.junctionIterator.back(this.junctions.size());
        return d0;
    }

}
