package com.telepathicgrunt.bumblezone.mixin.items;

import com.telepathicgrunt.bumblezone.modinit.BzEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    @Inject(method = "getPossibleEntries(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;",
            at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void thebumblezone_applyEnchantmentsCorrectly(int power, ItemStack stack, boolean treasureAllowed,
                                                                 CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir,
                                                                 List<EnchantmentLevelEntry> list, Item item,
                                                                 boolean bl, Iterator<Enchantment> var6, Enchantment enchantment)
    {
        if(enchantment == BzEnchantments.COMB_CUTTER && !BzEnchantments.COMB_CUTTER.isAcceptableItem(stack) && !list.isEmpty()) {
            list.remove(list.size() - 1);
        }
    }
}