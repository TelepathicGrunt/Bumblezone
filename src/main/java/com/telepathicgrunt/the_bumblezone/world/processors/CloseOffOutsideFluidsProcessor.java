package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

/**
 * For preventing floating fluids
 */
public class CloseOffOutsideFluidsProcessor extends StructureProcessor {

    public static final Codec<CloseOffOutsideFluidsProcessor> CODEC = Codec.unit(CloseOffOutsideFluidsProcessor::new);
    private CloseOffOutsideFluidsProcessor() { }

    @Override
    public Template.BlockInfo processBlock(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(structureBlockInfoWorld.pos);
        IChunk cachedChunk = worldView.getChunk(mutable);
        BlockPos worldPos = structureBlockInfoWorld.pos;

        if(structureBlockInfoWorld.state.isAir()) {
            BlockPos.Mutable sidePos = new BlockPos.Mutable();
            for(Direction direction : Direction.values()) {
                if(Direction.DOWN == direction) continue;

                sidePos.set(worldPos).move(direction);
                if(cachedChunk.getPos().x != sidePos.getX() >> 4 || cachedChunk.getPos().z != sidePos.getZ() >> 4)
                    cachedChunk = worldView.getChunk(sidePos);

                BlockState neighborState = cachedChunk.getBlockState(sidePos);
                if(neighborState.getFluidState().isSource()) {
                    if(!cachedChunk.getBlockState(sidePos.below()).getFluidState().isEmpty()) {
                        cachedChunk.setBlockState(sidePos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), false);
                    }
                    else if(sidePos.getY() > 0 && sidePos.getY() < 255) {
                        cachedChunk.getLiquidTicks().scheduleTick(structureBlockInfoWorld.pos, neighborState.getFluidState().getType(), 0);
                    }
                }
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return BzProcessors.CLOSE_OFF_OUTSIDE_FLUIDS_PROCESSOR;
    }
}
