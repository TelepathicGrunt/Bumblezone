package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.server.level.ServerPlayer;

public record DatapackSyncEvent(ServerPlayer player) {

    public static final EventHandler<DatapackSyncEvent> EVENT = new EventHandler<>();
}
