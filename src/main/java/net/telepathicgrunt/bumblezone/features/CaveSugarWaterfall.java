package net.telepathicgrunt.bumblezone.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;

import java.util.Random;


public class CaveSugarWaterfall extends Feature<DefaultFeatureConfig> {

    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();

    public CaveSugarWaterfall(Codec<DefaultFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos position, DefaultFeatureConfig config) {
        //creates a waterfall
        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable().set(position);
        BlockState blockstate = world.getBlockState(blockpos$Mutable.up());

        if (!blockstate.isOpaque() || Registry.BLOCK.getId(blockstate.getBlock()).getPath().contains("honey")) {
            return false;
        } else {
            //checks if we are in the side of a wall with air exposed on one side

            int numberOfSolidSides = 0;
            int neededNumberOfSides;
            blockstate = world.getBlockState(blockpos$Mutable.down());

            if (blockstate.isOpaque() && Registry.BLOCK.getId(blockstate.getBlock()).getPath().contains("honey")) {
                neededNumberOfSides = 3;
            } else if (blockstate.getBlock() == CAVE_AIR.getBlock()) {
                neededNumberOfSides = 4;
            } else {
                return false;
            }


            for (Direction face : Direction.Type.HORIZONTAL) {
                blockstate = world.getBlockState(blockpos$Mutable.offset(face));
                if (blockstate.isOpaque() && Registry.BLOCK.getId(blockstate.getBlock()).getPath().contains("honey")) {
                    ++numberOfSolidSides;
                } else if (blockstate.getBlock() != CAVE_AIR.getBlock()) {
                    return false;
                }
            }

            //position valid. begin making waterfall
            if (numberOfSolidSides == neededNumberOfSides) {
                world.setBlockState(blockpos$Mutable, BzBlocks.SUGAR_WATER_BLOCK.getDefaultState(), 2);
                world.getFluidTickScheduler().schedule(blockpos$Mutable, BzBlocks.SUGAR_WATER_FLUID, 0);
            }
            return true;
        }
    }

}