package com.telepathicgrunt.bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;


public class BeeDungeonPlacer extends PlacementModifier {
    private static final BeeDungeonPlacer INSTANCE = new BeeDungeonPlacer();
    public static final Codec<BeeDungeonPlacer> CODEC = Codec.unit(() -> INSTANCE);

    public static BeeDungeonPlacer beeDungeonPlacer() {
        return INSTANCE;
    }

    @Override
    public PlacementModifierType<?> type() {
        return BzPlacements.BEE_DUNGEON_PLACER;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, Random random, BlockPos blockPos) {
        ArrayList<BlockPos> validPositions = new ArrayList<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(blockPos);
        boolean validSpot;

        for (int currentAttempt = 0; currentAttempt <= 10; currentAttempt++) {
            validSpot = false;
            int sealevel = placementContext.getLevel().getLevel().getChunkSource().getGenerator().getSeaLevel();
            int x = random.nextInt(8) + blockPos.getX() + 4;
            int z = random.nextInt(8) + blockPos.getZ() + 4;
            int y = random.nextInt(placementContext.getGenDepth() - 10 - sealevel) + sealevel + 2;

            //find a cave air spot
            for (Direction face : Direction.Plane.HORIZONTAL) {
                mutable.set(x, y, z).move(face, 3);

                BlockState state = placementContext.getBlockState(mutable);
                if (state.is(Blocks.CAVE_AIR) || state.is(BzBlocks.PILE_OF_POLLEN))
                    validSpot = true;
            }

            //make sure we aren't too close to regular air
            for (int xOffset = -6; xOffset <= 6; xOffset += 6) {
                for (int zOffset = -6; zOffset <= 6; zOffset += 6) {
                    for (int yOffset = -3; yOffset <= 9; yOffset += 3) {
                        mutable.set(x, y, z).move(xOffset, yOffset, zOffset);

                        if (placementContext.getBlockState(mutable).is(Blocks.AIR))
                            validSpot = false;
                    }
                }
            }


            mutable.set(x, y, z);
            if (validSpot && placementContext.getBlockState(mutable).canOcclude()) {
                validPositions.add(mutable);
                break; // Only 1 dungeon max per chunk
            }
        }

        return validPositions.stream();
    }
}
