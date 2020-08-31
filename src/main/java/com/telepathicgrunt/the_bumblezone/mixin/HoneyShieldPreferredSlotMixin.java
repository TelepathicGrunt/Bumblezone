package com.telepathicgrunt.the_bumblezone.mixin;

import com.telepathicgrunt.the_bumblezone.items.BzItems;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class HoneyShieldPreferredSlotMixin {

    @Inject(method = "getSlotForItemStack",
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void isHoneyCrystalShield(ItemStack stack, CallbackInfoReturnable<EquipmentSlotType> cir) {
        if(stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD){
            cir.setReturnValue(EquipmentSlotType.OFFHAND);
        }
    }
}