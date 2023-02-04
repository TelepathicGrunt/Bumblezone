package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public record RegisterSpawnPlacementsEvent(Registrar registrar) {

    public static final EventHandler<RegisterSpawnPlacementsEvent> EVENT = new EventHandler<>();

    public <T extends Mob> void register(EntityType<T> entityType, SpawnPlacements.Type spawn, Heightmap.Types height, SpawnPlacements.SpawnPredicate<T> predicate) {
        registrar.register(entityType, new Placement<>(predicate, spawn, height));
    }

    public record Placement<T extends Entity>(SpawnPlacements.SpawnPredicate<T> predicate, SpawnPlacements.Type spawn, Heightmap.Types height) {

    }

    @FunctionalInterface
    public interface Registrar {
        <T extends Mob> void register(EntityType<T> type, Placement<T> place);
    }

}
