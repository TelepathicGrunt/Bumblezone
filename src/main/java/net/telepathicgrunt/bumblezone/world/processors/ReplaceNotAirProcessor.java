package net.telepathicgrunt.bumblezone.world.processors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.telepathicgrunt.bumblezone.modinit.BzProcessors;

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
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfoLocal, Structure.StructureBlockInfo structureBlockInfoWorld, StructurePlacementData structurePlacementData) {

        if(!blocksToAlwaysPlace.contains(structureBlockInfoWorld.state)){
            BlockPos position = structureBlockInfoWorld.pos;
            BlockState worldState = worldView.getBlockState(position);

            if (worldState.isAir() &&
                !structureBlockInfoWorld.state.getBlock().hasBlockEntity())
            {
                structureBlockInfoWorld = new Structure.StructureBlockInfo(structureBlockInfoWorld.pos, worldState, null);
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.REPLACE_NOT_AIR_PROCESSOR;
    }
}
