package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DefaultDispenseItemBehavior.class)
public interface DefaultDispenseItemBehaviorInvoker {

    @Invoker("dispenseStack")
    ItemStack bz_invokeDispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_);
}
