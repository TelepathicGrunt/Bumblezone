package com.telepathicgrunt.bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;


public class BeeDungeonPlacer extends FeatureDecorator<NoneDecoratorConfiguration> {
    public BeeDungeonPlacer(Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(DecorationContext context, Random random, NoneDecoratorConfiguration placementConfig, BlockPos pos) {
        ArrayList<BlockPos> validPositions = new ArrayList<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(pos);
        boolean validSpot;

        for (int currentAttempt = 0; currentAttempt <= 10; currentAttempt++) {
            validSpot = false;
            int sealevel = context.getLevel().getLevel().getChunkSource().getGenerator().getSeaLevel();
            int x = random.nextInt(8) + pos.getX() + 4;
            int z = random.nextInt(8) + pos.getZ() + 4;
            int y = random.nextInt(context.getGenDepth() - 10 - sealevel) + sealevel + 2;

            //find a cave air spot
            for (Direction face : Direction.Plane.HORIZONTAL) {
                mutable.set(x, y, z).move(face, 3);

                BlockState state = context.getBlockState(mutable);
                if (state.is(Blocks.CAVE_AIR) || state.is(BzBlocks.PILE_OF_POLLEN))
                    validSpot = true;
            }

            //make sure we aren't too close to regular air
            for (int xOffset = -6; xOffset <= 6; xOffset += 6) {
                for (int zOffset = -6; zOffset <= 6; zOffset += 6) {
                    for (int yOffset = -3; yOffset <= 9; yOffset += 3) {
                        mutable.set(x, y, z).move(xOffset, yOffset, zOffset);

                        if (context.getBlockState(mutable).is(Blocks.AIR))
                            validSpot = false;
                    }
                }
            }


            mutable.set(x, y, z);
            if (validSpot && context.getBlockState(mutable).canOcclude()) {
                validPositions.add(mutable);
                break; // Only 1 dungeon max per chunk
            }
        }

        return validPositions.stream();
    }
}
