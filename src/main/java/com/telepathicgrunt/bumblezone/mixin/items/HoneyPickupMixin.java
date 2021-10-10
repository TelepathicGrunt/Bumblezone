package com.telepathicgrunt.bumblezone.mixin.items;

import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public class HoneyPickupMixin {
    //bees attack player that picks up angerable tagged blocks
    @Inject(method = "playerTouch",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;take(Lnet/minecraft/world/entity/Entity;I)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void thebumblezone_onItemPickup(Player player, CallbackInfo ci, ItemStack itemStack, Item item, int i) {
        BeeAggression.itemPickupAnger(player, item);
    }
}