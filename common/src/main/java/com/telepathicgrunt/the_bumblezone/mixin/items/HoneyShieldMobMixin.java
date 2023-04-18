package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShieldBehavior;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class HoneyShieldMobMixin {

    @Inject(method = "maybeDisableShield",
            at = @At(value = "TAIL"))
    private void bumblezone$axeDisablesHoneyCrystalShield(Player playerEntity, ItemStack itemStack, ItemStack itemStack2, CallbackInfo ci) {
        if(!itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack2.getItem() == BzItems.HONEY_CRYSTAL_SHIELD.get() && itemStack.getItem() instanceof AxeItem) {
            HoneyCrystalShieldBehavior.setShieldCooldown(playerEntity, ((Mob)(Object)this));
            playerEntity.level.broadcastEntityEvent(playerEntity, (byte)30);
        }
    }
}