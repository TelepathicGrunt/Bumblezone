package com.telepathicgrunt.the_bumblezone.mixins.fabric;

import com.telepathicgrunt.the_bumblezone.events.player.PlayerLocateProjectileEvent;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerEntityMixin {

    @Shadow @Final private Abilities abilities;

    @Inject(method = "getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"), cancellable = true)
    private void thebumblezone$getProjectile(ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack defaultItem = this.abilities.instabuild ? new ItemStack(Items.ARROW) : null;
        PlayerLocateProjectileEvent event = new PlayerLocateProjectileEvent(itemStack, (Player)((Object) this));
        ItemStack ammo = PlayerLocateProjectileEvent.EVENT.invoke(event, defaultItem);
        if (ammo != null && !ammo.isEmpty()) {
            cir.setReturnValue(ammo);
        }
    }
}
