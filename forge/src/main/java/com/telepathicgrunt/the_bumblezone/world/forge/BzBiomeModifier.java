package com.telepathicgrunt.the_bumblezone.world.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.the_bumblezone.events.AddFeaturesEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public record BzBiomeModifier(@Nullable RegistryOps<?> ops, List<ModificationEntry> features) implements BiomeModifier {

    public static BzBiomeModifier of(@Nullable RegistryOps<?> ops) {
        List<ModificationEntry> features = new ArrayList<>();
        AddFeaturesEvent.EVENT.invoke(new AddFeaturesEvent((predicate, step, feature)
                -> features.add(new ModificationEntry(predicate, step, feature))));
        return new BzBiomeModifier(ops, features);
    }

    public static final Codec<BzBiomeModifier> CODEC = Codec.PASSTHROUGH.xmap(
            dynamic -> {
                var op = dynamic.getOps() instanceof RegistryOps<?> regOp ? regOp : null;
                return BzBiomeModifier.of(op);
            },
            biomeModifier -> new Dynamic<>(JsonOps.INSTANCE, JsonOps.INSTANCE.empty())
    );

    @Override
    public void modify(Holder<Biome> arg, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase.equals(Phase.ADD) && ops != null) {
            for (var entry : features) {
                if (entry.test(arg)) {
                    ops.getter(Registries.PLACED_FEATURE)
                            .flatMap(registry -> registry.get(entry.feature()))
                            .ifPresent(holder -> builder.getGenerationSettings().addFeature(entry.step(), holder));
                }
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return CODEC;
    }

    public record ModificationEntry(
            Predicate<Holder<Biome>> predicate,
            GenerationStep.Decoration step,
            ResourceKey<PlacedFeature> feature
    ) implements Predicate<Holder<Biome>> {

        @Override
        public boolean test(Holder<Biome> biome) {
            return predicate.test(biome);
        }
    }
}
