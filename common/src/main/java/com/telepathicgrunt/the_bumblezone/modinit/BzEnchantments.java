package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.PotentPoisonEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Bumblezone.MODID);

    public static final RegistryObject<CombCutterEnchantment> COMB_CUTTER = ENCHANTMENTS.register("comb_cutter", CombCutterEnchantment::new);
    public static final RegistryObject<PotentPoisonEnchantment> POTENT_POISON = ENCHANTMENTS.register("potent_poison", PotentPoisonEnchantment::new);
    public static final RegistryObject<NeurotoxinsEnchantment> NEUROTOXINS = ENCHANTMENTS.register("neurotoxins", NeurotoxinsEnchantment::new);
}
