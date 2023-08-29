package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * For preventing floating fluids
 */
public class CloseOffOutsideFluidsProcessor extends StructureProcessor {

    public static final Codec<CloseOffOutsideFluidsProcessor> CODEC = Codec.unit(CloseOffOutsideFluidsProcessor::new);
    private CloseOffOutsideFluidsProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        ChunkAccess cachedChunk = worldView.getChunk(structureBlockInfoWorld.pos());
        BlockPos worldPos = structureBlockInfoWorld.pos();

        if(structureBlockInfoWorld.state().isAir()) {
            BlockPos.MutableBlockPos sidePos = new BlockPos.MutableBlockPos();
            for(Direction direction : Direction.values()) {
                if(Direction.DOWN == direction) continue;

                sidePos.set(worldPos).move(direction);
                if(cachedChunk.getPos().x != sidePos.getX() >> 4 || cachedChunk.getPos().z != sidePos.getZ() >> 4)
                    cachedChunk = worldView.getChunk(sidePos);

                BlockState neighborState = cachedChunk.getBlockState(sidePos);
                if(neighborState.getFluidState().isSource()) {

                    if(!worldView.getBlockState(sidePos.below()).getFluidState().isEmpty()) {

                        // Copy what vanilla ores do.
                        // This bypasses the PaletteContainer's lock as it was throwing `Accessing PalettedContainer from multiple threads` crash
                        // even though everything seemed to be safe and fine.
                        int sectionYIndex = cachedChunk.getSectionIndex(sidePos.getY());
                        LevelChunkSection levelChunkSection = cachedChunk.getSection(sectionYIndex);
                        levelChunkSection.setBlockState(
                                SectionPos.sectionRelative(sidePos.getX()),
                                SectionPos.sectionRelative(sidePos.getY()),
                                SectionPos.sectionRelative(sidePos.getZ()),
                                BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(),
                                false);
                    }
                    else if(!worldView.isOutsideBuildHeight(sidePos)) {
                        ((LevelAccessor)worldView).scheduleTick(sidePos, neighborState.getFluidState().getType(), 0);
                    }
                }
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.CLOSE_OFF_OUTSIDE_FLUIDS_PROCESSOR.get();
    }
}
