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
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class PollinatedStreamStructure extends StructureFeature<DefaultFeatureConfig> {

    public PollinatedStreamStructure(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }

    @Override
    public boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeProvider, long seed, ChunkRandom random, ChunkPos chunkPos1, Biome biome, ChunkPos chunkPos2, DefaultFeatureConfig config, HeightLimitView heightLimitView) {
        BlockPos centerPos = new BlockPos(chunkPos1.x, 0, chunkPos1.z);

        int height = chunkGenerator.getHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Type.MOTION_BLOCKING, heightLimitView);
        centerPos = centerPos.up(height);
        return biome.getDepth() > 0 || validSpot(chunkGenerator, centerPos, heightLimitView);
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos, HeightLimitView heightLimitView) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(centerPos);
        VerticalBlockSample columnOfBlocks = chunkGenerator.getColumnSample(mutable.getX(), mutable.getZ(), heightLimitView);
        BlockState aboveState = columnOfBlocks.getState(mutable.move(Direction.UP, 5));
        if(aboveState.getMaterial().blocksMovement()) return false;

        for(Direction direction : Direction.Type.HORIZONTAL) {
            mutable.set(centerPos).move(direction, 12);
            columnOfBlocks = chunkGenerator.getColumnSample(mutable.getX(), mutable.getZ(), heightLimitView);
            BlockState state = columnOfBlocks.getState(mutable.move(Direction.DOWN, 2));
            aboveState = columnOfBlocks.getState(mutable.move(Direction.UP, 7));
            if(!state.getMaterial().blocksMovement() || aboveState.getMaterial().blocksMovement()) {
                return false;
            }

            mutable.set(centerPos).move(direction, 55);
            columnOfBlocks = chunkGenerator.getColumnSample(mutable.getX(), mutable.getZ(), heightLimitView);
            state = columnOfBlocks.getState(mutable.move(Direction.DOWN, 5));
            if(!state.getMaterial().blocksMovement()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public GenerationStep.Feature getGenerationStep() {
        return GenerationStep.Feature.SURFACE_STRUCTURES;
    }

    /**
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends MarginedStructureStart<DefaultFeatureConfig> {

        public Start(StructureFeature<DefaultFeatureConfig> structureIn, ChunkPos pos, int referenceIn, long seedIn) {
            super(structureIn, pos, referenceIn, seedIn);
        }

        @Override
        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, ChunkPos pos, Biome biomeIn, DefaultFeatureConfig config, HeightLimitView heightLimitView) {
            int x = pos.getStartX();
            int z = pos.getStartZ();
            int height = chunkGenerator.getHeight(x, z, Heightmap.Type.MOTION_BLOCKING, heightLimitView);
            BlockPos centerPos = new BlockPos(x, biomeIn.getDepth() > 0 ? random.nextInt(45) + 10 : height - 3, z);

            StructurePoolBasedGenerator.generate(
                    dynamicRegistryManager,
                    new StructurePoolFeatureConfig(() -> dynamicRegistryManager.get(Registry.STRUCTURE_POOL_KEY).get(new Identifier(Bumblezone.MODID, "pollinated_stream/waterfall_start")), 12),
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
            this.children.removeIf(piece -> piece.getBoundingBox().getMinY() <= 5);

            // Sets the bounds of the structure once you are finished.
            this.calculateBoundingBox();
        }

    }
}