package com.telepathicgrunt.bumblezone.mixin.items;

import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class HoneyShieldPreferredSlotMixin {

    @Inject(method = "getPreferredEquipmentSlot",
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void thebumblezone_isHoneyCrystalShield(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
        if(stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD){
            cir.setReturnValue(EquipmentSlot.OFFHAND);
        }
    }
}