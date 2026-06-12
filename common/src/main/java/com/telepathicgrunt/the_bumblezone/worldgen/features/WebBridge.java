package com.telepathicgrunt.the_bumblezone.worldgen.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyWeb;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFeatures;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.UnsafeBulkSectionAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


public class WebBridge extends Feature<NoneFeatureConfiguration> {

    public WebBridge(Codec<NoneFeatureConfiguration> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        mutableBlockPos.set(context.origin());

        UnsafeBulkSectionAccess bulkSectionAccess = new UnsafeBulkSectionAccess(context.level());
        if (!bulkSectionAccess.getBlockState(mutableBlockPos).is(Blocks.AIR)) {
            return false;
        }

        BlockPos startingPos = null;
        BlockPos endingPos = null;
        int maxBridgeRadius = 20;
        float maxAngle = 30;
        float angle = ((context.random().nextFloat() - 0.5f) * 2) * maxAngle;

        // check for left surface
        for (int i = 0; i < maxBridgeRadius; i++) {
            mutableBlockPos.move(Direction.WEST);
            BlockState currentState = bulkSectionAccess.getBlockState(mutableBlockPos);
            if (!currentState.is(Blocks.AIR) && !currentState.is(Blocks.CAVE_AIR)) {
                if (!currentState.is(BzTags.WEB_BRIDGE_VALID_SURFACES)) {
                    return false;
                }
                startingPos = mutableBlockPos.immutable();
                break;
            }
        }

        if (startingPos == null) {
            return false;
        }

        // check for right surface following an angle
        float yInclineAmount = 0;
        int maxDistToOtherSide = context.origin().getX() - startingPos.getX() + maxBridgeRadius;
        for (int i = 0; i < maxDistToOtherSide; i++) {
            mutableBlockPos.move(Direction.EAST);

            yInclineAmount += (0.3639702342662f * (angle / maxAngle));
            if (Math.abs(yInclineAmount) >= 1) {
                yInclineAmount -= 1 * Math.signum(yInclineAmount);
                mutableBlockPos.move(Direction.UP, (int) (1 * Math.signum(yInclineAmount)));
            }

            BlockState currentState = bulkSectionAccess.getBlockState(mutableBlockPos);
            if (!currentState.is(Blocks.AIR) && !currentState.is(Blocks.CAVE_AIR)) {
                if (!currentState.is(BzTags.WEB_BRIDGE_VALID_SURFACES)) {
                    return false;
                }
                endingPos = mutableBlockPos.immutable();
                break;
            }
        }

        if (endingPos == null) {
            return false;
        }

        // Now we uh go through hell.
        // Or in this case, create a metaball with my own homebrewed shitass equation
        int xDist = Math.abs(endingPos.getX() - startingPos.getX());
        int yDist = endingPos.getY() - startingPos.getY();
        if (xDist < 7) {
            return false;
        }

        int maxYRadius = 3 + (xDist / 10);

        // Better connection to wall
        for (int x = 1; x <= 3; x++) {
            int currentYRadius = (maxYRadius / 3) * (x + 1);

            mutableBlockPos.set(startingPos);
            mutableBlockPos.move(Direction.WEST, 4 - x);
            mutableBlockPos.move(Direction.DOWN, currentYRadius);

            for (int y = -currentYRadius; y < currentYRadius * 2; y++) {
                BlockState currentState = bulkSectionAccess.getBlockState(mutableBlockPos);
                if (validSpace(currentState)) {
                    bulkSectionAccess.setBlockState(
                            mutableBlockPos,
                            BzBlocks.HONEY_WEB.get().defaultBlockState().setValue(HoneyWeb.EASTWEST, true),
                            false);
                }
                mutableBlockPos.move(Direction.UP);
            }
        }

        // The bridge of jank
        float yInclineAmountAccumulated = 0;
        float YInclineAmountPerX = yDist / (float)xDist;
        int halfXDist = xDist / 2;
        for (int x = 0; x <= xDist; x++) {
            int currentYRadius = (int) (maxYRadius * Math.min(Math.max(Math.pow((Math.abs((float)halfXDist - x) / halfXDist), 0.75f), 0.4f), 1));

            mutableBlockPos.set(startingPos);
            mutableBlockPos.move(Direction.EAST, x);

            yInclineAmountAccumulated += YInclineAmountPerX;
            mutableBlockPos.move(Direction.UP, (int) (yInclineAmountAccumulated));

            mutableBlockPos.move(Direction.UP, -currentYRadius);
            for (int y = -currentYRadius; y < currentYRadius * 2; y++) {
                BlockState currentState = bulkSectionAccess.getBlockState(mutableBlockPos);
                if (validSpace(currentState)) {
                    bulkSectionAccess.setBlockState(
                            mutableBlockPos,
                            BzBlocks.HONEY_WEB.get().defaultBlockState().setValue(HoneyWeb.EASTWEST, true),
                            false);
                }

                if (x == 0 || x == xDist || !validSpace(currentState)) {
                    for(int attempt = 0; attempt < 25; attempt++) {
                        BzFeatures.STICKY_HONEY_RESIDUE_FEATURE.get().place(new FeaturePlaceContext<>(
                                context.topFeature(),
                                context.level(),
                                context.chunkGenerator(),
                                context.random(),
                                mutableBlockPos.offset(
                                        context.random().nextInt(5) - 2,
                                        context.random().nextInt(5) - 2,
                                        context.random().nextInt(5) - 2),
                                context.config()
                        ));
                    }
                }

                mutableBlockPos.move(Direction.UP);
            }
        }

        // Better connection to wall
        for (int x = 3; x >= 1; x--) {
            int currentYRadius = (maxYRadius / 3) * (x + 1);

            mutableBlockPos.set(endingPos);
            mutableBlockPos.move(Direction.EAST, 4 - x);
            mutableBlockPos.move(Direction.UP, -currentYRadius);

            for (int y = -currentYRadius; y < currentYRadius * 2; y++) {
                BlockState currentState = bulkSectionAccess.getBlockState(mutableBlockPos);
                if (validSpace(currentState)) {
                    bulkSectionAccess.setBlockState(
                            mutableBlockPos,
                            BzBlocks.HONEY_WEB.get().defaultBlockState().setValue(HoneyWeb.EASTWEST, true),
                            false);
                }
                mutableBlockPos.move(Direction.UP);
            }
        }

        return false;
    }

    private boolean validSpace(BlockState blockState) {
        return blockState.isAir() || blockState.is(BzBlocks.PILE_OF_POLLEN.get()) || blockState.is(BzBlocks.STICKY_HONEY_RESIDUE.get());
    }
}