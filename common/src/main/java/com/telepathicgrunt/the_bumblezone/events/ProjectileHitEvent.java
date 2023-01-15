package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

public record ProjectileHitEvent(Projectile projectile, HitResult hitResult) {

    public static final CancellableEventHandler<ProjectileHitEvent> EVENT = new CancellableEventHandler<>();
    public static final CancellableEventHandler<ProjectileHitEvent> EVENT_HIGH = new CancellableEventHandler<>();

}
