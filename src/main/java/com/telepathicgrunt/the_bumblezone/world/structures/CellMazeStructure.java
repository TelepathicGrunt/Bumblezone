package com.telepathicgrunt.the_bumblezone.world.structures;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
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
        if(serverPlayer.isCreative() || serverPlayer.isSpectator() || !BzBeeAggressionConfigs.aggressiveBees.get()) {
            return;
        }

        StructureFeatureManager structureFeatureManager = ((ServerLevel)serverPlayer.level).structureFeatureManager();
        Registry<ConfiguredStructureFeature<?,?>> configuredStructureFeatureRegistry = serverPlayer.level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
        for (Holder<ConfiguredStructureFeature<?, ?>> configuredStructureFeature : configuredStructureFeatureRegistry.getTagOrEmpty(BzTags.WRATH_CAUSING)) {
            if (structureFeatureManager.getStructureAt(serverPlayer.blockPosition(), configuredStructureFeature.value()).isValid()) {
                if (!serverPlayer.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())) {
                    serverPlayer.addEffect(new MobEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE.get(),
                            BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts.get(),
                            2,
                            false,
                            BzBeeAggressionConfigs.showWrathOfTheHiveParticles.get(),
                            true));
                }
            }
        }
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> generatePieces(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        BlockPos centerPos = context.chunkPos().getMiddleBlockPosition(context.chunkGenerator().getSeaLevel());

        // increase depth to 10
        JigsawConfiguration newConfig = new JigsawConfiguration(context.config().startPool(), 10);

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