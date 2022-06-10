package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class HoneycombHoleProcessor extends StructureProcessor {

    public static final Codec<HoneycombHoleProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("flood_level").forGetter(config -> config.floodLevel)
    ).apply(instance, instance.stable(HoneycombHoleProcessor::new)));

    private final int floodLevel;

    private HoneycombHoleProcessor(int floodLevel) { this.floodLevel = floodLevel; }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        BlockState placingState = structureBlockInfoWorld.state;
        BlockPos worldPos = structureBlockInfoWorld.pos;
        RandomSource random = new WorldgenRandom(new LegacyRandomSource(0));
        random.setSeed(worldPos.asLong() * worldPos.getY());

        ChunkAccess chunk = worldView.getChunk(structureBlockInfoWorld.pos);
        BlockState checkedState = chunk.getBlockState(structureBlockInfoWorld.pos);

        // does world checks for cave and pollen powder
        if(checkedState.isAir() || !checkedState.getFluidState().isEmpty()) {
            if (placingState.isAir() || placingState.is(BzBlocks.PILE_OF_POLLEN.get())) {
                if(!checkedState.getFluidState().isEmpty() || structureBlockInfoWorld.pos.getY() <= floodLevel) {
                    chunk.setBlockState(structureBlockInfoWorld.pos, BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState(), false);
                    return null;
                }
            }
            else {
                return null;
            }
        }

        // brood
        if(placingState.is(BzBlocks.HONEYCOMB_BROOD.get())) {
            if (random.nextInt(5) < 2) {
                return new StructureTemplate.StructureBlockInfo(worldPos, placingState.setValue(HoneycombBrood.STAGE, random.nextInt(3)), null);
            }
            else if (random.nextInt(13) == 0) {
                return new StructureTemplate.StructureBlockInfo(
                        worldPos,
                        BzBlocks.EMPTY_HONEYCOMB_BROOD.get().defaultBlockState()
                                .setValue(EmptyHoneycombBrood.FACING, placingState.getValue(HoneycombBrood.FACING)),
                        null);
            }
            else if (random.nextInt(4) == 0) {
                return new StructureTemplate.StructureBlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), null);
            }
            else {
                return new StructureTemplate.StructureBlockInfo(worldPos, Blocks.HONEY_BLOCK.defaultBlockState(), null);
            }
        }

        // ring around brood
        if(placingState.is(Blocks.HONEY_BLOCK) || placingState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get())) {
            if (random.nextInt(3) == 0) {
                return new StructureTemplate.StructureBlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), null);
            }
            else {
                return new StructureTemplate.StructureBlockInfo(worldPos, Blocks.HONEY_BLOCK.defaultBlockState(), null);
            }
        }

        // Pollen pile
        else if(placingState.is(BzBlocks.PILE_OF_POLLEN.get())) {
            // Check if pollen pile can even be placed here safely
            BlockState belowState = chunk.getBlockState(structureBlockInfoWorld.pos.below());
            if(belowState.isAir() || !belowState.getFluidState().isEmpty()) {
                return null;
            }

            if (random.nextInt(80) != 0) {
                return new StructureTemplate.StructureBlockInfo(worldPos, Blocks.CAVE_AIR.defaultBlockState(), null);
            }
            else {
                return new StructureTemplate.StructureBlockInfo(worldPos, BzBlocks.PILE_OF_POLLEN.get().defaultBlockState().setValue(PileOfPollen.LAYERS, random.nextInt(3) + 1), null);
            }
        }

        // main body
        else if(placingState.is(Blocks.HONEYCOMB_BLOCK)) {
            if (random.nextInt(3) != 0) {
                return new StructureTemplate.StructureBlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), null);
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.HONEYCOMB_HOLE_PROCESSOR;
    }
}