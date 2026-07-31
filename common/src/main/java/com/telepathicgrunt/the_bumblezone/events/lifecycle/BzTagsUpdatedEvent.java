package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.core.HolderLookup;

public record BzTagsUpdatedEvent(HolderLookup.Provider provider) {

    public static final EventHandler<BzTagsUpdatedEvent> EVENT = new EventHandler<>();
}
