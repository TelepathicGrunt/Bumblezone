package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShieldBehavior;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public class HoneyShieldPlayerMixin {

    @WrapOperation(method = "hurtCurrentlyUsedShield",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private boolean thebumblezone_damageHoneyCrystalShield(ItemStack callingItem, Item vanillaShield, Operation<Boolean> originalCall) {
        if(callingItem.is(BzItems.HONEY_CRYSTAL_SHIELD)) {
            return true;
        }
        return originalCall.call(callingItem, vanillaShield);
    }

    @Inject(method = "disableShield",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V"))
    private void thebumblezone_applyCooldownForHoneyCrystalShield(boolean sprinting, CallbackInfo ci) {
        ((Player)(Object)this).getCooldowns().addCooldown(BzItems.HONEY_CRYSTAL_SHIELD, 100);
    }

    //extra effects for honey shield such as slow attackers or melt shield when hit by fire
    @Inject(method = "hurt",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void thebumblezone_playerAttacked(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        HoneyCrystalShieldBehavior.slowPhysicalAttackers(source, ((Player) (Object) this));
        if(HoneyCrystalShieldBehavior.damageShieldFromExplosionAndFire(source, ((Player) (Object) this))) {
            cir.setReturnValue(true);
        }
    }
}