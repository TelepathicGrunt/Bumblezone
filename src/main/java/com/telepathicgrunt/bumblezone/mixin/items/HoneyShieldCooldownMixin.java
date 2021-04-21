package com.telepathicgrunt.bumblezone.mixin.items;

import com.telepathicgrunt.bumblezone.items.HoneyCrystalShieldBehavior;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class HoneyShieldCooldownMixin {

    @Inject(method = "disablePlayerShield",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void isHoneyCrystalShield(PlayerEntity playerEntity, ItemStack itemStack, ItemStack itemStack2, CallbackInfo ci) {
        if(itemStack2.getItem() == BzItems.HONEY_CRYSTAL_SHIELD && !itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.getItem() instanceof AxeItem){
            HoneyCrystalShieldBehavior.setShieldCooldown(playerEntity, ((MobEntity)(Object)this));
            ci.cancel();
        }
    }

}