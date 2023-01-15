package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;

public record EntitySpawnEvent(Mob entity, LevelAccessor level, boolean isBaby, MobSpawnType spawnType) {

    public static final CancellableEventHandler<EntitySpawnEvent> EVENT = new CancellableEventHandler<>();
}
