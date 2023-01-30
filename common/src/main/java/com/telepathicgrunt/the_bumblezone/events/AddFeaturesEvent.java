package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Predicate;

public record AddFeaturesEvent(TriConsumer<Predicate<Holder<Biome>>, GenerationStep.Decoration, ResourceKey<PlacedFeature>> adder) {

    public static final EventHandler<AddFeaturesEvent> EVENT = new EventHandler<>();

    public void addFeature(Predicate<Holder<Biome>> predicate, GenerationStep.Decoration step, ResourceLocation feature) {
        addFeature(predicate, step, ResourceKey.create(Registries.PLACED_FEATURE, feature));
    }

    public void addFeature(Predicate<Holder<Biome>> predicate, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
        adder.accept(predicate, step, feature);
    }
}
