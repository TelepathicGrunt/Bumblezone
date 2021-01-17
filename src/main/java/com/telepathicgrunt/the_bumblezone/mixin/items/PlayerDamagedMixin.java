package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShieldBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public class PlayerDamagedMixin {
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "attackEntityFrom",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void playerAttacked(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        HoneyCrystalShieldBehavior.slowPhysicalAttackers(source, ((PlayerEntity) (Object) this));
        if(HoneyCrystalShieldBehavior.damageShieldFromExplosionAndFire(source, ((PlayerEntity) (Object) this)))
            cir.cancel();
    }

}