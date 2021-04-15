package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
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
    public Template.BlockInfo func_230386_a_(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        BlockPos.Mutable mutable = new BlockPos.Mutable().setPos(structureBlockInfoWorld.pos);
        IChunk cachedChunk = worldView.getChunk(mutable);

        // attempts to remove invalid floating plants
        if(structureBlockInfoWorld.state.isAir() || structureBlockInfoWorld.state.getBlock() instanceof FlowingFluidBlock){

            // set the block in the world so that canPlaceAt's result changes
            cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
            BlockState aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));

            // detects the first invalidly placed block before going into a while loop
            if(!aboveWorldState.isValidPosition(worldView, mutable)){
                cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
                aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));

                while(mutable.getY() < worldView.getHeight() && !aboveWorldState.isValidPosition(worldView, mutable)){
                    cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
                    aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));
                }
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return BzProcessors.REMOVE_FLOATING_BLOCKS_PROCESSOR;
    }
}
