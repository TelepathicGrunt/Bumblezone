package com.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.player.PlayerEntity;
import com.telepathicgrunt.bumblezone.items.HoneyCrystalShieldBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class HoneyShieldDamageMixin {

    @Inject(method = "damageShield",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void isHoneyCrystalShield(float amount, CallbackInfo ci) {
        if(HoneyCrystalShieldBehavior.damageHoneyCrystalShield(((PlayerEntity)(Object)this), amount))
            ci.cancel();
    }
}