package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.enchantments.CombCutterEnchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BzEnchantments {
    public final static CombCutterEnchantment COMB_CUTTER = new CombCutterEnchantment();

    public static void registerEnchantment() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(Bumblezone.MODID, "comb_cutter"), COMB_CUTTER);
    }
}
