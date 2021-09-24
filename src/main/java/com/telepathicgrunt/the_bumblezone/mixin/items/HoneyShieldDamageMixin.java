package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.items.dispenserbehavior.HoneyCrystalShieldBehavior;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class HoneyShieldDamageMixin {

    @Inject(method = "hurtCurrentlyUsedShield",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_isHoneyCrystalShield(float amount, CallbackInfo ci) {
        if(HoneyCrystalShieldBehavior.damageHoneyCrystalShield(((PlayerEntity)(Object)this), amount))
            ci.cancel();
    }
}