package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record RegisterEntityLayersEvent(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> registrar) {

    public static final EventHandler<RegisterEntityLayersEvent> EVENT = new EventHandler<>();

    public void register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        registrar.accept(location, definition);
    }
}
