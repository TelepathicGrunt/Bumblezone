package com.telepathicgrunt.bumblezone.mixin.items;

import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public class HoneyPickupMixin {
    //bees attack player that picks up honey blocks
    @Inject(method = "onPlayerCollision",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void thebumblezone_onItemPickup(PlayerEntity player, CallbackInfo ci, ItemStack itemStack, Item item, int i) {
        BeeAggression.honeyPickupAnger(player, item);
    }

}