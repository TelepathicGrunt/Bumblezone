package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

public record BzClientTickEvent(boolean end) {

    public static final BzClientTickEvent START = new BzClientTickEvent(false);
    public static final BzClientTickEvent END = new BzClientTickEvent(true);

    public static final EventHandler<BzClientTickEvent> EVENT = new EventHandler<>();
}
