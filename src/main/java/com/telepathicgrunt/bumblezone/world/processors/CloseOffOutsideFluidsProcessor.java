package com.telepathicgrunt.bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
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
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(structureBlockInfoWorld.pos);
        ChunkAccess cachedChunk = worldView.getChunk(mutable);
        BlockPos worldPos = structureBlockInfoWorld.pos;

        if(structureBlockInfoWorld.state.isAir()) {
            BlockPos.MutableBlockPos sidePos = new BlockPos.MutableBlockPos();
            for(Direction direction : Direction.values()) {
                if(Direction.DOWN == direction) continue;

                sidePos.set(worldPos).move(direction);
                BlockState neighborState = worldView.getBlockState(sidePos);
                if(neighborState.getFluidState().isSource()) {

                    if(cachedChunk.getPos().x != sidePos.getX() >> 4 || cachedChunk.getPos().z != sidePos.getZ() >> 4)
                        cachedChunk = worldView.getChunk(sidePos);

                    if(!worldView.getBlockState(sidePos.below()).getFluidState().isEmpty()) {

                        // Copy what vanilla ores do.
                        // This bypasses the PaletteContainer's lock as it was throwing `Accessing PalettedContainer from multiple threads` crash
                        // even though everything seemed to be safe and fine.
                        int sectionYIndex = cachedChunk.getSectionIndex(sidePos.getY());
                        LevelChunkSection levelChunkSection = cachedChunk.getSection(sectionYIndex);
                        if (levelChunkSection == null) continue;

                        levelChunkSection.setBlockState(
                                SectionPos.sectionRelative(sidePos.getX()),
                                SectionPos.sectionRelative(sidePos.getY()),
                                SectionPos.sectionRelative(sidePos.getZ()),
                                BzBlocks.FILLED_POROUS_HONEYCOMB.defaultBlockState(),
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
        return BzProcessors.CLOSE_OFF_OUTSIDE_FLUIDS_PROCESSOR;
    }
}
