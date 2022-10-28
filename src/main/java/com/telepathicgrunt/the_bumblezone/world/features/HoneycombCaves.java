package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.mixin.world.WorldGenRegionAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
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
        setSeed(context.level().getSeed());
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(context.origin());

        int disallowedBottomRange = Integer.MAX_VALUE;
        int disallowedTopRange = Integer.MIN_VALUE;
        if (context.level() instanceof WorldGenRegion worldGenRegion) {
            Registry<Structure> structureRegistry = worldGenRegion.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
            StructureManager structureManager = ((WorldGenRegionAccessor)worldGenRegion).getStructureManager();
            ChunkPos chunkPos = new ChunkPos(mutableBlockPos);
            List<StructureStart> structureStarts = structureManager.startsForStructure(chunkPos,
                    struct -> structureRegistry.getOrCreateHolderOrThrow(structureRegistry.getResourceKey(struct).get()).is(BzTags.NO_CAVES));

            for (StructureStart structureStart : structureStarts) {
                disallowedBottomRange = Math.min(disallowedBottomRange, structureStart.getBoundingBox().minY());
                disallowedTopRange = Math.max(disallowedTopRange, structureStart.getBoundingBox().maxY());
            }
        }

        int orgX = context.origin().getX();
        int orgY = context.origin().getY();
        int orgZ = context.origin().getZ();

        for (int y = 15; y < 241; y++) {
            if (y > disallowedBottomRange && y < disallowedTopRange) {
                continue;
            }

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    mutableBlockPos.set(orgX, orgY, orgZ).move(x, y, z);

                    double noise1 = noiseGen.noise3_Classic(
                            mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    if(noise1 >= 0.0360555127546399D) {
                        continue;
                    }

                    double noise2 = noiseGen2.noise3_Classic(
                            mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    double finalNoise = noise1 * noise1 + noise2 * noise2;

                    if (finalNoise < 0.0013f) {
                        hexagon(context.level(), context.chunkGenerator(), mutableBlockPos, context.random(), noise1);
                    }
                }
            }
        }

        return true;
    }

    private static void hexagon(WorldGenLevel world, ChunkGenerator generator, BlockPos position, RandomSource random, double noise) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(position);
        BlockState blockState;
        int index = (int) (((noise * 0.5D) + 0.5D) * 7);
        BlockPos.MutableBlockPos tempMutable = new BlockPos.MutableBlockPos();
        ChunkAccess chunk = null;

        for (int x = 0; x < 14; x++) {
            for (int z = 0; z < 11; z++) {
                int posResult = hexagonArray[index][z][x];
                int abovePosResult = z == 10 ? 0 : hexagonArray[index][z + 1][x];

                if (posResult != 0) {
                    mutableBlockPos.set(position).move(x - 7, 0, z - 5);
                    chunk = getChunkForSpot(world, chunk, mutableBlockPos);
                    blockState = chunk.getBlockState(mutableBlockPos);
                    carveAtBlock(world, generator, random, mutableBlockPos, tempMutable, blockState, posResult, abovePosResult);

                    mutableBlockPos.set(position).move(0, x - 7, z - 5);
                    chunk = getChunkForSpot(world, chunk, mutableBlockPos);
                    blockState = chunk.getBlockState(mutableBlockPos);
                    carveAtBlock(world, generator, random, mutableBlockPos, tempMutable, blockState, posResult, abovePosResult);

                    mutableBlockPos.set(position).move(z - 5, x - 7, 0);
                    chunk = getChunkForSpot(world, chunk, mutableBlockPos);
                    blockState = chunk.getBlockState(mutableBlockPos);
                    carveAtBlock(world, generator, random, mutableBlockPos, tempMutable, blockState, posResult, abovePosResult);
                }
            }
        }
    }

    private static void carveAtBlock(WorldGenLevel world,
                                     ChunkGenerator generator,
                                     RandomSource random,
                                     BlockPos blockPos,
                                     BlockPos.MutableBlockPos mutable,
                                     BlockState blockState,
                                     int posResult,
                                     int abovePosResult)
    {
        if (blockState.canOcclude()) {
            ChunkAccess chunk = getChunkForSpot(world, null, blockPos);
            boolean isNextToAir = shouldCloseOff(world, chunk, blockPos, mutable);
            if(blockPos.getY() >= generator.getSeaLevel() && isNextToAir) return;

            if (posResult == 2) {
                if (blockPos.getY() < generator.getSeaLevel()) {
                    if(isNextToAir)
                        world.setBlock(blockPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), 3);
                    else
                        world.setBlock(blockPos, BzFluids.SUGAR_WATER_BLOCK.get().defaultBlockState(), 3);
                }
                else {
                    world.setBlock(blockPos, Blocks.CAVE_AIR.defaultBlockState(), 3);
                    if(abovePosResult <= 1) {
                        BlockPos abovePos = blockPos.above();
                        BlockState aboveState = world.getBlockState(abovePos);
                        if(!aboveState.isAir() && !aboveState.isCollisionShapeFullBlock(world, abovePos)) {
                            world.setBlock(abovePos, Blocks.CAVE_AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
            else if (posResult == 1) {
                if (random.nextInt(3) == 0) {
                    world.setBlock(blockPos, Blocks.HONEYCOMB_BLOCK.defaultBlockState(), 3);
                }
                else {
                    world.setBlock(blockPos, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), 3);
                }
            }
        }
    }

    private static boolean shouldCloseOff(WorldGenLevel world,
                                          ChunkAccess chunk,
                                          BlockPos position,
                                          BlockPos.MutableBlockPos mutableBlockPos)
    {
        BlockState blockState;
        for (Direction direction : Direction.values()) {
            mutableBlockPos.set(position).move(direction);
            chunk = getChunkForSpot(world, chunk, mutableBlockPos);
            blockState = chunk.getBlockState(mutableBlockPos);

            if (blockState.is(Blocks.AIR)) {
                return true;
            }
        }
        return false;
    }

    private static ChunkAccess getChunkForSpot(WorldGenLevel world, ChunkAccess chunkAccess, BlockPos blockPos) {
        if (chunkAccess == null || chunkAccess.getPos().x != blockPos.getX() >> 4 || chunkAccess.getPos().z != blockPos.getZ() >> 4) {
            return world.getChunk(blockPos);
        }
        return chunkAccess;
    }
}