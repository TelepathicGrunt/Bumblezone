package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.PotentPoisonEnchantment;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

public class BzEnchantments {
    public static final ResourcefulRegistry<Enchantment> ENCHANTMENTS = ResourcefulRegistries.create(BuiltInRegistries.ENCHANTMENT, Bumblezone.MODID);

    public static final RegistryEntry<CombCutterEnchantment> COMB_CUTTER = ENCHANTMENTS.register("comb_cutter", CombCutterEnchantment::new);
    public static final RegistryEntry<PotentPoisonEnchantment> POTENT_POISON = ENCHANTMENTS.register("potent_poison", PotentPoisonEnchantment::new);
    public static final RegistryEntry<NeurotoxinsEnchantment> NEUROTOXINS = ENCHANTMENTS.register("neurotoxins", NeurotoxinsEnchantment::new);
}
