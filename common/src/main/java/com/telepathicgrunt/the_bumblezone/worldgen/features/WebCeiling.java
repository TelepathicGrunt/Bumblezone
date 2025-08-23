package com.telepathicgrunt.the_bumblezone.worldgen.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyWeb;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.utils.UnsafeBulkSectionAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


public class WebCeiling extends Feature<NoneFeatureConfiguration> {

    public WebCeiling(Codec<NoneFeatureConfiguration> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        UnsafeBulkSectionAccess bulkSectionAccess = new UnsafeBulkSectionAccess(context.level());
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        mutableBlockPos.set(context.origin()).move(Direction.UP);

        if (!bulkSectionAccess.getBlockState(mutableBlockPos).isAir() &&
            bulkSectionAccess.getBlockState(mutableBlockPos.move(Direction.DOWN)).is(Blocks.AIR))
        {
            Direction.Axis chosenAxis = context.random().nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
            int chosenLength = 5 + context.random().nextInt(4);

            for (int i = 0; i < chosenLength; i++) {
                if (!bulkSectionAccess.getBlockState(mutableBlockPos).isAir()) {
                    break;
                }

                Block newBlock = (i > chosenLength / 2) ? BzBlocks.HONEY_WEB.get() : BzBlocks.REDSTONE_HONEY_WEB.get();
                BlockState newState = newBlock.defaultBlockState().setValue(HoneyWeb.AXIS_TO_PROP.get(chosenAxis), true);
                bulkSectionAccess.setBlockState(mutableBlockPos, newState, false);
                mutableBlockPos.move(Direction.DOWN);
            }

            return true;
        }

        return false;
    }
}