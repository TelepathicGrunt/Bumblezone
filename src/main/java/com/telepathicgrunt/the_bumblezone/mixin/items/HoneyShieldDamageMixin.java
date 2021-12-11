package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShieldBehavior;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class HoneyShieldDamageMixin {

    @Inject(method = "hurtCurrentlyUsedShield",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_isHoneyCrystalShield(float amount, CallbackInfo ci) {
        if(HoneyCrystalShieldBehavior.damageHoneyCrystalShield(((Player)(Object)this), amount))
            ci.cancel();
    }
}