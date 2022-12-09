package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import com.telepathicgrunt.the_bumblezone.world.features.configs.BiomeBasedLayerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
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
        Biome targetBiome = context.level().registryAccess().registryOrThrow(Registries.BIOME).get(context.config().biomeRL);

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int zOffset = -1; zOffset <= 1; zOffset++) {
                ChunkPos currentChunkPos = new ChunkPos(chunkPos.x + xOffset, chunkPos.z + zOffset);
                mutableBlockPosForChunk.set(currentChunkPos.getWorldPosition());
                ChunkAccess cachedChunk = context.level().getChunk(currentChunkPos.getWorldPosition());
                //boolean isCenterChunk = (xOffset == 0 && zOffset == 0);
                //if(isCenterChunk || doesBiomeChangeInChunk(context, mutableBlockPosForChunk)) {
                fillChunkWithPollen(context, currentChunkPos.getWorldPosition(), cachedChunk, targetBiome);
                //}
            }
        }
        return true;
    }

    private boolean doesBiomeChangeInChunk(FeaturePlaceContext<BiomeBasedLayerConfig> context, BlockPos.MutableBlockPos mutableBlockPosForChunk) {
        Biome biome = context.level().getNoiseBiome(QuartPos.fromBlock(mutableBlockPosForChunk.getX()), 40, QuartPos.fromBlock(mutableBlockPosForChunk.getZ())).value();
        if(biome != context.level().getNoiseBiome(QuartPos.fromBlock(mutableBlockPosForChunk.getX() + 16), 40, QuartPos.fromBlock(mutableBlockPosForChunk.getZ())).value()){
            return true;
        }
        else if(biome != context.level().getNoiseBiome(QuartPos.fromBlock(mutableBlockPosForChunk.getX()), 40, QuartPos.fromBlock(mutableBlockPosForChunk.getZ() + 16)).value()){
            return true;
        }
        else if(biome != context.level().getNoiseBiome(QuartPos.fromBlock(mutableBlockPosForChunk.getX() + 16), 40, QuartPos.fromBlock(mutableBlockPosForChunk.getZ() + 16)).value()){
            return true;
        }
        else {
            return false;
        }
    }

    private void fillChunkWithPollen(FeaturePlaceContext<BiomeBasedLayerConfig> context, BlockPos startPos, ChunkAccess chunk, Biome targetBiome) {
        int configHeight = context.config().height;
        BlockState configBlockState = context.config().state;
        BlockPos.MutableBlockPos mutable = context.origin().mutable();
        BlockState currentBlockState;
        BlockState previousBlockState = Blocks.AIR.defaultBlockState();
        boolean configBlockHasLayers = configBlockState.hasProperty(BlockStateProperties.LAYERS);

        for (int xOffset = 0; xOffset <= 15; xOffset++) {
            for (int zOffset = 0; zOffset <= 15; zOffset++) {
                mutable.set(startPos.getX() + xOffset, context.chunkGenerator().getGenDepth() + context.chunkGenerator().getMinY(), startPos.getZ() + zOffset);
                if(targetBiome != context.level().getBiome(mutable).value()) {
                    continue;
                }

                while (mutable.getY() >= context.chunkGenerator().getMinY()) {
                    currentBlockState = chunk.getBlockState(mutable);

                    if (!currentBlockState.isAir() && currentBlockState.getFluidState().isEmpty() &&
                        !currentBlockState.is(configBlockState.getBlock()) && previousBlockState.getBlock() == Blocks.AIR)
                    {
                        BlockState belowBlockState = chunk.getBlockState(mutable);
                        if (!belowBlockState.isFaceSturdy(context.level(), mutable, Direction.UP)) {
                            previousBlockState = currentBlockState;
                            mutable.move(Direction.DOWN);
                            continue;
                        }

                        for (int height = 1; height <= configHeight; height++) {
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
                                chunk.setBlockState(mutable.above(height), configBlockState.setValue(BlockStateProperties.LAYERS, layerHeight), false);
                                context.level().scheduleTick(mutable.above(height), configBlockState.getBlock(), 0);
                            }
                            else {
                                chunk.setBlockState(mutable.above(height), configBlockState, false);
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