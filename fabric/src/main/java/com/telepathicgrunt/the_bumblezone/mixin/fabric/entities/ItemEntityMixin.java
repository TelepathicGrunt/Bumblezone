package com.telepathicgrunt.the_bumblezone.mixin.fabric.entities;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerPickupItemEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(method = "playerTouch",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;take(Lnet/minecraft/world/entity/Entity;I)V")
    )
    private void bumelzone$onPlayerTouch(Player player, CallbackInfo ci, @Local(name = "itemStack") ItemStack stack) {
        BzPlayerPickupItemEvent.EVENT.invoke(new BzPlayerPickupItemEvent(player, stack.copy()));
    }
}
