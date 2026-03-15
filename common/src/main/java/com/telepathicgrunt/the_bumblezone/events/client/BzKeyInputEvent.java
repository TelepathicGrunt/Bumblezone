package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.input.KeyEvent;

public record BzKeyInputEvent(KeyEvent keyEvent) {

    public static final EventHandler<BzKeyInputEvent> EVENT = new EventHandler<>();
}
