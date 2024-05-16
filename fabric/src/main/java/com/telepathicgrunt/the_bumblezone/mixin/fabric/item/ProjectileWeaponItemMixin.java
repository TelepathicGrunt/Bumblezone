package com.telepathicgrunt.the_bumblezone.mixin.fabric.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.telepathicgrunt.the_bumblezone.platform.BzArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin {

    @ModifyExpressionValue(method = "hasInfiniteArrows(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    protected static boolean bumblezone$infinityBzAmmo(boolean vanillaArrow, ItemStack weapon, ItemStack ammo) {
        if (!vanillaArrow && ammo.getItem() instanceof BzArrowItem) {
            return true;
        }
        return vanillaArrow;
    }
}
