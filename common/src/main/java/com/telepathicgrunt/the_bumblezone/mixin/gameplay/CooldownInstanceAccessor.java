package com.telepathicgrunt.the_bumblezone.mixin.gameplay;

import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCooldowns.CooldownInstance.class)
public interface CooldownInstanceAccessor {
    @Accessor("startTime")
    int getStartTime();
}
