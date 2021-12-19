package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * For removing stuff like floating tall grass or kelp
 */
public class RemoveFloatingBlocksProcessor extends StructureProcessor {

    public static final Codec<RemoveFloatingBlocksProcessor> CODEC = Codec.unit(RemoveFloatingBlocksProcessor::new);
    private RemoveFloatingBlocksProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(structureBlockInfoWorld.pos);
        ChunkAccess cachedChunk = worldView.getChunk(mutable);

        // attempts to remove invalid floating plants
        if(structureBlockInfoWorld.state.isAir() || structureBlockInfoWorld.state.getMaterial().isLiquid()) {

            // set the block in the world so that canPlaceAt's result changes
            cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
            BlockState aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));

            // detects the invalidly placed blocks
            while(mutable.getY() < worldView.getHeight() && !aboveWorldState.canSurvive(worldView, mutable)) {
                cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
                aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));
            }
        }
        else if(!structureBlockInfoWorld.state.canSurvive(worldView, mutable)) {
            return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos, Blocks.CAVE_AIR.defaultBlockState(), null);
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.REMOVE_FLOATING_BLOCKS_PROCESSOR;
    }
}
