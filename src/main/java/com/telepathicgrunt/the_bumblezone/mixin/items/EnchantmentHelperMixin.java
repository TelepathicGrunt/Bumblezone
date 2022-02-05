package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    //most compat way to make enchantment table apply our enchantment properly
    @Inject(method = "getAvailableEnchantmentResults(ILnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void thebumblezone_applyEnchantmentsCorrectly(int power, ItemStack stack, boolean treasureAllowed,
                                                                 CallbackInfoReturnable<List<EnchantmentInstance>> cir,
                                                                 List<EnchantmentInstance> list, Item item,
                                                                 boolean bl, Iterator<Enchantment> var6, Enchantment enchantment)
    {
        if(enchantment == BzEnchantments.COMB_CUTTER && !BzEnchantments.COMB_CUTTER.canEnchant(stack) && !list.isEmpty()) {
            list.remove(list.size() - 1);
        }
        else if(enchantment == BzEnchantments.NEUROTOXINS && !BzEnchantments.NEUROTOXINS.canEnchant(stack) && !list.isEmpty()) {
            list.remove(list.size() - 1);
        }
        else if(enchantment == BzEnchantments.POTENT_POISON && !BzEnchantments.POTENT_POISON.canEnchant(stack) && !list.isEmpty()) {
            list.remove(list.size() - 1);
        }
    }
}