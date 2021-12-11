package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class HoneyShieldEnchantmentMixin {

    @Inject(method = "canEnchant",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_isHoneyCrystalShield(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD && ((Enchantment)(Object)this) instanceof MendingEnchantment)
            cir.setReturnValue(false);
    }
}