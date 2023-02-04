package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.core.RegistryAccess;

public record TagsUpdatedEvent(RegistryAccess registryAccess, boolean fromPacket) {

    public static final EventHandler<TagsUpdatedEvent> EVENT = new EventHandler<>();
}
