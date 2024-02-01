package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.mixin.world.WorldGenRegionAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.List;


public class PollinatedCaves extends Feature<NoneFeatureConfiguration> {
    //https://github.com/Deadrik/TFC2

    protected long seed;
    protected static OpenSimplex2F noiseGen;
    protected static OpenSimplex2F noiseGen2;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            noiseGen2 = new OpenSimplex2F(seed + 3451);
            this.seed = seed;
        }
    }

    public PollinatedCaves(Codec<NoneFeatureConfiguration> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        setSeed(level.getSeed());
        BlockPos.MutableBlockPos mutableBlockPos = context.origin().mutable();

        int disallowedBottomRange = Integer.MAX_VALUE;
        int disallowedTopRange = Integer.MIN_VALUE;
        if (context.level() instanceof WorldGenRegion worldGenRegion) {
            Registry<Structure> structureRegistry = worldGenRegion.registryAccess().registry(Registry.STRUCTURE_REGISTRY).get();
            StructureManager structureManager = ((WorldGenRegionAccessor)worldGenRegion).getStructureManager();
            ChunkPos chunkPos = new ChunkPos(mutableBlockPos);
            List<StructureStart> structureStarts = structureManager.startsForStructure(chunkPos,
                    struct -> structureRegistry.getHolderOrThrow(structureRegistry.getResourceKey(struct).get()).is(BzTags.NO_CAVES));

            for (StructureStart structureStart : structureStarts) {
                disallowedBottomRange = Math.min(disallowedBottomRange, structureStart.getBoundingBox().minY());
                disallowedTopRange = Math.max(disallowedTopRange, structureStart.getBoundingBox().maxY());
            }
        }

        double noise1;
        double noise2;
        double finalNoise;
        ChunkAccess[] cachedChunks = new ChunkAccess[7];
        cachedChunks[6] = level.getChunk(mutableBlockPos);

        for (int y = 15; y < context.chunkGenerator().getGenDepth() - 14; y++) {
            if (y > disallowedBottomRange && y < disallowedTopRange) {
                continue;
            }

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    mutableBlockPos.set(context.origin()).move(x, y, z);

                    if (cachedChunks[6].getSection(cachedChunks[6].getSectionIndex(mutableBlockPos.getY())).hasOnlyAir()) {
                        x = 16;
                        y += 16 - (y % 16);
                        break;
                    }

                    noise1 = noiseGen.noise3_Classic(
                            mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    if (noise1 >= 0.0360555127546399D) {
                        if (noise1 >= 0.6) {
                            z += 6;
                        }
                        else if (noise1 >= 0.4) {
                            z += 4;
                        }
                        else if (noise1 >= 0.2) {
                            z += 2;
                        }
                        continue;
                    }

                    noise2 = noiseGen2.noise3_Classic(
                            mutableBlockPos.getX() * 0.019D,
                            mutableBlockPos.getZ() * 0.019D,
                            mutableBlockPos.getY() * 0.038D);

                    double heightPressure = Math.max((30f - y) / 90f, 0);
                    finalNoise = (noise1 * noise1) + (noise2 * noise2) + heightPressure;

                    if (finalNoise < 0.01305f) {
                        carve(context.level(), mutableBlockPos, finalNoise, noise1, cachedChunks);
                    }
                    else if (finalNoise >= 0.6) {
                        z += 6;
                    }
                    else if (finalNoise >= 0.4) {
                        z += 4;
                    }
                    else if (finalNoise >= 0.2) {
                        z += 2;
                    }
                }
            }
        }

        return true;
    }

    private static void carve(WorldGenLevel world, BlockPos.MutableBlockPos position, double finalNoise, double noise, ChunkAccess[] cachedChunks) {
        ChunkAccess cachedChunk = GeneralUtils.getDirectionalBasedChunkForSpot(world, cachedChunks, null, position, position);

        BlockState currentState = cachedChunk.getBlockState(position);
        if (!currentState.isAir() && currentState.getFluidState().isEmpty() && !currentState.is(BzBlocks.PILE_OF_POLLEN.get())) {
            // varies the surface of the cave surface
            if (finalNoise > 0.0105f) {
                if ((noise * 3) % 2 < 0.35D) {
                    cachedChunk.setBlockState(position, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), false);
                }
                return;
            }

            // cannot carve next to fluids
            BlockPos.MutableBlockPos sidePos = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                ChunkAccess cachedChunkForFluid  = GeneralUtils.getDirectionalBasedChunkForSpot(world, cachedChunks, direction, position, sidePos);
                if (!cachedChunkForFluid.getBlockState(sidePos).getFluidState().isEmpty()) {
                    return;
                }
            }

            // places cave air or pollen pile
            position.move(Direction.DOWN);
            BlockState belowState = world.getBlockState(position);
            position.move(Direction.UP);

            if (!belowState.isAir() && belowState.getFluidState().isEmpty() && belowState.getMaterial().blocksMotion()) {
                cachedChunk.setBlockState(position, BzBlocks.PILE_OF_POLLEN.get().defaultBlockState().setValue(PileOfPollen.LAYERS, (int)Math.max(Math.min((noise + 1D) * 3D, 8), 1)), false);
                world.scheduleTick(position, BzBlocks.PILE_OF_POLLEN.get(), 0);

                int carveHeight = Math.abs((int) ((noise * 1000) % 0.8D)) * 2 + 1;
                for (int i = 0; i < carveHeight; i++) {
                    position.move(Direction.UP);
                    // cannot carve next to fluids
                    for (Direction direction : Direction.values()) {
                        ChunkAccess cachedChunkForFluid = GeneralUtils.getDirectionalBasedChunkForSpot(world, cachedChunks, direction, position, sidePos);
                        if (!cachedChunkForFluid.getBlockState(sidePos).getFluidState().isEmpty()) {
                            return;
                        }
                    }
                    cachedChunk.setBlockState(position, Blocks.CAVE_AIR.defaultBlockState(), false);
                }
                position.move(Direction.DOWN, carveHeight);
            }
            else {
                cachedChunk.setBlockState(position, Blocks.CAVE_AIR.defaultBlockState(), false);
            }
        }
    }
}