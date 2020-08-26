package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.item.ItemStack;
import net.telepathicgrunt.bumblezone.items.BzItems;
import net.telepathicgrunt.bumblezone.items.HoneyCrystalShieldBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class HoneyShieldItemstackGetMaximumDamageMixin {

    @Inject(method = "getMaxDamage",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void isHoneyCrystalShield(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack)(Object)this;
        if(stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD) {
            cir.setReturnValue(HoneyCrystalShieldBehavior.getMaximumDamage(stack));
        }
    }
}