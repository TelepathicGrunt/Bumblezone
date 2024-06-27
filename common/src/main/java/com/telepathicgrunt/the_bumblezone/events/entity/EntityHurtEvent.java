package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record EntityHurtEvent(LivingEntity entity, DamageSource source, float amount) {

    public static final EventHandler<EntityHurtEvent> EVENT_LOWEST = new EventHandler<>();
}
