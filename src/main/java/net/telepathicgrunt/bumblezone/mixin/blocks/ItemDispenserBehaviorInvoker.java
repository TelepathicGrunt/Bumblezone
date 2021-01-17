package net.telepathicgrunt.bumblezone.mixin.blocks;

import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemDispenserBehavior.class)
public interface ItemDispenserBehaviorInvoker {

    @Invoker("dispenseSilently")
    ItemStack bz_invokeDispenseSilently(BlockPointer pointer, ItemStack stack);
}
