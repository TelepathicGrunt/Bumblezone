package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record BabySpawnEvent(Mob parent1, Mob parent2, @Nullable Player player, AgeableMob child) {

    public static final CancellableEventHandler<BabySpawnEvent> EVENT = new CancellableEventHandler<>();

}
