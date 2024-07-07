package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.core.RegistryAccess;

public record BzTagsUpdatedEvent(RegistryAccess registryAccess, boolean fromPacket) {

    public static final EventHandler<BzTagsUpdatedEvent> EVENT = new EventHandler<>();
}
