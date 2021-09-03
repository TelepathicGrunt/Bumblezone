package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;


public class CaveSugarWaterfall extends Feature<NoFeatureConfig> {

    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();

    public CaveSugarWaterfall(Codec<NoFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NoFeatureConfig config) {
        //creates a waterfall
        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable().set(position);
        BlockState blockstate = world.getBlockState(blockpos$Mutable.above());

        if (!blockstate.canOcclude() || blockstate.is(BzBlockTags.HONEYCOMBS_THAT_FEATURES_CAN_CARVE)) {
            return false;
        } else {
            //checks if we are in the side of a wall with air exposed on one side

            int numberOfSolidSides = 0;
            int neededNumberOfSides;
            blockstate = world.getBlockState(blockpos$Mutable.below());

            if (blockstate.canOcclude() && blockstate.is(BzBlockTags.HONEYCOMBS_THAT_FEATURES_CAN_CARVE)) {
                neededNumberOfSides = 3;
            } else if (blockstate.getBlock() == CAVE_AIR.getBlock()) {
                neededNumberOfSides = 4;
            } else {
                return false;
            }


            for (Direction face : Direction.Plane.HORIZONTAL) {
                blockstate = world.getBlockState(blockpos$Mutable.relative(face));
                if (blockstate.canOcclude() && blockstate.is(BzBlockTags.HONEYCOMBS_THAT_FEATURES_CAN_CARVE)) {
                    ++numberOfSolidSides;
                } else if (blockstate.getBlock() != CAVE_AIR.getBlock()) {
                    return false;
                }
            }

            //position valid. begin making waterfall
            if (numberOfSolidSides == neededNumberOfSides) {
                world.setBlock(blockpos$Mutable, BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState(), 2);
                world.getLiquidTicks().scheduleTick(blockpos$Mutable, BzFluids.SUGAR_WATER_FLUID.get(), 0);
            }
            return true;
        }
    }
}