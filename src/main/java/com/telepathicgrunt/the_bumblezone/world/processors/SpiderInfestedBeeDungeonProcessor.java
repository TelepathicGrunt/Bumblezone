package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzWorldgenConfigs;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ProductiveBeesCompat;
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
            else {
                switch (metadata) {
                    case "center" -> {
                        if (random.nextFloat() < 0.6f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();
                        } else if (random.nextFloat() < 0.25f) {
                            blockState = GeneralUtils.VANILLA_CANDLES.get(random.nextInt(GeneralUtils.VANILLA_CANDLES.size()));
                            blockState = blockState.setValue(CandleBlock.CANDLES, random.nextInt(4) + 1);
                            blockState = blockState.setValue(CandleBlock.LIT, false);
                        }
                        else if (random.nextFloat() < 0.05f) {
                            blockState = Blocks.COBWEB.defaultBlockState();
                        } else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    case "inner_ring" -> {
                        if (random.nextFloat() < 0.3f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();
                        } else if (random.nextFloat() < 0.07f) {
                            blockState = Blocks.COBWEB.defaultBlockState();
                        } else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    case "outer_ring" -> {
                        if (random.nextFloat() < 0.4f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();
                        } else if (random.nextFloat() < 0.2f) {
                            blockState = GeneralUtils.VANILLA_CANDLES.get(random.nextInt(GeneralUtils.VANILLA_CANDLES.size()));
                            blockState = blockState.setValue(CandleBlock.CANDLES, random.nextInt(random.nextInt(4) + 1) + 1);
                            blockState = blockState.setValue(CandleBlock.LIT, false);
                        } else if (random.nextFloat() < 0.07f) {
                            blockState = Blocks.COBWEB.defaultBlockState();
                        } else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    default -> {
                    }
                }
            }
        }

        // main body and ceiling
        else if(blockState.is(Blocks.HONEYCOMB_BLOCK) || blockState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get())) {
            if(ModChecker.productiveBeesPresent && random.nextFloat() < BzModCompatibilityConfigs.oreHoneycombSpawnRateSpiderBeeDungeon.get()) {
                StructureTemplate.StructureBlockInfo info = ProductiveBeesCompat.PBGetRottenedHoneycomb(worldPos, random);
                if(info != null) return info;
            }
            else if (random.nextFloat() < 0.15f) {
                blockState = Blocks.HONEYCOMB_BLOCK.defaultBlockState();
            }
            else {
                blockState = BzBlocks.POROUS_HONEYCOMB.get().defaultBlockState();
            }
        }

        // walls
        else if(blockState.is(BzBlocks.HONEYCOMB_BROOD.get())) {
            if (random.nextFloat() < 0.6f) {
                blockState = BzBlocks.EMPTY_HONEYCOMB_BROOD.get().defaultBlockState()
                        .setValue(HoneycombBrood.FACING, blockState.getValue(HoneycombBrood.FACING));
            }
            else if (random.nextDouble() < BzWorldgenConfigs.spawnerRateSpiderBeeDungeon.get()) {
                blockState = Blocks.SPAWNER.defaultBlockState();
            }
            else {
                blockState = BzBlocks.POROUS_HONEYCOMB.get().defaultBlockState();
            }
        }

        // sugar water
        else if(blockState.is(BzFluids.SUGAR_WATER_BLOCK.get())) {
            blockState = Blocks.CAVE_AIR.defaultBlockState();
        }

        return new StructureTemplate.StructureBlockInfo(worldPos, blockState, structureBlockInfoWorld.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR;
    }
}