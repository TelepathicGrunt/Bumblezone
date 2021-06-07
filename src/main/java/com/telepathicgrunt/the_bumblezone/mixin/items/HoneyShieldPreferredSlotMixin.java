package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class HoneyShieldPreferredSlotMixin {

    @Inject(method = "getEquipmentSlotForItem",
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void thebumblezone_isHoneyCrystalShield(ItemStack stack, CallbackInfoReturnable<EquipmentSlotType> cir) {
        if(stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD.get()){
            cir.setReturnValue(EquipmentSlotType.OFFHAND);
        }
    }
}