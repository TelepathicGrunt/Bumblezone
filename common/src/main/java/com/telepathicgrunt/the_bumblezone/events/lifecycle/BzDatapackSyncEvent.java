package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.server.level.ServerPlayer;

public record BzDatapackSyncEvent(ServerPlayer player) {

    public static final EventHandler<BzDatapackSyncEvent> EVENT = new EventHandler<>();
}
