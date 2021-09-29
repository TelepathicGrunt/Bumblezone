package com.telepathicgrunt.bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;

import java.util.Random;

public class HoneycombHoleProcessor extends StructureProcessor {

    public static final Codec<HoneycombHoleProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("flood_level").forGetter(config -> config.floodLevel)
    ).apply(instance, instance.stable(HoneycombHoleProcessor::new)));

    private final int floodLevel;

    private HoneycombHoleProcessor(int floodLevel) { this.floodLevel = floodLevel; }

    @Override
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfoLocal, Structure.StructureBlockInfo structureBlockInfoWorld, StructurePlacementData structurePlacementData) {
        BlockState placingState = structureBlockInfoWorld.state;
        BlockPos worldPos = structureBlockInfoWorld.pos;
        Random random = new ChunkRandom();
        random.setSeed(worldPos.asLong() * worldPos.getY());

        Chunk chunk = worldView.getChunk(structureBlockInfoWorld.pos);
        BlockState checkedState = chunk.getBlockState(structureBlockInfoWorld.pos);

        // does world checks for cave and pollen powder
        if(checkedState.isAir() || !checkedState.getFluidState().isEmpty()) {
            if (placingState.isAir() || placingState.isOf(BzBlocks.PILE_OF_POLLEN)) {
                if(!checkedState.getFluidState().isEmpty() || structureBlockInfoWorld.pos.getY() <= floodLevel){
                    chunk.setBlockState(structureBlockInfoWorld.pos, BzFluids.SUGAR_WATER_BLOCK.getDefaultState(), false);
                    return null;
                }
            }
            else {
                return null;
            }
        }

        // brood
        if(placingState.isOf(BzBlocks.HONEYCOMB_BROOD)){
            if (random.nextInt(5) < 2) {
                return new Structure.StructureBlockInfo(worldPos, placingState.with(HoneycombBrood.STAGE, random.nextInt(3)), null);
            }
            else if (random.nextInt(13) == 0) {
                return new Structure.StructureBlockInfo(
                        worldPos,
                        BzBlocks.EMPTY_HONEYCOMB_BROOD.getDefaultState()
                                .with(EmptyHoneycombBrood.FACING, placingState.get(HoneycombBrood.FACING)),
                        null);
            }
            else if (random.nextInt(4) == 0) {
                return new Structure.StructureBlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), null);
            }
            else {
                return new Structure.StructureBlockInfo(worldPos, Blocks.HONEY_BLOCK.getDefaultState(), null);
            }
        }

        // ring around brood
        if(placingState.isOf(Blocks.HONEY_BLOCK) || placingState.isOf(BzBlocks.FILLED_POROUS_HONEYCOMB)){
            if (random.nextInt(3) == 0) {
                return new Structure.StructureBlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), null);
            }
            else {
                return new Structure.StructureBlockInfo(worldPos, Blocks.HONEY_BLOCK.getDefaultState(), null);
            }
        }

        // Pollen pile
        else if(placingState.isOf(BzBlocks.PILE_OF_POLLEN)) {
            // Check if pollen pile can even be placed here safely
            BlockState belowState = chunk.getBlockState(structureBlockInfoWorld.pos.down());
            if(belowState.isAir() || !belowState.getFluidState().isEmpty()) {
                return null;
            }

            if (random.nextInt(80) != 0) {
                return new Structure.StructureBlockInfo(worldPos, Blocks.CAVE_AIR.getDefaultState(), null);
            }
            else {
                return new Structure.StructureBlockInfo(worldPos, BzBlocks.PILE_OF_POLLEN.getDefaultState().with(PileOfPollen.LAYERS, random.nextInt(3) + 1), null);
            }
        }

        // main body
        else if(placingState.isOf(Blocks.HONEYCOMB_BLOCK)){
            if (random.nextInt(3) != 0) {
                return new Structure.StructureBlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), null);
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.HONEYCOMB_HOLE_PROCESSOR;
    }
}