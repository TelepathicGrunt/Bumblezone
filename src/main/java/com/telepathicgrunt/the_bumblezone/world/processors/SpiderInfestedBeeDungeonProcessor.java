package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

/**
 * POOL ENTRY MUST BE USING legacy_single_pool_element OR ELSE THE STRUCTURE BLOCK IS REMOVED BEFORE THIS PROCESSOR RUNS.
 */
public class SpiderInfestedBeeDungeonProcessor extends StructureProcessor {

    public static final Codec<SpiderInfestedBeeDungeonProcessor> CODEC = Codec.unit(SpiderInfestedBeeDungeonProcessor::new);
    private SpiderInfestedBeeDungeonProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        BlockState blockState = structureBlockInfoWorld.state;
        BlockPos worldPos = structureBlockInfoWorld.pos;
        Random random = new WorldgenRandom(new LegacyRandomSource(0));
        random.setSeed(worldPos.asLong() * worldPos.getY());

        // placing altar blocks
        if (blockState.is(Blocks.STRUCTURE_BLOCK)) {
            String metadata = structureBlockInfoWorld.nbt.getString("metadata");
            BlockState belowBlock = worldView.getChunk(worldPos).getBlockState(worldPos);

            //altar blocks cannot be placed on air
            if(belowBlock.isAir()) {
                blockState = Blocks.CAVE_AIR.defaultBlockState();
            }
            else{
                switch (metadata) {
                    case "center": {
                        if (random.nextFloat() < 0.6f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.defaultBlockState();
                        }
                        else if(random.nextFloat() < 0.25f)
                        {
                            blockState = GeneralUtils.VANILLA_CANDLES.get(random.nextInt(GeneralUtils.VANILLA_CANDLES.size()));
                            blockState = blockState.setValue(CandleBlock.CANDLES, random.nextInt(4) + 1);
                            blockState = blockState.setValue(CandleBlock.LIT, false);
                        }
                        /*
                        else if(ModChecker.beeBetterPresent && random.nextFloat() < 0.2f) {
                            blockState = BeeBetterRedirection.getCandle(random);
                        }
                        else if(ModChecker.beeBetterPresent && random.nextFloat() < 0.2f) {
                            blockState = BeeBetterRedirection.getCandle(random);
                        }
                         */
                        else if(random.nextFloat() < 0.05f) {
                            blockState = Blocks.COBWEB.defaultBlockState();
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    break;
                    case "inner_ring": {
                        if (random.nextFloat() < 0.3f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.defaultBlockState();
                        }
                        else if(random.nextFloat() < 0.07f) {
                            blockState = Blocks.COBWEB.defaultBlockState();
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    break;
                    case "outer_ring": {
                        if (random.nextFloat() < 0.4f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.defaultBlockState();
                        }
                        else if(random.nextFloat() < 0.2f)
                        {
                            blockState = GeneralUtils.VANILLA_CANDLES.get(random.nextInt(GeneralUtils.VANILLA_CANDLES.size()));
                            blockState = blockState.setValue(CandleBlock.CANDLES, random.nextInt(random.nextInt(4) + 1) + 1);
                            blockState = blockState.setValue(CandleBlock.LIT, false);
                        }
                        else if(random.nextFloat() < 0.07f) {
                            blockState = Blocks.COBWEB.defaultBlockState();
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
        else if(blockState.is(Blocks.HONEYCOMB_BLOCK) || blockState.is(BzBlocks.FILLED_POROUS_HONEYCOMB)) {
           if (random.nextFloat() < 0.15f) {
               blockState = Blocks.HONEYCOMB_BLOCK.defaultBlockState();
           }
           /*
           else if(ModChecker.beeBetterPresent && random.nextFloat() < 0.4f) {
               blockState = BeeBetterRedirection.getSpiderDungeonBlock(random);
           }
           */
           else {
               blockState = BzBlocks.POROUS_HONEYCOMB.defaultBlockState();
           }
        }

        // walls
        else if(blockState.is(BzBlocks.HONEYCOMB_BROOD)) {
            if (random.nextFloat() < 0.6f) {
                blockState = BzBlocks.EMPTY_HONEYCOMB_BROOD.defaultBlockState()
                        .setValue(HoneycombBrood.FACING, blockState.getValue(HoneycombBrood.FACING));
            }
            /*
            else if(ModChecker.beeBetterPresent && random.nextFloat() < 0.4f) {
                blockState = BeeBetterRedirection.getSpiderDungeonBlock(random);
            }
            */
            else if (random.nextDouble() < Bumblezone.BZ_CONFIG.BZDungeonsConfig.spawnerRateSpiderBeeDungeon) {
                blockState = Blocks.SPAWNER.defaultBlockState();
            }
            else {
                blockState = BzBlocks.POROUS_HONEYCOMB.defaultBlockState();
            }
        }

        // sugar water
        else if(blockState.is(BzFluids.SUGAR_WATER_BLOCK)) {
            blockState = Blocks.CAVE_AIR.defaultBlockState();
        }

        return new StructureTemplate.StructureBlockInfo(worldPos, blockState, structureBlockInfoWorld.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR;
    }
}