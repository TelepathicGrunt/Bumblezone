package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

public record ClientTickEvent(boolean end) {

    public static final ClientTickEvent START = new ClientTickEvent(false);
    public static final ClientTickEvent END = new ClientTickEvent(true);

    public static final EventHandler<ClientTickEvent> EVENT = new EventHandler<>();
}
