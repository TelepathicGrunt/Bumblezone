package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Map;

public record RegisterSpawnPlacementsEvent(Map<EntityType<?>, Placement<?>> placements) {

    public static final EventHandler<RegisterSpawnPlacementsEvent> EVENT = new EventHandler<>();

    public <T extends Entity> void register(EntityType<T> entityType, SpawnPlacements.Type spawn, Heightmap.Types height, SpawnPlacements.SpawnPredicate<T> predicate) {
        placements.put(entityType, new Placement<>(predicate, spawn, height));
    }

    public record Placement<T extends Entity>(SpawnPlacements.SpawnPredicate<T> predicate, SpawnPlacements.Type spawn, Heightmap.Types height) {

    }

}
