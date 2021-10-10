package com.telepathicgrunt.bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class FluidTickProcessor extends StructureProcessor {

    public static final Codec<FluidTickProcessor> CODEC = Codec.unit(FluidTickProcessor::new);

    public FluidTickProcessor() { }


    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        BlockState structureState = structureBlockInfoWorld.state;
        if(!structureState.getFluidState().isEmpty() && structureBlockInfoWorld.pos.getY() > worldView.getMinBuildHeight() && structureBlockInfoWorld.pos.getY() < worldView.getMaxBuildHeight()) {
            ChunkAccess chunk = worldView.getChunk(structureBlockInfoWorld.pos);
            chunk.getLiquidTicks().scheduleTick(structureBlockInfoWorld.pos, structureState.getFluidState().getType(), 0);
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.FLUID_TICK_PROCESSOR;
    }
}