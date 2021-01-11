package com.telepathicgrunt.the_bumblezone.mixin;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DispenserBlock.class)
public interface DispenserBlockInvoker {

    @Invoker("getBehavior")
    IDispenseItemBehavior bz_invokeGetBehavior(ItemStack stack);
}
