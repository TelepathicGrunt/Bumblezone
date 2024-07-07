package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

public class BzServerGoingToStopEvent {

    public static final BzServerGoingToStopEvent INSTANCE = new BzServerGoingToStopEvent();

    public static final EventHandler<BzServerGoingToStopEvent> EVENT = new EventHandler<>();

    private BzServerGoingToStopEvent() {}
}
