package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.block;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin {

    @Inject(method = "canPlaceItemThroughFace(ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Z",
            at = @At(value = "HEAD"), cancellable = true)
    private void thebumblezone_preventHoneyCocoonInShulker(int slot,
                                                           ItemStack stack,
                                                           Direction dir,
                                                           CallbackInfoReturnable<Boolean> cir)
    {
        if (stack.is(BzItems.HONEY_COCOON.get())) {
            cir.setReturnValue(false);
        }
    }
}