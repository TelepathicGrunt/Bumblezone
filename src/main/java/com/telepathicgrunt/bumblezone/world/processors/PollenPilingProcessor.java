package com.telepathicgrunt.bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.bumblezone.utils.OpenSimplex2F;
import net.minecraft.block.BlockState;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;

public class PollenPilingProcessor extends StructureProcessor {

    public static final Codec<PollenPilingProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.FLOAT.fieldOf("xz_scale").forGetter(config -> config.xzScale),
            Codec.FLOAT.fieldOf("y_scale").forGetter(config -> config.yScale),
            Codec.BOOL.fieldOf("pollen_replace_solids").forGetter(config -> config.pollenReplaceSolids)
    ).apply(instance, instance.stable(PollenPilingProcessor::new)));

    private final float xzScale;
    private final float yScale;
    private final boolean pollenReplaceSolids;
    protected long seed;
    private OpenSimplex2F noiseGenerator = null;

    public PollenPilingProcessor(float xzScale, float yScale, boolean pollenReplaceSolids) {
        this.xzScale = xzScale;
        this.yScale = yScale;
        this.pollenReplaceSolids = pollenReplaceSolids;
    }

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGenerator == null) {
            noiseGenerator = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    @Override
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfoLocal, Structure.StructureBlockInfo structureBlockInfoWorld, StructurePlacementData structurePlacementData) {
        setSeed(worldView instanceof ChunkRegion ? ((ChunkRegion) worldView).getSeed() : 0);
        Structure.StructureBlockInfo structureBlockInfoToReturn = structureBlockInfoWorld;
        BlockState structureState = structureBlockInfoToReturn.state;
        BlockPos worldPos = structureBlockInfoToReturn.pos;


        if(structureState.isOf(BzBlocks.PILE_OF_POLLEN)) {
            if(!pollenReplaceSolids && !worldView.getBlockState(worldPos).isAir()) {
                return null;
            }

            BlockPos belowPos = worldPos.down();
            if(belowPos.getY() <= worldView.getBottomY() || belowPos.getY() >= worldView.getTopY())
                return null;
            
            Chunk chunk = worldView.getChunk(belowPos);
            BlockState belowState = chunk.getBlockState(belowPos);
            if(!belowState.isOpaque()) {
                chunk.getBlockTickScheduler().schedule(belowPos, structureState.getBlock(), 0);
            }

            BlockPos.Mutable sidePos = new BlockPos.Mutable();
            for(Direction direction : Direction.values()) {
                sidePos.set(worldPos).move(direction);
                if(worldView.getBlockState(sidePos).getFluidState().isStill()) {
                    return new Structure.StructureBlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), null);
                }
            }

            double noiseVal = noiseGenerator.noise3_Classic(worldPos.getX() * xzScale, worldPos.getY() * yScale, worldPos.getZ() * xzScale);
            int layerHeight = Math.max(0, (int) (((noiseVal / 2D) + 0.5D) * 2.5D));
            layerHeight = Math.min(8, layerHeight + structureState.get(PileOfPollen.LAYERS));
            structureBlockInfoToReturn = new Structure.StructureBlockInfo(worldPos, structureState.with(PileOfPollen.LAYERS, layerHeight), structureBlockInfoToReturn.nbt);
        }

        if(!structureState.isOpaque()) {
            BlockPos abovePos = worldPos.up();
            Chunk chunk = worldView.getChunk(abovePos);
            BlockState aboveState = chunk.getBlockState(abovePos);
            if(aboveState.isOf(BzBlocks.PILE_OF_POLLEN.get())) {
                chunk.getBlockTickScheduler().schedule(abovePos, aboveState.getBlock(), 0);
            }
        }

        return structureBlockInfoToReturn;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.POLLEN_PILING_PROCESSOR;
    }
}