package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

import java.util.function.Consumer;

public record BzClientSetupEnqueuedEvent(Consumer<Runnable> enqueue) {

    public static final EventHandler<BzClientSetupEnqueuedEvent> EVENT = new EventHandler<>();

    /**
     * Forge runs in parallel with other mods so we need to enqueue some things.
     */
    public void enqueueWork(Runnable runnable) {
        enqueue.accept(runnable);
    }
}
