package com.telepathicgrunt.the_bumblezone.world.structures;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.block.BlockState;
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

public class PollinatedStreamStructure extends Structure<NoFeatureConfig> {

    public PollinatedStreamStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return PollinatedStreamStructure.Start::new;
    }

    @Override
    public boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long seed, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos centerPos = new BlockPos(x, 0, z);

        int height = chunkGenerator.getBaseHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Type.MOTION_BLOCKING);
        centerPos = centerPos.above(height);
        return biome.getDepth() > 0 || validSpot(chunkGenerator, centerPos);
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(centerPos);
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ());
        BlockState aboveState = columnOfBlocks.getBlockState(mutable.move(Direction.UP, 5));
        if(aboveState.getMaterial().blocksMotion()) return false;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            mutable.set(centerPos).move(direction, 12);
            columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ());
            BlockState state = columnOfBlocks.getBlockState(mutable.move(Direction.DOWN, 2));
            aboveState = columnOfBlocks.getBlockState(mutable.move(Direction.UP, 7));
            if(!state.getMaterial().blocksMotion() || aboveState.getMaterial().blocksMotion()) {
                return false;
            }

            mutable.set(centerPos).move(direction, 55);
            columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ());
            state = columnOfBlocks.getBlockState(mutable.move(Direction.DOWN, 5));
            if(!state.getMaterial().blocksMotion()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    /**
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends StructureStart<NoFeatureConfig>  {

        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            int x = chunkX * 16;
            int z = chunkZ * 16;

            int height = chunkGenerator.getBaseHeight(x, z, Heightmap.Type.MOTION_BLOCKING);
            BlockPos centerPos = new BlockPos(x, biomeIn.getDepth() > 0 ? random.nextInt(45) + 10 : height - 3, z);

            JigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(Bumblezone.MODID, "pollinated_stream/waterfall_start")), 12),
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
            this.pieces.removeIf(piece -> piece.getBoundingBox().y0 <= 5);

            // Sets the bounds of the structure once you are finished.
            this.calculateBoundingBox();
        }

    }
}