package net.telepathicgrunt.bumblezone.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;

import java.util.Random;
import java.util.function.Function;


public class HoneycombHole extends Feature<DefaultFeatureConfig> {
    

    private static final int[][] bodyLayout =
            {
                    {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 3, 3, 3, 3, 1, 0, 0, 0},
                    {0, 0, 1, 3, 3, 3, 3, 3, 3, 1, 0, 0},
                    {0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0},
                    {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1},
                    {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1},
                    {0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0},
                    {0, 0, 1, 3, 3, 3, 3, 3, 3, 1, 0, 0},
                    {0, 0, 0, 1, 3, 3, 3, 3, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0}
            };

    private static final int[][] largeHoneyLayout =
            {
                    {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
                    {0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 0, 0},
                    {0, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1, 0},
                    {1, 1, 2, 2, 3, 3, 3, 3, 2, 2, 1, 1},
                    {1, 1, 2, 2, 3, 3, 3, 3, 2, 2, 1, 1},
                    {0, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1, 0},
                    {0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0}
            };

    private static final int[][] smallHoneyLayout =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
                    {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
                    {0, 0, 1, 1, 2, 5, 5, 2, 1, 1, 0, 0},
                    {0, 0, 1, 1, 2, 5, 5, 2, 1, 1, 0, 0},
                    {0, 0, 0, 1, 1, 2, 2, 1, 1, 0, 0, 0},
                    {0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] endCapLayout =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final BlockState FILLED_POROUS_HONEYCOMB = BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState();
    private static final BlockState HONEY_BLOCK = Blocks.HONEY_BLOCK.getDefaultState();
    private static final BlockState HONEYCOMB_BLOCK = Blocks.HONEYCOMB_BLOCK.getDefaultState();
    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    private static final BlockState SUGAR_WATER = BzBlocks.SUGAR_WATER_BLOCK.getDefaultState();
    
    public HoneycombHole(Codec<DefaultFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos position, DefaultFeatureConfig config) {
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable().set(position);

        generateSlice(world, mutableBlockPos, endCapLayout, random, true);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), smallHoneyLayout, random, true);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), largeHoneyLayout, random, true);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, random, true);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, random, true);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, random, false);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, random, false);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), bodyLayout, random, false);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), largeHoneyLayout, random, false);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), smallHoneyLayout, random, false);
        generateSlice(world, mutableBlockPos.move(Direction.EAST), endCapLayout, random, false);


        return true;
    }

    private void generateSlice(ServerWorldAccess world, BlockPos.Mutable centerPos, int[][] slice, Random random, boolean westEnd) {
        //move to the position where the corner of the slice will begin at
        BlockPos.Mutable currentPosition = new BlockPos.Mutable().set(centerPos.add(-5, slice.length / 2, -slice[0].length / 2));
        BlockState blockState;

        //go through each row and column while replacing each solid block
        for (int y = 0; y < slice.length; y++) {
            for (int z = 0; z < slice[0].length; z++) {
                //finds solid block
                blockState = world.getBlockState(currentPosition);
                if (world.getBlockState(currentPosition).getMaterial() != Material.AIR && blockState.getFluidState().isEmpty()) {
                    //replace solid block with the slice's blocks
                    int sliceBlock = slice[y][z];
                    if (sliceBlock == 0) {
                        //do nothing.
                    } else if (sliceBlock == 1) {
                        //extra check so the ends of the hole exposed will not have this block
                        if (world.getBlockState(currentPosition.west()).isOpaque() && world.getBlockState(currentPosition.east()).isOpaque()) {
                            //reduced FILLED_POROUS_HONEYCOMB spawn rate
                            if (random.nextInt(3) == 0) {
                                world.setBlockState(currentPosition, HONEYCOMB_BLOCK, 2);
                            } else {
                                world.setBlockState(currentPosition, FILLED_POROUS_HONEYCOMB, 2);
                            }
                        }
                    } else if (sliceBlock == 2) {
                        //reduced HONEY_BLOCK spawn rate
                        if (random.nextInt(3) == 0) {
                            world.setBlockState(currentPosition, FILLED_POROUS_HONEYCOMB, 2);
                        } else {
                            world.setBlockState(currentPosition, HONEY_BLOCK, 2);
                        }
                    } else if (sliceBlock == 3) {
                        if (currentPosition.getY() >= BzDimension.getSeaLevel()) {
                            world.setBlockState(currentPosition, CAVE_AIR, 2);
                        } else {
                            world.setBlockState(currentPosition, SUGAR_WATER, 2);
                        }
                    } else if (sliceBlock == 5) {
                        //reduced HONEY_BLOCK spawn rate
                        int chance = random.nextInt(10);
                        if (chance <= 3) {
                            Direction facing;
                            if (westEnd)
                                facing = Direction.WEST;
                            else
                                facing = Direction.EAST;

                            if (random.nextFloat() < 0.8f)
                                world.setBlockState(currentPosition, BzBlocks.HONEYCOMB_BROOD.getDefaultState().with(HoneycombBrood.STAGE, Integer.valueOf(random.nextInt(3))).with(HoneycombBrood.FACING, facing), 2);
                            else
                                world.setBlockState(currentPosition, BzBlocks.EMPTY_HONEYCOMB_BROOD.getDefaultState().with(HoneycombBrood.FACING, facing), 2);
                        } else if (chance <= 6) {
                            world.setBlockState(currentPosition, FILLED_POROUS_HONEYCOMB, 2);
                        } else {
                            world.setBlockState(currentPosition, HONEY_BLOCK, 2);
                        }
                    }
                }

                //move down the row
                currentPosition.move(Direction.SOUTH);
            }

            //move back to start of row and down 1 column
            currentPosition.move(0, -1, -slice[0].length);
        }
    }
}