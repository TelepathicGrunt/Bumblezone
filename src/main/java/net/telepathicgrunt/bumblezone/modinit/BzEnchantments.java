package net.telepathicgrunt.bumblezone.modinit;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.enchantments.CombCutterEnchantment;

public class BzEnchantments {
    public final static CombCutterEnchantment COMB_CUTTER = new CombCutterEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.VANISHABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});

    public static void registerEnchantment() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(Bumblezone.MODID, "comb_cutter"), COMB_CUTTER);
    }
}
