package com.telepathicgrunt.bumblezone.world.structures;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.block.BlockState;
import net.minecraft.structure.MarginedStructureStart;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class HoneyCaveRoomStructure extends StructureFeature<DefaultFeatureConfig> {

    public HoneyCaveRoomStructure(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }

    @Override
    public boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeProvider, long seed, ChunkRandom random, ChunkPos chunkPos1, Biome biome, ChunkPos chunkPos2, DefaultFeatureConfig config, HeightLimitView heightLimitView) {
        BlockPos centerPos = new BlockPos(chunkPos1.x, 0, chunkPos1.z);

        ChunkRandom positionedRandom = new ChunkRandom(seed + (chunkPos1.x * (chunkPos1.z * 17L)));
        int height = chunkGenerator.getSeaLevel() + positionedRandom.nextInt(Math.max(chunkGenerator.getWorldHeight() - (chunkGenerator.getSeaLevel() + 50), 1));
        centerPos = centerPos.up(height);
        return validSpot(chunkGenerator, centerPos, heightLimitView);
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos, HeightLimitView heightLimitView) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int radius = 24;
        for(int x = -radius; x <= radius; x += radius) {
            for(int z = -radius; z <= radius; z += radius) {
                mutable.set(centerPos).move(x, 0, z);
                VerticalBlockSample columnOfBlocks = chunkGenerator.getColumnSample(mutable.getX(), mutable.getZ(), heightLimitView);
                BlockState state = columnOfBlocks.getState(mutable);
                moveMutable(mutable, Direction.UP, 15, centerPos);
                BlockState aboveState = columnOfBlocks.getState(mutable);
                if(state.isAir() || !state.getFluidState().isEmpty() ||
                    aboveState.isAir() || !aboveState.getFluidState().isEmpty())
                {
                    return false;
                }
            }
        }

        return true;
    }

    // Takes into account how bumblezone's terrain is bottom half reflected across top half.
    // chunkGenerator.getBaseColumn returns column of blocks as if the terrain wasn't mirrored.
    private static void moveMutable(BlockPos.Mutable mutable, Direction direction, int amount, BlockPos originalPos) {
        if(originalPos.getY() > 128) {
            mutable.move(direction.getOpposite(), amount);
            if(mutable.getY() > 128) {
                mutable.move(direction, mutable.getY() - 128);
            }
        }
        else {
            mutable.move(direction, amount);
            if(mutable.getY() > 128) {
                mutable.move(direction.getOpposite(), mutable.getY() - 128);
            }
        }
    }

    @Override
    public GenerationStep.Feature getGenerationStep() {
        return GenerationStep.Feature.LOCAL_MODIFICATIONS;
    }

    /**
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends MarginedStructureStart<DefaultFeatureConfig> {

        private final long seed;

        public Start(StructureFeature<DefaultFeatureConfig> structureIn, ChunkPos pos, int referenceIn, long seedIn) {
            super(structureIn, pos, referenceIn, seedIn);
            seed = seedIn;
        }

        @Override
        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, ChunkPos pos, Biome biomeIn, DefaultFeatureConfig config, HeightLimitView heightLimitView) {

            ChunkRandom positionedRandom = new ChunkRandom(seed + (pos.x * (pos.z * 17L)));
            int height = chunkGenerator.getSeaLevel() + positionedRandom.nextInt(Math.max(chunkGenerator.getWorldHeight() - (chunkGenerator.getSeaLevel() + 50), 1));
            BlockPos centerPos = new BlockPos(pos.getStartX(), height, pos.getStartZ());

            StructurePoolBasedGenerator.generate(
                    dynamicRegistryManager,
                    new StructurePoolFeatureConfig(() -> dynamicRegistryManager.get(Registry.STRUCTURE_POOL_KEY).get(new Identifier(Bumblezone.MODID, "honey_cave_room")), 12),
                    PoolStructurePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    centerPos,
                    this,
                    this.random,
                    false,
                    false,
                    heightLimitView);


            Vec3i structureCenter = this.children.get(0).getBoundingBox().getCenter();
            int xOffset = centerPos.getX() - structureCenter.getX();
            int zOffset = centerPos.getZ() - structureCenter.getZ();
            for(StructurePiece structurePiece : this.children){
                // centers the whole structure to structureCenter
                structurePiece.translate(xOffset, 0, zOffset);
            }

            // Sets the bounds of the structure once you are finished.
            this.calculateBoundingBox();
        }

    }
}