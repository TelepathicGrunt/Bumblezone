package com.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class HoneyShieldDisableMixin {

    @Inject(method = "disableShield",
            at = @At(value = "HEAD", target = "Lnet/minecraft/entity/player/PlayerEntity;getItemCooldownManager()Lnet/minecraft/entity/player/ItemCooldownManager;"))
    private void isHoneyCrystalShield(boolean sprinting, CallbackInfo ci) {
        ((PlayerEntity)(Object)this).getCooldownTracker().setCooldown(Items.SHIELD, 100);
    }
}