package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

/**
 * For removing stuff like floating tall grass or kelp
 */
public class RemoveFloatingBlocksProcessor extends StructureProcessor {

    public static final Codec<RemoveFloatingBlocksProcessor> CODEC = Codec.unit(RemoveFloatingBlocksProcessor::new);
    private RemoveFloatingBlocksProcessor() { }

    @Override
    public Template.BlockInfo processBlock(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(structureBlockInfoWorld.pos);
        IChunk cachedChunk = worldView.getChunk(mutable);

        // attempts to remove invalid floating plants
        if(structureBlockInfoWorld.state.isAir() || structureBlockInfoWorld.state.getMaterial().isLiquid()) {

            // set the block in the world so that canSurvive's result changes
            cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
            BlockState aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));

            // detects the invalidly placed blocks
            while(mutable.getY() < worldView.getMaxBuildHeight() && !aboveWorldState.canSurvive(worldView, mutable)) {
                cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
                aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));
            }
        }
        else if(!structureBlockInfoWorld.state.canSurvive(worldView, mutable)) {
            return new Template.BlockInfo(structureBlockInfoWorld.pos, Blocks.CAVE_AIR.defaultBlockState(), null);
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return BzProcessors.REMOVE_FLOATING_BLOCKS_PROCESSOR;
    }
}
