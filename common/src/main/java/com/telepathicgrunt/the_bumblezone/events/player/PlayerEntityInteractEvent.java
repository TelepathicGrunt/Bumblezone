package com.telepathicgrunt.the_bumblezone.events.player;

import com.telepathicgrunt.the_bumblezone.events.base.ReturnableEventHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record PlayerEntityInteractEvent(Player player, Entity entity, InteractionHand hand) {

    public static final ReturnableEventHandler<PlayerEntityInteractEvent, InteractionResult> PLAYER_ENTITY_INTERACT = new ReturnableEventHandler<>();
}
