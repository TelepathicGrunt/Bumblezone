package com.telepathicgrunt.the_bumblezone.mixin.fabric.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.platform.BzArrowItem;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @ModifyExpressionValue(method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    public boolean bumblezone$infinityBzAmmo(boolean vanillaArrow, @Local(ordinal = 1) ItemStack ammo) {
        if (!vanillaArrow && ammo.getItem() instanceof BzArrowItem) {
            return true;
        }
        return vanillaArrow;
    }
}
