package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.BiConsumer;

public record BzRegisterReloadListenerEvent(BiConsumer<Identifier, PreparableReloadListener> registrar) {

    public static final EventHandler<BzRegisterReloadListenerEvent> EVENT = new EventHandler<>();

    public void register(Identifier id, PreparableReloadListener listener) {
        registrar.accept(id, listener);
    }
}
