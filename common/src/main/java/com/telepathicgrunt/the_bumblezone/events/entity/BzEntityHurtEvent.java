package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record BzEntityHurtEvent(LivingEntity entity, DamageSource source, float amount) {

    public static final EventHandler<BzEntityHurtEvent> EVENT_LOWEST = new EventHandler<>();
}
