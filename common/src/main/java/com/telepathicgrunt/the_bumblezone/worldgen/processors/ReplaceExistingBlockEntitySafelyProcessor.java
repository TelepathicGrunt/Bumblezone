package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * For mimicking the dungeon look where they cannot replace air.
 */
public class ReplaceExistingBlockEntitySafelyProcessor extends StructureProcessor {

    public static final MapCodec<ReplaceExistingBlockEntitySafelyProcessor> CODEC = MapCodec.unit(ReplaceExistingBlockEntitySafelyProcessor::new);

    public ReplaceExistingBlockEntitySafelyProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings settings) {
        if (GeneralUtils.isOutsideStructureAllowedBounds(settings, structureBlockInfoWorld.pos())) {
            return structureBlockInfoWorld;
        }

        if (!structureBlockInfoWorld.state().is(Blocks.STRUCTURE_VOID)) {
            BlockPos position = structureBlockInfoWorld.pos();
            BlockEntity blockEntity = levelReader.getBlockEntity(position);

            if (blockEntity != null) {
                levelReader.getChunk(position).removeBlockEntity(position);
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.REPLACE_EXISTING_BLOCKENTITY_SAFELY_PROCESSOR.get();
    }
}
