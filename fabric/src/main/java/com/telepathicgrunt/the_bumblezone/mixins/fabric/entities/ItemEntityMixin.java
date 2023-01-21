package com.telepathicgrunt.the_bumblezone.mixins.fabric.entities;

import com.telepathicgrunt.the_bumblezone.events.player.PlayerPickupItemEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(
            method = "playerTouch",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;take(Lnet/minecraft/world/entity/Entity;I)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void bumelzone$onPlayerTouch(Player player, CallbackInfo ci, ItemStack stack) {
        PlayerPickupItemEvent.EVENT.invoke(new PlayerPickupItemEvent(player, stack.copy()));
    }
}
