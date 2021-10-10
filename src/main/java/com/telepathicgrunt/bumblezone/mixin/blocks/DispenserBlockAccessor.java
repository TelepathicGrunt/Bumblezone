package com.telepathicgrunt.bumblezone.mixin.blocks;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DispenserBlock.class)
public interface DispenserBlockAccessor {

    @Invoker("getDispenseMethod")
    DispenseItemBehavior thebumblezone_invokeGetBehaviorForItem(ItemStack stack);
}
