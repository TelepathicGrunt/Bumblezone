package com.telepathicgrunt.the_bumblezone.events.player;

import com.telepathicgrunt.the_bumblezone.events.base.ReturnableEventHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record BzPlayerLocateProjectileEvent(ItemStack weapon, LivingEntity shooter) {

    public static final ReturnableEventHandler<BzPlayerLocateProjectileEvent, ItemStack> EVENT = new ReturnableEventHandler<>();
}
