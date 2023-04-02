package com.telepathicgrunt.the_bumblezone.world.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.forge.BzBiomeModifiers;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record BzModCompatBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> feature, GenerationStep.Decoration step, String modid) implements BiomeModifier {

    public static Codec<BzModCompatBiomeModifier> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(BzModCompatBiomeModifier::biomes),
            PlacedFeature.LIST_CODEC.fieldOf("feature").forGetter(BzModCompatBiomeModifier::feature),
            GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(BzModCompatBiomeModifier::step),
            Codec.STRING.fieldOf("required_mod").forGetter(BzModCompatBiomeModifier::modid)
    ).apply(builder, BzModCompatBiomeModifier::new));

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        // add a feature to all specified biomes
        if (feature.size() != 0 && phase == Phase.ADD && PlatformHooks.isModLoaded(modid) && biomes.contains(biome)) {

            if (modid.equals("productivebees")) {
                if (BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants) {
                    feature.stream().forEach(pf -> builder.getGenerationSettings().addFeature(step, pf));
                }
            }
            else if (modid.equals("resourcefulbees")) {
                if (BzModCompatibilityConfigs.spawnResourcefulBeesHoneycombVeins) {
                    feature.stream().filter(placedFeatureHolder -> {
                        FeatureConfiguration featureConfiguration = placedFeatureHolder.get().feature().get().config();
                        if (featureConfiguration instanceof OreConfiguration oreConfiguration) {
                            return oreConfiguration.targetStates.stream().noneMatch(e -> e.state.isAir());
                        }
                        return true;
                    }).forEach(pf -> builder.getGenerationSettings().addFeature(step, pf));
                }
            }
            else {
                feature.stream().forEach(pf -> builder.getGenerationSettings().addFeature(step, pf));
            }
        }
    }

    public Codec<? extends BiomeModifier> codec() {
        return BzBiomeModifiers.BIOME_MODIFIER.get();
    }
}