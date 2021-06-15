package com.telepathicgrunt.bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.bumblezone.modcompat.CharmRedirection;
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
import net.minecraft.world.gen.ChunkRandom;

import java.util.Random;

/**
 * POOL ENTRY MUST BE USING legacy_single_pool_element OR ELSE THE STRUCTURE BLOCK IS REMOVED BEFORE THIS PROCESSOR RUNS.
 */
public class SpiderInfestedBeeDungeonProcessor extends StructureProcessor {

    public static final Codec<SpiderInfestedBeeDungeonProcessor> CODEC = Codec.unit(SpiderInfestedBeeDungeonProcessor::new);
    private SpiderInfestedBeeDungeonProcessor() { }

    @Override
    public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfoLocal, Structure.StructureBlockInfo structureBlockInfoWorld, StructurePlacementData structurePlacementData) {
        BlockState blockState = structureBlockInfoWorld.state;
        BlockPos worldPos = structureBlockInfoWorld.pos;
        Random random = new ChunkRandom();
        random.setSeed(worldPos.asLong() * worldPos.getY());

        // placing altar blocks
        if (blockState.isOf(Blocks.STRUCTURE_BLOCK)) {
            String metadata = structureBlockInfoWorld.nbt.getString("metadata");
            BlockState belowBlock = worldView.getChunk(worldPos).getBlockState(worldPos);

            //altar blocks cannot be placed on air
            if(belowBlock.isAir()){
                blockState = Blocks.CAVE_AIR.getDefaultState();
            }
            else{
                switch (metadata){
                    case "center": {
                        if (random.nextFloat() < 0.6f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.getDefaultState();
                        }
                        else if(ModChecker.charmPresent && random.nextFloat() < 0.25f)
                        {
                            blockState = CharmRedirection.CGetCandle(false, false);
                        }
                        /*
                        else if(ModChecker.beeBetterPresent && random.nextFloat() < 0.2f){
                            blockState = BeeBetterRedirection.getCandle(random);
                        }
                        else if(ModChecker.beeBetterPresent && random.nextFloat() < 0.2f){
                            blockState = BeeBetterRedirection.getCandle(random);
                        }
                         */
                        else if(random.nextFloat() < 0.05f) {
                            blockState = Blocks.COBWEB.getDefaultState();
                        }
                        else {
                            blockState = Blocks.CAVE_AIR.getDefaultState();
                        }
                    }
                    break;
                    case "inner_ring": {
                        if (random.nextFloat() < 0.3f) {
                            blockState = BzBlocks.HONEY_CRYSTAL.getDefaultState();
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
                            blockState = BzBlocks.HONEY_CRYSTAL.getDefaultState();
                        }
                        else if(ModChecker.charmPresent && random.nextFloat() < 0.2f)
                        {
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
        else if(blockState.isOf(Blocks.HONEYCOMB_BLOCK) || blockState.isOf(BzBlocks.FILLED_POROUS_HONEYCOMB)){
           if (random.nextFloat() < 0.15f) {
               blockState = Blocks.HONEYCOMB_BLOCK.getDefaultState();
           }
           /*
           else if(ModChecker.beeBetterPresent && random.nextFloat() < 0.4f){
               blockState = BeeBetterRedirection.getSpiderDungeonBlock(random);
           }
           */
           else {
               blockState = BzBlocks.POROUS_HONEYCOMB.getDefaultState();
           }
        }

        // walls
        else if(blockState.isOf(BzBlocks.HONEYCOMB_BROOD)){
            if (random.nextFloat() < 0.6f) {
                blockState = BzBlocks.EMPTY_HONEYCOMB_BROOD.getDefaultState()
                        .with(HoneycombBrood.FACING, blockState.get(HoneycombBrood.FACING));
            }
            /*
            else if(ModChecker.beeBetterPresent && random.nextFloat() < 0.4f){
                blockState = BeeBetterRedirection.getSpiderDungeonBlock(random);
            }
            */
            else if (random.nextDouble() < Bumblezone.BZ_CONFIG.BZDungeonsConfig.spawnerRateSpiderBeeDungeon) {
                blockState = Blocks.SPAWNER.getDefaultState();
            }
            else {
                blockState = BzBlocks.POROUS_HONEYCOMB.getDefaultState();
            }
        }

        // sugar water
        else if(blockState.isOf(BzBlocks.SUGAR_WATER_BLOCK)){
            blockState = Blocks.CAVE_AIR.getDefaultState();
        }

        return new Structure.StructureBlockInfo(worldPos, blockState, structureBlockInfoWorld.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.SPIDER_INFESTED_BEE_DUNGEON_PROCESSOR;
    }
}