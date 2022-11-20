package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class HoneyShieldLivingEntityMixin {

    @ModifyReturnValue(method = "getEquipmentSlotForItem",
            at = @At(value = "RETURN"))
    private static EquipmentSlot thebumblezone_correctSlotForHoneyCrystalShield(EquipmentSlot equipmentSlot, ItemStack stack) {
        if(stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD) {
            return EquipmentSlot.OFFHAND;
        }
        return equipmentSlot;
    }
}