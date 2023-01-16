package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

public record ServerStoppingEvent() {

    public static final EventHandler<ServerStoppingEvent> EVENT = new EventHandler<>();
}
