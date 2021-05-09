package com.telepathicgrunt.the_bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;


public class BeeDungeonPlacer extends Placement<NoPlacementConfig> {
    public BeeDungeonPlacer(Codec<NoPlacementConfig> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(WorldDecoratingHelper context, Random random, NoPlacementConfig placementConfig, BlockPos pos) {
        ArrayList<BlockPos> validPositions = new ArrayList<>();
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(pos);
        boolean validSpot;

        for (int currentAttempt = 0; currentAttempt <= 10; currentAttempt++) {
            validSpot = false;
            int x = random.nextInt(4) + pos.getX() + 6;
            int z = random.nextInt(4) + pos.getZ() + 6;
            int y = random.nextInt(context.getGenDepth() - 10 - context.getSeaLevel()) + context.getSeaLevel() + 2;

            //find a cave air spot
            for (Direction face : Direction.Plane.HORIZONTAL) {
                mutable.set(x, y, z).move(face, 3);

                if (context.getBlockState(mutable).getBlock() == Blocks.CAVE_AIR)
                    validSpot = true;
            }

            //make sure we aren't too close to regular air
            for (int xOffset = -6; xOffset <= 6; xOffset += 6) {
                for (int zOffset = -6; zOffset <= 6; zOffset += 6) {
                    for (int yOffset = -3; yOffset <= 9; yOffset += 3) {
                        mutable.set(x, y, z).move(xOffset, yOffset, zOffset);

                        if (context.getBlockState(mutable).getBlock() == Blocks.AIR)
                            validSpot = false;
                    }
                }
            }


            mutable.set(x, y, z);
            if (validSpot && context.getBlockState(mutable).canOcclude()) {
                validPositions.add(mutable);
                return validPositions.stream();
            }
        }

        return Stream.empty();
    }
}
