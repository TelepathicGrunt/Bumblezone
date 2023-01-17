package com.telepathicgrunt.the_bumblezone.mixins.fabric;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    public void bumblezone$onGetMaxDamage(CallbackInfoReturnable<Integer> cir) {
        if (getItem() instanceof ItemExtension extension) {
            cir.setReturnValue((extension).bz$getMaxDamage((ItemStack) (Object) this));
        }
    }

    @Inject(method = "setDamageValue", at = @At("HEAD"), cancellable = true)
    public void bumblezone$onSetDamage(int i, CallbackInfo ci) {
        if (getItem() instanceof ItemExtension extension) {
            (extension).bz$setDamage((ItemStack) (Object) this, i);
            ci.cancel();
        }
    }
}
