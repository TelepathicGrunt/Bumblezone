package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.StickyHoneyResidue;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class StickyHoneyResidueFeature extends Feature<NoneFeatureConfiguration> {

    public StickyHoneyResidueFeature(Codec<NoneFeatureConfiguration> configFactory) {
        super(configFactory);
    }

    private static final BlockState ALL_DIRECTION_RESIDUE = BzBlocks.STICKY_HONEY_RESIDUE.get().defaultBlockState()
            .setValue(StickyHoneyResidue.DOWN, true)
            .setValue(StickyHoneyResidue.UP, true)
            .setValue(StickyHoneyResidue.EAST, true)
            .setValue(StickyHoneyResidue.WEST, true)
            .setValue(StickyHoneyResidue.NORTH, true)
            .setValue(StickyHoneyResidue.SOUTH, true);
    /**
     * Place crystal block attached to a block if it is buried underground or underwater
     */
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        ChunkPos currentChunkPos = new ChunkPos(context.origin());
        BlockState originalBlockstate = context.level().getBlockState(context.origin());

        if (originalBlockstate.isAir()) {
            BlockState honeyResidue = ALL_DIRECTION_RESIDUE;
            if (honeyResidue.canSurvive(context.level(), context.origin())) {
                honeyResidue = honeyResidue.updateShape(Direction.DOWN, honeyResidue, context.level(), context.origin(), context.origin());

                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                for(Direction side : Direction.Plane.HORIZONTAL) {
                    mutableBlockPos.set(context.origin()).move(side);
                    if(!(new ChunkPos(mutableBlockPos).equals(currentChunkPos))) {
                        honeyResidue = honeyResidue.setValue(StickyHoneyResidue.FACING_TO_PROPERTY_MAP.get(side), false);
                    }
                }

                if(!StickyHoneyResidue.hasAtleastOneAttachment(honeyResidue)) {
                    return false;
                }

                context.level().setBlock(context.origin(), honeyResidue, 3);
                return true;
            }
        }

        return false;
    }
}