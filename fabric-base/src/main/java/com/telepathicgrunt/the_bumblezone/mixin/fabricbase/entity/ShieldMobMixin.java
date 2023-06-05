package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.telepathicgrunt.the_bumblezone.items.BzShieldItem;
import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShield;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class ShieldMobMixin {

    @Inject(method = "maybeDisableShield",
            at = @At(value = "TAIL"))
    private void bumblezone$axeDisablesHoneyCrystalShield(Player playerEntity, ItemStack itemStack, ItemStack itemStack2, CallbackInfo ci) {
        if(!itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack2.getItem() instanceof BzShieldItem && itemStack.getItem() instanceof AxeItem) {
            HoneyCrystalShield.setShieldCooldown(playerEntity, ((Mob)(Object)this));
            playerEntity.level().broadcastEntityEvent(playerEntity, (byte)30);
        }
    }
}