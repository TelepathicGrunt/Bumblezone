package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public record RegisterReloadListenerEvent(Consumer<PreparableReloadListener> registrar) {

    public static final EventHandler<RegisterReloadListenerEvent> EVENT = new EventHandler<>();

    public void register(PreparableReloadListener listener) {
        registrar.accept(listener);
    }
}
