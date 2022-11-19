package com.telepathicgrunt.the_bumblezone.mixin.enchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.items.BeeCannon;
import com.telepathicgrunt.the_bumblezone.items.CarpenterBeeBoots;
import com.telepathicgrunt.the_bumblezone.items.CrystalCannon;
import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShield;
import com.telepathicgrunt.the_bumblezone.items.StingerSpearItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @ModifyReturnValue(method = "canEnchant",
            at = @At(value = "RETURN"))
    private boolean thebumblezone_isEnchantmentInvalidForBzItem(boolean canEnchant, ItemStack stack) {
        Enchantment enchantment = ((Enchantment)(Object)this);

        if (HoneyCrystalShield.isInvalidForHoneyCrystalShield(stack, enchantment)) {
            return true;
        }
        else if (StingerSpearItem.isInvalidForStingerSpear(stack, enchantment)) {
            return true;
        }
        else if (StingerSpearItem.canBeEnchanted(stack, enchantment)) {
            return true;
        }
        else if (CarpenterBeeBoots.canBeEnchanted(stack, enchantment)) {
            return true;
        }
        else if (BeeCannon.canBeEnchanted(stack, enchantment)) {
            return true;
        }
        else if (CrystalCannon.canBeEnchanted(stack, enchantment)) {
            return true;
        }

        return canEnchant;
    }
}