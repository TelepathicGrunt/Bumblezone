package com.telepathicgrunt.bumblezone.world.processors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.HashSet;

/**
 * For mimicking the dungeon look where they cannot replace air.
 */
public class ReplaceNotAirProcessor extends StructureProcessor {

    public static final Codec<ReplaceNotAirProcessor> CODEC  = RecordCodecBuilder.create((instance) -> instance.group(
            BlockState.CODEC.listOf()
                    .xmap(Sets::newHashSet, Lists::newArrayList)
                    .optionalFieldOf("blocks_to_always_place", new HashSet<>())
                    .forGetter((config) -> config.blocksToAlwaysPlace))
            .apply(instance, instance.stable(ReplaceNotAirProcessor::new)));

    public final HashSet<BlockState> blocksToAlwaysPlace;
    private ReplaceNotAirProcessor(HashSet<BlockState> blocksToAlwaysPlace) {
        this.blocksToAlwaysPlace = blocksToAlwaysPlace;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {

        if(!blocksToAlwaysPlace.contains(structureBlockInfoWorld.state)) {
            BlockPos position = structureBlockInfoWorld.pos;
            BlockState worldState = worldView.getBlockState(position);

            if (worldState.isAir() &&
                !structureBlockInfoWorld.state.hasBlockEntity())
            {
                structureBlockInfoWorld = new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos, worldState, null);
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.REPLACE_NOT_AIR_PROCESSOR;
    }
}
