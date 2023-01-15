package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record EntityDeathEvent(LivingEntity entity, DamageSource source) {

    public static final CancellableEventHandler<EntityDeathEvent> EVENT = new CancellableEventHandler<>();
}
