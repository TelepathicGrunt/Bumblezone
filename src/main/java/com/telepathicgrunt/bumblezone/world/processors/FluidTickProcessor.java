package com.telepathicgrunt.bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
import net.minecraft.block.BlockState;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;

public class FluidTickProcessor extends StructureProcessor {

    public static final Codec<FluidTickProcessor> CODEC = Codec.unit(FluidTickProcessor::new);

    public FluidTickProcessor() { }


    @Override
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfoLocal, Structure.StructureBlockInfo structureBlockInfoWorld, StructurePlacementData structurePlacementData) {
        BlockState structureState = structureBlockInfoWorld.state;
        if(!structureState.getFluidState().isEmpty() && structureBlockInfoWorld.pos.getY() > worldView.getBottomY() && structureBlockInfoWorld.pos.getY() < worldView.getTopY()) {
            Chunk chunk = worldView.getChunk(structureBlockInfoWorld.pos);
            chunk.getFluidTickScheduler().schedule(structureBlockInfoWorld.pos, structureState.getFluidState().getFluid(), 0);
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.FLUID_TICK_PROCESSOR;
    }
}