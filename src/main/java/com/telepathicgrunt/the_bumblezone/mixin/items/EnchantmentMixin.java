package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShield;
import com.telepathicgrunt.the_bumblezone.items.StingerSpearItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Inject(method = "canEnchant",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_isEnchantmentInvalid(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (HoneyCrystalShield.isInvalidForHoneyCrystalShield(stack, ((Enchantment)(Object)this))) {
            cir.setReturnValue(false);
        }
        else if (StingerSpearItem.isInvalidForStingerSpear(stack, ((Enchantment)(Object)this))) {
            cir.setReturnValue(false);
        }
    }
}