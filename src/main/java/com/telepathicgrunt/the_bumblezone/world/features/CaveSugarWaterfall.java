package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


public class CaveSugarWaterfall extends Feature<NoneFeatureConfiguration> {

    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();

    public CaveSugarWaterfall(Codec<NoneFeatureConfiguration> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        //creates a waterfall
        BlockPos.MutableBlockPos blockpos$Mutable = new BlockPos.MutableBlockPos().set(context.origin());
        BlockState blockstate = context.level().getBlockState(blockpos$Mutable.above());

        if (!blockstate.canOcclude() || blockstate.is(BzBlockTags.HONEYCOMBS_THAT_FEATURES_CAN_CARVE)) {
            return false;
        } else {
            //checks if we are in the side of a wall with air exposed on one side

            int numberOfSolidSides = 0;
            int neededNumberOfSides;
            blockstate = context.level().getBlockState(blockpos$Mutable.below());

            if (blockstate.canOcclude() && blockstate.is(BzBlockTags.HONEYCOMBS_THAT_FEATURES_CAN_CARVE)) {
                neededNumberOfSides = 3;
            } else if (blockstate.getBlock() == CAVE_AIR.getBlock()) {
                neededNumberOfSides = 4;
            } else {
                return false;
            }


            for (Direction face : Direction.Plane.HORIZONTAL) {
                blockstate = context.level().getBlockState(blockpos$Mutable.relative(face));
                if (blockstate.canOcclude() && blockstate.is(BzBlockTags.HONEYCOMBS_THAT_FEATURES_CAN_CARVE)) {
                    ++numberOfSolidSides;
                } else if (blockstate.getBlock() != CAVE_AIR.getBlock()) {
                    return false;
                }
            }

            //position valid. begin making waterfall
            if (numberOfSolidSides == neededNumberOfSides) {
                context.level().setBlock(blockpos$Mutable, BzFluids.SUGAR_WATER_BLOCK.defaultBlockState(), 2);
                context.level().scheduleTick(blockpos$Mutable, BzFluids.SUGAR_WATER_FLUID, 0);
            }
            return true;
        }
    }

}