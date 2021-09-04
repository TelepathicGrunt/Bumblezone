package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.modcompat.BuzzierBeesCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.CharmCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ProductiveBeesCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ResourcefulBeesCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Random;

public class HoneycombHoleProcessor extends StructureProcessor {

    public static final Codec<HoneycombHoleProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("flood_level").forGetter(config -> config.floodLevel)
    ).apply(instance, instance.stable(HoneycombHoleProcessor::new)));

    private final int floodLevel;

    private HoneycombHoleProcessor(int floodLevel) { this.floodLevel = floodLevel; }

    @Override
    public Template.BlockInfo processBlock(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        BlockState placingState = structureBlockInfoWorld.state;
        BlockPos worldPos = structureBlockInfoWorld.pos;
        Random random = new SharedSeedRandom();
        random.setSeed(worldPos.asLong() * worldPos.getY());

        IChunk chunk = worldView.getChunk(structureBlockInfoWorld.pos);
        BlockState checkedState = chunk.getBlockState(structureBlockInfoWorld.pos);

        // does world checks for cave and pollen powder
        if(checkedState.isAir() || !checkedState.getFluidState().isEmpty()) {
            if (placingState.isAir() || placingState.is(BzBlocks.PILE_OF_POLLEN.get())) {
                if(!checkedState.getFluidState().isEmpty() || structureBlockInfoWorld.pos.getY() <= floodLevel){
                    chunk.setBlockState(structureBlockInfoWorld.pos, BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState(), false);
                    return null;
                }
            }
            else {
                return null;
            }
        }

        // brood
        if(placingState.is(BzBlocks.HONEYCOMB_BROOD.get())){
            if (random.nextInt(5) < 2) {
                return new Template.BlockInfo(worldPos, placingState.setValue(HoneycombBrood.STAGE, random.nextInt(3)), null);
            }
            else if (random.nextInt(13) == 0) {
                return new Template.BlockInfo(
                        worldPos,
                        BzBlocks.EMPTY_HONEYCOMB_BROOD.get().defaultBlockState()
                                .setValue(EmptyHoneycombBrood.FACING, placingState.getValue(HoneycombBrood.FACING)),
                        null);
            }
            else if (random.nextInt(4) == 0) {
                return new Template.BlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), null);
            }
            else if(ModChecker.resourcefulBeesPresent && random.nextFloat() < 0.01f) {
                return new Template.BlockInfo(worldPos, ResourcefulBeesCompat.getRBHoneyBlock(random), null);
            }
            else {
                return new Template.BlockInfo(worldPos, Blocks.HONEY_BLOCK.defaultBlockState(), null);
            }
        }

        // ring around brood
        if(placingState.is(Blocks.HONEY_BLOCK) || placingState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get())){
            if (random.nextInt(3) == 0) {
                return new Template.BlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), null);
            }
            else if(ModChecker.resourcefulBeesPresent && random.nextFloat() < 0.01f) {
                return new Template.BlockInfo(worldPos, ResourcefulBeesCompat.getRBHoneyBlock(random), null);
            }
            else {
                return new Template.BlockInfo(worldPos, Blocks.HONEY_BLOCK.defaultBlockState(), null);
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
                return new Template.BlockInfo(worldPos, Blocks.CAVE_AIR.defaultBlockState(), null);
            }
            else {
                return new Template.BlockInfo(worldPos, BzBlocks.PILE_OF_POLLEN.get().defaultBlockState().setValue(PileOfPollen.LAYERS, random.nextInt(3) + 1), null);
            }
        }

        // main body
        else if(placingState.is(Blocks.HONEYCOMB_BLOCK)){
            if (random.nextInt(3) != 0) {
                return new Template.BlockInfo(worldPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), null);
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return BzProcessors.HONEYCOMB_HOLE_PROCESSOR;
    }
}