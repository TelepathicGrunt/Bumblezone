package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.LivingEntity;

public record EntityTickEvent(LivingEntity entity) {

    public static final EventHandler<EntityTickEvent> EVENT = new EventHandler<>();
}
