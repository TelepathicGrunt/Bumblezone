package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DispenserBlock.class)
public interface DispenserAccessor {

    @Invoker("getBehaviorForItem")
    DispenserBehavior invokeGetBehaviorForItem(ItemStack stack);
}
