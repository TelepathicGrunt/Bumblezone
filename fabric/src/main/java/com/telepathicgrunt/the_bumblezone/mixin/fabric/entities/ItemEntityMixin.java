package com.telepathicgrunt.the_bumblezone.mixin.fabric.entities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerPickupItemEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @WrapOperation(method = "playerTouch",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z")
    )
    private boolean bumblezone$onPlayerTouch(Inventory instance, ItemStack itemStack, Operation<Boolean> original, Player player) {
        ItemStack copy = itemStack.copy();
        boolean addedToInventory = original.call(instance, itemStack);
        if (addedToInventory) {
            BzPlayerPickupItemEvent.EVENT.invoke(new BzPlayerPickupItemEvent(player, copy));
        }
        return addedToInventory;
    }
}
