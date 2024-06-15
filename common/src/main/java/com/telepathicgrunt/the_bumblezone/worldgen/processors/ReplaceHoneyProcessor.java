package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class ReplaceHoneyProcessor extends StructureProcessor {

    private Fluid cachedAlternativeFluid = null;
    public static final MapCodec<ReplaceHoneyProcessor> CODEC = MapCodec.unit(ReplaceHoneyProcessor::new);

    public ReplaceHoneyProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        BlockState structureState = structureBlockInfoWorld.state();
        if (!structureState.getFluidState().isEmpty() && structureState.getFluidState().is(BzTags.BZ_HONEY_FLUID) && !BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid.isEmpty() && cachedAlternativeFluid != Fluids.EMPTY) {
            ResourceLocation newFluidRl = ResourceLocation.fromNamespaceAndPath(BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid);
            Fluid newFluid = !BuiltInRegistries.FLUID.getKey(cachedAlternativeFluid).equals(newFluidRl) ? BuiltInRegistries.FLUID.get(newFluidRl) : cachedAlternativeFluid;
            if (newFluid != null) {
                cachedAlternativeFluid = newFluid;
                BlockState fluidBlock = newFluid.defaultFluidState().createLegacyBlock();
                for (Property<?> property : structureState.getProperties()) {
                    if (fluidBlock.hasProperty(property)) {
                        fluidBlock = GeneralUtils.getStateWithProperty(fluidBlock, structureBlockInfoWorld.state(), property);
                    }
                }
                return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), fluidBlock, structureBlockInfoWorld.nbt());
            }
            else {
                cachedAlternativeFluid = Fluids.EMPTY;
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.FLUID_TICK_PROCESSOR.get();
    }
}