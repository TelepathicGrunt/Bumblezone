package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * For mimicking the dungeon look where they cannot replace air.
 */
public class ReplaceAirOnlyProcessor extends StructureProcessor {

    private static final ReplaceAirOnlyProcessor INSTANCE = new ReplaceAirOnlyProcessor();
    public static final Codec<ReplaceAirOnlyProcessor> CODEC = Codec.unit(INSTANCE);

    private ReplaceAirOnlyProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {

        BlockPos position = structureBlockInfoWorld.pos;
        BlockState worldState = worldView.getBlockState(position);
        if(worldState.isAir()) {
            return structureBlockInfoWorld;
        }
        return null;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.REPLACE_AIR_PROCESSOR;
    }
}
