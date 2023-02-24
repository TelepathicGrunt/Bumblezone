package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.CancellableEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public record ItemUseOnBlockEvent(Player user, BlockPos clickedPos, BlockState blockstate, ItemStack usingStack) {

    public static final CancellableEventHandler<ItemUseOnBlockEvent> EVENT_HIGH = new CancellableEventHandler<>();

}
