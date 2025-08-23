package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Optional;

/**
 * POOL ENTRY MUST BE USING legacy_single_pool_element OR ELSE THE STRUCTURE BLOCK IS REMOVED BEFORE THIS PROCESSOR RUNS.
 */
public class BeeDungeonProcessor extends StructureProcessor {


    public static final MapCodec<BeeDungeonProcessor> CODEC = MapCodec.unit(BeeDungeonProcessor::new);

    private BeeDungeonProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings settings) {
        if (GeneralUtils.isOutsideStructureAllowedBounds(settings, structureBlockInfoWorld.pos())) {
            return structureBlockInfoWorld;
        }

        BlockState blockState = structureBlockInfoWorld.state();
        BlockPos worldPos = structureBlockInfoWorld.pos();
        CompoundTag nbt = structureBlockInfoWorld.nbt();

        // placing altar blocks
        if (blockState.is(Blocks.STRUCTURE_BLOCK)) {
            CompoundTag compoundTag = structureBlockInfoWorld.nbt();
            if (compoundTag == null) {
                return structureBlockInfoWorld;
            }
            String metadata = compoundTag.getString("metadata");
            BlockState belowBlock = levelReader.getChunk(worldPos).getBlockState(worldPos);

            if (!metadata.isEmpty()) {
                nbt = null;
            }

            //altar blocks cannot be placed on air
            if (belowBlock.isAir()) {
                blockState = Blocks.CAVE_AIR.defaultBlockState();
            }
            else {
                RandomSource random = settings.getRandom(worldPos);
                switch (metadata) {
                    case "center" -> {
                        if (random.nextFloat() < 0.1f) {
                            blockState = BzBlocks.HONEY_COCOON.get().defaultBlockState();
                            nbt = new CompoundTag();
                            nbt.putString("LootTable", "the_bumblezone:structures/bee_dungeon");
                        }
                        else if (random.nextFloat() < 0.6f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();
                        }
                        else if (random.nextFloat() < 0.6f) {
                            Optional<HolderSet.Named<Block>> optionalBlocks = BuiltInRegistries.BLOCK.getTag(BzTags.BEE_DUNGEON_POSSIBLE_CANDLES);
                            if (optionalBlocks.isPresent()) {
                                blockState = optionalBlocks.get().get(random.nextInt(optionalBlocks.get().size())).value().defaultBlockState();
                                blockState = blockState.setValue(CandleBlock.CANDLES, random.nextInt(4) + 1);
                                blockState = blockState.setValue(CandleBlock.LIT, true);
                            }
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    case "inner_ring" -> {
                        if (random.nextFloat() < 0.35f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();
                        }
                        else if (random.nextFloat() < 0.35f) {
                            Optional<HolderSet.Named<Block>> optionalBlocks = BuiltInRegistries.BLOCK.getTag(BzTags.BEE_DUNGEON_POSSIBLE_CANDLES);
                            if (optionalBlocks.isPresent()) {
                                blockState = optionalBlocks.get().get(random.nextInt(optionalBlocks.get().size())).value().defaultBlockState();
                                blockState = blockState.setValue(CandleBlock.CANDLES, random.nextInt(random.nextInt(4) + 1) + 1);
                                blockState = blockState.setValue(CandleBlock.LIT, true);
                            }
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    case "outer_ring" -> {
                        if (random.nextFloat() < 0.45f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState();
                        }
                        else if (random.nextFloat() < 0.2f) {
                            Optional<HolderSet.Named<Block>> optionalBlocks = BuiltInRegistries.BLOCK.getTag(BzTags.BEE_DUNGEON_POSSIBLE_CANDLES);
                            if (optionalBlocks.isPresent()) {
                                blockState = optionalBlocks.get().get(random.nextInt(optionalBlocks.get().size())).value().defaultBlockState();
                                blockState = blockState.setValue(CandleBlock.CANDLES, random.nextInt(random.nextInt(4) + 1) + 1);
                                blockState = blockState.setValue(CandleBlock.LIT, true);
                            }
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.defaultBlockState();
                        }
                    }
                    default -> {
                    }
                }
            }
        }

        // main body and ceiling
        else if (blockState.is(Blocks.HONEYCOMB_BLOCK) || blockState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get())) {

            RandomSource random = settings.getRandom(worldPos);
            boolean compatSuccess = false;

            for (ModCompat compat : ModChecker.DUNGEON_COMB_COMPATS) {
                if (compat.checkCombSpawn(worldPos, random, levelReader, false)) {
                    StructureTemplate.StructureBlockInfo info = compat.getHoneycomb(worldPos, random, levelReader, false);
                    if (info != null) {
                        return info;
                    }
                    compatSuccess = true;
                    break;
                }
            }

            if (!compatSuccess) {
                if (random.nextFloat() < 0.4f) {
                    blockState = Blocks.HONEYCOMB_BLOCK.defaultBlockState();
                } else {
                    blockState = BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState();
                }
            }
        }

        // walls
        else if (blockState.is(BzBlocks.HONEYCOMB_BROOD.get())) {
            RandomSource random = settings.getRandom(worldPos);
            if (random.nextFloat() < 0.6f) {
                blockState = BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
                        .setValue(HoneycombBrood.STAGE, random.nextInt(3))
                        .setValue(HoneycombBrood.FACING, blockState.getValue(HoneycombBrood.FACING));
            }
            else if (random.nextFloat() < 0.2f) {
                blockState = Blocks.HONEY_BLOCK.defaultBlockState();
            }
            else {
                blockState = BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState();
            }
        }

        // sugar water
        else if (blockState.is(BzFluids.SUGAR_WATER_BLOCK.get())) {
            RandomSource random = settings.getRandom(worldPos);
            if (random.nextFloat() < 0.1f) {
                blockState = BzBlocks.HONEY_CRYSTAL.get().defaultBlockState().setValue(HoneyCrystal.WATERLOGGED, true);
            }
        }

        return new StructureTemplate.StructureBlockInfo(worldPos, blockState, nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.BEE_DUNGEON_PROCESSOR.get();
    }
}