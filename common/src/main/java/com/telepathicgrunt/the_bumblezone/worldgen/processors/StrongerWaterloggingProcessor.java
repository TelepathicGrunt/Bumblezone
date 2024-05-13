package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class StrongerWaterloggingProcessor extends StructureProcessor {

    public static final MapCodec<StrongerWaterloggingProcessor> CODEC = MapCodec.unit(StrongerWaterloggingProcessor::new);

    private StrongerWaterloggingProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings settings) {
        if(structureBlockInfoWorld.state().hasProperty(BlockStateProperties.WATERLOGGED) && !structureBlockInfoWorld.state().getValue(BlockStateProperties.WATERLOGGED)) {
            ChunkAccess cachedChunk = levelReader.getChunk(structureBlockInfoWorld.pos());
            BlockPos worldPos = structureBlockInfoWorld.pos();
            BlockPos.MutableBlockPos sidePos = new BlockPos.MutableBlockPos();

            for(Direction direction : Direction.values()) {
                if(Direction.DOWN == direction) continue;

                sidePos.set(worldPos).move(direction);
                if(cachedChunk.getPos().x != sidePos.getX() >> 4 || cachedChunk.getPos().z != sidePos.getZ() >> 4)
                    cachedChunk = levelReader.getChunk(sidePos);

                BlockState neighborState = cachedChunk.getBlockState(sidePos);
                if(neighborState.getFluidState().isSource()) {

                    return new StructureTemplate.StructureBlockInfo(
                        structureBlockInfoWorld.pos(),
                        structureBlockInfoWorld.state().setValue(BlockStateProperties.WATERLOGGED, true),
                        structureBlockInfoWorld.nbt()
                    );
                }
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.WATERLOGGING_FIX_PROCESSOR.get();
    }
}
