package com.telepathicgrunt.bumblezone.mixin.items;

import com.telepathicgrunt.bumblezone.items.dispenserbehavior.HoneyCrystalShieldBehavior;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class HoneyShieldCooldownMixin {

    @Inject(method = "maybeDisableShield",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_isHoneyCrystalShield(Player playerEntity, ItemStack itemStack, ItemStack itemStack2, CallbackInfo ci) {
        if(itemStack2.getItem() == BzItems.HONEY_CRYSTAL_SHIELD && !itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.getItem() instanceof AxeItem){
            HoneyCrystalShieldBehavior.setShieldCooldown(playerEntity, ((Mob)(Object)this));
            ci.cancel();
        }
    }

}