package com.telepathicgrunt.the_bumblezone.mixin.enchantments;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Enchantment.class)
public interface EnchantmentAccessor {
    @Accessor("builtInRegistryHolder")
    Holder.Reference<Enchantment> getBuiltInRegistryHolder();
}
