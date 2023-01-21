package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record RegisterReloadListenerEvent(BiConsumer<ResourceLocation, PreparableReloadListener> registrar) {

    public static final EventHandler<RegisterReloadListenerEvent> EVENT = new EventHandler<>();

    public void register(ResourceLocation id, PreparableReloadListener listener) {
        registrar.accept(id, listener);
    }
}
