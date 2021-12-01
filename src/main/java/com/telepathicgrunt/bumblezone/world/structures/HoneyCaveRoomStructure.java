package com.telepathicgrunt.bumblezone.world.structures;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.mixin.JigsawConfigurationAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
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

public class HoneyCaveRoomStructure extends StructureFeature<JigsawConfiguration> {

    public HoneyCaveRoomStructure(Codec<JigsawConfiguration> codec) {
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
        BlockPos centerPos = new BlockPos(context.chunkPos().x, 0,context.chunkPos().z);

        WorldgenRandom positionedRandom = new WorldgenRandom(new LegacyRandomSource(context.seed() + (context.chunkPos().x * (context.chunkPos().z * 17L))));
        int height = context.chunkGenerator().getSeaLevel() + positionedRandom.nextInt(Math.max(context.chunkGenerator().getGenDepth() - (context.chunkGenerator().getSeaLevel() + 50), 1));
        centerPos = centerPos.above(height);
        return validSpot(context.chunkGenerator(), centerPos, context.heightAccessor());
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos, LevelHeightAccessor heightLimitView) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int radius = 24;
        for(int x = -radius; x <= radius; x += radius) {
            for(int z = -radius; z <= radius; z += radius) {
                mutable.set(centerPos).move(x, 0, z);
                NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
                moveMutable(mutable, Direction.UP, 0, centerPos);
                BlockState state = columnOfBlocks.getBlock(mutable.getY());
                moveMutable(mutable, Direction.UP, 15, centerPos);
                BlockState aboveState = columnOfBlocks.getBlock(mutable.getY());
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

    public static Optional<PieceGenerator<JigsawConfiguration>> generatePieces(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        WorldgenRandom positionedRandom = new WorldgenRandom(new LegacyRandomSource(context.seed() + (context.chunkPos().x * (context.chunkPos().z * 17L))));
        int height = context.chunkGenerator().getSeaLevel() + positionedRandom.nextInt(Math.max(context.chunkGenerator().getGenDepth() - (context.chunkGenerator().getSeaLevel() + 50), 1));
        BlockPos centerPos = new BlockPos(context.chunkPos().getMinBlockX(), height, context.chunkPos().getMinBlockZ());
        ((JigsawConfigurationAccessor)context.config()).setMaxDepth(12);
        return JigsawPlacement.addPieces(context, PoolElementStructurePiece::new, centerPos, false, false);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.LOCAL_MODIFICATIONS;
    }
}