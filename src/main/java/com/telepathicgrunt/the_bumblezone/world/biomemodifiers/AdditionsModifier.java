package com.telepathicgrunt.the_bumblezone.world.biomemodifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzBiomeModifiers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.fml.ModList;

public record AdditionsModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> feature, GenerationStep.Decoration step, String modid) implements BiomeModifier {

    public static Codec<AdditionsModifier> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(AdditionsModifier::biomes),
            PlacedFeature.LIST_CODEC.fieldOf("feature").forGetter(AdditionsModifier::feature),
            GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AdditionsModifier::step),
            Codec.STRING.fieldOf("required_mod").forGetter(AdditionsModifier::modid)
        ).apply(builder, AdditionsModifier::new));

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        // add a feature to all specified biomes
        if (feature.size() != 0 && phase == Phase.ADD && ModList.get().isLoaded(modid) && biomes.contains(biome)) {

            if (modid.equals("productivebees")) {
                if (BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants.get()) {
                    feature.stream().forEach(pf -> builder.getGenerationSettings().addFeature(step, pf));
                }
            }
            else {
                feature.stream().forEach(pf -> builder.getGenerationSettings().addFeature(step, pf));
            }
        }
    }

    public Codec<? extends BiomeModifier> codec() {
        return BzBiomeModifiers.MODDED_ADDITIONS_MODIFIER.get();
    }
}