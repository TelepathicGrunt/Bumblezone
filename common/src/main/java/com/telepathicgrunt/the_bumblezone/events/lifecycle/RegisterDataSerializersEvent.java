package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public record RegisterDataSerializersEvent(BiConsumer<ResourceLocation, EntityDataSerializer<?>> registrar) {

    public static final EventHandler<RegisterDataSerializersEvent> EVENT = new EventHandler<>();

    public void register(ResourceLocation id, EntityDataSerializer<?> serializer) {
        registrar.accept(id, serializer);
    }
}
