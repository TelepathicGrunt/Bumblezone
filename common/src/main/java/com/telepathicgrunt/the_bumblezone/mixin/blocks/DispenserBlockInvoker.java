package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DispenserBlock.class)
public interface DispenserBlockInvoker {

    @Invoker("getDispenseMethod")
    DispenseItemBehavior invokeGetDispenseMethod(ItemStack stack);
}
