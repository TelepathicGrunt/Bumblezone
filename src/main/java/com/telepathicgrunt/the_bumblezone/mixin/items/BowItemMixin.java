package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BowItem.class)
public class BowItemMixin {

    @ModifyReceiver(method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private ItemStack thebumblezone_validInfinity(ItemStack ammo, Item item) {
        return ammo.is(BzItems.BEE_STINGER) ? item.getDefaultInstance() : ammo;
    }
}