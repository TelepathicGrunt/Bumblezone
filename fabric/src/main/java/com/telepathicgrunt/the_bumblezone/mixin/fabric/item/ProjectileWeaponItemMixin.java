package com.telepathicgrunt.the_bumblezone.mixin.fabric.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.items.BeeStinger;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin {

    @ModifyExpressionValue(method = "useAmmo(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Z)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processAmmoUse(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I"))
    private static int bumblezone$AllowInfinityForBZAmmo(int useAmmoCount, @Local(ordinal = 0, argsOnly = true) ItemStack weapon, @Local(ordinal = 1, argsOnly = true) ItemStack ammo, @Local(ordinal = 0, argsOnly = true) LivingEntity player) {
        if (ammo.getItem() instanceof BeeStinger beeStinger) {
            OptionalBoolean optionalBoolean = beeStinger.bz$isInfinite(ammo, weapon, player);
            if (optionalBoolean.isPresent() && optionalBoolean.get()) {
                return 0;
            }
        }

        return useAmmoCount;
    }
}
