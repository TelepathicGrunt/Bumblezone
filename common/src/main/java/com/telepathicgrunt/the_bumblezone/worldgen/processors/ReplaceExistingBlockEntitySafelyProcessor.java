package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * For mimicking the dungeon look where they cannot replace air.
 */
public class ReplaceExistingBlockEntitySafelyProcessor extends StructureProcessor {

    public static final Codec<ReplaceExistingBlockEntitySafelyProcessor> CODEC = Codec.unit(ReplaceExistingBlockEntitySafelyProcessor::new);

    public ReplaceExistingBlockEntitySafelyProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings settings) {
        if (GeneralUtils.isOutsideStructureAllowedBounds(settings, structureBlockInfoWorld.pos())) {
            return structureBlockInfoWorld;
        }

        BlockPos position = structureBlockInfoWorld.pos();
        BlockState blockState = levelReader.getBlockState(position);

        if (blockState.hasBlockEntity() && !blockState.is(structureBlockInfoWorld.state().getBlock())) {
            ChunkAccess chunkAccess = levelReader.getChunk(position);
            chunkAccess.removeBlockEntity(position);
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.REPLACE_EXISTING_BLOCKENTITY_SAFELY_PROCESSOR.get();
    }
}
