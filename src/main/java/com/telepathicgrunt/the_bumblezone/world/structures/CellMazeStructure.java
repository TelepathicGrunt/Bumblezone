package com.telepathicgrunt.the_bumblezone.world.structures;

import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;

public class CellMazeStructure {

    public static void applyAngerIfInMaze(ServerPlayer serverPlayer) {
        if(serverPlayer.isCreative() || serverPlayer.isSpectator() || !BzConfig.aggressiveBees) {
            return;
        }

        StructureManager structureManager = ((ServerLevel)serverPlayer.level).structureManager();
        Registry<Structure> configuredStructureFeatureRegistry = serverPlayer.level.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
        for (Holder<Structure> configuredStructureFeature : configuredStructureFeatureRegistry.getTagOrEmpty(BzTags.WRATH_CAUSING)) {
            if (structureManager.getStructureAt(serverPlayer.blockPosition(), configuredStructureFeature.value()).isValid()) {
                if (!serverPlayer.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE)) {
                    serverPlayer.addEffect(new MobEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE,
                            BzConfig.howLongWrathOfTheHiveLasts,
                            2,
                            false,
                            BzConfig.showWrathOfTheHiveParticles,
                            true));
                }
            }
        }
    }
}