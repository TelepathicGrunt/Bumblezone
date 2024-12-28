package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class FluidTickProcessor extends StructureProcessor {

    public static final Codec<FluidTickProcessor> CODEC = Codec.unit(FluidTickProcessor::new);

    public FluidTickProcessor() { }


    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings settings) {
        if (!structureBlockInfoWorld.state().getFluidState().isEmpty()) {
            if (GeneralUtils.isOutsideStructureAllowedBounds(settings, structureBlockInfoWorld.pos())) {
                return structureBlockInfoWorld;
            }

            if (structureBlockInfoWorld.pos().getY() > levelReader.getMinBuildHeight() && structureBlockInfoWorld.pos().getY() < levelReader.getMaxBuildHeight()) {
                ((LevelAccessor) levelReader).scheduleTick(structureBlockInfoWorld.pos(), structureBlockInfoWorld.state().getFluidState().getType(), 0);
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.FLUID_TICK_PROCESSOR.get();
    }
}