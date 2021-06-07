package com.telepathicgrunt.bumblezone.mixin.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class HoneyShieldDisableMixin {

    @Inject(method = "disableShield",
            at = @At(value = "HEAD"))
    private void thebumblezone_isHoneyCrystalShield(boolean sprinting, CallbackInfo ci) {
        ((PlayerEntity)(Object)this).getItemCooldownManager().set(Items.SHIELD, 100);
    }
}