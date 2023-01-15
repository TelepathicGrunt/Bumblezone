package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

public record KeyInputEvent(int key, int scancode, int action) {

    public static final EventHandler<KeyInputEvent> EVENT = new EventHandler<>();
}
