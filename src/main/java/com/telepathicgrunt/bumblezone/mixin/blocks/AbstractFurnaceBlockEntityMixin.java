package com.telepathicgrunt.bumblezone.mixin.blocks;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {

    @Inject(method = "burn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void thebumblezone_fillWithStackCount(Recipe<?> recipe, NonNullList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> cir, ItemStack itemStack, ItemStack itemStack2, ItemStack itemStack3) {
        itemStack3.shrink(1); // undo previous increment
        itemStack3.grow(itemStack2.getCount()); // increment properly
    }

}