package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import com.telepathicgrunt.the_bumblezone.utils.UnsafeBulkSectionAccess;
import com.telepathicgrunt.the_bumblezone.world.features.configs.BiomeBasedLayerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;


public class LayeredBlockSurface extends Feature<BiomeBasedLayerConfig> {

    protected long seed;
    protected static OpenSimplex2F noiseGen;

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    public LayeredBlockSurface(Codec<BiomeBasedLayerConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<BiomeBasedLayerConfig> context) {
        setSeed(context.level().getSeed());
        BlockPos.MutableBlockPos mutableBlockPos = context.origin().mutable();
        BlockPos.MutableBlockPos mutableBlockPosForChunk = new BlockPos.MutableBlockPos();
        ChunkPos chunkPos = new ChunkPos(mutableBlockPos);
        Biome targetBiome = context.level().registryAccess().registry(Registry.BIOME_REGISTRY).get().get(context.config().biomeRL);

        UnsafeBulkSectionAccess bulkSectionAccess = new UnsafeBulkSectionAccess(context.level());
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int zOffset = -1; zOffset <= 1; zOffset++) {
                ChunkPos currentChunkPos = new ChunkPos(chunkPos.x + xOffset, chunkPos.z + zOffset);
                mutableBlockPosForChunk.set(currentChunkPos.getWorldPosition());
                ChunkAccess cachedChunk = context.level().getChunk(currentChunkPos.getWorldPosition());
                fillChunkWithPollen(context, bulkSectionAccess, currentChunkPos.getWorldPosition(), cachedChunk, targetBiome);
            }
        }
        return true;
    }

    private void fillChunkWithPollen(FeaturePlaceContext<BiomeBasedLayerConfig> context, UnsafeBulkSectionAccess bulkSectionAccess, BlockPos startPos, ChunkAccess chunk, Biome targetBiome) {
        int configHeight = context.config().height;
        BlockState configBlockState = context.config().state;
        BlockPos.MutableBlockPos mutable = context.origin().mutable();
        BlockState currentBlockState;
        BlockState previousBlockState = Blocks.AIR.defaultBlockState();
        boolean configBlockHasLayers = configBlockState.hasProperty(BlockStateProperties.LAYERS);

        int maxY = (context.chunkGenerator().getGenDepth() + context.chunkGenerator().getMinY()) - 1;
        for (int xOffset = 0; xOffset <= 15; xOffset++) {
            for (int zOffset = 0; zOffset <= 15; zOffset++) {
                mutable.set(startPos.getX() + xOffset, maxY, startPos.getZ() + zOffset);
                if(targetBiome != context.level().getBiome(mutable).value()) {
                    continue;
                }

                while (mutable.getY() >= context.chunkGenerator().getMinY()) {
                    currentBlockState = bulkSectionAccess.getBlockState(mutable);

                    if (!currentBlockState.isAir() && currentBlockState.getFluidState().isEmpty() &&
                        !currentBlockState.is(configBlockState.getBlock()) && previousBlockState.getBlock() == Blocks.AIR)
                    {
                        BlockState belowBlockState = bulkSectionAccess.getBlockState(mutable);
                        if (!belowBlockState.isFaceSturdy(context.level(), mutable, Direction.UP)) {
                            previousBlockState = currentBlockState;
                            mutable.move(Direction.DOWN);
                            continue;
                        }

                        for (int height = 1; height <= configHeight && mutable.getY() + height < maxY; height++) {
                            // Vary the pollen piles
                            if (configBlockHasLayers) {
                                int layerHeight = 8;
                                if (height == configHeight) {
                                    float xzScale = 0.035f;
                                    float yScale = 0.015f;
                                    double noiseVal = Math.abs(noiseGen.noise3_Classic(mutable.getX() * xzScale, (mutable.getY() * yScale) + height, mutable.getZ() * xzScale));
                                    layerHeight = Math.max(1, (int) (((noiseVal * 0.6D) + 0.4D) * 8D));
                                    layerHeight = Math.min(8, layerHeight);
                                }
                                bulkSectionAccess.setBlockState(mutable.above(height), configBlockState.setValue(BlockStateProperties.LAYERS, layerHeight), false);
                                context.level().scheduleTick(mutable.above(height), configBlockState.getBlock(), 0);
                            }
                            else {
                                bulkSectionAccess.setBlockState(mutable.above(height), configBlockState, false);
                            }
                        }
                    }

                    previousBlockState = currentBlockState;
                    mutable.move(Direction.DOWN);
                }
            }
        }
    }
}