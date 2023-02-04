package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public record EntityTravelingToDimensionEvent(ResourceKey<Level> dimension, Entity entity) {

    public static final CancellableEventHandler<EntityTravelingToDimensionEvent> EVENT = new CancellableEventHandler<>();
}
