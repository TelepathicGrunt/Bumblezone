package net.telepathicgrunt.bumblezone.mixin.blocks;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DispenserBlock.class)
public interface DispenserAccessor {

    @Invoker("getBehaviorForItem")
    DispenserBehavior bz_invokeGetBehaviorForItem(ItemStack stack);
}
