package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;

public class ServerGoingToStopEvent {

    public static final ServerGoingToStopEvent INSTANCE = new ServerGoingToStopEvent();

    public static final EventHandler<ServerGoingToStopEvent> EVENT = new EventHandler<>();

    private ServerGoingToStopEvent() {}
}
