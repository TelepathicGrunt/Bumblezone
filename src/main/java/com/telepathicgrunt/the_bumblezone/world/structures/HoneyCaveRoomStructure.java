package com.telepathicgrunt.the_bumblezone.world.structures;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.world.structures.pieces.BuriedStructurePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;

public class HoneyCaveRoomStructure extends StructureFeature<JigsawConfiguration> {

    public HoneyCaveRoomStructure(Codec<JigsawConfiguration> codec) {
        super(codec, HoneyCaveRoomStructure::generatePieces, PostPlacementProcessor.NONE);
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos, LevelHeightAccessor heightLimitView) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int radius = 24;
        for(int x = -radius; x <= radius; x += radius) {
            for(int z = -radius; z <= radius; z += radius) {
                mutable.set(centerPos).move(x, 0, z);
                NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
                BlockState state = columnOfBlocks.getBlock(mutable.getY());
                BlockState aboveState = columnOfBlocks.getBlock(mutable.getY() + 15);
                if(state.isAir() || !state.getFluidState().isEmpty() ||
                    aboveState.isAir() || !aboveState.getFluidState().isEmpty())
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> generatePieces(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        WorldgenRandom positionedRandom = new WorldgenRandom(new LegacyRandomSource(context.seed() + (context.chunkPos().x * (context.chunkPos().z * 17L))));
        int height = context.chunkGenerator().getSeaLevel() + positionedRandom.nextInt(Math.max(context.chunkGenerator().getGenDepth() - (context.chunkGenerator().getSeaLevel() + 50), 1));
        BlockPos centerPos = new BlockPos(context.chunkPos().getMinBlockX(), height, context.chunkPos().getMinBlockZ());

        if(!validSpot(context.chunkGenerator(), centerPos, context.heightAccessor())) {
            return Optional.empty();
        }

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

        return JigsawPlacement.addPieces(newContext, BuriedStructurePiece::new, centerPos, false, false);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.LOCAL_MODIFICATIONS;
    }
}