package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.item;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.platform.BzArrowItem;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BowItem.class)
public class BowItemMixin {

    @ModifyVariable(
            method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At(value = "STORE"),
            ordinal = 1
    )
    public boolean bumblezone$onSetIsInfinite(boolean value, @Local(ordinal = 0, name = "bow") ItemStack bow, @Local(ordinal = 0, name = "player") Player shooter, @Local(ordinal = 1, name = "ammo") ItemStack ammo) {
        if (ammo.getItem() instanceof BzArrowItem arrow) {
            OptionalBoolean result = arrow.bz$isInfinite(ammo, bow, shooter);
            if (result.isPresent()) {
                return shooter.getAbilities().instabuild || result.get();
            }
        }
        return value;
    }
}
