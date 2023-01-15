package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

public record ServerStoppedEvent() {

    public static final EventHandler<ServerStoppedEvent> EVENT = new EventHandler<>();
}
