package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

public record BzKeyInputEvent(int key, int scancode, int action) {

    public static final EventHandler<BzKeyInputEvent> EVENT = new EventHandler<>();
}
