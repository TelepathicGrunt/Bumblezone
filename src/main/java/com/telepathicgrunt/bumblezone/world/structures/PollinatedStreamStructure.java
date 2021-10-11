package com.telepathicgrunt.bumblezone.world.structures;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureStart;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class PollinatedStreamStructure extends StructureFeature<NoneFeatureConfiguration> {

    public PollinatedStreamStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    @Override
    public boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeProvider, long seed, WorldgenRandom random, ChunkPos chunkPos1, Biome biome, ChunkPos chunkPos2, NoneFeatureConfiguration config, LevelHeightAccessor heightLimitView) {
        BlockPos centerPos = new BlockPos(chunkPos1.x, 0, chunkPos1.z);

        int height = chunkGenerator.getBaseHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Types.MOTION_BLOCKING, heightLimitView);
        centerPos = centerPos.above(height);
        return biome.getDepth() > 0 || validSpot(chunkGenerator, centerPos, heightLimitView);
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos, LevelHeightAccessor heightLimitView) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(centerPos);
        NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
        BlockState aboveState = columnOfBlocks.getBlockState(mutable.move(Direction.UP, 5));
        if(aboveState.getMaterial().blocksMotion()) return false;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            mutable.set(centerPos).move(direction, 12);
            columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
            BlockState state = columnOfBlocks.getBlockState(mutable.move(Direction.DOWN, 2));
            aboveState = columnOfBlocks.getBlockState(mutable.move(Direction.UP, 7));
            if(!state.getMaterial().blocksMotion() || aboveState.getMaterial().blocksMotion()) {
                return false;
            }

            mutable.set(centerPos).move(direction, 55);
            columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
            state = columnOfBlocks.getBlockState(mutable.move(Direction.DOWN, 5));
            if(!state.getMaterial().blocksMotion()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    /**
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends NoiseAffectingStructureStart<NoneFeatureConfiguration> {

        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, ChunkPos pos, int referenceIn, long seedIn) {
            super(structureIn, pos, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, ChunkPos pos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor heightLimitView) {
            int x = pos.getMinBlockX();
            int z = pos.getMinBlockZ();
            int height = chunkGenerator.getBaseHeight(x, z, Heightmap.Types.MOTION_BLOCKING, heightLimitView);
            BlockPos centerPos = new BlockPos(x, biomeIn.getDepth() > 0 ? random.nextInt(45) + 10 : height - 3, z);

            JigsawPlacement.addPieces(
                    dynamicRegistryManager,
                    new JigsawConfiguration(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(Bumblezone.MODID, "pollinated_stream/waterfall_start")), 12),
                    PoolElementStructurePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    centerPos,
                    this,
                    this.random,
                    false,
                    false,
                    heightLimitView);


            Vec3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
            int xOffset = centerPos.getX() - structureCenter.getX();
            int zOffset = centerPos.getZ() - structureCenter.getZ();
            for(StructurePiece structurePiece : this.pieces){
                // centers the whole structure to structureCenter
                structurePiece.move(xOffset, 0, zOffset);
            }
            this.pieces.removeIf(piece -> piece.getBoundingBox().minY() <= 5);

            // Sets the bounds of the structure once you are finished.
            this.createBoundingBox();
        }

    }
}