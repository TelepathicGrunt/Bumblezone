package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public record RegisterEntityRenderersEvent(Registrar registrar) {

    public static final EventHandler<RegisterEntityRenderersEvent> EVENT = new EventHandler<>();

    public <T extends Entity> void register(EntityType<T> type, EntityRendererProvider<T> provider) {
        registrar.register(type, provider);
    }

    @FunctionalInterface
    public interface Registrar {
        <T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> provider);
    }
}
