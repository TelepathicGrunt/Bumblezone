package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.PotentPoisonEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class BzEnchantments {
    public final static CombCutterEnchantment COMB_CUTTER = new CombCutterEnchantment();
    public static final PotentPoisonEnchantment POTENT_POISON = new PotentPoisonEnchantment();
    public static final NeurotoxinsEnchantment NEUROTOXINS = new NeurotoxinsEnchantment();

    public static void registerEnchantment() {
        Registry.register(Registry.ENCHANTMENT, new ResourceLocation(Bumblezone.MODID, "comb_cutter"), COMB_CUTTER);
        Registry.register(Registry.ENCHANTMENT, new ResourceLocation(Bumblezone.MODID, "potent_poison"), POTENT_POISON);
        Registry.register(Registry.ENCHANTMENT, new ResourceLocation(Bumblezone.MODID, "neurotoxins"), NEUROTOXINS);
    }
}
