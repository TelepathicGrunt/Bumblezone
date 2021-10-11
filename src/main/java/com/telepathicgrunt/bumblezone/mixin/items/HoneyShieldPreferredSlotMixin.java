package com.telepathicgrunt.bumblezone.mixin.items;

import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class HoneyShieldPreferredSlotMixin {

    @Inject(method = "getEquipmentSlotForItem",
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void thebumblezone_isHoneyCrystalShield(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
        if(stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD){
            cir.setReturnValue(EquipmentSlot.OFFHAND);
        }
    }
}