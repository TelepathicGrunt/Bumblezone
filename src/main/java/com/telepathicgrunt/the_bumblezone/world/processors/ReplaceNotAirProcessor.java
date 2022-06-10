package com.telepathicgrunt.the_bumblezone.world.processors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
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
            Registry.BLOCK.byNameCodec().listOf()
                    .xmap(Sets::newHashSet, Lists::newArrayList)
                    .optionalFieldOf("blocks_to_always_replace", new HashSet<>())
                    .forGetter((config) -> config.blocksToAlwaysReplace))
            .apply(instance, instance.stable(ReplaceNotAirProcessor::new)));

    public final HashSet<Block> blocksToAlwaysReplace;
    private ReplaceNotAirProcessor(HashSet<Block> blocksToAlwaysReplace) {
        this.blocksToAlwaysReplace = blocksToAlwaysReplace;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {

        if(blocksToAlwaysReplace.isEmpty() || blocksToAlwaysReplace.contains(structureBlockInfoWorld.state.getBlock())) {
            BlockPos position = structureBlockInfoWorld.pos;
            BlockState worldState = worldView.getBlockState(position);

            if (worldState.isAir()) {
                return null;
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.REPLACE_NOT_AIR_PROCESSOR.get();
    }
}
