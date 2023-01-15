package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

public record ClientTickEvent(boolean end) {

    public static final EventHandler<ClientTickEvent> EVENT = new EventHandler<>();
}
