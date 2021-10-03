package com.telepathicgrunt.bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
import net.minecraft.block.BlockState;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

/**
 * For preventing floating fluids
 */
public class CloseOffOutsideFluidsProcessor extends StructureProcessor {

    public static final Codec<CloseOffOutsideFluidsProcessor> CODEC = Codec.unit(CloseOffOutsideFluidsProcessor::new);
    private CloseOffOutsideFluidsProcessor() { }

    @Override
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfoLocal, Structure.StructureBlockInfo structureBlockInfoWorld, StructurePlacementData structurePlacementData) {
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(structureBlockInfoWorld.pos);
        Chunk cachedChunk = worldView.getChunk(mutable);
        BlockPos worldPos = structureBlockInfoWorld.pos;

        if(structureBlockInfoWorld.state.isAir()) {
            BlockPos.Mutable sidePos = new BlockPos.Mutable();
            for(Direction direction : Direction.values()) {
                if(Direction.DOWN == direction) continue;

                sidePos.set(worldPos).move(direction);
                BlockState neighborState = worldView.getBlockState(sidePos);
                if(neighborState.getFluidState().isStill()) {

                    if(cachedChunk.getPos().x != sidePos.getX() >> 4 || cachedChunk.getPos().z != sidePos.getZ() >> 4)
                        cachedChunk = worldView.getChunk(sidePos);

                    if(!worldView.getBlockState(sidePos.down()).getFluidState().isEmpty()) {

                        // Copy what vanilla ores do.
                        // This bypasses the PaletteContainer's lock as it was throwing `Accessing PalettedContainer from multiple threads` crash
                        // even though everything seemed to be safe and fine.
                        int sectionYIndex = cachedChunk.getSectionIndex(sidePos.getY());
                        ChunkSection levelChunkSection = cachedChunk.getSection(sectionYIndex);
                        if (ChunkSection.isEmpty(levelChunkSection)) continue;

                        levelChunkSection.setBlockState(
                                ChunkSectionPos.getLocalCoord(sidePos.getX()),
                                ChunkSectionPos.getLocalCoord(sidePos.getY()),
                                ChunkSectionPos.getLocalCoord(sidePos.getZ()),
                                BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(),
                                false);
                    }
                    else if(!worldView.isOutOfHeightLimit(sidePos)) {
                        cachedChunk.getFluidTickScheduler().schedule(sidePos, neighborState.getFluidState().getFluid(), 0);
                    }
                }
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.CLOSE_OFF_OUTSIDE_FLUIDS_PROCESSOR;
    }
}
