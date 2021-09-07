package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

public class FluidTickProcessor extends StructureProcessor {

    public static final Codec<FluidTickProcessor> CODEC = Codec.unit(FluidTickProcessor::new);

    public FluidTickProcessor() { }


    @Override
    public Template.BlockInfo processBlock(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        BlockState structureState = structureBlockInfoWorld.state;
        if(!structureState.getFluidState().isEmpty() && structureBlockInfoWorld.pos.getY() > 0 && structureBlockInfoWorld.pos.getY() < worldView.getMaxBuildHeight()) {
            IChunk chunk = worldView.getChunk(structureBlockInfoWorld.pos);
            chunk.getLiquidTicks().scheduleTick(structureBlockInfoWorld.pos, structureState.getFluidState().getType(), 0);
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return BzProcessors.FLUID_TICK_PROCESSOR;
    }
}