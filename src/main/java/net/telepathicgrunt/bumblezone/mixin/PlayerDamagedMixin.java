package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.telepathicgrunt.bumblezone.items.HoneyCrystalShieldBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public class PlayerDamagedMixin
{
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "damage",
            at = @At(value="INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void playerAttacked(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, PlayerEntity player) {
        HoneyCrystalShieldBehavior.damageShieldFromExplosionAndFire(source, player);
        HoneyCrystalShieldBehavior.slowPhysicalAttackers(source, player);
    }

}