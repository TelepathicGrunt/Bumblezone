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
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureStart;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class HoneyCaveRoomStructure extends StructureFeature<NoneFeatureConfiguration> {

    public HoneyCaveRoomStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return Start::new;
    }

    @Override
    public boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeProvider, long seed, WorldgenRandom random, ChunkPos chunkPos1, Biome biome, ChunkPos chunkPos2, NoneFeatureConfiguration config, LevelHeightAccessor heightLimitView) {
        BlockPos centerPos = new BlockPos(chunkPos1.x, 0, chunkPos1.z);

        WorldgenRandom positionedRandom = new WorldgenRandom(seed + (chunkPos1.x * (chunkPos1.z * 17L)));
        int height = chunkGenerator.getSeaLevel() + positionedRandom.nextInt(Math.max(chunkGenerator.getGenDepth() - (chunkGenerator.getSeaLevel() + 50), 1));
        centerPos = centerPos.above(height);
        return validSpot(chunkGenerator, centerPos, heightLimitView);
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos, LevelHeightAccessor heightLimitView) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int radius = 24;
        for(int x = -radius; x <= radius; x += radius) {
            for(int z = -radius; z <= radius; z += radius) {
                mutable.set(centerPos).move(x, 0, z);
                NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
                moveMutable(mutable, Direction.UP, 0, centerPos);
                BlockState state = columnOfBlocks.getBlockState(mutable);
                moveMutable(mutable, Direction.UP, 15, centerPos);
                BlockState aboveState = columnOfBlocks.getBlockState(mutable);
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
    // chunkGenerator.getColumnSample returns column of blocks as if the terrain wasn't mirrored.
    private static void moveMutable(BlockPos.MutableBlockPos mutable, Direction direction, int amount, BlockPos originalPos) {
        if(originalPos.getY() > 128) {
            if(mutable.getY() > 128) {
                mutable.move(Direction.DOWN, (mutable.getY() - 128) * 2);
            }
            mutable.move(direction.getOpposite(), amount);
        }
        else {
            mutable.move(direction, amount);
            if(mutable.getY() > 128) {
                mutable.move(direction.getOpposite(), mutable.getY() - 128);
            }
        }
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.LOCAL_MODIFICATIONS;
    }

    /**
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends NoiseAffectingStructureStart<NoneFeatureConfiguration> {

        private final long seed;

        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, ChunkPos pos, int referenceIn, long seedIn) {
            super(structureIn, pos, referenceIn, seedIn);
            seed = seedIn;
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, ChunkPos pos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor heightLimitView) {

            WorldgenRandom positionedRandom = new WorldgenRandom(seed + (pos.x * (pos.z * 17L)));
            int height = chunkGenerator.getSeaLevel() + positionedRandom.nextInt(Math.max(chunkGenerator.getGenDepth() - (chunkGenerator.getSeaLevel() + 50), 1));
            BlockPos centerPos = new BlockPos(pos.getMinBlockX(), height, pos.getMinBlockZ());

            JigsawPlacement.addPieces(
                    dynamicRegistryManager,
                    new JigsawConfiguration(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(Bumblezone.MODID, "honey_cave_room")), 12),
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

            // Sets the bounds of the structure once you are finished.
            this.createBoundingBox();
        }

    }
}