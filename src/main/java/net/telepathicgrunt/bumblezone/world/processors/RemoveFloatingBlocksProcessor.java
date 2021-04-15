package net.telepathicgrunt.bumblezone.world.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import net.telepathicgrunt.bumblezone.modinit.BzProcessors;

/**
 * For removing stuff like floating tall grass or kelp
 */
public class RemoveFloatingBlocksProcessor extends StructureProcessor {

    public static final Codec<RemoveFloatingBlocksProcessor> CODEC = Codec.unit(RemoveFloatingBlocksProcessor::new);
    private RemoveFloatingBlocksProcessor() { }

    @Override
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfoLocal, Structure.StructureBlockInfo structureBlockInfoWorld, StructurePlacementData structurePlacementData) {
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(structureBlockInfoWorld.pos);
        Chunk cachedChunk = worldView.getChunk(mutable);

        // attempts to remove invalid floating plants
        if(structureBlockInfoWorld.state.isAir() || structureBlockInfoWorld.state.getBlock() instanceof FluidBlock){

            // set the block in the world so that canPlaceAt's result changes
            cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
            BlockState aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));

            // detects the first invalidly placed block before going into a while loop
            if(!aboveWorldState.canPlaceAt(worldView, mutable)){
                cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
                aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));

                while(mutable.getY() < worldView.getHeight() && !aboveWorldState.canPlaceAt(worldView, mutable)){
                    cachedChunk.setBlockState(mutable, structureBlockInfoWorld.state, false);
                    aboveWorldState = worldView.getBlockState(mutable.move(Direction.UP));
                }
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.REMOVE_FLOATING_BLOCKS_PROCESSOR;
    }
}
