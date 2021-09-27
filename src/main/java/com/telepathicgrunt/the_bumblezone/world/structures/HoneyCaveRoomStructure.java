package com.telepathicgrunt.the_bumblezone.world.structures;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class HoneyCaveRoomStructure extends Structure<NoFeatureConfig> {

    public HoneyCaveRoomStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return HoneyCaveRoomStructure.Start::new;
    }

    @Override
    public boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long seed, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos centerPos = new BlockPos(x, 0, z);

        SharedSeedRandom positionedRandom = new SharedSeedRandom(seed + (chunkX * (chunkZ * 17L)));
        int height = chunkGenerator.getSeaLevel() + positionedRandom.nextInt(Math.max(chunkGenerator.getGenDepth() - (chunkGenerator.getSeaLevel() + 50), 1));
        centerPos = centerPos.above(height);
        return validSpot(chunkGenerator, centerPos);
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(centerPos);
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ());
        BlockState aboveState = columnOfBlocks.getBlockState(mutable.move(Direction.UP, 8));
        if(aboveState.is(Blocks.AIR)) return false;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            mutable.set(centerPos).move(direction, 24);
            columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ());
            BlockState state = columnOfBlocks.getBlockState(mutable.move(Direction.DOWN, 2));
            aboveState = columnOfBlocks.getBlockState(mutable.move(Direction.UP, 12));
            if(state.is(Blocks.AIR) || aboveState.is(Blocks.AIR)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.LOCAL_MODIFICATIONS;
    }

    /**
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends StructureStart<NoFeatureConfig>  {

        private final long seed;

        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
            seed = seedIn;
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            int x = chunkX * 16;
            int z = chunkZ * 16;

            SharedSeedRandom positionedRandom = new SharedSeedRandom(seed + (chunkX * (chunkZ * 17L)));
            int height = chunkGenerator.getSeaLevel() + positionedRandom.nextInt(Math.max(chunkGenerator.getGenDepth() - (chunkGenerator.getSeaLevel() + 50), 1));
            BlockPos centerPos = new BlockPos(x, height, z);

            JigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(Bumblezone.MODID, "honey_cave_room")), 12),
                    AbstractVillagePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    centerPos,
                    this.pieces,
                    this.random,
                    false,
                    false);


            Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
            int xOffset = centerPos.getX() - structureCenter.getX();
            int zOffset = centerPos.getZ() - structureCenter.getZ();
            for(StructurePiece structurePiece : this.pieces){
                // centers the whole structure to structureCenter
                structurePiece.move(xOffset, 0, zOffset);
            }

            // Sets the bounds of the structure once you are finished.
            this.calculateBoundingBox();
        }

    }
}