package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

import java.util.function.Consumer;

public record BzFinalSetupEvent(Consumer<Runnable> enqueue) {

    public static final EventHandler<BzFinalSetupEvent> EVENT = new EventHandler<>();

    /**
     * Forge runs in parallel with other mods so we need to enqueue some things.
     */
    public void enqueueWork(Runnable runnable) {
        enqueue.accept(runnable);
    }
}
