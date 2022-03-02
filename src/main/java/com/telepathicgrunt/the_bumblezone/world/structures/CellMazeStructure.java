package com.telepathicgrunt.the_bumblezone.world.structures;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.world.structures.pieces.BuriedStructurePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;

public class CellMazeStructure extends StructureFeature<JigsawConfiguration> {

    public CellMazeStructure(Codec<JigsawConfiguration> codec) {
        super(codec, CellMazeStructure::generatePieces, PostPlacementProcessor.NONE);
    }

    public static void applyAngerIfInMaze(ServerPlayer serverPlayer) {
        if(serverPlayer.isCreative() || serverPlayer.isSpectator() || !Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressiveBees) {
            return;
        }

        StructureFeatureManager structureFeatureManager = ((ServerLevel)serverPlayer.level).structureFeatureManager();
        Registry<ConfiguredStructureFeature<?,?>> configuredStructureFeatureRegistry = serverPlayer.level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
        for (Holder<ConfiguredStructureFeature<?, ?>> configuredStructureFeature : configuredStructureFeatureRegistry.getTag(BzTags.WRATH_CAUSING).get()) {
            if (structureFeatureManager.getStructureAt(serverPlayer.blockPosition(), configuredStructureFeature.value()).isValid()) {
                if (!serverPlayer.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE)) {
                    serverPlayer.addEffect(new MobEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE,
                            Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts,
                            2,
                            false,
                            Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.showWrathOfTheHiveParticles,
                            true));
                }
            }
        }
    }

    private static boolean validSpot(ChunkGenerator chunkGenerator, BlockPos centerPos, LevelHeightAccessor heightLimitView) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int radius = 5;
        for(int x = -radius; x <= radius; x += radius) {
            for(int z = -radius; z <= radius; z += radius) {
                mutable.set(centerPos).move(x * 16, 0, z * 16);
                NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView);
                BlockState state = columnOfBlocks.getBlock(mutable.getY() - 3);
                BlockState aboveState = columnOfBlocks.getBlock(mutable.getY() + 15);
                if(!(state.getFluidState().isEmpty() && !state.isAir()) ||
                    !(aboveState.getFluidState().isEmpty() && !aboveState.isAir()))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> generatePieces(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        WorldgenRandom positionedRandom = new WorldgenRandom(new LegacyRandomSource(context.seed() + (context.chunkPos().x * (context.chunkPos().z * 17L))));
        int height;
        BlockPos centerPos = context.chunkPos().getWorldPosition();

        for (int i = 0; i < 3; i++) {
            int topY = context.chunkGenerator().getMinY() + context.chunkGenerator().getGenDepth();
            int lowerBounds = Math.max(context.chunkGenerator().getSeaLevel(), topY - 120);
            height = lowerBounds + positionedRandom.nextInt(Math.max(topY - (lowerBounds - 10), 1));
            centerPos = context.chunkPos().getMiddleBlockPosition(height);

            if (validSpot(context.chunkGenerator(), centerPos, context.heightAccessor())) {
                break;
            }
            else {
                centerPos = context.chunkPos().getMiddleBlockPosition(context.chunkGenerator().getSeaLevel());
            }
        }

        // increase depth to 12
        JigsawConfiguration newConfig = new JigsawConfiguration(context.config().startPool(), 8);

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