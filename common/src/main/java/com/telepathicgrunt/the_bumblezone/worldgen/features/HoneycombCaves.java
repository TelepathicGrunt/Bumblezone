package com.telepathicgrunt.the_bumblezone.worldgen.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import com.telepathicgrunt.the_bumblezone.utils.UnsafeBulkSectionAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.List;


public class HoneycombCaves extends Feature<NoneFeatureConfiguration> {
    protected long seed;
    protected static OpenSimplex2F noiseGen;
    protected static OpenSimplex2F noiseGen2;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            noiseGen2 = new OpenSimplex2F(seed + 1000);
            this.seed = seed;
        }
    }


    private static final int[][] hexagon7 =
            {
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                    {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
                    {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon6 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon5 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon4 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon3 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon2 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][] hexagon1 =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };

    private static final int[][][] hexagonArray = new int[][][]{hexagon1, hexagon2, hexagon3, hexagon4, hexagon5, hexagon6, hexagon7};

    public HoneycombCaves(Codec<NoneFeatureConfiguration> configFactory) {
        super(configFactory);
    }


    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        setSeed(level.getSeed());
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(context.origin());

        int disallowedBottomRange = Integer.MAX_VALUE;
        int disallowedTopRange = Integer.MIN_VALUE;
        if (context.level() instanceof WorldGenRegion worldGenRegion) {
            Registry<Structure> structureRegistry = worldGenRegion.registryAccess().registry(Registries.STRUCTURE).get();
            StructureManager structureManager = context.level().getLevel().structureManager();
            SectionPos sectionPos = SectionPos.of(mutableBlockPos);
            List<StructureStart> structureStarts = GeneralUtils.startsForAllStructure(worldGenRegion, structureManager, sectionPos,
                    struct -> structureRegistry.getHolderOrThrow(structureRegistry.getResourceKey(struct).get()).is(BzTags.NO_CAVES));

            for (StructureStart structureStart : structureStarts) {
                disallowedBottomRange = Math.min(disallowedBottomRange, structureStart.getBoundingBox().minY());
                disallowedTopRange = Math.max(disallowedTopRange, structureStart.getBoundingBox().maxY());
            }
        }

        int orgX = context.origin().getX();
        int orgY = context.origin().getY();
        int orgZ = context.origin().getZ();

        UnsafeBulkSectionAccess bulkSectionAccess = new UnsafeBulkSectionAccess(context.level());
        for (int y = 15; y < 241; y++) {
            if (y > disallowedBottomRange && y < disallowedTopRange) {
                continue;
            }

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    mutableBlockPos.set(orgX, orgY, orgZ).move(x, y, z);

                    if (bulkSectionAccess.getSection(mutableBlockPos).hasOnlyAir()) {
                        x = 16;
                        y += 16 - (y % 16);
                        break;
                    }

                    double noise1 = noiseGen.noise3_Classic(
                            mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    if (noise1 >= 0.0360555127546399D) {
                        z = zSkipping(z, noise1);
                        continue;
                    }

                    double noise2 = noiseGen2.noise3_Classic(
                            mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    double finalNoise = noise1 * noise1 + noise2 * noise2;

                    if (finalNoise < 0.0013f) {
                        hexagon(level, bulkSectionAccess, context.chunkGenerator(), mutableBlockPos, context.random(), noise1);
                    }
                    else z = zSkipping(z, finalNoise);
                }
            }
        }
        return true;
    }

    private int zSkipping(int z, double noise1) {
        if (noise1 >= 0.8) {
            z += 7;
        }
        else if (noise1 >= 0.7) {
            z += 6;
        }
        else if (noise1 >= 0.6) {
            z += 5;
        }
        else if (noise1 >= 0.5) {
            z += 4;
        }
        else if (noise1 >= 0.4) {
            z += 3;
        }
        else if (noise1 >= 0.3) {
            z += 2;
        }
        else if (noise1 >= 0.2) {
            z += 1;
        }
        return z;
    }

    private static void hexagon(WorldGenLevel world, UnsafeBulkSectionAccess bulkSectionAccess, ChunkGenerator generator, BlockPos position, RandomSource random, double noise) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(position);
        BlockState blockState;
        int index = (int) (((noise * 0.5D) + 0.5D) * 7);
        BlockPos.MutableBlockPos tempMutable = new BlockPos.MutableBlockPos();

        for (int x = 0; x < 14; x++) {
            for (int z = 0; z < 11; z++) {
                int posResult = hexagonArray[index][z][x];
                if (posResult != 0) {
                    mutableBlockPos.set(position).move(x - 7, 0, z - 5);
                    blockState = bulkSectionAccess.getBlockState(mutableBlockPos);
                    carveAtBlock(world, bulkSectionAccess, generator, random, mutableBlockPos, tempMutable, blockState, posResult);

                    mutableBlockPos.set(position).move(0, x - 7, z - 5);
                    blockState = bulkSectionAccess.getBlockState(mutableBlockPos);
                    carveAtBlock(world, bulkSectionAccess, generator, random, mutableBlockPos, tempMutable, blockState, posResult);

                    mutableBlockPos.set(position).move(z - 5, x - 7, 0);
                    blockState = bulkSectionAccess.getBlockState(mutableBlockPos);
                    carveAtBlock(world, bulkSectionAccess, generator, random, mutableBlockPos, tempMutable, blockState, posResult);
                }
            }
        }
    }

    private static void carveAtBlock(WorldGenLevel world,
                                     UnsafeBulkSectionAccess bulkSectionAccess,
                                     ChunkGenerator generator,
                                     RandomSource random,
                                     BlockPos blockPos,
                                     BlockPos.MutableBlockPos mutable,
                                     BlockState blockState,
                                     int posResult)
    {
        if (blockState.canOcclude() && !blockState.canBeReplaced() && !blockState.is(BzTags.FORCE_CAVE_TO_NOT_CARVE)) {
            boolean isNextToAir = shouldCloseOff(bulkSectionAccess, blockPos, mutable);
            if(blockPos.getY() >= generator.getSeaLevel() && isNextToAir) return;

            boolean isBlockEntity = blockState.hasBlockEntity();
            if (posResult == 2) {
                if (blockPos.getY() < generator.getSeaLevel()) {
                    if (isNextToAir) {
                        bulkSectionAccess.setBlockState(blockPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), false);
                    }
                    else {
                        bulkSectionAccess.setBlockState(blockPos, BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState(), false);
                    }

                    if (isBlockEntity) {
                        world.getChunk(blockPos).removeBlockEntity(blockPos);
                    }
                }
                else {
                    bulkSectionAccess.setBlockState(blockPos, Blocks.CAVE_AIR.defaultBlockState(), false);

                    BlockPos abovePos = blockPos.above();
                    BlockState aboveState = bulkSectionAccess.getBlockState(abovePos);
                    if (!aboveState.isAir() && !aboveState.isCollisionShapeFullBlock(world, abovePos)) {
                        bulkSectionAccess.setBlockState(blockPos, Blocks.CAVE_AIR.defaultBlockState(), false);

                        if (aboveState.getBlock() instanceof DoublePlantBlock && aboveState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                            bulkSectionAccess.setBlockState(abovePos, Blocks.CAVE_AIR.defaultBlockState(), false);
                        }

                        if (isBlockEntity) {
                            world.getChunk(blockPos).removeBlockEntity(blockPos);
                        }
                    }
                }
            }
            else if (posResult == 1) {
                if (random.nextInt(3) == 0) {
                    bulkSectionAccess.setBlockState(blockPos, Blocks.HONEYCOMB_BLOCK.defaultBlockState(), false);
                }
                else {
                    bulkSectionAccess.setBlockState(blockPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), false);
                }

                if (isBlockEntity) {
                    world.getChunk(blockPos).removeBlockEntity(blockPos);
                }
            }
        }
    }

    private static boolean shouldCloseOff(UnsafeBulkSectionAccess bulkSectionAccess,
                                          BlockPos position,
                                          BlockPos.MutableBlockPos mutableBlockPos)
    {
        BlockState blockState;
        for (Direction direction : Direction.values()) {
            mutableBlockPos.set(position).move(direction);
            blockState = bulkSectionAccess.getBlockState(mutableBlockPos);

            if (blockState.is(Blocks.AIR)) {
                return true;
            }
        }
        return false;
    }
}