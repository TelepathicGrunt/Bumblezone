package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
