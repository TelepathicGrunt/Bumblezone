package com.telepathicgrunt.the_bumblezone.mixin;

import com.telepathicgrunt.the_bumblezone.items.BzItems;
import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShieldBehavior;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class HoneyShieldCooldownMixin {

    @Inject(method = "func_233655_a_",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void isHoneyCrystalShield(PlayerEntity playerEntity, ItemStack itemStack, ItemStack itemStack2, CallbackInfo ci) {
        if(itemStack2.getItem() == BzItems.HONEY_CRYSTAL_SHIELD.get() && !itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.getItem() instanceof AxeItem){
            HoneyCrystalShieldBehavior.setShieldCooldown(playerEntity, ((MobEntity)(Object)this));
            ci.cancel();
        }
    }

}