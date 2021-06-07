package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DispenserBlock.class)
public interface DispenserBlockInvoker {

    @Invoker("getDispenseMethod")
    IDispenseItemBehavior thebumblezone_invokeGetDispenseMethod(ItemStack stack);
}
