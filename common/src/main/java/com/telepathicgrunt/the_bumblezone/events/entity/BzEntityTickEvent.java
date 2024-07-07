package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.LivingEntity;

public record BzEntityTickEvent(LivingEntity entity) {

    public static final EventHandler<BzEntityTickEvent> EVENT = new EventHandler<>();
}
