package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShieldBehavior;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class HoneyShieldItemstackMixin {

    @Inject(method = "getMaxDamage",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_getMaxDamageForHoneyCrystalShield(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if(stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD) {
            cir.setReturnValue(HoneyCrystalShieldBehavior.getMaximumDamage(stack));
        }
    }
    @Inject(method = "setDamageValue",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_setDamageForHoneyCrystalShield(int damage, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object)this;
        if(stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD) {
            stack.getOrCreateTag().putInt("Damage", Math.max(0, HoneyCrystalShieldBehavior.setDamage(stack, damage)));
            ci.cancel();
        }
    }
}