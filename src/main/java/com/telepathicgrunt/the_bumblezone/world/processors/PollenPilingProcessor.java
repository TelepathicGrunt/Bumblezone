package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

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
    public Template.BlockInfo processBlock(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        setSeed(worldView instanceof WorldGenRegion ? ((WorldGenRegion) worldView).getSeed() : 0);
        Template.BlockInfo structureBlockInfoToReturn = structureBlockInfoWorld;
        BlockState structureState = structureBlockInfoToReturn.state;
        BlockPos worldPos = structureBlockInfoToReturn.pos;


        if(structureState.is(BzBlocks.PILE_OF_POLLEN.get())) {
            if(!pollenReplaceSolids && !worldView.getBlockState(worldPos).isAir()) {
                return null;
            }

            BlockPos belowPos = worldPos.below();
            if(belowPos.getY() <= 0 || belowPos.getY() >= worldView.getMaxBuildHeight())
                return null;
            
            IChunk chunk = worldView.getChunk(belowPos);
            BlockState belowState = chunk.getBlockState(belowPos);
            if(!belowState.canOcclude()) {
                chunk.getBlockTicks().scheduleTick(belowPos, structureState.getBlock(), 0);
            }

            BlockPos.Mutable sidePos = new BlockPos.Mutable();
            for(Direction direction : Direction.values()) {
                sidePos.set(worldPos).move(direction);
                if(worldView.getBlockState(sidePos).getFluidState().isSource()) {
                    return new Template.BlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), null);
                }
            }

            double noiseVal = noiseGenerator.noise3_Classic(worldPos.getX() * xzScale, worldPos.getY() * yScale, worldPos.getZ() * xzScale);
            int layerHeight = Math.max(0, (int) (((noiseVal / 2D) + 0.5D) * 2.5D));
            layerHeight = Math.min(8, layerHeight + structureState.getValue(PileOfPollen.LAYERS));
            structureBlockInfoToReturn = new Template.BlockInfo(worldPos, structureState.setValue(PileOfPollen.LAYERS, layerHeight), structureBlockInfoToReturn.nbt);
        }

        if(!structureState.canOcclude()) {
            BlockPos abovePos = worldPos.above();
            IChunk chunk = worldView.getChunk(abovePos);
            BlockState aboveState = chunk.getBlockState(abovePos);
            if(aboveState.is(BzBlocks.PILE_OF_POLLEN.get())) {
                chunk.getBlockTicks().scheduleTick(abovePos, aboveState.getBlock(), 0);
            }
        }

        return structureBlockInfoToReturn;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return BzProcessors.BEE_DUNGEON_PROCESSOR;
    }
}