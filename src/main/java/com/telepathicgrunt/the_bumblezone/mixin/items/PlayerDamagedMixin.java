package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.items.dispenserbehavior.HoneyCrystalShieldBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public class PlayerDamagedMixin {
    //extra effects for honey shield such as slow attackers or melt shield when hit by fire
    @Inject(method = "hurt",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void thebumblezone_playerAttacked(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        HoneyCrystalShieldBehavior.slowPhysicalAttackers(source, ((PlayerEntity) (Object) this));
        if(HoneyCrystalShieldBehavior.damageShieldFromExplosionAndFire(source, ((PlayerEntity) (Object) this)))
            cir.cancel();
    }
}