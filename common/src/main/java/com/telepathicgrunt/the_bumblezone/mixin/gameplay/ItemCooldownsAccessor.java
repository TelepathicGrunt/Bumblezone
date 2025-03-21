package com.telepathicgrunt.the_bumblezone.mixin.gameplay;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemCooldowns.class)
public interface ItemCooldownsAccessor {
    @Accessor("cooldowns")
    Map<Item, ItemCooldowns.CooldownInstance> bumblezone$getCooldowns();

    @Accessor("tickCount")
    int bumblezone$getTickCount();
}
