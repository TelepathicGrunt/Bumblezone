package com.telepathicgrunt.the_bumblezone.mixin;

import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DefaultDispenseItemBehavior.class)
public interface DefaultDispenseItemBehaviorInvoker {

    @Invoker("dispenseStack")
    ItemStack invokeDispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_);
}
