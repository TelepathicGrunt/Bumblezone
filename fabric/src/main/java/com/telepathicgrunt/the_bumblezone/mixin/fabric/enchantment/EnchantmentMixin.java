package com.telepathicgrunt.the_bumblezone.mixin.fabric.enchantment;

import com.telepathicgrunt.the_bumblezone.platform.BzEnchantment;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Inject(
            method = "canEnchant",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void bumblezone$canEnchant(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        Enchantment enchantment = ((Enchantment)(Object)this);
        if (enchantment instanceof BzEnchantment bzEnchantment) {
            OptionalBoolean result = bzEnchantment.bz$canApplyAtEnchantingTable(itemStack);
            if (result.isPresent()) {
                cir.setReturnValue(result.get());
                return;
            }
        }
        if (itemStack.getItem() instanceof ItemExtension extension) {
            OptionalBoolean result = extension.bz$canApplyAtEnchantingTable(itemStack, enchantment);
            if (result.isPresent()) {
                cir.setReturnValue(result.get());
            }
        }
    }
}