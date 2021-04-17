package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modcompat.*;
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

/**
 * POOL ENTRY MUST BE USING legacy_single_pool_element OR ELSE THE STRUCTURE BLOCK IS REMOVED BEFORE THIS PROCESSOR RUNS.
 */
public class SpiderInfestedBeeDungeonProcessor extends StructureProcessor {

    public static final Codec<SpiderInfestedBeeDungeonProcessor> CODEC = Codec.unit(SpiderInfestedBeeDungeonProcessor::new);
    private SpiderInfestedBeeDungeonProcessor() { }

    @Override
    public Template.BlockInfo func_230386_a_(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        BlockState blockState = structureBlockInfoWorld.state;
        BlockPos worldPos = structureBlockInfoWorld.pos;
        Random random = new SharedSeedRandom();
        random.setSeed(worldPos.toLong() * worldPos.getY());

        // placing altar blocks
        if (blockState.isIn(Blocks.STRUCTURE_BLOCK)) {
            String metadata = structureBlockInfoWorld.nbt.getString("metadata");
            BlockState belowBlock = worldView.getChunk(worldPos).getBlockState(worldPos);

            //altar blocks cannot be placed on air
            if(belowBlock.isAir()){
                blockState = Blocks.CAVE_AIR.getDefaultState();
            }
            else{
                switch (metadata){
                    case "center": {
                        if(ModChecker.buzzierBeesPresent && random.nextFloat() < 0.25f && Bumblezone.BzModCompatibilityConfig.allowScentedCandlesBeeDungeon.get()) {
                            blockState = BuzzierBeesRedirection.BBGetRandomTier3Candle(
                                    random,
                                    Bumblezone.BzModCompatibilityConfig.powerfulCandlesRaritySpiderBeeDungeon.get()+1,
                                    random.nextInt(random.nextInt(random.nextInt(3)+1)+1)+1,
                                    false,
                                    true);
                        }
                        else if(ModChecker.charmPresent && Bumblezone.BzModCompatibilityConfig.allowCCandlesBeeDungeon.get() &&
                                (ModChecker.buzzierBeesPresent ? random.nextFloat() < 0.1f : random.nextFloat() < 0.33f))
                        {
                            blockState = CharmRedirection.CGetCandle(false, false);
                        }
                        else if (ModChecker.buzzierBeesPresent ? random.nextFloat() < 0.3f : random.nextFloat() < 0.6f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().getDefaultState();
                        }
                        else if(ModChecker.buzzierBeesPresent || random.nextFloat() < 0.05f) {
                            blockState = Blocks.COBWEB.getDefaultState();
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.getDefaultState();
                        }
                    }
                    break;
                    case "inner_ring": {
                        if (random.nextFloat() < 0.3f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().getDefaultState();
                        }
                        else if(random.nextFloat() < 0.07f) {
                            blockState = Blocks.COBWEB.getDefaultState();
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.getDefaultState();
                        }
                    }
                    break;
                    case "outer_ring": {
                        if (random.nextFloat() < 0.4f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().getDefaultState();
                        }
                        else if(ModChecker.charmPresent && Bumblezone.BzModCompatibilityConfig.allowCCandlesSpiderBeeDungeon.get() && random.nextFloat() < 0.07f) {
                            blockState = CharmRedirection.CGetCandle(false, false);
                        }
                        else if(random.nextFloat() < 0.07f) {
                            blockState = Blocks.COBWEB.getDefaultState();
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.getDefaultState();
                        }
                    }
                    break;
                    default: break;
                }
            }
        }

        // main body and ceiling
        else if(blockState.isIn(Blocks.HONEYCOMB_BLOCK) || blockState.isIn(BzBlocks.FILLED_POROUS_HONEYCOMB.get())){
            if(ModChecker.productiveBeesPresent && random.nextFloat() < Bumblezone.BzModCompatibilityConfig.PBOreHoneycombSpawnRateSpiderBeeDungeon.get()) {
                blockState = ProductiveBeesRedirection.PBGetRandomHoneycomb(random, Bumblezone.BzModCompatibilityConfig.PBGreatHoneycombRaritySpiderBeeDungeon.get());
            }
            else if(ModChecker.resourcefulBeesPresent && random.nextFloat() < Bumblezone.BzModCompatibilityConfig.RBOreHoneycombSpawnRateSpiderBeeDungeon.get()) {
                blockState = ResourcefulBeesRedirection.RBGetRandomHoneycomb(random, Bumblezone.BzModCompatibilityConfig.RBGreatHoneycombRaritySpiderBeeDungeon.get());
            }
            else if(ModChecker.productiveBeesPresent && random.nextFloat() < 0.5f && Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get()) {
                blockState = ProductiveBeesRedirection.PBGetRottenedHoneycomb(random);
            }
            else if(ModChecker.resourcefulBeesPresent && random.nextFloat() < 0.5f && Bumblezone.BzModCompatibilityConfig.spawnResourcefulBeesHoneycombVariants.get()) {
                blockState = ResourcefulBeesRedirection.RBGetSpiderHoneycomb(random);
            }
            else if (random.nextFloat() < 0.15f) {
                blockState = Blocks.HONEYCOMB_BLOCK.getDefaultState();
            }
            else {
                blockState = BzBlocks.POROUS_HONEYCOMB.get().getDefaultState();
            }
        }

        // walls
        else if(blockState.isIn(BzBlocks.HONEYCOMB_BROOD.get())){
            if (random.nextFloat() < 0.6f) {
                blockState = BzBlocks.EMPTY_HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.FACING, blockState.get(HoneycombBrood.FACING));
            }
            else if (random.nextDouble() < Bumblezone.BzDungeonsConfig.spawnerRateSpiderBeeDungeon.get()) {
                blockState = Blocks.SPAWNER.getDefaultState();
            }
            else if(ModChecker.productiveBeesPresent && random.nextFloat() < 0.5f && Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get()) {
                blockState = ProductiveBeesRedirection.PBGetRottenedHoneycomb(random);
            }
            else if(ModChecker.resourcefulBeesPresent && random.nextFloat() < 0.5f && Bumblezone.BzModCompatibilityConfig.spawnResourcefulBeesHoneycombVariants.get()) {
                blockState = ResourcefulBeesRedirection.RBGetSpiderHoneycomb(random);
            }
            else {
                blockState = BzBlocks.POROUS_HONEYCOMB.get().getDefaultState();
            }
        }

        // sugar water
        else if(blockState.isIn(BzFluids.SUGAR_WATER_BLOCK.get())){
            if(ModChecker.buzzierBeesPresent) {
                blockState = BuzzierBeesRedirection.getCrystallizedHoneyBlock();
            }
            else {
                blockState = Blocks.CAVE_AIR.getDefaultState();
            }
        }

        return new Template.BlockInfo(worldPos, blockState, structureBlockInfoWorld.nbt);
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return BzProcessors.SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR;
    }
}