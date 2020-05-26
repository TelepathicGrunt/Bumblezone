package net.telepathicgrunt.bumblezone.features.decorators;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;


public class BeeDungeonPlacer extends Decorator<NopeDecoratorConfig> {
    public BeeDungeonPlacer(Function<Dynamic<?>, ? extends NopeDecoratorConfig> configFactory) {
        super(configFactory);
    }


    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, NopeDecoratorConfig placementConfig, BlockPos pos) {
        ArrayList<BlockPos> validPositions = new ArrayList<BlockPos>();
        BlockPos.Mutable mutable = new BlockPos.Mutable().set(pos);
        boolean validSpot;

        for (int currentAttempt = 0; currentAttempt <= 10; currentAttempt++) {
            validSpot = false;
            int x = random.nextInt(8) + pos.getX() + 4;
            int z = random.nextInt(8) + pos.getZ() + 4;
            int y = random.nextInt(chunkGenerator.getMaxY() - 10 - chunkGenerator.getSeaLevel()) + chunkGenerator.getSeaLevel() + 2;

            //find a cave air spot
            for (Direction face : Direction.Type.HORIZONTAL) {
                mutable.set(x, y, z).setOffset(face, 3);

                if (world.getBlockState(mutable).getBlock() == Blocks.CAVE_AIR)
                    validSpot = true;
            }

            //make sure we aren't too close to regular air
            for (int xOffset = -6; xOffset <= 6; xOffset += 6) {
                for (int zOffset = -6; zOffset <= 6; zOffset += 6) {
                    for (int yOffset = -3; yOffset <= 9; yOffset += 3) {
                        mutable.set(x, y, z).setOffset(xOffset, yOffset, zOffset);

                        if (world.getBlockState(mutable).getBlock() == Blocks.AIR)
                            validSpot = false;
                    }
                }
            }


            mutable.set(x, y, z);
            if (validSpot && world.getBlockState(mutable).isOpaque()) {
                validPositions.add(mutable);
                return validPositions.stream();
            }
        }

        return validPositions.stream();
    }
}
