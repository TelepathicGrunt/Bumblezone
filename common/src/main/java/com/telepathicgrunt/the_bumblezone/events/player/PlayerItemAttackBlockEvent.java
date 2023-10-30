package com.telepathicgrunt.the_bumblezone.events.player;

import com.telepathicgrunt.the_bumblezone.events.base.ReturnableEventHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record PlayerItemAttackBlockEvent(Player user, Level level, InteractionHand hand, ItemStack usingStack) {

    public static final ReturnableEventHandler<PlayerItemAttackBlockEvent, InteractionResult> EVENT_HIGH = new ReturnableEventHandler<>();

}
