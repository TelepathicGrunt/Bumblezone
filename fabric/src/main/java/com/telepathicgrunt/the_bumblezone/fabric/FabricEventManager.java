package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.fabric.fabricapi.BiomeModificationContextImplMixin;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class FabricEventManager {

    public static void lateInit() {
        if (PlatformHooks.isModLoaded("resourcefulbees") && BzModCompatibilityConfigs.spawnResourcefulBeesHoneycombVeins) {
            BiomeModifications.create(new ResourceLocation(Bumblezone.MODID, "resourceful_bees_compat"))
                .add(ModificationPhase.ADDITIONS,
                    (context) -> context.hasTag(TagKey.create(Registries.BIOME, new ResourceLocation(Bumblezone.MODID, Bumblezone.MODID))),
                    (context) -> {
                        for (Holder<PlacedFeature> placedFeatureHolder : getPlacedFeaturesByTag(context, BzTags.RESOURCEFUL_BEES_COMBS)) {
                            FeatureConfiguration featureConfiguration = placedFeatureHolder.value().feature().value().config();

                            if (featureConfiguration instanceof OreConfiguration oreConfiguration && oreConfiguration.targetStates.stream().noneMatch(e -> e.state.isAir())) {
                                context.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.unwrapKey().get());
                            }
                        }
                    });
        }
    }

    private static Iterable<Holder<PlacedFeature>> getPlacedFeaturesByTag(BiomeModificationContext context, TagKey<PlacedFeature> placedFeatureTagKey) {
        RegistryAccess registryAccess = ((BiomeModificationContextImplMixin)context).getRegistries();
        Registry<PlacedFeature> placedFeatureRegistry = registryAccess.registryOrThrow(Registries.PLACED_FEATURE);
        return placedFeatureRegistry.getTagOrEmpty(placedFeatureTagKey);
    }
}
