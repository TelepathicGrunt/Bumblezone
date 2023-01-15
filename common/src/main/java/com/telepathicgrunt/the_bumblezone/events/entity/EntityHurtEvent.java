package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record EntityHurtEvent(LivingEntity entity, DamageSource source, float amount) {

    public static final CancellableEventHandler<EntityHurtEvent> EVENT = new CancellableEventHandler<>();
}
