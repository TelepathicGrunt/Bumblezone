package com.telepathicgrunt.bumblezone.mixin.items;

import com.telepathicgrunt.bumblezone.items.HoneyCrystalShieldBehavior;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public class PlayerDamagedMixin {
    //extra effects for honey shield such as slow attackers or melt shield when hit by fire
    @Inject(method = "hurt",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void thebumblezone_playerAttacked(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        HoneyCrystalShieldBehavior.slowPhysicalAttackers(source, ((Player) (Object) this));
        if(HoneyCrystalShieldBehavior.damageShieldFromExplosionAndFire(source, ((Player) (Object) this)))
            cir.cancel();
    }
}