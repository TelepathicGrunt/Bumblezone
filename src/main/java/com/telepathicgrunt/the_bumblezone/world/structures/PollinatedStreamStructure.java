package com.telepathicgrunt.the_bumblezone.world.structures;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.Optional;

public class PollinatedStreamStructure extends StructureFeature<JigsawConfiguration> {

    public PollinatedStreamStructure(Codec<JigsawConfiguration> codec) {
        super(codec, (context) -> {
                    if (!isFeatureChunk(context)) {
                        return Optional.empty();
                    }
                    else {
                        return generatePieces(context);
                    }
                },
                PostPlacementProcessor.NONE);
    }

    protected static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        BlockPos centerPos = new BlockPos(context.chunkPos().x, 0, context.chunkPos().z);
        int height = context.chunkGenerator().getBaseHeight(centerPos.getX(), centerPos.getZ(), Heightmap.Types.MOTION_BLOCKING, context.heightAccessor());
        centerPos = centerPos.above(height);
        return validSpot(context.chunkGenerator(), centerPos, context.heightAccessor());
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos, LevelHeightAccessor heightLimitView) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(centerPos);
        NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
        BlockState aboveState = columnOfBlocks.getBlock(mutable.move(Direction.UP, 5).getY());
        if(aboveState.getMaterial().blocksMotion()) return false;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            mutable.set(centerPos).move(direction, 12);
            columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
            BlockState state = columnOfBlocks.getBlock(mutable.move(Direction.DOWN, 2).getY());
            aboveState = columnOfBlocks.getBlock(mutable.move(Direction.UP, 7).getY());
            if(!state.getMaterial().blocksMotion() || aboveState.getMaterial().blocksMotion()) {
                return false;
            }

            mutable.set(centerPos).move(direction, 55);
            columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
            state = columnOfBlocks.getBlock(mutable.move(Direction.DOWN, 5).getY());
            if(!state.getMaterial().blocksMotion()) {
                return false;
            }
        }

        return true;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> generatePieces(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        WorldgenRandom positionedRandom = new WorldgenRandom(new LegacyRandomSource(context.seed() + (context.chunkPos().x * (context.chunkPos().z * 17L))));
        int x = context.chunkPos().getMinBlockX();
        int z = context.chunkPos().getMinBlockZ();
        BlockPos centerPos = new BlockPos(x, positionedRandom.nextInt(45) + 10, z);

        // increase depth to 12
        JigsawConfiguration newConfig = new JigsawConfiguration(context.config().startPool(), 12);

        // Create a new context with the new config that has our json pool. We will pass this into JigsawPlacement.addPieces
        PieceGeneratorSupplier.Context<JigsawConfiguration> newContext = new PieceGeneratorSupplier.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.heightAccessor(),
                context.validBiome(),
                context.structureManager(),
                context.registryAccess()
        );

        return JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, centerPos, false, false);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

}