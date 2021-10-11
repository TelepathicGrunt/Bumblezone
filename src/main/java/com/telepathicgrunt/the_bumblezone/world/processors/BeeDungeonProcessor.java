package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modcompat.BuzzierBeesCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.CavesAndCliffsBackportCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.CharmCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ProductiveBeesCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ResourcefulBeesCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Random;

public class BeeDungeonProcessor extends StructureProcessor {

    public static final Codec<BeeDungeonProcessor> CODEC = Codec.unit(BeeDungeonProcessor::new);
    private BeeDungeonProcessor() { }

    @Override
    public Template.BlockInfo processBlock(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        BlockState blockState = structureBlockInfoWorld.state;
        BlockPos worldPos = structureBlockInfoWorld.pos;
        Random random = new SharedSeedRandom();
        random.setSeed(worldPos.asLong() * worldPos.getY());

        // placing altar blocks
        if (blockState.is(Blocks.STRUCTURE_BLOCK)) {
            String metadata = structureBlockInfoWorld.nbt.getString("metadata");
            BlockState belowBlock = worldView.getChunk(worldPos).getBlockState(worldPos);

            //altar blocks cannot be placed on air
            if(belowBlock.isAir()){
                blockState = Blocks.CAVE_AIR.defaultBlockState();
            }
            else{
                switch (metadata){
                    case "center": {
                        if(ModChecker.buzzierBeesPresent && random.nextFloat() < 0.8f && Bumblezone.BzModCompatibilityConfig.allowScentedCandlesBeeDungeon.get()) {
                            blockState = BuzzierBeesCompat.BBGetRandomTier3Candle(
                                    random,
                                    Bumblezone.BzModCompatibilityConfig.powerfulCandlesRarityBeeDungeon.get()+1,
                                    random.nextInt(random.nextInt(random.nextInt(3)+1)+1)+1,
                                    false,
                                    true);
                        }
                        else if(ModChecker.cavesAndCliffsPresent && !ModChecker.buzzierBeesPresent &&
                                Bumblezone.BzModCompatibilityConfig.allowCACCandlesBeeDungeon.get() && random.nextFloat() < 0.33f)
                        {
                            blockState = CavesAndCliffsBackportCompat.CACGetRandomCandle(random, random.nextInt(random.nextInt(random.nextInt(3)+1)+1)+1, false, true);
                        }
                        else if(ModChecker.charmPresent && !ModChecker.buzzierBeesPresent &&
                                Bumblezone.BzModCompatibilityConfig.allowCCandlesBeeDungeon.get() && random.nextFloat() < 0.33f)
                        {
                            blockState = CharmCompat.CGetCandle(false, true);
                        }
                        else if (ModChecker.buzzierBeesPresent || random.nextFloat() < 0.6f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    break;
                    case "inner_ring": {
                        if(ModChecker.buzzierBeesPresent && random.nextFloat() < 0.4f && Bumblezone.BzModCompatibilityConfig.allowScentedCandlesBeeDungeon.get()) {
                            blockState = BuzzierBeesCompat.BBGetRandomTier2Candle(
                                            random,
                                            Bumblezone.BzModCompatibilityConfig.powerfulCandlesRarityBeeDungeon.get(),
                                            random.nextInt(random.nextInt(3)+1)+1,
                                            false,
                                            true);
                        }
                        else if(ModChecker.cavesAndCliffsPresent && Bumblezone.BzModCompatibilityConfig.allowCACCandlesBeeDungeon.get() &&
                                (ModChecker.buzzierBeesPresent ? random.nextFloat() < 0.05f : random.nextFloat() < 0.99))
                        {
                            blockState = CavesAndCliffsBackportCompat.CACGetRandomCandle(random, random.nextInt(random.nextInt(3)+1)+1, false, true);
                        }
                        else if(ModChecker.charmPresent && Bumblezone.BzModCompatibilityConfig.allowCCandlesBeeDungeon.get() &&
                                (ModChecker.buzzierBeesPresent ? random.nextFloat() < 0.05f : random.nextFloat() < 0.25f))
                        {
                            blockState = CharmCompat.CGetCandle(false, true);
                        }
                        else if (ModChecker.buzzierBeesPresent ? random.nextBoolean() : random.nextFloat() < 0.35f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    break;
                    case "outer_ring": {
                        if(ModChecker.buzzierBeesPresent && random.nextFloat() < 0.25f && Bumblezone.BzModCompatibilityConfig.allowScentedCandlesBeeDungeon.get()) {
                            blockState = BuzzierBeesCompat.BBGetRandomTier1Candle(
                                            random,
                                            random.nextInt(3)+1,
                                            false,
                                            true);
                        }
                        else if(ModChecker.cavesAndCliffsPresent && Bumblezone.BzModCompatibilityConfig.allowCACCandlesBeeDungeon.get() &&
                                (ModChecker.buzzierBeesPresent ? random.nextFloat() < 0.1f : random.nextFloat() < 0.99f))
                        {
                            blockState = CavesAndCliffsBackportCompat.CACGetRandomCandle(random, random.nextInt(3)+1, false, true);
                        }
                        else if(ModChecker.charmPresent && Bumblezone.BzModCompatibilityConfig.allowCCandlesBeeDungeon.get() &&
                                (ModChecker.buzzierBeesPresent ? random.nextFloat() < 0.1f : random.nextFloat() < 0.2f))
                        {
                            blockState = CharmCompat.CGetCandle(false, true);
                        }
                        else if (random.nextFloat() < 0.45f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    break;
                    default: break;
                }
            }
        }

        // main body and ceiling
        else if(blockState.is(Blocks.HONEYCOMB_BLOCK) || blockState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get())){
            if(ModChecker.productiveBeesPresent && random.nextFloat() < Bumblezone.BzModCompatibilityConfig.PBOreHoneycombSpawnRateBeeDungeon.get()) {
                blockState = ProductiveBeesCompat.PBGetRandomHoneycomb(random, Bumblezone.BzModCompatibilityConfig.PBGreatHoneycombRarityBeeDungeon.get());
            }
            else if(ModChecker.resourcefulBeesPresent && random.nextFloat() < Bumblezone.BzModCompatibilityConfig.RBOreHoneycombSpawnRateBeeDungeon.get()) {
                blockState = ResourcefulBeesCompat.RBGetRandomHoneycomb(random, Bumblezone.BzModCompatibilityConfig.RBGreatHoneycombRarityBeeDungeon.get());
            }
            else if (random.nextFloat() < 0.4f) {
                blockState = Blocks.HONEYCOMB_BLOCK.defaultBlockState();
            }
            else {
                blockState = BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState();
            }
        }

        // walls
        else if(blockState.is(BzBlocks.HONEYCOMB_BROOD.get())){
            if (random.nextFloat() < 0.6f) {
                blockState = BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
                        .setValue(HoneycombBrood.STAGE, random.nextInt(3))
                        .setValue(HoneycombBrood.FACING, blockState.getValue(HoneycombBrood.FACING));
            }
            else if (random.nextFloat() < 0.2f) {
                blockState = Blocks.HONEY_BLOCK.defaultBlockState();
            }
            else if(ModChecker.resourcefulBeesPresent && random.nextFloat() < 0.1f) {
                blockState = ResourcefulBeesCompat.getRBHoneyBlock(random);
            }
            else {
                blockState = BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState();
            }
        }

        // sugar water
        else if(blockState.is(BzFluids.SUGAR_WATER_BLOCK.get())){
            if(random.nextFloat() < 0.1f){
                blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState().setValue(HoneyCrystal.WATERLOGGED, true);
            }
        }

        return new Template.BlockInfo(worldPos, blockState, structureBlockInfoWorld.nbt);
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return BzProcessors.BEE_DUNGEON_PROCESSOR;
    }
}