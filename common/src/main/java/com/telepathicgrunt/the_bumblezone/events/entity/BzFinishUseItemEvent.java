package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record BzFinishUseItemEvent(LivingEntity user, ItemStack item, int duration) {

    public static final EventHandler<BzFinishUseItemEvent> EVENT = new EventHandler<>();
}
